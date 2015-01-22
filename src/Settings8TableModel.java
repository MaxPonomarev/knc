import java.util.*;
import javax.swing.*;

class Settings8TableModel extends FiltrableTableModel implements SettingsTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final String[] columnNames = {" ","№","Адрес","Интернет","<HTML><CENTER>Качество связи<br>за сутки</CENTER></HTML>","<HTML><CENTER>Качество обслуживания<br>за сутки</CENTER></HTML>","<HTML><CENTER>Качество связи<br>за неделю</CENTER></HTML>","<HTML><CENTER>Качество обслуживания<br>за неделю</CENTER></HTML>"};
  private final Object[][] initData = {Collections.nCopies(columnNames.length, "").toArray()};
  private ArrayList<KioskProfile> kiosks = null;
  private ArrayList<KioskProfile> filteredKiosks = null;//Отфильтрованный список, либо равен kiosks, если фильтр пуст

  public Settings8TableModel() {
    super();
    data = initData;
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

  public synchronized void putKiosks(ArrayList<KioskProfile> kiosks) {
    this.kiosks = kiosks;
    if (autoFilter != null) {
      filteredKiosks = new ArrayList<>();
      for (KioskProfile kiosk : kiosks) {
        String kioskNumber = (String) kiosk.data[1];
        if (kioskNumber.startsWith(autoFilter)) filteredKiosks.add(kiosk);
      }
    } else if (!filter.isEmpty()) {
      filteredKiosks = new ArrayList<>();
      for (KioskProfile kiosk : kiosks) {
        String kioskNumber = (String) kiosk.data[1];
        if (filter.contains(kioskNumber)) filteredKiosks.add(kiosk);
      }
    } else filteredKiosks = kiosks;
    Object[][] buffer = new Object[filteredKiosks.size()+1][columnNames.length];
    for (int i=0; i<filteredKiosks.size(); i++) {
      KioskProfile kiosk = filteredKiosks.get(i);
      buffer[i][0] = iconStat0;
      buffer[i][1] = kiosk.data[1];
      buffer[i][2] = kiosk.data[2];
      String operator = "";
      Integer redial = (Integer)kiosk.data2[1][18];
      Object login = kiosk.data2[1][20];
      Object passw = kiosk.data2[1][21];
      if (redial == 0) {
        operator = "Выделенная линия";
      } else {
        if (login.equals("mts") && passw.equals("mts")) operator = "МТС";
        if (login.equals("mobile") && passw.equals("internet")) operator = "Скайлинк";
        if (login.equals("beeline")) operator = "Билайн";
        if (login.equals("megafon")) operator = "Билайн";
      }
      buffer[i][3] = operator;
      if (kiosk.data.length > 13) {
        buffer[i][4] = new IntegerProc(((int[])kiosk.data[13])[0]);
        buffer[i][5] = new IntegerProc(((int[])kiosk.data[13])[1]);
        buffer[i][6] = new IntegerProc(((int[])kiosk.data[13])[2]);
        buffer[i][7] = new IntegerProc(((int[])kiosk.data[13])[3]);
      } else {
        buffer[i][4] = "";
        buffer[i][5] = "";
        buffer[i][6] = "";
        buffer[i][7] = "";
      }
    }
    if (buffer.length > 1) {
      int lastRow = buffer.length-1;
      buffer[lastRow][0] = iconStat0;
      buffer[lastRow][1] = filteredKiosks.size();
      buffer[lastRow][2] = "Итого";
    }
    if (buffer.length > 1) data = buffer; else data = initData;
    fireTableDataChanged();
  }

  public synchronized KioskProfile[] getKiosks() {//получить список киосков (отфильтровано)
    KioskProfile[] kioskProfiles = new KioskProfile[filteredKiosks.size()];
    kioskProfiles = filteredKiosks.toArray(kioskProfiles);
    return kioskProfiles;
  }

  protected void updateData() {
    putKiosks(kiosks);
  }
}
