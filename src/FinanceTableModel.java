import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

class FinanceTableModel extends FiltrableTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final ImageIcon iconStatOff = new ImageIcon("images/3_kiosk_black.png");
  private final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy MMMMM");
  private final String[] initColumnNames = {" ","№","Адрес","Выручка","Доход","Рентабельность","Операций","<HTML><CENTER>Средний<br>платеж</CENTER></HTML>","<HTML><CENTER>Выручка<br>в день</CENTER></HTML>","<HTML><CENTER>Доход<br>в день</CENTER></HTML>","<HTML><CENTER>Операций<br>в день</CENTER></HTML>","Актуальность"};
  private String[] columnNames = initColumnNames;
  private int gatewaysCount = 0;
  private ArrayList<FinanceProfile> finances = null;
  private ArrayList<FinanceProfile> filteredFinances = null;//Отфильтрованный список, либо равен finances, если фильтр пуст
  private boolean history;
  private boolean structureChanged = false;
  private boolean combination = false;//комбинировать киоски с одним адресом в один киоск

  public FinanceTableModel() {
    super();
    data = new Object[][] {Collections.nCopies(initColumnNames.length, "").toArray()};
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public int getRowCount() {
    return data.length;
  }

  public Object getValueAt(int row, int col) {
    if (row < data.length) return data[row][col];
    else return null;
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  public Class<?> getColumnClass(int c) {
    if (c >= 4 && c < 4+gatewaysCount) return getValueAt(getRowCount()-1, c).getClass();
    return getValueAt(0, c).getClass();
  }

  public synchronized void putFinances(ArrayList<FinanceProfile> finances, boolean history) {
    this.finances = finances;
    this.history = history;
    if (autoFilter != null) {
      filteredFinances = new ArrayList<>();
      for (FinanceProfile finance : finances) {
        String kioskNumber = (String) finance.data[1];
        if (kioskNumber.startsWith(autoFilter)) filteredFinances.add(finance);
      }
    } else if (!filter.isEmpty()) {
      filteredFinances = new ArrayList<>();
      for (FinanceProfile finance : finances) {
        String kioskNumber = (String) finance.data[1];
        if (filter.contains(kioskNumber)) filteredFinances.add(finance);
      }
    } else filteredFinances = finances;
    if (combination && !history) {
      HashMap<String,FinanceProfile> map = new HashMap<>();
      for (FinanceProfile finance : filteredFinances) {
        String address = removeTags(finance.data[2].toString());
        if (map.containsKey(address)) {
          FinanceProfile combinedFinance = combine(map.get(address), finance);
          map.put(address, combinedFinance);
        } else map.put(address, finance);
      }
      filteredFinances = new ArrayList<>();
      filteredFinances.addAll(map.values());
    }
    HashSet<String> uniqueGateways = new HashSet<>();
    for (FinanceProfile finance : filteredFinances) {
      if (finance.data[0] == null) continue;
      uniqueGateways.addAll(((HashMap<String, Double>) finance.data[0]).keySet());
    }
    ArrayList<String> gateways = new ArrayList<>(uniqueGateways);
    KNC_Terminal.sortGateways(gateways);
    gatewaysCount = gateways.size();
    ArrayList<String> gatewayNames = new ArrayList<>();
    ArrayList<Double> gatewaysTotal = new ArrayList<>();
    for (String gateway : gateways) {
      gatewayNames.add(KNC_Terminal.getGatewayName(gateway));
      gatewaysTotal.add(0.0d);
    }
    String[] wasColumnNames = columnNames;
    columnNames = new String[initColumnNames.length+gatewaysCount];
    for (int i=0; i<columnNames.length; i++) {
      if (i < 4) columnNames[i] = initColumnNames[i];
      else if (i < 4+gatewaysCount) columnNames[i] = "<HTML><CENTER>Выручка<br>"+gatewayNames.get(i-4)+"</CENTER></HTML>";
      else columnNames[i] = initColumnNames[i-gatewaysCount];
    }
    if (history) columnNames[11+gatewaysCount] = "Период";
    Object[][] buffer = new Object[filteredFinances.size()+1][columnNames.length];
    double totalSum = 0;
    double totalApproach = 0;
    double profit = 0;
    double profitApproach = 0;
    int quantity = 0;
    double totalInDay = 0;
    double profitInDay = 0;
    double quantityInDay = 0;
    boolean actual = true;
    for (int i=0; i<filteredFinances.size(); i++) {
      FinanceProfile finance = filteredFinances.get(i);
      byte days = (byte)((finance.to.getTime() - finance.from.getTime() + 77) / 86400000);
      totalSum += (Double) finance.data[3];
      profit += (Double) finance.data[4];
      if (!history) {
        if (days >= 30 || days < 1) {
          totalApproach += (Double) finance.data[3];
          profitApproach += (Double) finance.data[4];
        } else {
          totalApproach += ((Double)finance.data[3])/days*30;
          profitApproach += ((Double)finance.data[4])/days*30;
        }
      }
      quantity += (Integer) finance.data[5];
      if (!history && System.currentTimeMillis()-finance.to.getTime() > 86400001) {
        actual = false;
        buffer[i][0] = iconStatOff;
      } else
        buffer[i][0] = iconStat0;
      buffer[i][1] = finance.data[1];
      buffer[i][2] = finance.data[2];
      if (history) buffer[i][3] = finance.data[3];//total
      else buffer[i][3] = new DoubleApproach((Double)finance.data[3]);//total
      HashMap<String,Double> map = (HashMap<String,Double>)finance.data[0];
      for (int j=0; j<gateways.size(); j++) {
        if (map == null) {
          buffer[i][4 + j] = "";
          continue;
        }
        Double sum = map.get(gateways.get(j));
        buffer[i][4+j] = (sum == null) ?  "" : sum.doubleValue();
        if (sum != null) gatewaysTotal.set(j, gatewaysTotal.get(j) + sum);
      }
      if (history) buffer[i][4+gatewaysCount] = finance.data[4];//total-pay=profit
      else buffer[i][4+gatewaysCount] = new DoubleApproach((Double)finance.data[4]);//total-pay=profit
      buffer[i][5+gatewaysCount] = round(((Double) finance.data[4] / (Double) finance.data[3] * 100), 2);
      buffer[i][6+gatewaysCount] = finance.data[5];//count
      buffer[i][7+gatewaysCount] = round(((Double) finance.data[3] / (Integer) finance.data[5]), 2);
      buffer[i][8+gatewaysCount] = new DoubleTotal(round((Double) finance.data[3] / days, 2));
      buffer[i][9+gatewaysCount] = new DoubleTotal(round((Double) finance.data[4] / days, 2));
      buffer[i][10+gatewaysCount] = new DoubleTotal(round((Integer) finance.data[5] / days, 2));
      if (!history)
        buffer[i][11+gatewaysCount] = dateFormat1.format(new Date(finance.to.getTime()+77));
      else
        buffer[i][11+gatewaysCount] = dateFormat2.format(new Date(finance.to.getTime()));
      totalInDay += ((DoubleTotal)buffer[i][8+gatewaysCount]).getValue1();
      profitInDay += ((DoubleTotal)buffer[i][9+gatewaysCount]).getValue1();
      quantityInDay += ((DoubleTotal)buffer[i][10+gatewaysCount]).getValue1();
    }
    if (buffer.length > 1) {
      int lastRow = buffer.length-1;
      if (actual) buffer[lastRow][0] = iconStat0;
      else buffer[lastRow][0] = iconStatOff;
      buffer[lastRow][1] = filteredFinances.size();
      buffer[lastRow][2] = "Итого";
      if (history) {
        buffer[lastRow][3] = totalSum;
        buffer[lastRow][4+gatewaysCount] = profit;
        buffer[lastRow][8+gatewaysCount] = new DoubleTotal(round(totalInDay/filteredFinances.size(), 2));
        buffer[lastRow][9+gatewaysCount] = new DoubleTotal(round(profitInDay/filteredFinances.size(), 2));
        buffer[lastRow][10+gatewaysCount] = new DoubleTotal(round(quantityInDay/filteredFinances.size(), 0));
      } else {
        boolean showApproach = (Math.abs(totalApproach-totalSum) > totalSum/100);
        if (showApproach) {
          buffer[lastRow][3] = new DoubleApproach(totalSum, round(totalApproach,0));
          buffer[lastRow][4+gatewaysCount] = new DoubleApproach(profit, round(profitApproach,2));
        } else {
          buffer[lastRow][3] = new DoubleApproach(totalSum);
          buffer[lastRow][4+gatewaysCount] = new DoubleApproach(profit);
        }
        buffer[lastRow][8+gatewaysCount] = new DoubleTotal(round(totalInDay/filteredFinances.size(), 2), round(totalInDay, 2));
        buffer[lastRow][9+gatewaysCount] = new DoubleTotal(round(profitInDay/filteredFinances.size(), 2), round(profitInDay, 2));
        buffer[lastRow][10+gatewaysCount] = new DoubleTotal(round(quantityInDay/filteredFinances.size(), 0), round(quantityInDay, 2));
      }
      for (int j=0; j<gatewaysTotal.size(); j++) {
        buffer[lastRow][4+j] = round(gatewaysTotal.get(j), 2);
      }
      buffer[lastRow][5+gatewaysCount] = round(profit/totalSum*100, 2);
      buffer[lastRow][6+gatewaysCount] = quantity;
      buffer[lastRow][7+gatewaysCount] = round(totalSum/quantity, 2);
      buffer[lastRow][11+gatewaysCount] = "";
    }
    if (buffer.length > 1) data = buffer; else data = new Object[][] {Collections.nCopies(initColumnNames.length, "").toArray()};
    structureChanged = false;
    if (history) fireTableDataChanged();
    else if (wasColumnNames.length == columnNames.length) fireTableDataChanged();
    else {
      structureChanged = true;
      fireTableStructureChanged();
    }
  }

  public int getGatewaysCount() {
    return gatewaysCount;
  }

  public boolean getStructureChanged() {
    return structureChanged;
  }

  public void setCombination(boolean combination) {
    this.combination = combination;
    if (finances != null) updateData();
  }

  protected void updateData() {
    putFinances(finances, history);
  }

  //к профилю 1 добавляется профиль 2 и возвращается результат В НОВОМ FinanceProfile
  private FinanceProfile combine(FinanceProfile finance1, FinanceProfile finance2) {
    FinanceProfile finance = new FinanceProfile();
    String str;
    Double sum1, sum2;
    Integer oper1, oper2;
    HashMap<String,Double> map1, map2;
    if (finance1.from.before(finance2.from)) finance.from = finance1.from;
    else finance.from = finance2.from;
    if (finance1.to.before(finance2.to)) finance.to = finance1.to;
    else finance.to = finance2.to;
    finance.data = new Object[6];
    map1 = (HashMap<String,Double>)finance1.data[0];
    map2 = (HashMap<String,Double>)finance2.data[0];
    if (map1 == null) finance.data[0] = map2;
    if (map1 != null && map2 != null) {
      finance.data[0] = new HashMap<String,Double>();
      for (String key : map2.keySet()) {
        sum2 = map2.get(key);
        if (map1.containsKey(key)) {
          sum1 = map1.get(key);
          ((HashMap<String,Double>)finance.data[0]).put(key, sum1 + sum2);
        } else ((HashMap<String,Double>)finance.data[0]).put(key, sum2);
      }
    }
    str = finance1.data[1].toString();
    if (!str.contains("[")) finance.data[1] = "[2]";
    else {
      str = str.replaceAll("\\[", "");
      str = str.replaceAll("]", "");
      int num = 1+Integer.parseInt(str);
      finance.data[1] = "["+num+"]";
    }
    str = finance1.data[2].toString();
    str = str.replaceAll(" \\[.+?]", "");
    str = str.replaceAll("\\[.+?]", "");
    finance.data[2] = str;
    sum1 = (Double)finance1.data[3];
    sum2 = (Double)finance2.data[3];
    finance.data[3] = sum1 + sum2;
    sum1 = (Double)finance1.data[4];
    sum2 = (Double)finance2.data[4];
    finance.data[4] = sum1 + sum2;
    oper1 = (Integer)finance1.data[5];
    oper2 = (Integer)finance2.data[5];
    finance.data[5] = oper1 + oper2;
    return finance;
  }

  private String removeTags(String str) {
    str = str.replaceAll("<.+?>", "");
    str = str.replaceAll("\\[.+?]", "");
    str = str.replaceAll(" ", "");
    return str;
  }

  private double round(double value, int quantity) {
    return KNC_Terminal.round(value, quantity);
  }
}
