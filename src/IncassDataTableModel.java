import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.table.AbstractTableModel;

class IncassDataTableModel extends AbstractTableModel {
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final String[] columnNames = {"Услуга","Операций","Внесено","Зачислено","Ошибочных операций","Отклонено","Первая операция","Последняя операция"};
  private Object[][] data = {Collections.nCopies(columnNames.length, "").toArray()};
  private Incass[] incass;

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

  public synchronized void putIncass(final Incass[] incasses) {
    this.incass = incasses;
    Object[][] buffer;
    int operQuantity = 0;
    double sumByOpers = 0;
    double paySum = 0;
    int errQuantity = 0;
    double errSum = 0;
    if (incasses.length == 1) {
      buffer = new Object[incasses[0].data.length+1][columnNames.length];
      for (int j=0; j<incasses[0].data.length; j++) {
        buffer[j][0] = incasses[0].data[j].productName;
        buffer[j][1] = incasses[0].data[j].operQuantity;
        buffer[j][2] = incasses[0].data[j].sumByOpers;
        buffer[j][3] = incasses[0].data[j].paySum;
        buffer[j][4] = incasses[0].data[j].errQuantity;
        buffer[j][5] = incasses[0].data[j].errSum;
        if (incasses[0].data[j].fromDate != null && incasses[0].data[j].toDate != null) {
          buffer[j][6] = "№"+incasses[0].data[j].fromOperation+" от "+dateFormat.format(incasses[0].data[j].fromDate);
          buffer[j][7] = "№"+incasses[0].data[j].toOperation+" от "+dateFormat.format(incasses[0].data[j].toDate);
        } else {
          buffer[j][6] = "";
          buffer[j][7] = "";
        }
        operQuantity += incasses[0].data[j].operQuantity;
        sumByOpers += incasses[0].data[j].sumByOpers;
        paySum += incasses[0].data[j].paySum;
        errQuantity += incasses[0].data[j].errQuantity;
        errSum += incasses[0].data[j].errSum;
      }
    } else {
      HashMap<String,IncassData> map = new HashMap<>();
      ArrayList<String> indexes = new ArrayList<>();
      for (Incass incass : incasses) {
        for (int j = 0; j < incass.data.length; j++) {
          if (map.containsKey(incass.data[j].productName)) {
            IncassData incassData = map.get(incass.data[j].productName);
            incassData.operQuantity += incass.data[j].operQuantity;
            incassData.sumByOpers += incass.data[j].sumByOpers;
            incassData.paySum += incass.data[j].paySum;
            incassData.errQuantity += incass.data[j].errQuantity;
            incassData.errSum += incass.data[j].errSum;
            incassData.fromOperation = min(incassData.fromOperation, incass.data[j].fromOperation);
            incassData.fromDate = min(incassData.fromDate, incass.data[j].fromDate);
            incassData.toOperation = max(incassData.toOperation, incass.data[j].toOperation);
            incassData.toDate = max(incassData.fromDate, incass.data[j].toDate);
          } else {
            IncassData incassData = new IncassData();
            incassData.productName = incass.data[j].productName;
            incassData.fromOperation = incass.data[j].fromOperation;
            incassData.toOperation = incass.data[j].toOperation;
            if (incass.data[j].fromDate != null) {
              incassData.fromDate = (Date) incass.data[j].fromDate.clone();
            } else {
              incassData.fromDate = null;
            }
            if (incassData.toDate != null) {
              incassData.toDate = (Date) incass.data[j].toDate.clone();
            } else {
              incassData.toDate = null;
            }
            incassData.operQuantity = incass.data[j].operQuantity;
            incassData.errQuantity = incass.data[j].errQuantity;
            incassData.sumByOpers = incass.data[j].sumByOpers;
            incassData.paySum = incass.data[j].paySum;
            incassData.errSum = incass.data[j].errSum;
            map.put(incass.data[j].productName, incassData);
            indexes.add(incass.data[j].productName);
          }
        }
      }
      buffer = new Object[map.size()+1][columnNames.length];
      for (int j=0; j<indexes.size(); j++) {
        String productName = indexes.get(j);
        IncassData data = map.get(productName);
        buffer[j][0] = data.productName;
        buffer[j][1] = data.operQuantity;
        buffer[j][2] = data.sumByOpers;
        buffer[j][3] = data.paySum;
        buffer[j][4] = data.errQuantity;
        buffer[j][5] = data.errSum;
        if (data.fromDate != null && data.toDate != null) {
          buffer[j][6] = "№"+data.fromOperation+" от "+dateFormat.format(data.fromDate);
          buffer[j][7] = "№"+data.toOperation+" от "+dateFormat.format(data.toDate);
        } else {
          buffer[j][6] = "";
          buffer[j][7] = "";
        }
        operQuantity += data.operQuantity;
        sumByOpers += data.sumByOpers;
        paySum += data.paySum;
        errQuantity += data.errQuantity;
        errSum += data.errSum;
      }
    }
    int lastRow = buffer.length-1;
    buffer[lastRow][0] = "Итого: "+incasses[0].data.length;
    buffer[lastRow][1] = operQuantity;
    buffer[lastRow][2] = sumByOpers;
    buffer[lastRow][3] = paySum;
    buffer[lastRow][4] = errQuantity;
    buffer[lastRow][5] = errSum;
    buffer[lastRow][6] = "";
    buffer[lastRow][7] = "";
    if (incasses.length > 0) data = buffer; else data = new Object[][] {Collections.nCopies(columnNames.length, "").toArray()};
    fireTableDataChanged();
  }

  private int min(int a, int b) {
    if (a == 0 && b == 0) return 0;
    if (a == 0) return b;
    if (b == 0) return a;
    return Math.min(a, b);
  }

  private Date min(Date a, Date b) {
    if (a == null && b == null) return null;
    if (a == null) return b;
    if (b == null) return a;
    return new Date(Math.min(a.getTime(), b.getTime()));
  }

  private int max(int a, int b) {
    if (a == 0 && b == 0) return 0;
    if (a == 0) return b;
    if (b == 0) return a;
    return Math.max(a, b);
  }

  private Date max(Date a, Date b) {
    if (a == null && b == null) return null;
    if (a == null) return b;
    if (b == null) return a;
    return new Date(Math.max(a.getTime(), b.getTime()));
  }
}
