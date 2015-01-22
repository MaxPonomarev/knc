import java.util.*;
import javax.swing.*;

class Settings2TableModel extends FiltrableTableModel implements SettingsTableModel {
  private static final String ND = "н/д";
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final String[] columnNames = {" ","№","Адрес","<HTML><CENTER>СУ<br>Интервал связи</CENTER></HTML>","<HTML><CENTER>СУ<br>IP-адрес</CENTER></HTML>","<HTML><CENTER>СУ<br>Порт</CENTER></HTML>","<HTML><CENTER>ЦФС<br>Интервал связи</CENTER></HTML>","<HTML><CENTER>ЦФС<br>Адрес сервера</CENTER></HTML>","<HTML><CENTER>ППУ<br>Интервал связи</CENTER></HTML>","<HTML><CENTER>ППУ<br>Адрес сервера</CENTER></HTML>","<HTML><CENTER>РПЦ<br>Интервал связи</CENTER></HTML>","<HTML><CENTER>РПЦ<br>Лимит баланса</CENTER></HTML>","<HTML><CENTER>РПЦ<br>Лимит платежей</CENTER></HTML>","<HTML><CENTER>РПЦ<br>Лимит потери связи</CENTER></HTML>","<HTML><CENTER>РПЦ<br>IP-адрес</CENTER></HTML>","<HTML><CENTER>РПЦ<br>Порт</CENTER></HTML>","<HTML><CENTER>E-Port<br>Интервал связи</CENTER></HTML>","<HTML><CENTER>E-Port<br>Лимит баланса</CENTER></HTML>","<HTML><CENTER>E-Port<br>Лимит платежей</CENTER></HTML>","<HTML><CENTER>E-Port<br>Карта</CENTER></HTML>","<HTML><CENTER>E-Port<br>Точка</CENTER></HTML>","<HTML><CENTER>E-Port<br>Отсылка комиссий</CENTER></HTML>","<HTML><CENTER>E-Port<br>ЭЦП</CENTER></HTML>","<HTML><CENTER>CyberPlat<br>Интервал связи</CENTER></HTML>","<HTML><CENTER>CyberPlat<br>Лимит баланса</CENTER></HTML>","<HTML><CENTER>CyberPlat<br>Лимит платежей</CENTER></HTML>","<HTML><CENTER>CyberPlat<br>Код дилера</CENTER></HTML>","<HTML><CENTER>CyberPlat<br>Код точки</CENTER></HTML>","<HTML><CENTER>CyberPlat<br>Код оператора</CENTER></HTML>","<HTML><CENTER>Город-К<br>Интервал связи</CENTER></HTML>","<HTML><CENTER>Город-К<br>Лимит баланса</CENTER></HTML>","<HTML><CENTER>Город-К<br>Лимит платежей</CENTER></HTML>","<HTML><CENTER>Город-К<br>Номер точки</CENTER></HTML>","<HTML><CENTER>GreenPost<br>Интервал связи</CENTER></HTML>","<HTML><CENTER>GreenPost<br>Лимит баланса</CENTER></HTML>","<HTML><CENTER>GreenPost<br>Лимит платежей</CENTER></HTML>","<HTML><CENTER>GreenPost<br>Карта</CENTER></HTML>"};
  private final Object[][] initData = {Collections.nCopies(columnNames.length, "").toArray()};
  private ArrayList<KioskProfile> kiosks = null;
  private ArrayList<KioskProfile> filteredKiosks = null;//Отфильтрованный список, либо равен kiosks, если фильтр пуст
  private boolean[] gateways = new boolean[] {false,false,false,false,false,false,false};
  //0-ЦФС
  //1-ППУ
  //2-РПЦ
  //3-E-Port
  //4-CyberPlat
  //5-Город-К
  //6-GreenPost

  public Settings2TableModel() {
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
    gateways = new boolean[] {false,false,false,false,false,false,false};
    Object[][] buffer = new Object[filteredKiosks.size()+1][columnNames.length];
    String str;
    for (int i = 0; i < filteredKiosks.size(); i++) {
      KioskProfile kiosk = filteredKiosks.get(i);
      buffer[i][0] = iconStat0;
      buffer[i][1] = kiosk.data[1];
      buffer[i][2] = kiosk.data[2];
      buffer[i][3] = ((Integer) kiosk.data2[1][30] / 1000 / 60) + " мин.";
      str = (String) kiosk.data2[1][31];
      if (str.contains(",")) str = "<html>" + str.replaceFirst(",", "<br>") + "</html>";
      buffer[i][4] = str;
      buffer[i][5] = kiosk.data2[1][32].toString();
      if (kiosk.data2[1].length < 92 || kiosk.data2[1][90] == null) {//ЦФС
        buffer[i][6] = ND;
        buffer[i][7] = ND;
      } else {
        gateways[0] = true;
        buffer[i][6] = ((Integer) kiosk.data2[1][90] / 1000 / 60) + " мин.";
        str = (String) kiosk.data2[1][91];
        if (str.contains(",")) str = "<html>" + str.replaceFirst(",", "<br>") + "</html>";
        buffer[i][7] = str;
      }
      if (kiosk.data2[1].length < 102 || kiosk.data2[1][100] == null) {//ППУ
        buffer[i][8] = ND;
        buffer[i][9] = ND;
      } else {
        gateways[1] = true;
        buffer[i][8] = ((Integer) kiosk.data2[1][100] / 1000 / 60) + " мин.";
        str = (String) kiosk.data2[1][101];
        if (str.contains(",")) str = "<html>" + str.replaceFirst(",", "<br>") + "</html>";
        buffer[i][9] = str;
      }
      if (kiosk.data2[1][40] == null) {//РПЦ
        buffer[i][10] = ND;
        buffer[i][11] = ND;
        buffer[i][12] = ND;
        buffer[i][13] = ND;
        buffer[i][14] = ND;
        buffer[i][15] = ND;
      } else {
        gateways[2] = true;
        buffer[i][10] = ((Integer) kiosk.data2[1][40] / 1000 / 60) + " мин.";
        buffer[i][11] = kiosk.data2[1][41];
        buffer[i][12] = kiosk.data2[1][42];
        if (kiosk.data2[1][45] != null) {
          buffer[i][13] = ((Integer) kiosk.data2[1][45] / 1000 / 60) + " мин.";
        } else {
          buffer[i][13] = ND;
        }
        str = (String) kiosk.data2[1][43];
        if (str.contains(",")) str = "<html>" + str.replaceFirst(",", "<br>") + "</html>";
        buffer[i][14] = str;
        buffer[i][15] = kiosk.data2[1][44].toString();
      }
      if (kiosk.data2[1][50] == null) {//E-Port
        buffer[i][16] = ND;
        buffer[i][17] = ND;
        buffer[i][18] = ND;
        buffer[i][19] = ND;
        buffer[i][20] = ND;
        buffer[i][21] = ND;
        buffer[i][22] = ND;
      } else {
        gateways[3] = true;
        buffer[i][16] = ((Integer) kiosk.data2[1][50] / 1000 / 60) + " мин.";
        buffer[i][17] = kiosk.data2[1][51];
        buffer[i][18] = kiosk.data2[1][52];
        buffer[i][19] = kiosk.data2[1][53];
        buffer[i][20] = kiosk.data2[1][54].toString();
        buffer[i][21] = (Boolean) kiosk.data2[1][55] ? "да" : "нет";
        int ds = (Integer) kiosk.data2[1][56];
        if (ds == -1) str = "не используется";
        else if (ds == 0) str = "регистрация";
        else if (ds == 1) str = "зарегистрирована";
        buffer[i][22] = str;
      }
      if (kiosk.data2[1][60] == null) {//CyberPlat
        buffer[i][23] = ND;
        buffer[i][24] = ND;
        buffer[i][25] = ND;
        buffer[i][26] = ND;
        buffer[i][27] = ND;
        buffer[i][28] = ND;
      } else {
        gateways[4] = true;
        buffer[i][23] = ((Integer) kiosk.data2[1][60] / 1000 / 60) + " мин.";
        buffer[i][24] = kiosk.data2[1][61];
        buffer[i][25] = kiosk.data2[1][62];
        buffer[i][26] = kiosk.data2[1][63].toString();
        buffer[i][27] = kiosk.data2[1][64].toString();
        buffer[i][28] = kiosk.data2[1][65].toString();
      }
      if (kiosk.data2[1][70] == null) {//Город-К
        buffer[i][29] = ND;
        buffer[i][30] = ND;
        buffer[i][31] = ND;
        buffer[i][32] = ND;
      } else {
        gateways[5] = true;
        buffer[i][29] = ((Integer) kiosk.data2[1][70] / 1000 / 60) + " мин.";
        buffer[i][30] = kiosk.data2[1][71];
        buffer[i][31] = kiosk.data2[1][72];
        buffer[i][32] = kiosk.data2[1][73].toString();
      }
      if (kiosk.data2[1][80] == null) {//GreenPost
        buffer[i][33] = ND;
        buffer[i][34] = ND;
        buffer[i][35] = ND;
        buffer[i][36] = ND;
      } else {
        gateways[6] = true;
        buffer[i][33] = ((Integer) kiosk.data2[1][80] / 1000 / 60) + " мин.";
        buffer[i][34] = kiosk.data2[1][81];
        buffer[i][35] = kiosk.data2[1][82];
        buffer[i][36] = kiosk.data2[1][83].toString();
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

  public boolean isGateway(int num) {
    return gateways[num];
  }

  protected void updateData() {
    putKiosks(kiosks);
  }
}
