import java.util.*;
import javax.swing.*;

class Settings9TableModel extends FiltrableTableModel implements SettingsTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final String[] columnNames = {" ","№","Адрес","Долгота","Широта","Яндекс.Карты","OpenStreetMap","Карты Google"};
  private final Object[][] initData = {Collections.nCopies(columnNames.length, "").toArray()};
  private ArrayList<KioskProfile> kiosks = null;
  private ArrayList<KioskProfile> filteredKiosks = null;//Отфильтрованный список, либо равен kiosks, если фильтр пуст

  public Settings9TableModel() {
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

  public boolean isCellEditable(int row, int col) {
    if (col == 5 || col == 6 || col == 7) {
      Object val = getValueAt(row, col);
      if (val != null && ((String)val).length() > 0) return true;
      else return false;
    } else return false;
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
      String lon = (String)kiosk.data2[1][24];
      String lat = (String)kiosk.data2[1][25];
      if (lon != null) {
        buffer[i][3] = lon;
      } else {
        buffer[i][3] = "";
      }
      if (lat != null) {
        buffer[i][4] = lat;
      } else {
        buffer[i][4] = "";
      }
      if (lon != null && lat != null && lon.length() > 0 && lat.length() > 0) {
        buffer[i][5] = "http://maps.yandex.ru/?ll="+lon+","+lat+"&z=17&pt="+lon+","+lat;
        buffer[i][6] = "http://www.openstreetmap.org/index.html?mlat="+lat+"&mlon="+lon+"&zoom=17";
        buffer[i][7] = "http://maps.google.com/maps?ll="+lat+","+lon+"&z=17&q="+lat+","+lon+"&hl=ru&t=m";
      } else {
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
