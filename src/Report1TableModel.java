import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

class Report1TableModel extends FiltrableTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final ImageIcon iconStatOff = new ImageIcon("images/3_kiosk_black.png");
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private final String[] initColumnNames = {" ","№","Адрес","Выручка","Доход","Рентабельность","Операций","<HTML><CENTER>Средний<br>платеж</CENTER></HTML>","<HTML><CENTER>Доля в<br>обороте</CENTER></HTML>","<HTML><CENTER>Доля в<br>доходе</CENTER></HTML>","Актуальность"};
  private String[] columnNames = initColumnNames;
  private int gatewaysCount = 0;
  private Object[] reportData = null;
  private ArrayList<FinanceProfile> finances = null;
  private ArrayList<FinanceProfile> filteredFinances = null;//Отфильтрованный список, либо равен finances, если фильтр пуст

  public Report1TableModel() {
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

  public synchronized void putFinances(Object[] reportData, ArrayList<FinanceProfile> finances) {
    this.reportData = reportData;
    this.finances = finances;
    HashSet<String> uniqueGateways = new HashSet<>();
    for (FinanceProfile finance : finances) {
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
    columnNames = new String[initColumnNames.length+gatewaysCount];
    for (int i=0; i<columnNames.length; i++) {
      if (i < 4) columnNames[i] = initColumnNames[i];
      else if (i < 4+gatewaysCount) columnNames[i] = "<HTML><CENTER>Выручка<br>"+gatewayNames.get(i-4)+"</CENTER></HTML>";
      else columnNames[i] = initColumnNames[i-gatewaysCount];
    }
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
    Object[][] buffer = new Object[filteredFinances.size()+1][columnNames.length];
    double totalSum = 0;
    double profit = 0;
    int quantity = 0;
    boolean actual = true;
    for (int i=0; i<filteredFinances.size(); i++) {
      FinanceProfile finance = filteredFinances.get(i);
      totalSum += (Double) finance.data[3];
      profit += (Double) finance.data[4];
      quantity += (Integer) finance.data[5];
      if (((Date)reportData[3]).after(finance.to)) {
        actual = false;
        buffer[i][0] = iconStatOff;
      } else
        buffer[i][0] = iconStat0;
      buffer[i][1] = finance.data[1];
      buffer[i][2] = finance.data[2];
      buffer[i][3] = finance.data[3];//total
      HashMap<String,Double> map = (HashMap<String,Double>)finance.data[0];
      for (int j=0; j<gateways.size(); j++) {
        Double sum = map.get(gateways.get(j));
        buffer[i][4+j] = (sum == null) ?  "" : sum.doubleValue();
        if (sum != null) gatewaysTotal.set(j, gatewaysTotal.get(j) + sum);
      }
      buffer[i][4+gatewaysCount] = finance.data[4];//total-pay=profit
      buffer[i][5+gatewaysCount] = round(((Double) finance.data[4] / (Double) finance.data[3] * 100), 2);
      buffer[i][6+gatewaysCount] = finance.data[5];//count
      buffer[i][7+gatewaysCount] = round(((Double) finance.data[3] / (Integer) finance.data[5]), 2);
      buffer[i][10+gatewaysCount] = dateFormat.format(new Date(finance.to.getTime()+77));
    }
    for (int i=0; i<filteredFinances.size(); i++) {
      FinanceProfile finance = filteredFinances.get(i);
      buffer[i][8+gatewaysCount] = round((Double) finance.data[3] / totalSum * 100, 2);
      buffer[i][9+gatewaysCount] = round((Double) finance.data[4] / profit * 100, 2);
    }
    if (buffer.length > 1) {
      int lastRow = buffer.length-1;
      if (actual) buffer[lastRow][0] = iconStat0;
      else buffer[lastRow][0] = iconStatOff;
      buffer[lastRow][1] = filteredFinances.size();
      buffer[lastRow][2] = "Итого";
      buffer[lastRow][3] = totalSum;
      for (int j=0; j<gatewaysTotal.size(); j++) {
        buffer[lastRow][4+j] = round(gatewaysTotal.get(j), 2);
      }
      buffer[lastRow][4+gatewaysCount] = profit;
      buffer[lastRow][5+gatewaysCount] = round(profit/totalSum*100, 2);
      buffer[lastRow][6+gatewaysCount] = quantity;
      buffer[lastRow][7+gatewaysCount] = round(totalSum/quantity, 2);
      buffer[lastRow][8+gatewaysCount] = 100.00;
      buffer[lastRow][9+gatewaysCount] = 100.00;
      buffer[lastRow][10+gatewaysCount] = "";
    }
    if (!(buffer.length > 1)) {
      buffer = new Object[][] {Collections.nCopies(columnNames.length, "").toArray()};
    }
    data = buffer;
    fireTableDataChanged();
  }

  public int getGatewaysCount() {
    return gatewaysCount;
  }

  protected void updateData() {
    putFinances(reportData, finances);
  }

  private double round(double value, int quantity) {
    return KNC_Terminal.round(value, quantity);
  }
}
