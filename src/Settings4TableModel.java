import java.util.*;
import javax.swing.*;

class Settings4TableModel extends FiltrableTableModel implements SettingsTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final String[] columnNames = {" ","№","Адрес","Разрешение экрана","Интерфейс","Описание интерфейса","Описание первого экрана"};
  private final Object[][] initData = {Collections.nCopies(columnNames.length, "").toArray()};
  private ArrayList<KioskProfile> kiosks = null;
  private ArrayList<KioskProfile> filteredKiosks = null;//Отфильтрованный список, либо равен kiosks, если фильтр пуст

  public Settings4TableModel() {
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
    for (int i = 0; i < filteredKiosks.size(); i++) {
      KioskProfile kiosk = filteredKiosks.get(i);
      buffer[i][0] = iconStat0;
      buffer[i][1] = kiosk.data[1];
      buffer[i][2] = kiosk.data[2];
      if (kiosk.data2.length > 12) {
        buffer[i][3] = kiosk.data2[12][0];
        buffer[i][4] = kiosk.data2[12][1];
        buffer[i][5] = (kiosk.data2[12][2] == null ? "" : kiosk.data2[12][2]);
        buffer[i][6] = (kiosk.data2[12][3] == null ? "" : kiosk.data2[12][3]);
      } else {
        buffer[i][3] = "";
        buffer[i][4] = "";
        buffer[i][5] = "";
        buffer[i][6] = "";
      }
    }
    if (buffer.length > 1) {
      int lastRow = buffer.length - 1;
      buffer[lastRow][0] = iconStat0;
      buffer[lastRow][1] = filteredKiosks.size();
      buffer[lastRow][2] = "Итого";
    }
    if (buffer.length > 1) data = buffer;
    else data = initData;
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
