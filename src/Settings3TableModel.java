import java.util.*;
import javax.swing.*;

class Settings3TableModel extends FiltrableTableModel implements SettingsTableModel {
  private final ImageIcon iconStat0 = new ImageIcon("images/5_kiosk_norm.png");
  private final String[] initColumnNames = {" ","№","Адрес","Описание"};
  private String[] columnNames = initColumnNames;
  private final Object[][] initData = {Collections.nCopies(initColumnNames.length, "").toArray()};
  private ArrayList<KioskProfile> kiosks = null;
  private ArrayList<KioskProfile> filteredKiosks = null;//Отфильтрованный список, либо равен kiosks, если фильтр пуст

  public Settings3TableModel() {
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

    TreeSet<KNCProduct> allProducts = new TreeSet<>();
    HashMap<KioskProfile, HashMap<Integer, KNCProduct>> map = new HashMap<>();
    for (KioskProfile kiosk : filteredKiosks) {
      HashMap<Integer, KNCProduct> kioskProducts = new HashMap<>();
      Object[][] prodList = kiosk.productList;
      for (Object[] product : prodList) {
        if (product.length < 3) continue;
        KNCProduct kncProduct = new KNCProduct(product);
        kioskProducts.put(kncProduct.getId(), kncProduct);
        allProducts.add(kncProduct);
      }
      map.put(kiosk, kioskProducts);
    }

    String[] wasColumnNames = columnNames;
    columnNames = new String[initColumnNames.length + allProducts.size()];
    System.arraycopy(initColumnNames, 0, columnNames, 0, initColumnNames.length);
    int index = initColumnNames.length;
    for (KNCProduct product : allProducts) {
      columnNames[index] = product.getName();
      index++;
    }

    Object[][] buffer = new Object[filteredKiosks.size() + 1][columnNames.length];
    for (int i = 0; i < filteredKiosks.size(); i++) {
      KioskProfile kiosk = filteredKiosks.get(i);
      buffer[i][0] = iconStat0;
      buffer[i][1] = kiosk.data[1];
      buffer[i][2] = kiosk.data[2];
      buffer[i][3] = (kiosk.data2.length > 13 && kiosk.data2[13][0] != null) ? kiosk.data2[13][0] : "";
      int j = initColumnNames.length;
      HashMap<Integer, KNCProduct> kioskProducts = map.get(kiosk);
      for (KNCProduct allProduct : allProducts) {
        KNCProduct product = kioskProducts.get(allProduct.getId());
        buffer[i][j] = (product == null) ? "" : product.toString();
        j++;
      }
    }

    for (int j = 4; j < buffer[0].length; j++) {//поиск отличающихся настроек комиссий по столбцам
      HashMap<String, Integer> diffMap = new HashMap<>();
      for (int i = 0; i < buffer.length - 1; i++) {//анализируем столбец
        String str = (String) buffer[i][j];
        if (str.equals("")) continue;
        if (diffMap.containsKey(str)) diffMap.put(str, diffMap.get(str) + 1);
        else diffMap.put(str, 1);
      }
      if (diffMap.size() == 1) continue;//услуга одинаково настроена
      int min = Integer.MAX_VALUE;
      int max = 0;
      for (Integer integer : diffMap.values()) {//находим максимальное число одинаковых значений в столбце (это значение есть основное)
        min = Math.min(min, integer);
        max = Math.max(max, integer);
      }
      for (int i = 0; i < buffer.length - 1; i++) {//вносим изменения в столбце где нужно
        String str = (String) buffer[i][j];
        if (str.equals("")) continue;
        if (diffMap.get(str) != max || diffMap.get(str) == min) {
          str = str.replace("</html>", "<diff></html>");
          buffer[i][j] = str;
        }
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
    if (wasColumnNames.length == columnNames.length) fireTableDataChanged();
    else fireTableStructureChanged();
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
