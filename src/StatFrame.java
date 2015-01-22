import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdesktop.swingx.JXSummaryTable;

class StatFrame extends JFrame implements ActionListener {
  private final String MAIN_TITLE = "Таблица статистики";
  private boolean updatingFlag = false;
  private String filterCurrentName = null;
  private String filterTitle = "";

  private StatTableModel tableModel;
  private FilterMenu filterMenu;
  private boolean groupMode = false;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
  private boolean firstShow = true;
  private StatTableRenderer statTableRenderer;
  private Date date1;//начало периода
  private Date date2;//конец периода
  private JPanel contentPane;
  private BorderLayout borderLayout = new BorderLayout();
  private ToolBarPanel3 toolBarPanel3;
  private JXSummaryTable table;
  private JScrollPane scrollPane;
  private JPopupMenu popupMenu = new JPopupMenu();

  public StatFrame() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateTitle() {
    String title = MAIN_TITLE;
    if (groupMode) title += KNC_Terminal.groupTitle;
    title += filterTitle;
    if (updatingFlag) title += KNC_Terminal.updatingTitle;
    this.setTitle(title);
  }

  private void jbInit() {
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout);
    this.setIconImages(KNC_Terminal.icons);
    this.setTitle(MAIN_TITLE);
    int y = 100;
    if (y+519 > Toolkit.getDefaultToolkit().getScreenSize().height-35) y = Toolkit.getDefaultToolkit().getScreenSize().height-35-519;
    if (y < 0) y = 0;
    this.setBounds(12, y, Toolkit.getDefaultToolkit().getScreenSize().width-24, 519);
    this.addComponentListener(new StatFrame_this_componentAdapter(this));
    if (KNC_Terminal.defaultTableMaximized > 0) this.setExtendedState(Frame.MAXIMIZED_BOTH);
    toolBarPanel3 = new ToolBarPanel3();
    toolBarPanel3.setActionListener(this);
    contentPane.add(toolBarPanel3, java.awt.BorderLayout.NORTH);
    tableModel = new StatTableModel();
    tableModel.addTableModelListener(new StatFrame_tableModel_tableModelAdapter(this));
    table = new JXSummaryTable(tableModel);
    table.setRowHeight(33);
    table.setColumnControlVisible(true);
    table.getColumnModel().getColumn(0).setMinWidth(20);
    table.getColumnModel().getColumn(0).setMaxWidth(30);
    table.getColumnModel().getColumn(1).setMinWidth(54);
    table.getColumnModel().getColumn(1).setMaxWidth(58);
    table.setHorizontalScrollEnabled(true);
    table.addMouseListener(new StatFrame_table_popupAdapter(this));
    table.addKeyListener(new StatFrame_table_keyAdapter(this));
    statTableRenderer = new StatTableRenderer(table);
    scrollPane = new JScrollPane(table);
    contentPane.add(scrollPane, java.awt.BorderLayout.CENTER);
    filterMenu = KNC_Terminal.filterMenuFactory.getMenu(KNC_Terminal.user.filters, true);
    filterMenu.addActionListener(new StatFrame_popupMenu_actionAdapter(this));
    popupMenu.add(filterMenu.getSubMenu());
  }

  public void this_componentShown(ComponentEvent e) {
    if (firstShow) {
      updatingFlag = true;
      updateTitle();
      date1 = toolBarPanel3.startDatePicker.getDate();
      date2 = KNC_Terminal.endOfDay(toolBarPanel3.endDatePicker.getDate());
      new At_net("GET_STATISTICS", new Request(KNC_Terminal.user, new Object[] {date1, date2}), this).start();
      firstShow = false;
    }
  }

  public void tableModel_tableChanged(TableModelEvent e) {
    table.packAll();
  }

  public void table_keyPressed(KeyEvent e) {
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E) {
      exportTable(false);
    }
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
      exportTable(true);
    }
  }

  public void table_maybeShowPopup(MouseEvent e) {
    int activeRow;
    String activeKioskNum;
    activeRow = table.rowAtPoint(new Point(e.getX(),e.getY()));
    if (activeRow == -1) {
      table.removeRowSelectionInterval(0, table.getRowCount()-1);
    } else {
      if (e.isPopupTrigger()) {
        if (table.convertRowIndexToModel(activeRow) == table.getModel().getRowCount()-1) return;
        activeKioskNum = tableModel.getValueAt(table.convertRowIndexToModel(activeRow), 1).toString();
        if (activeKioskNum.equals("")) return;
        if (!table.isRowSelected(activeRow)) table.setRowSelectionInterval(activeRow, activeRow);
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  public void popupMenu_actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(FilterMenu.RECONST_FILTERS)) {
      if (filterCurrentName != null) {
        for (int i=0; i<filterMenu.getSubMenu().getItemCount(); i++) {
          JMenuItem filterMenuItem = filterMenu.getSubMenu().getItem(i);
          if (filterMenuItem != null && filterMenuItem.getText().equals(filterCurrentName)) {
            if (filterMenuItem.getActionCommand().startsWith(FilterMenu.SET_FILTERA) || filterMenuItem.getActionCommand().startsWith(FilterMenu.SET_FILTER)) {
              filterMenuItem.doClick();
              break;
            }
          }
        }
      }
      return;
    }
    int[] rows = table.getSelectedRows();
    int[] buffer = new int[rows.length];
    boolean totalFlag = false;
    int j=0;
    for (int row : rows) {
      if (table.convertRowIndexToModel(row) != table.getModel().getRowCount() - 1) {
        buffer[j] = row;
      } else {
        totalFlag = true;
        j--;
      }
      j++;
    }
    if (totalFlag) {
      rows = new int[rows.length-1];
      System.arraycopy(buffer, 0, rows, 0, rows.length);
    }
    String[] kioskNumbers = new String[rows.length];
    for (int i=0; i<rows.length; i++) {
      kioskNumbers[i] = tableModel.getValueAt(table.convertRowIndexToModel(rows[i]), 1).toString();
    }
    if (e.getActionCommand().equals(FilterMenu.CANCEL_FILTER)) {
      tableModel.removeFilter();
      filterCurrentName = null;
      filterTitle = FilterMenu.getTitle(null);
      updateTitle();
      filterMenu.updateAfterCommand(e.getActionCommand());
    }
    if (e.getActionCommand().equals(FilterMenu.SAVE_FILTER)) {
      String filterName = FilterDialog.showSaveDialog(this);
      if (filterName != null) {
        TreeMap<String,HashSet<String>> newFilters = (KNC_Terminal.user.filters == null) ? new TreeMap<String,HashSet<String>>() : new TreeMap<>(KNC_Terminal.user.filters);
        newFilters.put(filterName, tableModel.getFilter());
        new At_net("SET_FILTERS", new Request(KNC_Terminal.user, new Object[] {newFilters, filterName, (byte) 1}), this).start();
      }
      return;
    }
    if (e.getActionCommand().equals(FilterMenu.ADD_FILTER)) {
      String filterName = FilterDialog.showAddDialog(this);
      if (filterName != null && KNC_Terminal.user.filters != null) {
        TreeMap<String,HashSet<String>> newFilters = new TreeMap<>(KNC_Terminal.user.filters);
        HashSet<String> filter = newFilters.get(filterName);
        Collections.addAll(filter, kioskNumbers);
        new At_net("SET_FILTERS", new Request(KNC_Terminal.user, new Object[] {newFilters, filterName, (byte) 1}), this).start();
      }
      return;
    }
    if (e.getActionCommand().equals(FilterMenu.DEL_FILTER)) {
      String filterName = FilterDialog.showDelDialog(this);
      if (filterName != null) {
        TreeMap<String,HashSet<String>> newFilters = (KNC_Terminal.user.filters == null) ? new TreeMap<String,HashSet<String>>() : new TreeMap<>(KNC_Terminal.user.filters);
        newFilters.remove(filterName);
        new At_net("SET_FILTERS", new Request(KNC_Terminal.user, new Object[] {newFilters, filterName, (byte) 0}), this).start();
      }
      return;
    }
    if (e.getActionCommand().equals(FilterMenu.INCLUDE)) {
      tableModel.setFilter(kioskNumbers);
      filterCurrentName = null;
      filterTitle = FilterMenu.getTitle("");
      updateTitle();
      filterMenu.updateAfterCommand(e.getActionCommand());
    }
    if (e.getActionCommand().equals(FilterMenu.EXCLUDE)) {
      try {
        tableModel.excludeFilter(kioskNumbers);
        filterCurrentName = null;
        filterTitle = FilterMenu.getTitle("");
        updateTitle();
        filterMenu.updateAfterCommand(e.getActionCommand());
      }
      catch (FiltrableTableModelException ex) {
        RPCMessage.showMessageDialog(this, "Нельзя исключить все киоски");
      }
    }
    if (e.getActionCommand().startsWith(FilterMenu.SET_FILTERA) || e.getActionCommand().startsWith(FilterMenu.SET_FILTER)) {
      filterCurrentName = FilterMenu.getFilterNameFromCommand(e.getActionCommand());
      if (e.getActionCommand().startsWith(FilterMenu.SET_FILTERA)) tableModel.setAutoFilter(KNC_Terminal.frame.getAutoFilters().get(filterCurrentName));
      if (e.getActionCommand().startsWith(FilterMenu.SET_FILTER)) tableModel.setFilter(KNC_Terminal.user.filters.get(filterCurrentName));
      filterTitle = FilterMenu.getTitle(filterCurrentName);
      updateTitle();
      filterMenu.updateAfterCommand(e.getActionCommand());
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_GET_STATISTICS")) {
      @SuppressWarnings("unchecked")
      final ArrayList<StatProfile> statList = (ArrayList<StatProfile>)((At_net)e.getSource()).getResult();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          KNC_Terminal.frame.updateAutoFilters(statList);
          statTableRenderer.clear();
          tableModel.putStats(statList);
          updatingFlag = false;
          updateTitle();
        }
      });
    }
    if (e.getActionCommand().equals("fail_GET_STATISTICS")) {
      updatingFlag = false;
      updateTitle();
      RPCMessage.showMessageDialog(this, "Ошибка соединения с центральным сервером");
    }
    if (e.getActionCommand().equals("finished_SET_FILTERS")) {
      UserProfile newUser = (UserProfile)((At_net)e.getSource()).getResult();
      if (newUser.name.equals("CHANGEERROR")) {
        RPCMessage.showMessageDialog(this, "Ошибка сервера: невозможно сохранить фильтр");
      } else {
        KNC_Terminal.user = newUser;
        final Object[] data = ((Request)((At_net)e.getSource()).getObject()).data;
        final JFrame finalThis = this;
        filterCurrentName = (String)data[1];
        KNC_Terminal.filterMenuFactory.reconstFilters(KNC_Terminal.user.filters);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            KNC_Terminal.filterMenuFactory.reconstFilters(KNC_Terminal.user.filters);
            if ((Byte) data[2] == 1) {
              RPCMessage.showMessageDialog(finalThis, "Фильтр \""+filterCurrentName+"\" успешно сохранен");
            } else {
              RPCMessage.showMessageDialog(finalThis, "Фильтр \""+filterCurrentName+"\" успешно удален");
            }
          }
        });
      }
    }
    if (e.getActionCommand().equals("fail_SET_FILTERS")) {
      if (((At_net)e.getSource()).getStatus())
        RPCMessage.showMessageDialog(this, "Команда отправлена на сервер, но подтверждение не было получено");
      else
        RPCMessage.showMessageDialog(this, "Ошибка соединения с центральным сервером");
    }
    if (e.getActionCommand().equals(ToolBarPanel3.BUTTON_SEARCH)) {
      table.getActionMap().get("find").actionPerformed(null);
    }
    if (e.getActionCommand().equals(ToolBarPanel3.BUTTON_EXPORT)) {
      exportTable(false);
    }
    if (e.getActionCommand().equals(ToolBarPanel3.BUTTON_SAVE)) {
      exportTable(true);
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_GROUP_ON)) {
      groupMode = true;
      updateTitle();
      filterMenu.setGrouping(true);
      tableModel.setCombination(true);
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_GROUP_OFF)) {
      groupMode = false;
      updateTitle();
      filterMenu.setGrouping(false);
      tableModel.setCombination(false);
    }
    if (e.getActionCommand().equals(ToolBarPanel3.BUTTON_GET)) {
      date1 = toolBarPanel3.startDatePicker.getDate();
      date2 = KNC_Terminal.endOfDay(toolBarPanel3.endDatePicker.getDate());
      if (date2.getTime()-date1.getTime() > 2678400001L) {
        RPCMessage.showMessageDialog(this, "Указан период больше месяца, уменьшите период");
        return;
      }
      updatingFlag = true;
      updateTitle();
      new At_net("GET_STATISTICS", new Request(KNC_Terminal.user, new Object[] {date1, date2}), this, 40000).start();
    }
  }

  private void exportTable(boolean toFile) {
    if (toFile) {
      KNC_Terminal.saveRegistryFileDialog.setDialogTitle("Сохранить таблицу статистики");
      KNC_Terminal.saveRegistryFileDialog.setSelectedFile(new File("Таблица статистики с "+dateFormat.format(date1)+" по "+dateFormat.format(date2)));
      if (KNC_Terminal.saveRegistryFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = KNC_Terminal.saveRegistryFileDialog.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) file = new File(file.getAbsolutePath()+".xls");
        if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \""+file.getName()+"\"?") == RPCDialog.Result.OK) {
          HSSFWorkbook wb = new HSSFWorkbook();
          wb = new ExcelCreater(wb, "Таблица статистики", table).createStatSheet();
          try (FileOutputStream out = new FileOutputStream(file)) {
            wb.write(out);
          }
          catch (IOException ex) {
            RPCMessage.showMessageDialog(this, "Не удалось сохранить таблицу статистики: "+ex.getMessage());
          }
        }
      }
    } else {
      try {
        File file = File.createTempFile("Таблица статистики с " + dateFormat.format(date1) + " по " + dateFormat.format(date2) + " ", ".xls");
        file.deleteOnExit();
        HSSFWorkbook wb = new HSSFWorkbook();
        wb = new ExcelCreater(wb, "Таблица статистики", table).createStatSheet();
        try (FileOutputStream out = new FileOutputStream(file)) {
          wb.write(out);
        }
        Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
      } catch (IOException ex) {
        RPCMessage.showMessageDialog(this, "Не удалось экспортировать таблицу статистики: " + ex.getMessage());
      }
    }
  }
}

class StatFrame_this_componentAdapter extends ComponentAdapter {
  private StatFrame adaptee;
  StatFrame_this_componentAdapter(StatFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void componentShown(ComponentEvent e) {
    adaptee.this_componentShown(e);
  }
}

class StatFrame_tableModel_tableModelAdapter implements TableModelListener {
  private StatFrame adaptee;
  StatFrame_tableModel_tableModelAdapter(StatFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel_tableChanged(e);
  }
}

class StatFrame_table_keyAdapter extends KeyAdapter {
  private StatFrame adaptee;
  StatFrame_table_keyAdapter(StatFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void keyPressed(KeyEvent e) {
    adaptee.table_keyPressed(e);
  }
}

class StatFrame_table_popupAdapter extends MouseAdapter {
  private StatFrame adaptee;
  StatFrame_table_popupAdapter(StatFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mousePressed(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
}

class StatFrame_popupMenu_actionAdapter implements ActionListener {
  private StatFrame adaptee;
  StatFrame_popupMenu_actionAdapter(StatFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.popupMenu_actionPerformed(e);
  }
}
