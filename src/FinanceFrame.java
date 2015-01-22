import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdesktop.swingx.JXSummaryTable;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.renderer.*;

class FinanceFrame extends JFrame implements ActionListener {
  private final String MAIN_TITLE = "Финансовая сводка за последние 30 дней";
  private boolean updatingFlag = false;
  private String filterCurrentName = null;
  private String filterTitle = "";

  private FinanceTableModel tableModel;
  private FilterMenu filterMenu;
  private boolean groupMode = false;
  private JPanel contentPane;
  private BorderLayout borderLayout = new BorderLayout();
  private ToolBarPanel1 toolBarPanel1;
  private JXSummaryTable table;
  private JScrollPane scrollPane;
  private JPopupMenu popupMenu = new JPopupMenu();
  private JMenuItem jMenuItem1 = new JMenuItem();

  public FinanceFrame() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  void updateTitle() {
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
    this.addComponentListener(new FinanceFrame_this_componentAdapter(this));
    if (KNC_Terminal.defaultTableMaximized > 0) this.setExtendedState(Frame.MAXIMIZED_BOTH);
    toolBarPanel1 = new ToolBarPanel1((byte)3);
    toolBarPanel1.setActionListener(this);
    contentPane.add(toolBarPanel1, java.awt.BorderLayout.NORTH);
    tableModel = new FinanceTableModel();
    tableModel.addTableModelListener(new FinanceFrame_tableModel_tableModelAdapter(this));
    table = new JXSummaryTable(tableModel);
    table.setRowHeight(33);
    table.setColumnControlVisible(true);
    table.getColumnModel().getColumn(0).setMinWidth(20);
    table.getColumnModel().getColumn(0).setMaxWidth(30);
    table.getColumnModel().getColumn(1).setMinWidth(54);
    table.getColumnModel().getColumn(1).setMaxWidth(58);
    table.getColumnModel().getColumn(2).setPreferredWidth(180);
    table.getColumnModel().getColumn(3).setMinWidth(58); //Выручка
    table.getColumnModel().getColumn(4).setMinWidth(65); //Доход
    table.getColumnModel().getColumn(5).setMinWidth(48); //Рентабельность
    table.getColumnModel().getColumn(6).setMinWidth(44); //Операций
    table.getColumnModel().getColumn(7).setMinWidth(44); //Средний платеж
    table.getColumnModel().getColumn(8).setMinWidth(58); //Выручка в день
    table.getColumnModel().getColumn(9).setMinWidth(48); //Доход в день
    table.getColumnModel().getColumn(10).setMinWidth(40); //Операций в день
    table.setSortOrder(8, SortOrder.DESCENDING);
    table.setHorizontalScrollEnabled(true);
    table.addMouseListener(new FinanceFrame_table_popupAdapter(this));
    table.addKeyListener(new FinanceFrame_table_keyAdapter(this));
    scrollPane = new JScrollPane(table);
    contentPane.add(scrollPane, java.awt.BorderLayout.CENTER);
    jMenuItem1.setText("Показать историю");
    jMenuItem1.setActionCommand("GET_FINANCEHISTORY");
    jMenuItem1.addActionListener(new FinanceFrame_popupMenu_actionAdapter(this));
    filterMenu = KNC_Terminal.filterMenuFactory.getMenu(KNC_Terminal.user.filters, true);
    filterMenu.addActionListener(new FinanceFrame_popupMenu_actionAdapter(this));
    popupMenu.add(jMenuItem1);
    popupMenu.addSeparator();
    popupMenu.add(filterMenu.getSubMenu());
  }

  public void this_componentShown(ComponentEvent e) {
    updatingFlag = true;
    updateTitle();
    new At_net("GET_FINANCELIST", KNC_Terminal.user, this).start();
  }

  public void tableModel_tableChanged(TableModelEvent e) {
    if (tableModel.getStructureChanged()) {
      table.getColumnModel().getColumn(0).setMinWidth(20);
      table.getColumnModel().getColumn(0).setMaxWidth(30);
      table.getColumnModel().getColumn(1).setMinWidth(54);
      table.getColumnModel().getColumn(1).setMaxWidth(58);
      table.getColumnModel().getColumn(2).setPreferredWidth(180);
      table.getColumnModel().getColumn(3).setMinWidth(58); //Выручка
      int gatewaysCount = tableModel.getGatewaysCount();
      for (int i=0; i<gatewaysCount; i++) {
        table.getColumnModel().getColumn(4+i).setMinWidth(58); //Выручка шлюза
      }
      table.getColumnModel().getColumn(4+gatewaysCount).setMinWidth(65); //Доход
      table.getColumnModel().getColumn(5+gatewaysCount).setMinWidth(48); //Рентабельность
      table.getColumnModel().getColumn(6+gatewaysCount).setMinWidth(44); //Операций
      table.getColumnModel().getColumn(7+gatewaysCount).setMinWidth(44); //Средний платеж
      table.getColumnModel().getColumn(8+gatewaysCount).setMinWidth(58); //Выручка в день
      table.getColumnModel().getColumn(9+gatewaysCount).setMinWidth(48); //Доход в день
      table.getColumnModel().getColumn(10+gatewaysCount).setMinWidth(40); //Операций в день
      table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableRenderer(StringValues.TO_STRING, JLabel.RIGHT)); //Выручка
      table.getColumnModel().getColumn(4+gatewaysCount).setCellRenderer(new DefaultTableRenderer(StringValues.TO_STRING, JLabel.RIGHT)); //Доход
      table.getColumnModel().getColumn(8+gatewaysCount).setCellRenderer(new DefaultTableRenderer(StringValues.TO_STRING, JLabel.RIGHT));
      table.getColumnModel().getColumn(9+gatewaysCount).setCellRenderer(new DefaultTableRenderer(StringValues.TO_STRING, JLabel.RIGHT));
      table.getColumnModel().getColumn(10+gatewaysCount).setCellRenderer(new DefaultTableRenderer(StringValues.TO_STRING, JLabel.RIGHT));
      table.setSortOrder(8+gatewaysCount, SortOrder.DESCENDING);
      for (int i=gatewaysCount-1; i>=0; i--) {
        table.getColumnExt(4+i).setVisible(false);
      }
    }
    table.packAll();
  }

  public void table_keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_F5) {
      updatingFlag = true;
      updateTitle();
      new At_net("GET_FINANCELIST", KNC_Terminal.user, this).start();
    }
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
    if (e.getActionCommand().equals("GET_FINANCEHISTORY")) {
      new At_net("GET_FINANCEHISTORY", new Request(KNC_Terminal.user, kioskNumbers), this).start();
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_GET_FINANCELIST")) {
      @SuppressWarnings("unchecked")
      final ArrayList<FinanceProfile> financeList = (ArrayList<FinanceProfile>)((At_net)e.getSource()).getResult();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          KNC_Terminal.frame.updateAutoFilters(financeList);
          tableModel.putFinances(financeList, false);
          updatingFlag = false;
          updateTitle();
        }
      });
    }
    if (e.getActionCommand().equals("finished_GET_FINANCEHISTORY")) {
      @SuppressWarnings("unchecked")
      ArrayList<FinanceProfile[]> financeHistory = (ArrayList<FinanceProfile[]>)((At_net)e.getSource()).getResult();
      new FinanceHistoryFrame(financeHistory).setVisible(true);
    }
    if (e.getActionCommand().equals("fail_GET_FINANCELIST") || e.getActionCommand().equals("fail_GET_FINANCEHISTORY")) {
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
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_REFRESH)) {
      updatingFlag = true;
      updateTitle();
      new At_net("GET_FINANCELIST", KNC_Terminal.user, this).start();
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_SEARCH)) {
      table.getActionMap().get("find").actionPerformed(null);
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_EXPORT)) {
      exportTable(false);
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_SAVE)) {
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
  }

  private void exportTable(boolean toFile) {
    if (toFile) {
      KNC_Terminal.saveRegistryFileDialog.setDialogTitle("Сохранить финансовую сводку");
      KNC_Terminal.saveRegistryFileDialog.setSelectedFile(new File("Финансовая сводка от "+new SimpleDateFormat("dd.MM.yyyy").format(new Date())));
      if (KNC_Terminal.saveRegistryFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = KNC_Terminal.saveRegistryFileDialog.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) file = new File(file.getAbsolutePath()+".xls");
        if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \""+file.getName()+"\"?") == RPCDialog.Result.OK) {
          HSSFWorkbook wb = new HSSFWorkbook();
          wb = new ExcelCreater(wb, "Финансовая сводка", table).createFinanceSheet(((FinanceTableModel)table.getModel()).getGatewaysCount());
          try (FileOutputStream out = new FileOutputStream(file)) {
            wb.write(out);
          } catch (IOException ex) {
            RPCMessage.showMessageDialog(this, "Не удалось сохранить финансовую сводку: " + ex.getMessage());
          }
        }
      }
    } else {
      try {
        File file = File.createTempFile("Финансовая сводка от " + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + " ", ".xls");
        file.deleteOnExit();
        HSSFWorkbook wb = new HSSFWorkbook();
        wb = new ExcelCreater(wb, "Финансовая сводка", table).createFinanceSheet(((FinanceTableModel) table.getModel()).getGatewaysCount());
        try (FileOutputStream out = new FileOutputStream(file)) {
          wb.write(out);
        }
        Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
      } catch (IOException ex) {
        RPCMessage.showMessageDialog(this, "Не удалось экспортировать финансовую сводку: " + ex.getMessage());
      }
    }
  }
}

class FinanceFrame_popupMenu_actionAdapter implements ActionListener {
  private FinanceFrame adaptee;
  FinanceFrame_popupMenu_actionAdapter(FinanceFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.popupMenu_actionPerformed(e);
  }
}

class FinanceFrame_table_popupAdapter extends MouseAdapter {
  private FinanceFrame adaptee;
  FinanceFrame_table_popupAdapter(FinanceFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mousePressed(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
}

class FinanceFrame_table_keyAdapter extends KeyAdapter {
  private FinanceFrame adaptee;
  FinanceFrame_table_keyAdapter(FinanceFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void keyPressed(KeyEvent e) {
    adaptee.table_keyPressed(e);
  }
}

class FinanceFrame_this_componentAdapter extends ComponentAdapter {
  private FinanceFrame adaptee;
  FinanceFrame_this_componentAdapter(FinanceFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void componentShown(ComponentEvent e) {
    adaptee.this_componentShown(e);
  }
}

class FinanceFrame_tableModel_tableModelAdapter implements TableModelListener {
  private FinanceFrame adaptee;
  FinanceFrame_tableModel_tableModelAdapter(FinanceFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel_tableChanged(e);
  }
}
