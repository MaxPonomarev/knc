import java.text.SimpleDateFormat;
import java.util.Collections;
import javax.swing.table.AbstractTableModel;

class RegistryTableModel extends AbstractTableModel implements FiltrableCustomTableModel {
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final String[] columnNames = {"Киоск","№","Время","Артикул","Плат.сист.","Услуга","Субсчет","Лиц.счет","Внесено","Зачислено","Комис.(%)","Комис.(руб.)","Завершение","Состояние","Статус","Комментарий"};
  private Object[][] data = {Collections.nCopies(columnNames.length, "").toArray()};
  private KNCPayment[] payments;
  private KNCPayment[] origPayments;
  private boolean subFlag = false;

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

  public synchronized void putPayments(final KNCPayment[] payments) {
    this.origPayments = payments.clone();
    putPaymentsInternal(payments);
  }

  public synchronized void putPaymentsInternal(final KNCPayment[] payments) {
    this.payments = payments;
    Object[][] buffer = new Object[payments.length+1][columnNames.length];
    int opers = payments.length;
    double totalSum = 0;
    double paySum = 0;
    double cashCommission = 0;
    for (int i=0; i<payments.length; i++) {
      if (payments[i].data[5] != null && !payments[i].data[5].equals("")) subFlag = true;
      /*
      Integer data[0]   ID операции (платежа)
      Date    data[1]   Дата и время создания операции (платежа)
      Integer data[2]   Артикул товара (услуги) в платежной системе
      String  data[3]   Платежная система
      String  data[4]   Наименование товара (услуги)
      String  data[5]   Субсчет
      String  data[6]   Номер телефона, лиц. счета, договора и т.д.
      Double  data[7]   Внесённая сумма
      Double  data[8]   Сумма пополнения (за вычетом комиссии)
      Double  data[9]   Комиссия, взятая для данной операции (платежа) в %
      Double  data[10]  Комиссия, взятая для данной операции (платежа) в руб.
      Boolean data[11]  Завершена ли работа с данным платежом
      Boolean data[12]  Успешно ли проведен платеж
      String  data[13]  Статус платежа (Действующий, Проведен, Отклонен)
      String  data[14]  Комментарий
      */
      String kioskNumber = payments[i].device+"";
      buffer[i][0] = kioskNumber.substring(1, kioskNumber.length());
      buffer[i][1] = payments[i].data[0];
      buffer[i][2] = dateFormat.format(payments[i].data[1]);
      buffer[i][3] = payments[i].data[2];
      buffer[i][4] = KNC_Terminal.getGatewayName((String)payments[i].data[3]);
      buffer[i][5] = payments[i].data[4];
      buffer[i][6] = payments[i].data[5];
      buffer[i][7] = payments[i].data[6];
      buffer[i][8] = payments[i].data[7];
      buffer[i][9] = payments[i].data[8];
      buffer[i][10] = payments[i].data[9];
      buffer[i][11] = payments[i].data[10];
      if ((Boolean) payments[i].data[11]) buffer[i][12] = "да";
        else buffer[i][12] = "нет";
      if ((Boolean) payments[i].data[12]) buffer[i][13] = "да";
        else buffer[i][13] = "нет";
      buffer[i][14] = payments[i].data[13];
      buffer[i][15] = payments[i].data[14];
      totalSum += (Double) payments[i].data[7];
      paySum += (Double) payments[i].data[8];
      cashCommission += (Double) payments[i].data[10];
    }
    int lastRow = buffer.length-1;
    buffer[lastRow][1] = opers;
    buffer[lastRow][8] = totalSum;
    buffer[lastRow][9] = paySum;
    buffer[lastRow][10] = cashCommission / paySum * 100;
    buffer[lastRow][11] = cashCommission;
    if (payments.length > 0) data = buffer; else data = new Object[][] {Collections.nCopies(columnNames.length, "").toArray()};
    fireTableDataChanged();
  }

  public synchronized void deleteRows(int[] rows) throws FiltrableTableModelException {
    KNCPayment[] buffer;
    boolean tryDelTotal = false;
    for (int row : rows) {
      if (row == data.length - 1) {
        tryDelTotal = true;
        break;
      }
    }
    if (tryDelTotal) buffer = new KNCPayment[payments.length-rows.length+1];
    else buffer = new KNCPayment[payments.length-rows.length];
    if (buffer.length == 0) throw new FiltrableTableModelException("empty");
    int i = 0;//buffer index
    int j = 0;//payments index
    while (i<buffer.length) {
      boolean delFlag = false;
      for (int row : rows) {
        if (row == j) {
          delFlag = true;
          break;
        }
      }
      if (!delFlag) {
        buffer[i] = payments[j];
        i++;
        j++;
      } else {
        j++;
      }
    }
    putPaymentsInternal(buffer);
  }

  public synchronized void leaveRows(int[] rows) {
    KNCPayment[] buffer;
    boolean tryLeaveTotal = false;
    for (int row : rows) {
      if (row == data.length - 1) {
        tryLeaveTotal = true;
        break;
      }
    }
    if (tryLeaveTotal) buffer = new KNCPayment[rows.length-1];
    else buffer = new KNCPayment[rows.length];
    int i = 0;//buffer index
    int j = 0;//payments index
    while (i<buffer.length) {
      boolean leaveFlag = false;
      for (int row : rows) {
        if (row == j) {
          leaveFlag = true;
          break;
        }
      }
      if (leaveFlag) {
        buffer[i] = payments[j];
        i++;
        j++;
      } else {
        j++;
      }
    }
    putPaymentsInternal(buffer);
  }

  public synchronized void revertRows() {
    putPaymentsInternal(origPayments.clone());
  }

  public synchronized boolean isSubAccounts() {
    return subFlag;
  }
}
