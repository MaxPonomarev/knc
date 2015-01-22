import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

class Settings7TableModel extends FiltrableTableModel implements SettingsTableModel {
  private final static String UNKNOWN = "неизвестно";
  private final static String NONE = "не установлен";
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final ImageIcon iconStat2 = new ImageIcon("images/2_kiosk_yell.png");
  private final ImageIcon iconStat3 = new ImageIcon("images/1_kiosk_red.png");
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private final String[] columnNames = {" ","№","Адрес","Антивирус","Брандмауэр","Статус"};
  private final Object[][] initData = {Collections.nCopies(columnNames.length, "").toArray()};
  private ArrayList<KioskProfile> kiosks = null;
  private ArrayList<KioskProfile> filteredKiosks = null;//Отфильтрованный список, либо равен kiosks, если фильтр пуст
  private boolean machineMode = false;
  private long machineTime;

  public Settings7TableModel() {
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
    Boolean bool;
    boolean avir, vir, fw;
    for (int i=0; i<filteredKiosks.size(); i++) {
      KioskProfile kiosk = filteredKiosks.get(i);
      buffer[i][1] = kiosk.data[1];
      buffer[i][2] = kiosk.data[2];
      if (kiosk.data2[4] == null) {
        buffer[i][3] = UNKNOWN;
        avir = false;
      } else if (kiosk.data2[4][0] == null || kiosk.data2[4][1] == null) {
        buffer[i][3] = NONE;
        avir = false;
      } else {
        avir = true;
        String dbDate = "";
        if (kiosk.data2.length > 9 && kiosk.data2[9] != null) {
          String color;
          Date date = (Date)kiosk.data2[9][1];
          long dt;
          if (machineMode) dt = machineTime - date.getTime();
          else dt = System.currentTimeMillis() - date.getTime();
          if (dt <= (long)60*24*60*60*1000) color = "c color=green";
          else if (dt <= (long)120*24*60*60*1000) color = "b color=#FFE600";
          else color = "a color=red";
          dbDate = " база от <b><font "+color+">"+dateFormat.format(date)+"</font></b>";
        }
        String avirOn = "";
        if (kiosk.data2[4].length > 2 && !Boolean.valueOf((String) kiosk.data2[4][2])) {
          avirOn = " <font color=red>[отключен]</font>";
          avir = false;
        }
        buffer[i][3] = "<HTML>"+kiosk.data2[4][0]+avirOn+"<br>"+kiosk.data2[4][1]+dbDate+"</HTML>";
      }
      if (kiosk.data2[5] == null) {
        buffer[i][4] = UNKNOWN;
        fw = false;
      } else if (kiosk.data2[5][0] == null || kiosk.data2[5][1] == null) {
        buffer[i][4] = NONE;
        fw = false;
      } else {
        fw = true;
        String fwOn = "";
        if (kiosk.data2[5].length > 2 && !Boolean.valueOf((String) kiosk.data2[5][2])) {
          fwOn = " <font color=red>[отключен]</font>";
          fw = false;
        }
        buffer[i][4] = "<HTML>"+kiosk.data2[5][0]+fwOn+"<br>"+kiosk.data2[5][1]+"</HTML>";
      }
      bool = (Boolean)kiosk.data2[6][0];
      if (bool) {
        buffer[i][5] = "вероятно система заражена";
        vir = true;
      } else {
        buffer[i][5] = "вероятно система не заражена";
        vir = false;
      }
      if (vir && !avir) buffer[i][0] = iconStat3;
      else if (!vir && avir && fw) buffer[i][0] = iconStat0;
      else buffer[i][0] = iconStat2;
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

  public void setMachineMode(boolean b) {
    machineMode = b;
  }

  public void setMachineTime(long time) {
    machineTime = time;
  }
}
