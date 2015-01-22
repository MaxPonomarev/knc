import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

class KNCTableModel extends FiltrableTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final ImageIcon iconStat1 = new ImageIcon("images/4_kiosk_norm.png");
  private final ImageIcon iconStat2 = new ImageIcon("images/2_kiosk_yell.png");
  private final ImageIcon iconStat3 = new ImageIcon("images/1_kiosk_red.png");
  private final ImageIcon iconStatOff = new ImageIcon("images/3_kiosk_black.png");
  private final ActionListener listener;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  private final String[] columnNames = {" ","№","Адрес","Дата инкассации","Сумма","Купюры","Стекер","Операции","Ошибки","Лента","Буфер","Связь","Последняя операция","Комментарий"};
  private ArrayList<KioskProfile> kiosks = null;//Полный список киосков, который прислал сервер
  private ArrayList<KioskProfile> filteredKiosks = null;//Отфильтрованный список киосков, либо равен kiosks, если фильтр пуст
  private HashMap<String,String> internalComments = new HashMap<>();//Номер киоска=>внутренний комментарий
  private HashMap<String,ArrayList<AdminCommand>> requestResults = new HashMap<>();//Номер киоска=>результат запроса
  private boolean bufferPresent = false;
  static final int COLUMN_STACKER = 6;
  static final int COLUMN_PAPER = 9;
  static final int COLUMN_BUFFER = 10;
  static final int COLUMN_NET = 11;
  static final int COLUMN_LASTOPER = 12;
  static final int COLUMN_COMMENT = 13;

  public KNCTableModel(ActionListener listener) {
    super();
    this.listener = listener;
    data = new Object[][] {Collections.nCopies(columnNames.length, "").toArray()};
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

  public synchronized void putKiosks(ArrayList<KioskProfile> kiosks, ArrayList<AdminCommand> results) {
    this.kiosks = kiosks;
    for (AdminCommand result : results) {
      String kioskNumber = result.kiosk_number;
      if (requestResults.containsKey(kioskNumber)) {
        ArrayList<AdminCommand> list = requestResults.get(kioskNumber);
        list.add(result);
      } else {
        ArrayList<AdminCommand> newList = new ArrayList<>();
        newList.add(result);
        requestResults.put(kioskNumber, newList);
      }
      internalComments.put(kioskNumber, KNC_Terminal.wasGot);
    }
    if (autoFilter != null) {
      filteredKiosks = new ArrayList<>();
      for (KioskProfile kiosk : kiosks) {
        String kioskNumber = (String) kiosk.getData()[1];
        if (kioskNumber.startsWith(autoFilter)) filteredKiosks.add(kiosk);
      }
    } else if (!filter.isEmpty()) {
      filteredKiosks = new ArrayList<>();
      for (KioskProfile kiosk : kiosks) {
        String kioskNumber = (String) kiosk.getData()[1];
        if (filter.contains(kioskNumber)) filteredKiosks.add(kiosk);
      }
    } else filteredKiosks = kiosks;
    Object[][] buffer = new Object[filteredKiosks.size()+1][columnNames.length];
    KioskProfile kiosk;
    Object[] kioskData;
    int totalSum = 0;
    int totalDenom = 0;
    int opers = 0;
    int errors = 0;
    bufferPresent = false;
    for (int i=0; i<filteredKiosks.size(); i++) {
      kiosk = filteredKiosks.get(i);
      kioskData = kiosk.getData();
      totalSum += (Integer)kioskData[4];
      totalDenom += (Integer)kioskData[5];
      opers += (Integer)kioskData[7];
      errors += (Integer)kioskData[8];
      if (kioskData[0].equals("0")) buffer[i][0] = iconStat0;
      if (kioskData[0].equals("1")) buffer[i][0] = iconStat1;
      if (kioskData[0].equals("2")) buffer[i][0] = iconStat2;
      if (kioskData[0].equals("3")) buffer[i][0] = iconStat3;
      if (kiosk.timeout) {
        if (kioskData[0].equals("0")) buffer[i][0] = iconStatOff;
        if (kioskData[0].equals("1")) buffer[i][0] = iconStatOff;
      }
      buffer[i][1] = kioskData[1];
      buffer[i][2] = kioskData[2];
      if (kioskData[3].equals("2000-01-01 00:00:00")) {
        buffer[i][3] = "нет";
      } else {
        buffer[i][3] = kioskData[3];
      }
      buffer[i][4] = kioskData[4];
      buffer[i][5] = kioskData[5];
      buffer[i][6] = kioskData[6];
      buffer[i][7] = kioskData[7];
      buffer[i][8] = kioskData[8];
      buffer[i][9] = new PaperLength((Integer) kioskData[9], false);
      if (kioskData.length > 12 && kioskData[12] != null) {
        buffer[i][COLUMN_BUFFER] = kioskData[12];
        bufferPresent = true;
      } else {
        buffer[i][COLUMN_BUFFER] = "";
      }
      if (kioskData.length > 13) {
        buffer[i][COLUMN_NET] = new IntegerProc(((int[])kioskData[13])[0]);
      } else {
        buffer[i][COLUMN_NET] = "";
      }
      if (kioskData[10].equals("2000-01-01 00:00:00")) {
        buffer[i][COLUMN_LASTOPER] = "нет";
      } else {
        buffer[i][COLUMN_LASTOPER] = kioskData[10];
      }
      buffer[i][COLUMN_COMMENT] = kioskData[11];
      if (kiosk.timeout) {
        if (!buffer[i][COLUMN_COMMENT].equals("")) buffer[i][COLUMN_COMMENT] = buffer[i][COLUMN_COMMENT] + "; ";
        buffer[i][COLUMN_COMMENT] = buffer[i][COLUMN_COMMENT] + "нет связи с "+dateFormat.format(new Date(kiosk.lat));
      }
      String kioskNumber = (String) kioskData[1];
      if (internalComments.containsKey(kioskNumber)) {
        if (!buffer[i][COLUMN_COMMENT].equals("")) buffer[i][COLUMN_COMMENT] = internalComments.get(kioskNumber) + "; " + buffer[i][COLUMN_COMMENT];
        else buffer[i][COLUMN_COMMENT] = internalComments.get(kioskNumber);
      }
    }
    int lastRow = buffer.length-1;
    buffer[lastRow][0] = iconStat0;
    buffer[lastRow][1] = filteredKiosks.size();
    buffer[lastRow][2] = "Итого";
    buffer[lastRow][3] = "";
    buffer[lastRow][4] = totalSum;
    buffer[lastRow][5] = totalDenom;
    buffer[lastRow][6] = null;
    buffer[lastRow][7] = opers;
    buffer[lastRow][8] = errors;
    boolean fullUpdate = false;
    if (data.length != buffer.length) fullUpdate = true;
    if (buffer.length > 1) data = buffer; else data = new Object[][] {Collections.nCopies(columnNames.length, "").toArray()};
    if (fullUpdate) fireTableDataChanged(); else fireTableRowsUpdated(0, data.length-1);
  }

  public synchronized KioskProfile[] getKiosks() {//получить список киосков (отфильтровано)
    KioskProfile[] kioskProfiles = new KioskProfile[filteredKiosks.size()];
    kioskProfiles = filteredKiosks.toArray(kioskProfiles);
    return kioskProfiles;
  }

  public synchronized ArrayList<AdminCommand> getResults(String[] kioskNumbers) {
    ArrayList<AdminCommand> result = new ArrayList<>();
    for (String kioskNumber : kioskNumbers) {
      if (requestResults.containsKey(kioskNumber)) result.addAll(requestResults.get(kioskNumber));
    }
    return result;
  }

  public synchronized HashMap<String,ArrayList<AdminCommand>> getResults() {
    return requestResults;
  }

  public synchronized void setResults(HashMap<String,ArrayList<AdminCommand>> requestResults) {
    this.requestResults = requestResults;
    String[] kioskNumbers = new String[requestResults.keySet().size()];
    kioskNumbers = requestResults.keySet().toArray(kioskNumbers);
    putInternalComments(kioskNumbers, KNC_Terminal.wasLoaded);
  }

  public synchronized boolean isResultExists(String kioskNumber) {
    return requestResults.containsKey(kioskNumber);
  }

  public synchronized void putInternalComments(String[] kioskNumbers, String comment) {
    for (String kioskNumber : kioskNumbers) {
      internalComments.put(kioskNumber, comment);
    }
    for (int i=0; i<data.length; i++) {
      if (internalComments.containsKey(data[i][1].toString())) {
        reconstructComment(i);
      }
    }
    fireTableRowsUpdated(0, data.length-1);
    listener.actionPerformed(new ActionEvent(this, 0, "comment_updated"));
  }

  public synchronized void clearInternalComments(String[] kioskNumbers, String comment) {
    for (String kioskNumber : kioskNumbers) {
      if (internalComments.containsKey(kioskNumber) && internalComments.get(kioskNumber).equals(comment)) {
        internalComments.remove(kioskNumber);
        if (isResultExists(kioskNumber)) internalComments.put(kioskNumber, KNC_Terminal.wasLoaded);
      }
    }
    for (int i=0; i<data.length-1; i++) {
      reconstructComment(i);
    }
    fireTableRowsUpdated(0, data.length-1);
    listener.actionPerformed(new ActionEvent(this, 0, "comment_updated"));
  }

  public synchronized void clearResults(String[] kioskNumbers) {
    for (String kioskNumber : kioskNumbers) {
      requestResults.remove(kioskNumber);
    }
  }

  public boolean isBufferPresent() {
    return bufferPresent;
  }

  protected void updateData() {
    putKiosks(kiosks, new ArrayList<AdminCommand>());
  }

  private void reconstructComment(int i) {
    KioskProfile kiosk = filteredKiosks.get(i);
    Object[] kioskData = kiosk.getData();
    String kioskNumber = kioskData[1].toString();
    String buffer = kioskData[11].toString();
    if (kiosk.timeout) {
      if (!buffer.equals("")) buffer += " ";
      buffer = buffer + "нет связи с "+dateFormat.format(new Date(kiosk.lat));
    }
    if (internalComments.containsKey(kioskNumber)) {
      if (!buffer.equals("")) buffer = internalComments.get(kioskNumber) + "; " + buffer;
      else buffer = internalComments.get(kioskNumber);
    }
    data[i][COLUMN_COMMENT] = buffer;
  }
}
