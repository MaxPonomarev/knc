import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.table.AbstractTableModel;

class Report3TableModel extends AbstractTableModel implements FiltrableCustomTableModel {
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd / EE");
  private final String[] initColumnNames = {"День","Выручка","Доход","Рентабельность","Операций","<HTML><CENTER>Средний<br>платеж</CENTER></HTML>","<HTML><CENTER>Доля в<br>обороте</CENTER></HTML>","<HTML><CENTER>Доля в<br>доходе</CENTER></HTML>"};
  private String[] columnNames = initColumnNames;
  private Object[][] data = {Collections.nCopies(initColumnNames.length, "").toArray()};
  private ArrayList<FinanceProfile> finances;
  private Object[] reportData;
  private ArrayList<FinanceProfile> origFinances;
  private ArrayList<String> gateways;
  private int gatewaysCount = 0;

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
    if (c >= 2 && c < 2+gatewaysCount) return getValueAt(getRowCount()-1, c).getClass();
    return getValueAt(0, c).getClass();
  }

  public synchronized void putFinances(Object[] reportData, ArrayList<FinanceProfile> finances) {
    this.reportData = reportData;
    this.origFinances = new ArrayList<>(finances);
    HashSet<String> uniqueGateways = new HashSet<>();
    for (FinanceProfile finance : finances) {
      uniqueGateways.addAll(((HashMap<String, Double>) finance.data[0]).keySet());
    }
    gateways = new ArrayList<>(uniqueGateways);
    KNC_Terminal.sortGateways(gateways);
    putFinancesInternal(finances);
  }

  private synchronized void putFinancesInternal(ArrayList<FinanceProfile> finances) {
    this.finances = finances;
    FinanceProfile finance;
    gatewaysCount = gateways.size();
    ArrayList<String> gatewayNames = new ArrayList<>();
    ArrayList<Double> gatewaysTotal = new ArrayList<>();
    for (String gateway : gateways) {
      gatewayNames.add(KNC_Terminal.getGatewayName(gateway));
      gatewaysTotal.add(0.0d);
    }
    columnNames = new String[initColumnNames.length+gatewaysCount];
    for (int i=0; i<columnNames.length; i++) {
      if (i < 2) columnNames[i] = initColumnNames[i];
      else if (i < 2+gatewaysCount) columnNames[i] = "<HTML><CENTER>Выручка<br>"+gatewayNames.get(i-2)+"</CENTER></HTML>";
      else columnNames[i] = initColumnNames[i-gatewaysCount];
    }
    Object[][] buffer = new Object[finances.size()+1][columnNames.length];
    double totalSum = 0;
    double profit = 0;
    int quantity = 0;
    for (int i=0; i<finances.size(); i++) {
      finance = finances.get(i);
      totalSum += (Double) finance.data[3];
      profit += (Double) finance.data[4];
      quantity += (Integer) finance.data[5];
      buffer[i][0] = dateFormat.format((Date)finance.data[2]);
      buffer[i][1] = finance.data[3];
      HashMap<String,Double> map = (HashMap<String,Double>)finance.data[0];
      for (int j=0; j<gateways.size(); j++) {
        Double sum = map.get(gateways.get(j));
        buffer[i][2+j] = (sum == null) ?  "" : sum.doubleValue();
        if (sum != null) gatewaysTotal.set(j, gatewaysTotal.get(j) + sum);
      }
      buffer[i][2+gatewaysCount] = finance.data[4];//total-pay=profit
      buffer[i][3+gatewaysCount] = round(((Double) finance.data[4] / (Double) finance.data[3] * 100), 2);
      buffer[i][4+gatewaysCount] = finance.data[5];//count
      buffer[i][5+gatewaysCount] = round(((Double) finance.data[3] / (Integer) finance.data[5]), 2);
    }
    for (int i=0; i<finances.size(); i++) {
      finance = finances.get(i);
      buffer[i][6+gatewaysCount] = round((Double) finance.data[3] / totalSum * 100, 2);
      buffer[i][7+gatewaysCount] = round((Double) finance.data[4] / profit * 100, 2);
    }
    if (buffer.length > 1) {
      int lastRow = buffer.length-1;
      buffer[lastRow][0] = "Итого: "+finances.size();
      buffer[lastRow][1] = totalSum;
      for (int j=0; j<gatewaysTotal.size(); j++) {
        buffer[lastRow][2+j] = round(gatewaysTotal.get(j), 2);
      }
      buffer[lastRow][2+gatewaysCount] = profit;
      buffer[lastRow][3+gatewaysCount] = round(profit/totalSum*100, 2);
      buffer[lastRow][4+gatewaysCount] = quantity;
      buffer[lastRow][5+gatewaysCount] = round(totalSum/quantity, 2);
      buffer[lastRow][6+gatewaysCount] = 100.00;
      buffer[lastRow][7+gatewaysCount] = 100.00;
    }
    if (buffer.length > 1) data = buffer; else data = new Object[][] {Collections.nCopies(initColumnNames.length, "").toArray()};
    fireTableDataChanged();
  }

  public int getGatewaysCount() {
    return gatewaysCount;
  }

  public synchronized void deleteRows(int[] rows) throws FiltrableTableModelException {
    ArrayList<FinanceProfile> newFinances = new ArrayList<>();
    Arrays.sort(rows);
    for (int i=rows.length-1; i>=0; i--) {
      if (rows[i] < finances.size()) newFinances.add(finances.remove(rows[i]));
    }
    if (finances.size() == 0) {
      finances = newFinances;
      throw new FiltrableTableModelException("empty");
    }
    putFinancesInternal(finances);
  }

  public synchronized void leaveRows(int[] rows) {
    ArrayList<FinanceProfile> newFinances = new ArrayList<>();
    Arrays.sort(rows);
    for (int i=rows.length-1; i>=0; i--) {
      if (rows[i] < finances.size()) newFinances.add(finances.remove(rows[i]));
    }
    putFinancesInternal(newFinances);
  }

  public synchronized void revertRows() {
    putFinancesInternal(new ArrayList<>(origFinances));
  }

  private double round(double value, int quantity) {
    return KNC_Terminal.round(value, quantity);
  }
}
