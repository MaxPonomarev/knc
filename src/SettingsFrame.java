import java.awt.*;
import java.awt.event.*;
import java.awt.event.ComponentAdapter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.decorator.*;

class SettingsFrame extends JFrame implements ActionListener {
  private final String MAIN_TITLE = "Таблица настроек";
  private boolean updatingFlag = false;
  private String filterCurrentName = null;
  private String filterTitle = "";

  private ArrayList<FiltrableTableModel> tableModels = new ArrayList<>();
  private Settings1TableModel tableModel1;
  private Settings2TableModel tableModel2;
  private Settings3TableModel tableModel3;
  private Settings4TableModel tableModel4;
  private Settings5TableModel tableModel5;
  private Settings6TableModel tableModel6;
  private Settings7TableModel tableModel7;
  private Settings8TableModel tableModel8;
  private Settings9TableModel tableModel9;
  private FilterMenu filterMenu;
  private boolean machineMode = false;
  private boolean actualFlag = true; //если false, значит данные машины времени не соответствуют запрошенным (ошибка связи при запросе)
  private TreeMap<String, KioskProfile> machineData = null;
  private long machineTime = 0;
  private JPanel contentPane;
  private BorderLayout borderLayout = new BorderLayout();
  private ToolBarPanel1 toolBarPanel1;
  private JTabbedPane jTabbedPane = new JTabbedPane();
  private JXSummaryTable table1;
  private JXSummaryTable table2;
  private JXSummaryTable table3;
  private JXSummaryTable table4;
  private JXSummaryTable table5;
  private JXSummaryTable table6;
  private JXSummaryTable table7;
  private JXSummaryTable table8;
  private JXSummaryTable table9;
  private JPopupMenu popupMenu = new JPopupMenu();
  private JMenuItem jMenuItem1 = new JMenuItem();
  private JMenuItem jMenuItem2 = new JMenuItem();
  private JMenuItem jMenuItem3 = new JMenuItem();
  private JMenuItem jMenuItem4 = new JMenuItem();
  private JMenuItem jMenuItem5 = new JMenuItem();
  private JMenuItem jMenuItem6 = new JMenuItem();

  public SettingsFrame() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateTitle() {
    String title = MAIN_TITLE;
    if (machineMode) title += KNC_Terminal.machineTitle;
    title += filterTitle;
    if (updatingFlag) title += KNC_Terminal.updatingTitle;
    else if (!actualFlag) title += KNC_Terminal.actualTitle;
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
    this.addComponentListener(new SettingsFrame_this_componentAdapter(this));
    if (KNC_Terminal.defaultTableMaximized > 0) this.setExtendedState(Frame.MAXIMIZED_BOTH);
    if (KNC_Terminal.user.level < 1) toolBarPanel1 = new ToolBarPanel1((byte)0);
    else toolBarPanel1 = new ToolBarPanel1((byte)1);
    toolBarPanel1.setActionListener(this);
    contentPane.add(toolBarPanel1, java.awt.BorderLayout.NORTH);

    tableModel1 = new Settings1TableModel();
    tableModel1.addTableModelListener(new SettingsFrame_tableModel1_tableModelAdapter(this));
    tableModels.add(tableModel1);
    table1 = new JXSummaryTable(tableModel1);
    table1.setRowHeight(33);
    table1.setColumnControlVisible(false);
    table1.getColumnModel().getColumn(0).setMinWidth(20);
    table1.getColumnModel().getColumn(0).setMaxWidth(30);
    table1.getColumnModel().getColumn(1).setMinWidth(54);
    table1.getColumnModel().getColumn(1).setMaxWidth(58);
    table1.getColumnModel().getColumn(2).setPreferredWidth(180);
    table1.setHorizontalScrollEnabled(true);
    table1.addMouseListener(new SettingsFrame_table_popupAdapter(this));
    table1.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    jTabbedPane.addTab("Общая конфигурация", new JScrollPane(table1));

    tableModel2 = new Settings2TableModel();
    tableModel2.addTableModelListener(new SettingsFrame_tableModel2_tableModelAdapter(this));
    tableModels.add(tableModel2);
    table2 = new JXSummaryTable(tableModel2);
    table2.setRowHeight(33);
    table2.setColumnControlVisible(false);
    table2.getColumnModel().getColumn(0).setMinWidth(20);
    table2.getColumnModel().getColumn(0).setMaxWidth(30);
    table2.getColumnModel().getColumn(1).setMinWidth(54);
    table2.getColumnModel().getColumn(1).setMaxWidth(58);
    table2.getColumnModel().getColumn(2).setPreferredWidth(180);
    table2.setHorizontalScrollEnabled(true);
    table2.addMouseListener(new SettingsFrame_table_popupAdapter(this));
    table2.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    jTabbedPane.addTab("Конфигурация шлюзов", new JScrollPane(table2));

    tableModel3 = new Settings3TableModel();
    tableModel3.addTableModelListener(new SettingsFrame_tableModel3_tableModelAdapter(this));
    tableModels.add(tableModel3);
    table3 = new JXSummaryTable(tableModel3);
    table3.setRowHeight(33);
    table3.setColumnControlVisible(false);
    table3.getColumnModel().getColumn(0).setMinWidth(20);
    table3.getColumnModel().getColumn(0).setMaxWidth(30);
    table3.getColumnModel().getColumn(1).setMinWidth(54);
    table3.getColumnModel().getColumn(1).setMaxWidth(58);
    table3.getColumnModel().getColumn(2).setPreferredWidth(180);
    table3.setHorizontalScrollEnabled(true);
    table3.addMouseListener(new SettingsFrame_table_popupAdapter(this));
    table3.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    jTabbedPane.addTab("Конфигурация услуг", new JScrollPane(table3));

    tableModel4 = new Settings4TableModel();
    tableModel4.addTableModelListener(new SettingsFrame_tableModel4_tableModelAdapter(this));
    tableModels.add(tableModel4);
    table4 = new JXSummaryTable(tableModel4);
    table4.setRowHeight(33);
    table4.setColumnControlVisible(true);
    table4.getColumnModel().getColumn(0).setMinWidth(20);
    table4.getColumnModel().getColumn(0).setMaxWidth(30);
    table4.getColumnModel().getColumn(1).setMinWidth(54);
    table4.getColumnModel().getColumn(1).setMaxWidth(58);
    table4.getColumnModel().getColumn(2).setPreferredWidth(180);
    table4.setHorizontalScrollEnabled(true);
    table4.addMouseListener(new SettingsFrame_table_popupAdapter(this));
    table4.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    jTabbedPane.addTab("Оформление", new JScrollPane(table4));

    tableModel5 = new Settings5TableModel();
    tableModel5.addTableModelListener(new SettingsFrame_tableModel5_tableModelAdapter(this));
    tableModels.add(tableModel5);
    table5 = new JXSummaryTable(tableModel5);
    table5.setRowHeight(33);
    table5.setColumnControlVisible(true);
    table5.getColumnModel().getColumn(0).setMinWidth(20);
    table5.getColumnModel().getColumn(0).setMaxWidth(30);
    table5.getColumnModel().getColumn(1).setMinWidth(54);
    table5.getColumnModel().getColumn(1).setMaxWidth(58);
    table5.getColumnModel().getColumn(2).setPreferredWidth(180);
    table5.setHorizontalScrollEnabled(true);
    table5.addMouseListener(new SettingsFrame_table_popupAdapter(this));
    table5.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    jTabbedPane.addTab("Система", new JScrollPane(table5));

    tableModel6 = new Settings6TableModel();
    tableModel6.addTableModelListener(new SettingsFrame_tableModel6_tableModelAdapter(this));
    tableModels.add(tableModel6);
    table6 = new JXSummaryTable(tableModel6);
    table6.setRowHeight(33);
    table6.setColumnControlVisible(true);
    table6.getColumnModel().getColumn(0).setMinWidth(20);
    table6.getColumnModel().getColumn(0).setMaxWidth(30);
    table6.getColumnModel().getColumn(1).setMinWidth(54);
    table6.getColumnModel().getColumn(1).setMaxWidth(58);
    table6.getColumnModel().getColumn(2).setPreferredWidth(180);
    table6.setHorizontalScrollEnabled(true);
    table6.addMouseListener(new SettingsFrame_table_popupAdapter(this));
    table6.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    jTabbedPane.addTab("Оборудование", new JScrollPane(table6));

    tableModel7 = new Settings7TableModel();
    tableModel7.addTableModelListener(new SettingsFrame_tableModel7_tableModelAdapter(this));
    tableModels.add(tableModel7);
    table7 = new JXSummaryTable(tableModel7);
    table7.setRowHeight(33);
    table7.setColumnControlVisible(true);
    table7.getColumnModel().getColumn(0).setMinWidth(20);
    table7.getColumnModel().getColumn(0).setMaxWidth(30);
    table7.getColumnModel().getColumn(1).setMinWidth(54);
    table7.getColumnModel().getColumn(1).setMaxWidth(58);
    table7.getColumnModel().getColumn(2).setPreferredWidth(180);
    table7.toggleSortOrder(0);
    table7.setHorizontalScrollEnabled(true);
    table7.addMouseListener(new SettingsFrame_table_popupAdapter(this));
    table7.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    jTabbedPane.addTab("Безопасность", new JScrollPane(table7));

    tableModel8 = new Settings8TableModel();
    tableModel8.addTableModelListener(new SettingsFrame_tableModel8_tableModelAdapter(this));
    tableModels.add(tableModel8);
    table8 = new JXSummaryTable(tableModel8);
    table8.setRowHeight(33);
    table8.setColumnControlVisible(true);
    table8.getColumnModel().getColumn(0).setMinWidth(20);
    table8.getColumnModel().getColumn(0).setMaxWidth(30);
    table8.getColumnModel().getColumn(1).setMinWidth(54);
    table8.getColumnModel().getColumn(1).setMaxWidth(58);
    table8.getColumnModel().getColumn(2).setPreferredWidth(180);
    table8.toggleSortOrder(0);
    table8.setHorizontalScrollEnabled(true);
    table8.addMouseListener(new SettingsFrame_table_popupAdapter(this));
    table8.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    jTabbedPane.addTab("Связь", new JScrollPane(table8));

    tableModel9 = new Settings9TableModel();
    tableModel9.addTableModelListener(new SettingsFrame_tableModel9_tableModelAdapter(this));
    tableModels.add(tableModel9);
    table9 = new JXSummaryTable(tableModel9);
    table9.setRowHeight(33);
    table9.setColumnControlVisible(true);
    table9.getColumnModel().getColumn(0).setMinWidth(20);
    table9.getColumnModel().getColumn(0).setMaxWidth(30);
    table9.getColumnModel().getColumn(1).setMinWidth(54);
    table9.getColumnModel().getColumn(1).setMaxWidth(58);
    table9.getColumnModel().getColumn(2).setPreferredWidth(180);
    table9.toggleSortOrder(0);
    table9.setHorizontalScrollEnabled(true);
    table9.addMouseListener(new SettingsFrame_table_popupAdapter(this));
    table9.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    new Settings9TableRenderer(table9);
    jTabbedPane.addTab("Геопривязка", new JScrollPane(table9));

    jTabbedPane.addKeyListener(new SettingsFrame_table_keyAdapter(this));
    contentPane.add(jTabbedPane, java.awt.BorderLayout.CENTER);

    jMenuItem1.setText("Изменить конфигурацию");
    jMenuItem1.setActionCommand("EDIT_CONFIG");
    jMenuItem2.setText("Установить обновление");
    jMenuItem2.setActionCommand("SET_UPDATE");
    jMenuItem3.setText("Перезапустить киоск");
    jMenuItem3.setActionCommand("RESTART_KIOSK");
    jMenuItem4.setText("Отложенная перезагрузка");
    jMenuItem4.setActionCommand("REBOOT_LATER");
    jMenuItem5.setText("Удалить обновление");
    jMenuItem5.setActionCommand("DEL_UPDATE");
    jMenuItem6.setText("Отменить последнюю команду");
    jMenuItem6.setActionCommand("UNDO_COMMAND");
    if (KNC_Terminal.user.level < 3) jMenuItem1.setEnabled(false);
    if (KNC_Terminal.user.level < 3) jMenuItem2.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem3.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem4.setEnabled(false);
    if (KNC_Terminal.user.level < 3) jMenuItem5.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem6.setEnabled(false);
    filterMenu = KNC_Terminal.filterMenuFactory.getMenu(KNC_Terminal.user.filters, true);
    jMenuItem1.addActionListener(new SettingsFrame_popupMenu_actionAdapter(this));
    jMenuItem2.addActionListener(new SettingsFrame_popupMenu_actionAdapter(this));
    jMenuItem3.addActionListener(new SettingsFrame_popupMenu_actionAdapter(this));
    jMenuItem4.addActionListener(new SettingsFrame_popupMenu_actionAdapter(this));
    jMenuItem5.addActionListener(new SettingsFrame_popupMenu_actionAdapter(this));
    jMenuItem6.addActionListener(new SettingsFrame_popupMenu_actionAdapter(this));
    filterMenu.addActionListener(new SettingsFrame_popupMenu_actionAdapter(this));
    popupMenu.add(jMenuItem1);
    popupMenu.add(jMenuItem2);
    popupMenu.add(jMenuItem3);
    popupMenu.add(jMenuItem4);
    popupMenu.addSeparator();
    popupMenu.add(jMenuItem5);
    popupMenu.add(jMenuItem6);
    popupMenu.addSeparator();
    popupMenu.add(filterMenu.getSubMenu());
  }

  public void this_componentShown(ComponentEvent e) {
    if (!machineMode) {
      updatingFlag = true;
      updateTitle();
      new At_net("GET_KIOSKDATALIST", KNC_Terminal.user, this).start();
    }
  }

  public void tableModel1_tableChanged(TableModelEvent e) {
    Object columnID = tableModel1.getColumnName(5);
    if (tableModel1.isUpdates()) table1.getColumnExt(columnID).setVisible(true);
    else table1.getColumnExt(columnID).setVisible(false);
    table1.packAll();
  }

  public void tableModel2_tableChanged(TableModelEvent e) {
    Object columnID;
    for (int i=6; i<=7; i++) {
      columnID = tableModel2.getColumnName(i);
      if (tableModel2.isGateway(0)) table2.getColumnExt(columnID).setVisible(true);
      else table2.getColumnExt(columnID).setVisible(false);
    }
    for (int i=8; i<=9; i++) {
      columnID = tableModel2.getColumnName(i);
      if (tableModel2.isGateway(1)) table2.getColumnExt(columnID).setVisible(true);
      else table2.getColumnExt(columnID).setVisible(false);
    }
    for (int i=10; i<=15; i++) {
      columnID = tableModel2.getColumnName(i);
      if (tableModel2.isGateway(2)) table2.getColumnExt(columnID).setVisible(true);
      else table2.getColumnExt(columnID).setVisible(false);
    }
    for (int i=16; i<=22; i++) {
      columnID = tableModel2.getColumnName(i);
      if (tableModel2.isGateway(3)) table2.getColumnExt(columnID).setVisible(true);
      else table2.getColumnExt(columnID).setVisible(false);
    }
    for (int i=23; i<=28; i++) {
      columnID = tableModel2.getColumnName(i);
      if (tableModel2.isGateway(4)) table2.getColumnExt(columnID).setVisible(true);
      else table2.getColumnExt(columnID).setVisible(false);
    }
    for (int i=29; i<=32; i++) {
      columnID = tableModel2.getColumnName(i);
      if (tableModel2.isGateway(5)) table2.getColumnExt(columnID).setVisible(true);
      else table2.getColumnExt(columnID).setVisible(false);
    }
    for (int i=33; i<=36; i++) {
      columnID = tableModel2.getColumnName(i);
      if (tableModel2.isGateway(6)) table2.getColumnExt(columnID).setVisible(true);
      else table2.getColumnExt(columnID).setVisible(false);
    }
    table2.packAll();
  }

  public void tableModel3_tableChanged(TableModelEvent e) {
    if (e.getLastRow() == TableModelEvent.HEADER_ROW) {
      table3.getColumnModel().getColumn(0).setMinWidth(20);
      table3.getColumnModel().getColumn(0).setMaxWidth(30);
      table3.getColumnModel().getColumn(1).setMinWidth(54);
      table3.getColumnModel().getColumn(1).setMaxWidth(58);
      table3.getColumnModel().getColumn(2).setPreferredWidth(180);
    }
    int count = table3.getModel().getColumnCount();
    ColorHighlighter[] colorHighlighters = new ColorHighlighter[count-4];
    for (int i = 4; i < count; i++) {
      colorHighlighters[i-4] = new ColorHighlighter(new PatternPredicate("<diff>", i, i), new Color(255,209,155), Color.BLACK, new Color(210,200,179), Color.BLACK);
    }
    table3.setHighlighters(colorHighlighters);
    table3.packAll();
  }

  public void tableModel4_tableChanged(TableModelEvent e) {
    table4.packAll();
  }

  public void tableModel5_tableChanged(TableModelEvent e) {
    table5.packAll();
  }

  public void tableModel6_tableChanged(TableModelEvent e) {
    table6.packAll();
  }

  public void tableModel7_tableChanged(TableModelEvent e) {
    table7.packAll();
  }

  public void tableModel8_tableChanged(TableModelEvent e) {
    table8.packAll();
  }

  public void tableModel9_tableChanged(TableModelEvent e) {
    table9.packAll();
  }

  public void table_keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_F5 && !machineMode) {
      updatingFlag = true;
      updateTitle();
      new At_net("GET_KIOSKDATALIST", KNC_Terminal.user, this).start();
    }
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F && e.getSource().equals(jTabbedPane)) {
      JXTable table = (JXTable)((JScrollPane)jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
      table.getActionMap().get("find").actionPerformed(null);
    }
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E) {
      exportTable(false);
    }
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
      exportTable(true);
    }
  }

  public void table_maybeShowPopup(MouseEvent e) {
    JXTable table = (JXTable)e.getSource();
    int activeRow = table.rowAtPoint(new Point(e.getX(),e.getY()));
    if (activeRow == -1) {
      table.removeRowSelectionInterval(0, table.getRowCount()-1);
    } else {
      if (e.isPopupTrigger()) {
        if (table.convertRowIndexToModel(activeRow) == table.getModel().getRowCount()-1) return;
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
    JXTable table = (JXTable)((JScrollPane)jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
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
    String[] kioskAddresses = new String[rows.length];
    KioskProfile[] kioskProfiles = new KioskProfile[rows.length];
    String kioskList;
    StringBuilder kioskListB = new StringBuilder();
    for (int i=0; i<rows.length; i++) {
      kioskNumbers[i] = table.getModel().getValueAt(table.convertRowIndexToModel(rows[i]), 1).toString();
      kioskAddresses[i] = table.getModel().getValueAt(table.convertRowIndexToModel(rows[i]), 2).toString();
      kioskListB.append("№").append(kioskNumbers[i]).append(", ").append(kioskAddresses[i]).append("\n");
    }
    kioskList = kioskListB.toString();
    if (e.getActionCommand().equals(FilterMenu.CANCEL_FILTER)) {
      for (FiltrableTableModel tableModel : tableModels) {
        tableModel.removeFilter();
      }
      filterCurrentName = null;
      filterTitle = FilterMenu.getTitle(null);
      updateTitle();
      filterMenu.updateAfterCommand(e.getActionCommand());
    }
    if (e.getActionCommand().equals(FilterMenu.SAVE_FILTER)) {
      String filterName = FilterDialog.showSaveDialog(this);
      if (filterName != null) {
        TreeMap<String,HashSet<String>> newFilters = (KNC_Terminal.user.filters == null) ? new TreeMap<String,HashSet<String>>() : new TreeMap<>(KNC_Terminal.user.filters);
        newFilters.put(filterName, tableModel1.getFilter());
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
      for (FiltrableTableModel tableModel : tableModels) {
        tableModel.setFilter(kioskNumbers);
      }
      filterCurrentName = null;
      filterTitle = FilterMenu.getTitle("");
      updateTitle();
      filterMenu.updateAfterCommand(e.getActionCommand());
    }
    if (e.getActionCommand().equals(FilterMenu.EXCLUDE)) {
      try {
        for (FiltrableTableModel tableModel : tableModels) {
          tableModel.excludeFilter(kioskNumbers);
        }
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
      if (e.getActionCommand().startsWith(FilterMenu.SET_FILTERA)) {
        String autoFilter = KNC_Terminal.frame.getAutoFilters().get(filterCurrentName);
        for (FiltrableTableModel tableModel : tableModels) {
          tableModel.setAutoFilter(autoFilter);
        }
      }
      if (e.getActionCommand().startsWith(FilterMenu.SET_FILTER)) {
        HashSet<String> filter = KNC_Terminal.user.filters.get(filterCurrentName);
        for (FiltrableTableModel tableModel : tableModels) {
          tableModel.setFilter(filter);
        }
      }
      filterTitle = FilterMenu.getTitle(filterCurrentName);
      updateTitle();
      filterMenu.updateAfterCommand(e.getActionCommand());
    }

    boolean simpleCommand = false;
    String message = "";
    if (e.getActionCommand().equals("RESTART_KIOSK")) {
      simpleCommand = true;
      message = "Перезапустить следующие киоски?\n"+kioskList;
    }
    if (e.getActionCommand().equals("REBOOT_LATER")) {
      simpleCommand = true;
      message = "Запланировать перезагрузку следующих киосков?\n"+kioskList;
    }
    if (e.getActionCommand().equals("DEL_UPDATE")) {
      simpleCommand = true;
      message = "Удалить обновление на следующих киосках?\n"+kioskList;
    }
    if (e.getActionCommand().equals("UNDO_COMMAND")) {
      simpleCommand = true;
      message = "Отменить последнюю посланную команду для следующих киосков?\n"+kioskList;
    }
    if (simpleCommand) {
      if (machineMode) {RPCMessage.showMessageDialog(this, KNC_Terminal.machineNotAllow);return;}
      if (RPCDialog.showMessageDialog(this, message, "Вопрос", SwingConstants.LEFT) == RPCDialog.Result.OK) {
        AdminCommand[] commands = new AdminCommand[rows.length];
        for (int i=0; i<commands.length; i++) {
          commands[i] = new AdminCommand(kioskNumbers[i], KNC_Terminal.user.name);
          commands[i].setObjectArray(new Object[] {e.getActionCommand()});
        }
        new At_net("ADMIN_COMMAND_START", new Request(KNC_Terminal.user, commands), this).start();
      }
    } else {
      if (e.getActionCommand().equals("EDIT_CONFIG") || e.getActionCommand().equals("SET_UPDATE")) {
        if (machineMode) {RPCMessage.showMessageDialog(this, KNC_Terminal.machineNotAllow);return;}
        for (int i=0; i<rows.length; i++) {
          kioskProfiles[i] = ((SettingsTableModel)table.getModel()).getKiosks()[table.convertRowIndexToModel(rows[i])];
        }
      }
      if (e.getActionCommand().equals("EDIT_CONFIG")) {
        new EditConfigDialog(this, Dialog.ModalityType.DOCUMENT_MODAL, kioskProfiles).setVisible(true);
      }
      if (e.getActionCommand().equals("SET_UPDATE")) {
        if (KNC_Terminal.openUpdateDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
          File updFile = KNC_Terminal.openUpdateDialog.getSelectedFile();
          if (!updFile.getName().endsWith(".kupd")) {
            RPCMessage.showMessageDialog(this, "Необходим файл .kupd");
            return;
          }
          if (RPCDialog.showMessageDialog(this, "Установить обновление \""+updFile.getName()+"\" на следующих киосках?\n"+kioskList, "Вопрос", SwingConstants.LEFT) == RPCDialog.Result.OK) {
            AdminCommand command = new AdminCommand(kioskNumbers[0], KNC_Terminal.user.name);
            byte[] file = new byte[(int) updFile.length()];
            try (FileInputStream in = new FileInputStream(updFile)) {
              in.read(file);
              command.setObjectArray(new Object[]{e.getActionCommand(), updFile.getName(), file, kioskNumbers});
              new At_net("ADMIN_COMMAND_START", new Request(KNC_Terminal.user, new AdminCommand[]{command}), this).start();
            } catch (IOException ex) {
              RPCMessage.showMessageDialog(this, "Не удалось открыть файл \"" + updFile.getName() + "\"");
            }
          }
        }
      }
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_GET_KIOSKDATALIST")) {
      @SuppressWarnings("unchecked")
      final ArrayList<KioskProfile> kioskList = (ArrayList<KioskProfile>)((At_net)e.getSource()).getResult();
      tableModel7.setMachineMode(false);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          KNC_Terminal.frame.updateAutoFilters(kioskList);
          tableModel1.putKiosks(kioskList);
          tableModel2.putKiosks(kioskList);
          tableModel3.putKiosks(kioskList);
          tableModel4.putKiosks(kioskList);
          tableModel5.putKiosks(kioskList);
          tableModel6.putKiosks(kioskList);
          tableModel7.putKiosks(kioskList);
          tableModel8.putKiosks(kioskList);
          tableModel9.putKiosks(kioskList);
          updatingFlag = false;
          actualFlag = true;
          updateTitle();
        }
      });
    }
    if (e.getActionCommand().equals("fail_GET_KIOSKDATALIST")) {
      updatingFlag = false;
      updateTitle();
      RPCMessage.showMessageDialog(this, "Ошибка соединения с центральным сервером");
    }
    if (e.getActionCommand().equals("finished_ADMIN_COMMAND_START")) {
      if (((At_net)e.getSource()).getResult().equals("OK")) {
        String command = ((AdminCommand)((Request)((At_net)e.getSource()).getObject()).data[0]).getObjectArray()[0].toString();
        if (command.equals("SET_UPDATE")) RPCMessage.showMessageDialog(this, "Обновление успешно отправлено на сервер. Установка будет проведена после перезапуска киоска");
        if (command.equals("RESTART_KIOSK")) RPCMessage.showMessageDialog(this, "Команда перезапуска успешно отправлена на сервер");
        if (command.equals("REBOOT_LATER")) RPCMessage.showMessageDialog(this, "Команда отложенной перезагрузки успешно отправлена на сервер");
        if (command.equals("DEL_UPDATE")) RPCMessage.showMessageDialog(this, "Команда удаления обновления успешно отправлена на сервер");
        if (command.equals("UNDO_COMMAND")) RPCMessage.showMessageDialog(this, "Команда отмены последней команды успешно отправлена на сервер");
      }
    }
    if (e.getActionCommand().equals("fail_ADMIN_COMMAND_START")) {
      if (((At_net)e.getSource()).getStatus())
        RPCMessage.showMessageDialog(this, "Команда отправлена на сервер, но подтверждение не было получено");
      else
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
      if (!machineMode) {
        updatingFlag = true;
        updateTitle();
        new At_net("GET_KIOSKDATALIST", KNC_Terminal.user, this).start();
      }
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_SEARCH)) {
      JXTable table = (JXTable)((JScrollPane)jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
      table.getActionMap().get("find").actionPerformed(null);
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_EXPORT)) {
      exportTable(false);
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_SAVE)) {
      exportTable(true);
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_MACHINE_ON)) {
      machineMode = true;
      updateTitle();
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_MACHINE_OFF)) {
      machineMode = false;
      machineData = null;
      updatingFlag = true;
      updateTitle();
      new At_net("GET_KIOSKDATALIST", KNC_Terminal.user, this).start();
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_MACHINE_DATE)) {
      toolBarPanel1.setLockSlider(true);
      Date date = toolBarPanel1.getMachineDate();
      machineTime = date.getTime();
      updatingFlag = true;
      updateTitle();
      new At_net("GET_MACHINE", new Request(KNC_Terminal.user, new Object[] {1, date}), this).start();
    }
    if (e.getActionCommand().equals("finished_GET_MACHINE")) {
      if (!machineMode) return;
      machineData = (TreeMap<String, KioskProfile>)((At_net)e.getSource()).getResult();
      final ArrayList<KioskProfile> kioskList = getMachineKiosks();
      tableModel7.setMachineTime(machineTime);
      tableModel7.setMachineMode(true);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          tableModel1.putKiosks(kioskList);
          tableModel2.putKiosks(kioskList);
          tableModel3.putKiosks(kioskList);
          tableModel4.putKiosks(kioskList);
          tableModel5.putKiosks(kioskList);
          tableModel6.putKiosks(kioskList);
          tableModel7.putKiosks(kioskList);
          tableModel8.putKiosks(kioskList);
          tableModel9.putKiosks(kioskList);
          updatingFlag = false;
          updateTitle();
        }
      });
      updatingFlag = false;
      actualFlag = true;
      updateTitle();
    }
    if (e.getActionCommand().equals("fail_GET_MACHINE")) {
      updatingFlag = false;
      actualFlag = false;
      updateTitle();
      RPCMessage.showMessageDialog(this, "Ошибка соединения с центральным сервером");
    }
  }

  private void exportTable(boolean toFile) {
    if (toFile) {
      KNC_Terminal.saveRegistryFileDialog.setDialogTitle("Сохранить таблицу настроек");
      KNC_Terminal.saveRegistryFileDialog.setSelectedFile(new File("Таблица настроек от "+new SimpleDateFormat("dd.MM.yyyy").format(new Date())));
      if (KNC_Terminal.saveRegistryFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = KNC_Terminal.saveRegistryFileDialog.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) file = new File(file.getAbsolutePath()+".xls");
        if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \""+file.getName()+"\"?") == RPCDialog.Result.OK) {
          HSSFWorkbook wb = new HSSFWorkbook();
          wb = new ExcelCreater(wb, "Общая конфигурация", table1).createSettingsSheet1();
          wb = new ExcelCreater(wb, "Конфигурация шлюзов", table2).createSettingsSheet2();
          wb = new ExcelCreater(wb, "Конфигурация услуг", table3).createSettingsSheet3();
          wb = new ExcelCreater(wb, "Оформление", table4).createSettingsSheet4();
          wb = new ExcelCreater(wb, "Система", table5).createSettingsSheet5();
          wb = new ExcelCreater(wb, "Оборудование", table6).createSettingsSheet6();
          wb = new ExcelCreater(wb, "Безопасность", table7).createSettingsSheet7();
          wb = new ExcelCreater(wb, "Связь", table8).createSettingsSheet8();
          wb = new ExcelCreater(wb, "Геопривязка", table9).createSettingsSheet9();
          try (FileOutputStream out = new FileOutputStream(file)) {
            wb.write(out);
          } catch (IOException ex) {
            RPCMessage.showMessageDialog(this, "Не удалось сохранить таблицу настроек: " + ex.getMessage());
          }
        }
      }
    } else {
      try {
        File file = File.createTempFile("Таблица настроек от "+new SimpleDateFormat("dd.MM.yyyy").format(new Date())+" ", ".xls");
        file.deleteOnExit();
        HSSFWorkbook wb = new HSSFWorkbook();
        wb = new ExcelCreater(wb, "Общая конфигурация", table1).createSettingsSheet1();
        wb = new ExcelCreater(wb, "Конфигурация шлюзов", table2).createSettingsSheet2();
        wb = new ExcelCreater(wb, "Конфигурация услуг", table3).createSettingsSheet3();
        wb = new ExcelCreater(wb, "Оформление", table4).createSettingsSheet4();
        wb = new ExcelCreater(wb, "Система", table5).createSettingsSheet5();
        wb = new ExcelCreater(wb, "Оборудование", table6).createSettingsSheet6();
        wb = new ExcelCreater(wb, "Безопасность", table7).createSettingsSheet7();
        wb = new ExcelCreater(wb, "Связь", table8).createSettingsSheet8();
        wb = new ExcelCreater(wb, "Геопривязка", table9).createSettingsSheet9();
        try (FileOutputStream out = new FileOutputStream(file)) {
          wb.write(out);
        }
        Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
      } catch (IOException ex) {
        RPCMessage.showMessageDialog(this, "Не удалось экспортировать таблицу настроек: " + ex.getMessage());
      }
    }
  }

  private ArrayList<KioskProfile> getMachineKiosks() {
    ArrayList<KioskProfile> kiosks = new ArrayList<>();
    Set<String> keys = machineData.keySet();
    for (String number : keys) {
      KioskProfile kiosk = machineData.get(number);
      if (kiosk.lat < machineTime) kiosks.add(kiosk);
    }
    return kiosks;
  }
}

class SettingsFrame_popupMenu_actionAdapter implements ActionListener {
  private SettingsFrame adaptee;
  SettingsFrame_popupMenu_actionAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.popupMenu_actionPerformed(e);
  }
}

class SettingsFrame_table_popupAdapter extends MouseAdapter {
  private SettingsFrame adaptee;
  SettingsFrame_table_popupAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mousePressed(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
}

class SettingsFrame_table_keyAdapter extends KeyAdapter {
  private SettingsFrame adaptee;
  SettingsFrame_table_keyAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void keyPressed(KeyEvent e) {
    adaptee.table_keyPressed(e);
  }
}

class SettingsFrame_this_componentAdapter extends ComponentAdapter {
  private SettingsFrame adaptee;
  SettingsFrame_this_componentAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void componentShown(ComponentEvent e) {
    adaptee.this_componentShown(e);
  }
}

class SettingsFrame_tableModel1_tableModelAdapter implements TableModelListener {
  private SettingsFrame adaptee;
  SettingsFrame_tableModel1_tableModelAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel1_tableChanged(e);
  }
}

class SettingsFrame_tableModel2_tableModelAdapter implements TableModelListener {
  private SettingsFrame adaptee;
  SettingsFrame_tableModel2_tableModelAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel2_tableChanged(e);
  }
}

class SettingsFrame_tableModel3_tableModelAdapter implements TableModelListener {
  private SettingsFrame adaptee;
  SettingsFrame_tableModel3_tableModelAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel3_tableChanged(e);
  }
}

class SettingsFrame_tableModel4_tableModelAdapter implements TableModelListener {
  private SettingsFrame adaptee;
  SettingsFrame_tableModel4_tableModelAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel4_tableChanged(e);
  }
}

class SettingsFrame_tableModel5_tableModelAdapter implements TableModelListener {
  private SettingsFrame adaptee;
  SettingsFrame_tableModel5_tableModelAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel5_tableChanged(e);
  }
}

class SettingsFrame_tableModel6_tableModelAdapter implements TableModelListener {
  private SettingsFrame adaptee;
  SettingsFrame_tableModel6_tableModelAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel6_tableChanged(e);
  }
}

class SettingsFrame_tableModel7_tableModelAdapter implements TableModelListener {
  private SettingsFrame adaptee;
  SettingsFrame_tableModel7_tableModelAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel7_tableChanged(e);
  }
}

class SettingsFrame_tableModel8_tableModelAdapter implements TableModelListener {
  private SettingsFrame adaptee;
  SettingsFrame_tableModel8_tableModelAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel8_tableChanged(e);
  }
}

class SettingsFrame_tableModel9_tableModelAdapter implements TableModelListener {
  private SettingsFrame adaptee;
  SettingsFrame_tableModel9_tableModelAdapter(SettingsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel9_tableChanged(e);
  }
}
