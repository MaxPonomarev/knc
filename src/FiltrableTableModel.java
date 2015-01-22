

import java.util.*;
import javax.swing.table.AbstractTableModel;

abstract class FiltrableTableModel extends AbstractTableModel {
  protected Object[][] data;
  protected String autoFilter = null;//����������, �������� 23003, ���� �� null, �� filter ������ ���� ����
  protected HashSet<String> filter = new HashSet<>();//������ ������� �������, ������� ���� ����������. ���� ����, �� ���� ���������� ���

  public synchronized HashSet<String> getFilter() {
    return filter;
  }

  public synchronized void setAutoFilter(String autoFilter) {
    filter.clear();
    this.autoFilter = autoFilter;
    updateData();
  }

  public synchronized void setFilter(String[] kioskNumbers) {
    autoFilter = null;
    filter.clear();
    Collections.addAll(filter, kioskNumbers);
    updateData();
  }

  public synchronized void setFilter(HashSet<String> filter) {
    autoFilter = null;
    this.filter = new HashSet<>(filter);
    updateData();
  }

  public synchronized void excludeFilter(String[] kioskNumbers) throws FiltrableTableModelException {
    autoFilter = null;
    if (filter.isEmpty()) {
      for (int i = 0; i < data.length - 1; i++) {
        filter.add(data[i][1].toString());
      }
    }
    for (String kioskNumber : kioskNumbers) {
      filter.remove(kioskNumber);
    }
    if (filter.isEmpty()) throw new FiltrableTableModelException("empty");
    updateData();
  }

  public synchronized void removeFilter() {
    autoFilter = null;
    filter.clear();
    updateData();
  }

  protected abstract void updateData();
}
