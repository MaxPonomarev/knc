import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

class Report45TableModel extends FiltrableTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final ImageIcon iconStatOff = new ImageIcon("images/3_kiosk_black.png");
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//  private final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd / EE");
  private final String[] initColumnNames = {" ","№","Адрес","Выручка","Актуальность"};
  private String[] columnNames = initColumnNames;
  private Object[] reportData = null;
  private ArrayList<FinanceProfile> finances = null;
  private ArrayList<FinanceProfile> filteredFinances = null;//Отфильтрованный список, либо равен finances, если фильтр пуст
  private int type;
  private String columnName = "Выручка";

  public Report45TableModel() {
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
    return getValueAt(0, c).getClass();
  }

  public synchronized void putFinances(Object[] reportData, ArrayList<FinanceProfile> finances, int type) {
    this.reportData = reportData;
    this.finances = new ArrayList<>(finances);
    this.type = type;
    if (type == 3) columnName = "Выручка";
    if (type == 4) columnName = "Доход";
    FinanceProfile firstFinance = finances.remove(0);
    if (finances.size() > 0) {//если пусто не надо добавлять колонки, иначе проблемы
      columnNames = new String[firstFinance.data.length+1];
      for (int i=0; i<columnNames.length; i++) {
        if (i < 3) columnNames[i] = initColumnNames[i];
        else if (i != columnNames.length-1) columnNames[i] = "<HTML><CENTER>"+columnName+" за<br>"+dateFormat.format((Date)firstFinance.data[i])+"</CENTER></HTML>";
        else columnNames[i] = "Актуальность";
      }
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
    double[] total = new double[firstFinance.data.length];
    boolean actual = true;
    for (int i=0; i<filteredFinances.size(); i++) {
      FinanceProfile finance = filteredFinances.get(i);
      for (int j=3; j<total.length; j++) {
        total[j] += (Double) finance.data[j];
      }
      if (((Date)reportData[3]).after(finance.to)) {
        actual = false;
        buffer[i][0] = iconStatOff;
      } else
        buffer[i][0] = iconStat0;
      System.arraycopy(finance.data, 1, buffer[i], 1, finance.data.length - 1);//for (int j=1; j<finance.data.length; j++) buffer[i][j] = finance.data[j];
      buffer[i][columnNames.length-1] = dateFormat.format(new Date(finance.to.getTime()+77));
    }
    if (buffer.length > 1) {
      int lastRow = buffer.length-1;
      if (actual) buffer[lastRow][0] = iconStat0;
      else buffer[lastRow][0] = iconStatOff;
      buffer[lastRow][1] = filteredFinances.size();
      buffer[lastRow][2] = "Итого";
      for (int j=3; j<total.length; j++) {
        buffer[lastRow][j] = total[j];
      }
      buffer[lastRow][columnNames.length-1] = "";
    }
    if (!(buffer.length > 1)) {
      buffer = new Object[][] {Collections.nCopies(columnNames.length, "").toArray()};
    }
    data = buffer;
    fireTableDataChanged();
  }

  protected void updateData() {
    putFinances(reportData, finances, type);
  }
}
