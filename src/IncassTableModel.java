import java.text.*;
import java.util.*;
import javax.swing.table.AbstractTableModel;

class IncassTableModel extends AbstractTableModel implements FiltrableCustomTableModel {
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final DecimalFormat intFormat = new DecimalFormat("############");
  private final String[] columnNames = {"Киоск","Дата","Сумма","Переплата","Купюр","10 руб.","50 руб.","100 руб.","500 руб.","1000 руб.","5000 руб.","Инкассатор"};
  private Object[][] data = {Collections.nCopies(columnNames.length, "").toArray()};
  private Incass[] incass;
  private Incass[] origIncass;
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

  public synchronized void putIncass(final Incass[] incass) {
    this.origIncass = incass.clone();
    putIncassInternal(incass);
  }

  public synchronized void putIncassInternal(final Incass[] incassf) {
    this.incass = clearIncass(incassf);
    Object[][] buffer = new Object[incass.length+1][columnNames.length];
    int quantity = incass.length;
    double sum = 0;
    double refund = 0;
    int bills = 0;
    int[] billDivision = {0,0,0,0,0,0};
    for (int i=0; i<incass.length; i++) {
      String kioskNumber = incass[i].device+"";
      buffer[i][0] = kioskNumber.substring(1, kioskNumber.length());
      buffer[i][1] = dateFormat.format(incass[i].date);
      buffer[i][2] = incass[i].sum;
      buffer[i][3] = incass[i].refund;
      buffer[i][4] = incass[i].bills;
      buffer[i][5] = incass[i].billDivision[0];
      buffer[i][6] = incass[i].billDivision[1];
      buffer[i][7] = incass[i].billDivision[2];
      buffer[i][8] = incass[i].billDivision[3];
      buffer[i][9] = incass[i].billDivision[4];
      buffer[i][10] = incass[i].billDivision[5];
      buffer[i][11] = incass[i].cashier;
      sum += incass[i].sum;
      refund += incass[i].refund;
      bills += incass[i].bills;
      billDivision[0] += incass[i].billDivision[0];
      billDivision[1] += incass[i].billDivision[1];
      billDivision[2] += incass[i].billDivision[2];
      billDivision[3] += incass[i].billDivision[3];
      billDivision[4] += incass[i].billDivision[4];
      billDivision[5] += incass[i].billDivision[5];
    }
    int lastRow = buffer.length-1;
    buffer[lastRow][1] = "Итого: "+quantity;
    buffer[lastRow][2] = sum;
    buffer[lastRow][3] = refund;
    buffer[lastRow][4] = bills;
    buffer[lastRow][5] = billDivision[0];
    buffer[lastRow][6] = billDivision[1];
    buffer[lastRow][7] = billDivision[2];
    buffer[lastRow][8] = billDivision[3];
    buffer[lastRow][9] = billDivision[4];
    buffer[lastRow][10] = billDivision[5];
    buffer[lastRow][11] = null;
    if (incass.length > 0) data = buffer; else data = new Object[][] {Collections.nCopies(columnNames.length, "").toArray()};
    fireTableDataChanged();
  }

  public synchronized Incass[] getIncass(int[] rows) {
    Incass[] buffer;
    boolean totalFlag = false;
    for (int row : rows) {
      if (row == data.length - 1) {
        totalFlag = true;
        break;
      }
    }
    if (totalFlag) buffer = new Incass[rows.length-1];
    else buffer = new Incass[rows.length];
    int j = 0;
    for (int row : rows) {
      if (row != data.length - 1) {
        buffer[j] = incass[row];
        j++;
      }
    }
    return buffer;
  }

  public synchronized void deleteRows(int[] rows) throws FiltrableTableModelException {
    Incass[] buffer;
    boolean tryDelTotal = false;
    for (int row : rows) {
      if (row == data.length - 1) {
        tryDelTotal = true;
        break;
      }
    }
    if (tryDelTotal) buffer = new Incass[incass.length-rows.length+1];
    else buffer = new Incass[incass.length-rows.length];
    if (buffer.length == 0) throw new FiltrableTableModelException("empty");
    int i = 0;//buffer index
    int j = 0;//incass index
    while (i<buffer.length) {
      boolean delFlag = false;
      for (int row : rows) {
        if (row == j) {
          delFlag = true;
          break;
        }
      }
      if (!delFlag) {
        buffer[i] = incass[j];
        i++;
        j++;
      } else {
        j++;
      }
    }
    putIncassInternal(buffer);
  }

  public synchronized void leaveRows(int[] rows) {
    Incass[] buffer;
    boolean tryLeaveTotal = false;
    for (int row : rows) {
      if (row == data.length - 1) {
        tryLeaveTotal = true;
        break;
      }
    }
    if (tryLeaveTotal) buffer = new Incass[rows.length-1];
    else buffer = new Incass[rows.length];
    int i = 0;//buffer index
    int j = 0;//incass index
    while (i<buffer.length) {
      boolean leaveFlag = false;
      for (int row : rows) {
        if (row == j) {
          leaveFlag = true;
          break;
        }
      }
      if (leaveFlag) {
        buffer[i] = incass[j];
        i++;
        j++;
      } else {
        j++;
      }
    }
    putIncassInternal(buffer);
  }

  public synchronized void revertRows() {
    putIncassInternal(origIncass.clone());
  }

  public synchronized String getFilteredIncass1C() {
    StringBuilder result = new StringBuilder();
    for (Incass incas : incass) {
      result.append("СекцияДокумент=Инкассация\r\n");
      String kioskNumber = incas.device + "";
      kioskNumber = kioskNumber.substring(1, kioskNumber.length());
      result.append("НомерТерминала=").append(kioskNumber).append("\r\n");
      result.append("Дата=").append(dateFormat.format(incas.date)).append("\r\n");
      result.append("Сумма=").append(intFormat.format(incas.sum)).append("\r\n");
      result.append("Инкассатор=").append(incas.cashier).append("\r\n");
      result.append("КонецДокумента\r\n\r\n\r\n");
    }
    return result.toString();
  }

  private Incass[] clearIncass(Incass[] incass) {//очистка от нулевых инкассаций
    Incass[] result;
    ArrayList<Incass> temp = new ArrayList<>();
    for (Incass incas : incass) {
      if (incas.bills == 0 && Math.abs(incas.sum) < 0.01) continue; //пропуск "нулевых" инкассаций
      temp.add(incas);
    }
    result = new Incass[temp.size()];
    result = temp.toArray(result);
    return result;
  }
}
