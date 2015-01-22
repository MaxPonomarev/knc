

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class FilterMenu {
  static final String CANCEL_FILTER = "CANCEL_FILTER";
  static final String INCLUDE = "INCLUDE";
  static final String EXCLUDE = "EXCLUDE";
  static final String SET_FILTER = "SET_FILTER_";
  static final String SET_FILTERA = "SET_FILTERA_";
  static final String SAVE_FILTER = "SAVE_FILTER";
  static final String ADD_FILTER = "ADD_FILTER";
  static final String DEL_FILTER = "DEL_FILTER";
  static final String RECONST_FILTERS = "RECONST_FILTERS";
  private static final String NO_NAME = "Без названия";
  //SIMPLE-простое меню
  //FILTERS-со списком шаблонов
  //FILTERS_SAVE_DEL-со списком шаблонов и сохранением/удалением
  private enum Type {SIMPLE, FILTERS, FILTERS_SAVE_DEL}
  private final Type type;
  private String[] filters = null;
  private ActionListener actionListener = null;
  private int autoFiltersCount = 0;
  private boolean grouping = false;
  private JMenu subMenu = new JMenu();
  private JMenuItem[] jMenuItemA = null;
  private JMenuItem[] jMenuItemF = null;
  private JMenuItem jMenuItem1 = new JMenuItem();
  private JMenuItem jMenuItem2 = new JMenuItem();
  private JMenuItem jMenuItem3 = new JMenuItem();
  private JMenuItem jMenuItem4 = new JMenuItem();
  private JMenuItem jMenuItem5 = new JMenuItem();
  private JMenuItem jMenuItem6 = new JMenuItem();

  private FilterMenu(Type type, String[] filters) {
    this.type = type;
    this.filters = filters;
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() {
    subMenu.setText("Фильтр");
    jMenuItem1.setText("Отменить фильтр");
    jMenuItem1.setActionCommand(CANCEL_FILTER);
    jMenuItem4.setText("Отфильтровать выделенное");
    jMenuItem4.setActionCommand(INCLUDE);
    jMenuItem5.setText("Исключить выделенное");
    jMenuItem5.setActionCommand(EXCLUDE);
    resetStatus();
    if (type != Type.SIMPLE) reconstFilters(filters);
    if (type == Type.FILTERS_SAVE_DEL) {
      jMenuItem2.setText("Сохранить фильтр...");
      jMenuItem2.setActionCommand(SAVE_FILTER);
      jMenuItem6.setText("Добавить в фильтр...");
      jMenuItem6.setActionCommand(ADD_FILTER);
      jMenuItem3.setText("Удалить фильтр...");
      jMenuItem3.setActionCommand(DEL_FILTER);
    }
    subMenu.add(jMenuItem1);
    subMenu.addSeparator();
    if (type == Type.FILTERS_SAVE_DEL) {
      subMenu.add(jMenuItem2);
      subMenu.add(jMenuItem6);
      subMenu.add(jMenuItem3);
      subMenu.addSeparator();
    }
    subMenu.add(jMenuItem4);
    subMenu.add(jMenuItem5);
  }

  public static FilterMenu createMenu() {
    return new FilterMenu(Type.SIMPLE, null);
  }

  public static FilterMenu createMenu(Map<String, HashSet<String>> filtersMap, boolean saveDelExt) {//ключи Map - фильтры
    String[] filters;
    if (filtersMap == null) filters = new String[0];
    else {
      filters = new String[filtersMap.size()];
      filters = filtersMap.keySet().toArray(filters);
    }
    return createMenu(filters, saveDelExt);
  }

  public static FilterMenu createMenu(String[] filters, boolean saveDelExt) {
    FilterMenu filterMenu;
    if (saveDelExt) filterMenu = new FilterMenu(Type.FILTERS_SAVE_DEL, filters);
    else filterMenu = new FilterMenu(Type.FILTERS, filters);
    return filterMenu;
  }

  public void addActionListener(ActionListener l) {
    jMenuItem1.addActionListener(l);
    jMenuItem4.addActionListener(l);
    jMenuItem5.addActionListener(l);
    if (type != Type.SIMPLE && jMenuItemA != null) {
      for (JMenuItem menuItem : jMenuItemA) {
        menuItem.addActionListener(l);
      }
    }
    if (type != Type.SIMPLE && jMenuItemF != null) {
      for (JMenuItem menuItem : jMenuItemF) {
        menuItem.addActionListener(l);
      }
    }
    if (type == Type.FILTERS_SAVE_DEL) {
      jMenuItem2.addActionListener(l);
      jMenuItem6.addActionListener(l);
      jMenuItem3.addActionListener(l);
    }
    actionListener = l;
  }

  public JMenu getSubMenu() {
    return subMenu;
  }

  public void addToPopupMenu(JPopupMenu popupMenu) {
    if (type == Type.SIMPLE) {
      popupMenu.add(jMenuItem1);
      popupMenu.addSeparator();
      popupMenu.add(jMenuItem4);
      popupMenu.add(jMenuItem5);
    }
  }

  public void updateAfterCommand(String command) {
    if (command.equals(CANCEL_FILTER)) {
      jMenuItem1.setEnabled(false);
      jMenuItem2.setEnabled(false);
    }
    if (command.equals(INCLUDE)) {
      jMenuItem1.setEnabled(true);
      jMenuItem2.setEnabled(true);
    }
    if (command.equals(EXCLUDE)) {
      jMenuItem1.setEnabled(true);
      jMenuItem2.setEnabled(true);
    }
    if (command.startsWith(FilterMenu.SET_FILTERA)) {
      jMenuItem1.setEnabled(true);
      jMenuItem2.setEnabled(false);
    }
    if (command.startsWith(FilterMenu.SET_FILTER)) {
      jMenuItem1.setEnabled(true);
      jMenuItem2.setEnabled(true);
    }
  }

  public void resetStatus() {
    jMenuItem1.setEnabled(false);
    jMenuItem2.setEnabled(false);
  }

  public void reconstAutoFilters(Map<String, String> filtersMap) {//ключи Map - фильтры
    String[] filters;
    if (filtersMap == null) filters = new String[0];
    else {
      filters = new String[filtersMap.size()];
      filters = filtersMap.keySet().toArray(filters);
    }
    reconstAutoFilters(filters);
  }

  public void reconstAutoFilters(String[] filters) {
    if (type == Type.SIMPLE) return;
    if (jMenuItemA != null) {
      for (JMenuItem menuItem : jMenuItemA) subMenu.remove(menuItem);
    }
    if (filters != null && filters.length > 0) {
      jMenuItemA = new JMenuItem[filters.length];
      for (int i = 0; i < jMenuItemA.length; i++) {
        jMenuItemA[i] = new JMenuItem();
        jMenuItemA[i].setText(filters[i]);
        jMenuItemA[i].setActionCommand(SET_FILTERA + filters[i]);
        if (actionListener != null) jMenuItemA[i].addActionListener(actionListener);
      }
      for (int i = jMenuItemA.length - 1; i >= 0; i--)
        subMenu.insert(jMenuItemA[i], 0);
      autoFiltersCount = jMenuItemA.length;
    } else {
      jMenuItemA = null;
    }
    if (actionListener != null) actionListener.actionPerformed(new ActionEvent(this, 0, RECONST_FILTERS));
  }

  public void reconstFilters(Map<String, HashSet<String>> filtersMap) {//ключи Map - фильтры
    String[] filters;
    if (filtersMap == null) filters = new String[0];
    else {
      filters = new String[filtersMap.size()];
      filters = filtersMap.keySet().toArray(filters);
    }
    reconstFilters(filters);
  }

  public void reconstFilters(String[] newFilters) {
    if (type == Type.SIMPLE) return;
    this.filters = newFilters;
    if (jMenuItemF != null) {
      for (JMenuItem menuItem : jMenuItemF) subMenu.remove(menuItem);
    }
    if (filters != null && filters.length > 0) {
      jMenuItemF = new JMenuItem[filters.length];
      for (int i = 0; i < jMenuItemF.length; i++) {
        jMenuItemF[i] = new JMenuItem();
        jMenuItemF[i].setText(filters[i]);
        jMenuItemF[i].setActionCommand(SET_FILTER + filters[i]);
        if (actionListener != null) jMenuItemF[i].addActionListener(actionListener);
      }
      for (int i = jMenuItemF.length - 1; i >= 0; i--)
        subMenu.insert(jMenuItemF[i], autoFiltersCount);
      jMenuItem3.setEnabled(true);
      if (!grouping) jMenuItem6.setEnabled(true);
    } else {
      jMenuItemF = null;
      jMenuItem3.setEnabled(false);
      jMenuItem6.setEnabled(false);
    }
    if (actionListener != null) actionListener.actionPerformed(new ActionEvent(this, 0, RECONST_FILTERS));
  }

  public void setFiltersEnabled(boolean b) {
    if (type == Type.SIMPLE) return;
    if (jMenuItemA != null) {
      for (JMenuItem menuItem : jMenuItemA) menuItem.setEnabled(b);
    }
    if (jMenuItemF != null) {
      for (JMenuItem menuItem : jMenuItemF) menuItem.setEnabled(b);
    }
  }

  public void setGrouping(boolean grouping) {
    this.grouping = grouping;
    if (grouping) {
      jMenuItem4.setEnabled(false);
      jMenuItem5.setEnabled(false);
      jMenuItem6.setEnabled(false);
    } else {
      jMenuItem4.setEnabled(true);
      jMenuItem5.setEnabled(true);
      if (filters != null && filters.length > 0) jMenuItem6.setEnabled(true);
    }
  }

  static public String getTitle(String filterName) {
    if (filterName == null) return "";
    if (filterName.length() == 0) return " (Фильтр активен: \"Без названия\")";
    else return " (Фильтр активен: \"" + filterName + "\")";
  }

  static public String getNoName() {
    return NO_NAME;
  }

  static public String getFilterNameFromCommand(String command) {
    if (command.startsWith(FilterMenu.SET_FILTERA)) {
      return command.replaceAll(FilterMenu.SET_FILTERA, "");
    } else if (command.startsWith(FilterMenu.SET_FILTER)) {
      return command.replaceAll(FilterMenu.SET_FILTER, "");
    } else return null;
  }
}
