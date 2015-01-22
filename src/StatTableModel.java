import java.util.*;
import javax.swing.*;

class StatTableModel extends FiltrableTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final String[] columnNames = {" ","№","Адрес","Работа","Простой","из-за связи","из-за прочих ошибок","График работы"};
  private final Object[][] initData = {Collections.nCopies(columnNames.length, "").toArray()};
  private ArrayList<StatProfile> stats = null;
  private ArrayList<StatProfile> filteredStats = null;//Отфильтрованный список, либо равен stats, если фильтр пуст
  private boolean combination = false;//комбинировать киоски с одним адресом в один киоск
  static final int COLUMN_GRAPH = 7;

  public StatTableModel() {
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

  public synchronized void putStats(ArrayList<StatProfile> stats) {
    this.stats = stats;
    if (autoFilter != null) {
      filteredStats = new ArrayList<>();
      for (StatProfile stat : stats) {
        String kioskNumber = stat.number;
        if (kioskNumber.startsWith(autoFilter)) filteredStats.add(stat);
      }
    } else if (!filter.isEmpty()) {
      filteredStats = new ArrayList<>();
      for (StatProfile stat : stats) {
        String kioskNumber = stat.number;
        if (filter.contains(kioskNumber)) filteredStats.add(stat);
      }
    } else filteredStats = stats;

    if (combination) {
      HashMap<String,StatProfile> map = new HashMap<>();
      for (StatProfile stat : filteredStats) {
        String address = removeTags(stat.address);
        if (map.containsKey(address)) {
          StatProfile combinedStat = combine(map.get(address), stat);
          map.put(address, combinedStat);
        } else map.put(address, stat);
      }
      filteredStats = new ArrayList<>();
      filteredStats.addAll(map.values());
    }

    Object[][] buffer = new Object[filteredStats.size()+1][columnNames.length];
    int ts = 0; //кол-во
    int ts1 = 0; //работа
    int ts2 = 0; //простой
    int ts3 = 0; //из-за связи
    int ts4 = 0; //из-за прочих ошибок
    for (int i=0; i<filteredStats.size(); i++) {
      StatProfile stat = filteredStats.get(i);
      buffer[i][0] = iconStat0;
      buffer[i][1] = stat.number;
      buffer[i][2] = stat.address;
      int s = 0; //кол-во
      int s1 = 0; //работа
      int s2 = 0; //простой
      int s3 = 0; //из-за связи
      int s4 = 0; //из-за прочих ошибок
      for (int j=0; j<stat.data.length; j++) {
        if (stat.data[j] == -1) continue;
        s++;
        if (stat.data[j] == 0 || stat.data[j] == 1) s1++;
        else if (stat.data[j] == 2 || stat.data[j] == 3) {
          s2++;
          s4++;
        } else if (stat.data[j] == 10 || stat.data[j] == 11) {
          s2++;
          s3++;
        } else if (stat.data[j] == 12 || stat.data[j] == 13) {
          s2++;
          s4++;
        }
      }
      ts += s;
      ts1 += s1;
      ts2 += s2;
      ts3 += s3;
      ts4 += s4;
      int ps1 = (int)round((double)s1/s*100, 0);
      int ps2 = (int)round((double)s2/s*100, 0);
      int ps3 = (int)round((double)s3/s*100, 0);
      int ps4 = (int)round((double)s4/s*100, 0);
      buffer[i][3] = new IntegerProc(ps1);
      buffer[i][4] = new IntegerProc(ps2);
      buffer[i][5] = new IntegerProc(ps3);
      buffer[i][6] = new IntegerProc(ps4);
      buffer[i][7] = stat;//stat.data.length+" "+Arrays.toString(stat.data);
    }
    if (buffer.length > 1) {
      int lastRow = buffer.length-1;
      buffer[lastRow][0] = iconStat0;
      buffer[lastRow][1] = filteredStats.size();
      buffer[lastRow][2] = "Итого";
      int ps1 = (int)round((double)ts1/ts*100, 0);
      int ps2 = (int)round((double)ts2/ts*100, 0);
      int ps3 = (int)round((double)ts3/ts*100, 0);
      int ps4 = (int)round((double)ts4/ts*100, 0);
      buffer[lastRow][3] = new IntegerProc(ps1);
      buffer[lastRow][4] = new IntegerProc(ps2);
      buffer[lastRow][5] = new IntegerProc(ps3);
      buffer[lastRow][6] = new IntegerProc(ps4);
    }
    if (buffer.length > 1) data = buffer; else data = initData;
    fireTableDataChanged();
  }

  public void setCombination(boolean combination) {
    this.combination = combination;
    if (stats != null) updateData();
  }

  protected void updateData() {
    putStats(stats);
  }

  //к профилю 1 добавляется профиль 2 и возвращается результат В НОВОМ StatProfile
  private StatProfile combine(StatProfile stat1, StatProfile stat2) {
    StatProfile stat = new StatProfile();
    String str;
    stat.from = stat1.from;
    stat.to = stat1.to;
    stat.interval = stat1.interval;
    str = stat1.number;
    if (!str.contains("[")) stat.number = "[2]";
    else {
      str = str.replaceAll("\\[", "");
      str = str.replaceAll("]", "");
      int num = 1+Integer.parseInt(str);
      stat.number = "["+num+"]";
    }
    str = stat1.address;
    str = str.replaceAll(" \\[.+?]", "");
    str = str.replaceAll("\\[.+?]", "");
    stat.address = str;
    stat.data = new byte[stat1.data.length];
    for (int i=0; i<stat.data.length; i++) {
      if (stat1.data[i] == 0 || stat2.data[i] == 0) stat.data[i] = 0;
      else if (stat1.data[i] == 1 || stat2.data[i] == 1) stat.data[i] = 1;
      else if (stat1.data[i] == 2 || stat2.data[i] == 2) stat.data[i] = 2;
      else if (stat1.data[i] == 3 || stat2.data[i] == 3) stat.data[i] = 3;
      else if (stat1.data[i] == 10 || stat2.data[i] == 10) stat.data[i] = 10;
      else if (stat1.data[i] == 11 || stat2.data[i] == 11) stat.data[i] = 11;
      else if (stat1.data[i] == 12 || stat2.data[i] == 12) stat.data[i] = 12;
      else if (stat1.data[i] == 13 || stat2.data[i] == 13) stat.data[i] = 13;
      else stat.data[i] = -1;
    }
    return stat;
  }

  private String removeTags(String str) {
    str = str.replaceAll("<.+?>", "");
    str = str.replaceAll("\\[.+?]", "");
    str = str.replaceAll(" ", "");
    return str;
  }

  private double round(double value, int quantity) {
    return KNC_Terminal.round(value, quantity);
  }
}
