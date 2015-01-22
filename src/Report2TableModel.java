import java.util.*;
import javax.swing.table.AbstractTableModel;

class Report2TableModel extends AbstractTableModel implements FiltrableCustomTableModel {
  private final String[] columnNames = {"Услуга","Выручка","Доход","Рентабельность","Операций","<HTML><CENTER>Средний<br>платеж</CENTER></HTML>","<HTML><CENTER>Доля в<br>обороте</CENTER></HTML>","<HTML><CENTER>Доля в<br>доходе</CENTER></HTML>"};
  private Object[][] data = {Collections.nCopies(columnNames.length, "").toArray()};
  private ArrayList<FinanceProfile> finances;
  private Object[] reportData;
  private ArrayList<FinanceProfile> origFinances = null;

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
    return getValueAt(0, c).getClass();
  }

  public synchronized void putFinances(Object[] reportData, ArrayList<FinanceProfile> finances) {
    this.reportData = reportData;
    this.origFinances = new ArrayList<>(finances);
    putFinancesInternal(finances);
  }

  private synchronized void putFinancesInternal(ArrayList<FinanceProfile> finances) {
    this.finances = finances;
    FinanceProfile finance;
    Object[][] buffer = new Object[finances.size()+1][columnNames.length];
    double totalSum = 0;
    double profit = 0;
    int quantity = 0;
    for (int i=0; i<finances.size(); i++) {
      finance = finances.get(i);
      totalSum += (Double) finance.data[3];
      profit += (Double) finance.data[4];
      quantity += (Integer) finance.data[5];
      buffer[i][0] = finance.data[2];
      buffer[i][1] = finance.data[3];//total
      buffer[i][2] = finance.data[4];//total-pay=profit
      buffer[i][3] = round(((Double) finance.data[4] / (Double) finance.data[3] * 100), 2);
      buffer[i][4] = finance.data[5];//count
      buffer[i][5] = round(((Double) finance.data[3] / (Integer) finance.data[5]), 2);
    }
    for (int i=0; i<finances.size(); i++) {
      finance = finances.get(i);
      buffer[i][6] = round((Double) finance.data[3] / totalSum * 100, 2);
      buffer[i][7] = round((Double) finance.data[4] / profit * 100, 2);
    }
    if (buffer.length > 1) {
      int lastRow = buffer.length-1;
      buffer[lastRow][0] = "Итого: "+ finances.size();
      buffer[lastRow][1] = totalSum;
      buffer[lastRow][2] = profit;
      buffer[lastRow][3] = round(profit/totalSum*100, 2);
      buffer[lastRow][4] = quantity;
      buffer[lastRow][5] = round(totalSum/quantity, 2);
      buffer[lastRow][6] = 100.00;
      buffer[lastRow][7] = 100.00;
    }
    if (buffer.length > 1) data = buffer; else data = new Object[][] {Collections.nCopies(columnNames.length, "").toArray()};
    fireTableDataChanged();
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
