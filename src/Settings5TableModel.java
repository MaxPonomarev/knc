import java.util.*;
import javax.swing.*;

class Settings5TableModel extends FiltrableTableModel implements SettingsTableModel {
  private static final String ND = "н/д";
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final String[] columnNames = {" ","№","Адрес","Операционная система","Дата установки","Процессор","Память","Жесткий диск","Параметры экрана","Заставка","Автообновление","Часовой пояс","JVM"};
  private final Object[][] initData = {Collections.nCopies(columnNames.length, "").toArray()};
  private ArrayList<KioskProfile> kiosks = null;
  private ArrayList<KioskProfile> filteredKiosks = null;//Отфильтрованный список, либо равен kiosks, если фильтр пуст

  public Settings5TableModel() {
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
    String str;
    Boolean bool;
    for (int i = 0; i < filteredKiosks.size(); i++) {
      KioskProfile kiosk = filteredKiosks.get(i);
      buffer[i][0] = iconStat0;
      buffer[i][1] = kiosk.data[1];
      buffer[i][2] = kiosk.data[2];
      if (kiosk.data2[3] == null) {
        buffer[i][3] = ND;
        buffer[i][4] = ND;
        buffer[i][5] = ND;
        buffer[i][6] = ND;
        buffer[i][7] = ND;
        buffer[i][8] = ND;
        buffer[i][9] = ND;
        buffer[i][10] = ND;
        buffer[i][11] = ND;
        buffer[i][12] = ND;
      } else {
        buffer[i][3] = "<HTML>" + kiosk.data2[3][0] + "<br>" + kiosk.data2[3][1] + "</HTML>";
        buffer[i][4] = (kiosk.data2[3][2] == null ? ND : kiosk.data2[3][2]);
        buffer[i][5] = (kiosk.data2[3][3] == null ? ND : kiosk.data2[3][3]);
        buffer[i][6] = (kiosk.data2[3][4] == null ? ND : kiosk.data2[3][4]);
        buffer[i][7] = (kiosk.data2[3][5] == null ? ND : kiosk.data2[3][5]);
        buffer[i][8] = (kiosk.data2[3][6] == null ? ND : kiosk.data2[3][6]);
        bool = (Boolean) kiosk.data2[3][7];
        if (bool == null) str = ND;
        else if (bool) str = "включена";
        else str = "выключена";
        buffer[i][9] = str;
        buffer[i][10] = (kiosk.data2[3][8] == null ? ND : kiosk.data2[3][8]);
        str = (String) kiosk.data2[3][9];
        if (str == null) str = ND;
        else if (str.contains("; ")) {
          String[] tz = str.split("; ");
          str = "<html>" + tz[0] + "<br>" + tz[1] + "</html>";
        }
        buffer[i][11] = str;
        buffer[i][12] = (kiosk.data2[3].length > 10) ? kiosk.data2[3][10] : "";
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
