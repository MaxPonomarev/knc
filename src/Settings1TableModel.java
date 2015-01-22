import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

class Settings1TableModel extends FiltrableTableModel implements SettingsTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final String[] columnNames = {" ","�","�����","Java","������","����������","��������� ������","<HTML><CENTER>����������<br>������������</CENTER></HTML>","��� ������","<HTML><CENTER>������ �����<br>MonitorOFF</CENTER></HTML>","<HTML><CENTER>���� ��<br>AcceptorPort</CENTER></HTML>","<HTML><CENTER>�������� ��<br>AcceptorProtocol</CENTER></HTML>","<HTML><CENTER>��������������<br>AutoIncass</CENTER></HTML>","<HTML><CENTER>����������<br>AutoRun</CENTER></HTML>","<HTML><CENTER>����� ��������������<br>Backup</CENTER></HTML>","<HTML><CENTER>���� ��������������<br>BackupTo</CENTER></HTML>","<HTML><CENTER>���� � flash-�����<br>FlashPath</CENTER></HTML>","<HTML><CENTER>���������� flash-�����<br>FlashSafe</CENTER></HTML>","<HTML><CENTER>�������� ����� ��<br>InitOperAllow</CENTER></HTML>","<HTML><CENTER>����� ������ ��������<br>InitOperID</CENTER></HTML>","<HTML><CENTER>����� �������<br>InterfaceTimeOut</CENTER></HTML>","<HTML><CENTER>���-�� ������� ������� ������<br>MaxBillReturns</CENTER></HTML>","<HTML><CENTER>������ NFC<br>NFC</CENTER></HTML>","<HTML><CENTER>���������� ��� ��������� ������<br>PaperLock</CENTER></HTML>","<HTML><CENTER>����� ������ ������<br>PaperRollLength</CENTER></HTML>","<HTML><CENTER>�������<br>Printer</CENTER></HTML>","<HTML><CENTER>������������ ������<br>PrinterDelay</CENTER></HTML>","<HTML><CENTER>����������<br>Redial</CENTER></HTML>","<HTML><CENTER>�������� ����������<br>RedialEntryName</CENTER></HTML>","<HTML><CENTER>����� ����������<br>RedialLogin</CENTER></HTML>","<HTML><CENTER>������ ����������<br>RedialPassword</CENTER></HTML>","<HTML><CENTER>������ QR-����<br>SBQR</CENTER></HTML>","<HTML><CENTER>������ �������<br>StackCapacity</CENTER></HTML>"};
  private final Object[][] initData = {Collections.nCopies(columnNames.length, "").toArray()};
  private ArrayList<KioskProfile> kiosks = null;
  private ArrayList<KioskProfile> filteredKiosks = null;//��������������� ������, ���� ����� kiosks, ���� ������ ����
  private boolean updates = false;

  public Settings1TableModel() {
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
    updates = false;
    Object[][] buffer = new Object[filteredKiosks.size()+1][columnNames.length];
    String str;
    for (int i = 0; i < filteredKiosks.size(); i++) {
      KioskProfile kiosk = filteredKiosks.get(i);
      buffer[i][0] = iconStat0;
      buffer[i][1] = kiosk.data[1];
      buffer[i][2] = kiosk.data[2];
      buffer[i][3] = (kiosk.data2[0].length > 1) ? kiosk.data2[0][1] : "";
      buffer[i][4] = "Kiosk " + kiosk.data2[0][0];
      if (kiosk.data2[2] == null) str = "";
      else {
        updates = true;
        str = (String) kiosk.data2[2][0];
      }
      buffer[i][5] = str;
      buffer[i][6] = dateFormat.format((Date) kiosk.data2[7][0]);
      buffer[i][7] = (kiosk.data2.length > 11 && (Boolean) kiosk.data2[11][0]) ? "��" : "���";
      buffer[i][8] = (kiosk.data2[1][22] == null) ? "" : kiosk.data2[1][22];//��� ������
      buffer[i][9] = kiosk.data2[1][12];//������ �����
      buffer[i][10] = kiosk.data2[1][0];
      buffer[i][11] = kiosk.data2[1][1];
      buffer[i][12] = (Boolean) kiosk.data2[1][2] ? "��" : "���";
      buffer[i][13] = kiosk.data2[1][3];
      buffer[i][14] = kiosk.data2[1][4];
      buffer[i][15] = kiosk.data2[1][5];
      buffer[i][16] = kiosk.data2[1][6];
      buffer[i][17] = (Boolean) kiosk.data2[1][7] ? "����������" : "�����������";
      buffer[i][18] = (Boolean) kiosk.data2[1][8] ? "���������" : "���������";
      buffer[i][19] = kiosk.data2[1][9];
      buffer[i][20] = ((Integer) kiosk.data2[1][10] / 1000) + " ���.";
      buffer[i][21] = kiosk.data2[1][11];
      buffer[i][22] = (kiosk.data2[1][26] != null && (Boolean) kiosk.data2[1][26]) ? "����������" : "���";
      buffer[i][23] = (Boolean) kiosk.data2[1][14] ? "��" : "���";
      buffer[i][24] = new PaperLength((Integer) kiosk.data2[1][15], true);
      buffer[i][25] = KNC_Terminal.getPrinterName((Integer) kiosk.data2[1][16], true);
      buffer[i][26] = ((Integer) kiosk.data2[1][17] / 1000) + " ���.";
      int wd = (Integer) kiosk.data2[1][18];
      if (wd == 0) str = "��������";
      else if (wd == 1) str = "�������";
      else if (wd == 2) str = "����������";
      else str = "";
      buffer[i][27] = str;
      buffer[i][28] = kiosk.data2[1][19];
      buffer[i][29] = kiosk.data2[1][20];
      buffer[i][30] = kiosk.data2[1][21];
      buffer[i][31] = (kiosk.data2[1][27] != null && (Boolean) kiosk.data2[1][27]) ? "��" : "���";
      buffer[i][32] = kiosk.data2[1][23];
    }
    if (buffer.length > 1) {
      int lastRow = buffer.length - 1;
      buffer[lastRow][0] = iconStat0;
      buffer[lastRow][1] = filteredKiosks.size();
      buffer[lastRow][2] = "�����";
    }
    if (buffer.length > 1) data = buffer;
    else data = initData;
    fireTableDataChanged();
  }

  public synchronized KioskProfile[] getKiosks() {//�������� ������ ������� (�������������)
    KioskProfile[] kioskProfiles = new KioskProfile[filteredKiosks.size()];
    kioskProfiles = filteredKiosks.toArray(kioskProfiles);
    return kioskProfiles;
  }

  public boolean isUpdates() {
    return updates;
  }

  protected void updateData() {
    putKiosks(kiosks);
  }
}
