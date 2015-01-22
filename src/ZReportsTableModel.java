import java.text.*;
import java.util.Collections;
import javax.swing.table.AbstractTableModel;

class ZReportsTableModel extends AbstractTableModel implements FiltrableCustomTableModel {
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final DecimalFormat intFormat = new DecimalFormat("############");
  private final String[] columnNames = {"Номер ФР","Z-отчет","Дата","Сумма","Необн. сумма","Инкассация","Сист. время","Модель ФР","Киоск"};
  private final Object[][] initData = {Collections.nCopies(columnNames.length, "").toArray()};
  private Object[][] data = initData;
  private ZReport[] zReports;
  private ZReport[] origZReports;

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

  public synchronized void putZReports(final ZReport[] zReports) {
    this.origZReports = zReports.clone();
    putZReportsInternal(zReports);
  }

  public synchronized void putZReportsInternal(final ZReport[] zReports) {
    this.zReports = zReports;
    Object[][] buffer = new Object[zReports.length+1][columnNames.length];
    int quantity = zReports.length;
    double totalSum = 0;
    for (int i=0; i<zReports.length; i++) {
      String kioskNumber = zReports[i].device+"";
      buffer[i][0] = ""+zReports[i].serial;
      buffer[i][1] = ""+zReports[i].number;
      buffer[i][2] = zReports[i].timestamp;
      buffer[i][3] = zReports[i].sum;
      buffer[i][4] = zReports[i].totalSum;
      if (zReports[i].incass) buffer[i][5] = "да";
      else buffer[i][5] = "нет";
      buffer[i][6] = dateFormat.format(zReports[i].date);
      buffer[i][7] = KNC_Terminal.getPrinterName(zReports[i].model, false);
      buffer[i][8] = kioskNumber.substring(1, kioskNumber.length());
      totalSum += zReports[i].sum;
    }
    int lastRow = buffer.length-1;
    buffer[lastRow][1] = "Итого: "+quantity;
    buffer[lastRow][3] = totalSum;
    if (zReports.length > 0) data = buffer; else data = initData;
    fireTableDataChanged();
  }

  public synchronized void deleteRows(int[] rows) throws FiltrableTableModelException {
    ZReport[] buffer;
    boolean tryDelTotal = false;
    for (int row : rows) {
      if (row == data.length - 1) {
        tryDelTotal = true;
        break;
      }
    }
    if (tryDelTotal) buffer = new ZReport[zReports.length-rows.length+1];
    else buffer = new ZReport[zReports.length-rows.length];
    if (buffer.length == 0) throw new FiltrableTableModelException("empty");
    int i = 0;//buffer index
    int j = 0;//zReports index
    while (i<buffer.length) {
      boolean delFlag = false;
      for (int row : rows) {
        if (row == j) {
          delFlag = true;
          break;
        }
      }
      if (!delFlag) {
        buffer[i] = zReports[j];
        i++;
        j++;
      } else {
        j++;
      }
    }
    putZReportsInternal(buffer);
  }

  public synchronized void leaveRows(int[] rows) {
    ZReport[] buffer;
    boolean tryLeaveTotal = false;
    for (int row : rows) {
      if (row == data.length - 1) {
        tryLeaveTotal = true;
        break;
      }
    }
    if (tryLeaveTotal) buffer = new ZReport[rows.length-1];
    else buffer = new ZReport[rows.length];
    int i = 0;//buffer index
    int j = 0;//zReports index
    while (i<buffer.length) {
      boolean leaveFlag = false;
      for (int row : rows) {
        if (row == j) {
          leaveFlag = true;
          break;
        }
      }
      if (leaveFlag) {
        buffer[i] = zReports[j];
        i++;
        j++;
      } else {
        j++;
      }
    }
    putZReportsInternal(buffer);
  }

  public synchronized void revertRows() {
    putZReportsInternal(origZReports.clone());
  }

  public synchronized String getFilteredZReports1C() {
    StringBuilder result = new StringBuilder();
    for (ZReport zReport : zReports) {
      result.append("СекцияДокумент=Z-отчет\r\n");
      result.append("ИНН=").append(zReport.INN).append("\r\n");
      String kioskNumber = zReport.device + "";
      kioskNumber = kioskNumber.substring(1, kioskNumber.length());
      result.append("НомерТерминала=").append(kioskNumber).append("\r\n");
      result.append("НомерФР=").append(zReport.serial).append("\r\n");
      result.append("НомерОтчета=").append(zReport.number).append("\r\n");
      result.append("Дата=").append(zReport.timestamp).append("\r\n");
      result.append("Сумма=").append(intFormat.format(zReport.sum)).append("\r\n");
      result.append("КонецДокумента\r\n\r\n\r\n");
    }
    return result.toString();
  }
}
