import java.awt.*;
import java.awt.event.*;
import java.awt.event.ComponentAdapter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdesktop.swingx.JXSummaryTable;
import org.jdesktop.swingx.decorator.*;

class TableFrame extends JFrame implements ActionListener {
  private final String MAIN_TITLE = "Таблица киосков";
  private boolean updatingFlag = false;
  private String filterCurrentName = null;
  private String filterTitle = "";

  private KNCTableModel tableModel;
  private FilterMenu filterMenu;
  private KNCTableRenderer4 renderer4;
  private byte updateFlag = 0; //когда становится равным 2 колонка буфер не трогается
  private boolean machineMode = false;
  private boolean actualFlag = true; //если false, значит данные машины времени не соответствуют запрошенным (ошибка связи при запросе)
  private TreeMap<String, ArrayList<KioskProfile>> machineData = null;
  private JPanel contentPane;
  private BorderLayout borderLayout = new BorderLayout();
  private ToolBarPanel1 toolBarPanel1;
  private JXSummaryTable table;
  private JScrollPane scrollPane;
  private JPopupMenu popupMenu = new JPopupMenu();
  private JMenuItem jMenuItem1 = new JMenuItem();
  private JMenuItem jMenuItem2 = new JMenuItem();
  private JMenuItem jMenuItem3 = new JMenuItem();
  private JMenuItem jMenuItem4 = new JMenuItem();
  private JMenuItem jMenuItem5 = new JMenuItem();
  private JMenuItem jMenuItem6 = new JMenuItem();
  private JMenu subMenu1 = new JMenu();
  private JMenuItem jMenuItem11 = new JMenuItem();
  private JMenuItem jMenuItem12 = new JMenuItem();
  private JMenu subMenu2 = new JMenu();
  private JMenuItem jMenuItem20 = new JMenuItem();
  private JMenuItem jMenuItem21 = new JMenuItem();
  private JMenuItem jMenuItem22 = new JMenuItem();
  private JMenuItem jMenuItem23 = new JMenuItem();
  private JMenuItem jMenuItem231 = new JMenuItem();
  private JMenuItem jMenuItem24 = new JMenuItem();
  private JMenuItem jMenuItem25 = new JMenuItem();
  private JMenuItem jMenuItem26 = new JMenuItem();
  private JMenuItem jMenuItem27 = new JMenuItem();
  private JMenuItem jMenuItem28 = new JMenuItem();
  private JMenuItem jMenuItem29 = new JMenuItem();
  private JMenuItem jMenuItemData = new JMenuItem();
  private JMenuItem jMenuItemClear = new JMenuItem();

  public TableFrame() {
    try {
      jbInit();
      KNC_Terminal.dataUpdater = new DataUpdater(tableModel, renderer4);
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
    title += KNC_Terminal.interruptTitle;
    this.setTitle(title);
  }

  private void jbInit() {
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout);
    this.setIconImages(KNC_Terminal.icons);
    this.setTitle(MAIN_TITLE+KNC_Terminal.interruptTitle);
    int y = 100;
    if (y+519 > Toolkit.getDefaultToolkit().getScreenSize().height-35) y = Toolkit.getDefaultToolkit().getScreenSize().height-35-519;
    if (y < 0) y = 0;
    this.setBounds(12, y, Toolkit.getDefaultToolkit().getScreenSize().width-24, 519);
    this.addComponentListener(new TableFrame_this_componentAdapter(this));
    if (KNC_Terminal.defaultTableMaximized > 0) this.setExtendedState(Frame.MAXIMIZED_BOTH);
    if (KNC_Terminal.user.level < 1) toolBarPanel1 = new ToolBarPanel1((byte)0);
    else toolBarPanel1 = new ToolBarPanel1((byte)2);
    toolBarPanel1.setActionListener(this);
    contentPane.add(toolBarPanel1, java.awt.BorderLayout.NORTH);
    tableModel = new KNCTableModel(this);
    tableModel.addTableModelListener(new TableFrame_tableModel_tableModelAdapter(this));
    table = new JXSummaryTable(tableModel);
    table.setRowHeight(33);
    table.setColumnControlVisible(true);
    table.getColumnModel().getColumn(0).setMinWidth(20);
    table.getColumnModel().getColumn(0).setMaxWidth(30);
    table.getColumnModel().getColumn(1).setMinWidth(54);
    table.getColumnModel().getColumn(1).setMaxWidth(58);
    table.getColumnModel().getColumn(2).setPreferredWidth(180);
    table.getColumnModel().getColumn(3).setMinWidth(120);       //Дата инкассации
    table.getColumnModel().getColumn(3).setPreferredWidth(120); //Дата инкассации
    table.getColumnModel().getColumn(4).setMinWidth(42);
    table.getColumnModel().getColumn(4).setPreferredWidth(50);
    table.getColumnModel().getColumn(5).setMinWidth(48);
    table.getColumnModel().getColumn(5).setPreferredWidth(50);
    table.getColumnModel().getColumn(6).setMinWidth(40);
    table.getColumnModel().getColumn(6).setPreferredWidth(50);
    table.getColumnModel().getColumn(7).setMinWidth(30);
    table.getColumnModel().getColumn(7).setPreferredWidth(60);
    table.getColumnModel().getColumn(8).setMinWidth(30);
    table.getColumnModel().getColumn(8).setPreferredWidth(50);
    table.getColumnModel().getColumn(9).setMinWidth(65);        //Лента
    table.getColumnModel().getColumn(9).setPreferredWidth(70);  //Лента
    table.getColumnModel().getColumn(KNCTableModel.COLUMN_BUFFER).setMinWidth(40);       //Буфер
    table.getColumnModel().getColumn(KNCTableModel.COLUMN_BUFFER).setPreferredWidth(46); //Буфер
    table.getColumnModel().getColumn(KNCTableModel.COLUMN_NET).setMinWidth(40);       //Сеть
    table.getColumnModel().getColumn(KNCTableModel.COLUMN_NET).setPreferredWidth(46); //Сеть
    table.getColumnModel().getColumn(KNCTableModel.COLUMN_LASTOPER).setMinWidth(120);      //Последняя операция
    table.getColumnModel().getColumn(KNCTableModel.COLUMN_LASTOPER).setPreferredWidth(120);//Последняя операция
    table.toggleSortOrder(KNC_Terminal.defaultSortColumn);
    table.setHorizontalScrollEnabled(true);
    table.addMouseListener(new TableFrame_table_popupAdapter(this));
    table.addKeyListener(new TableFrame_table_keyAdapter(this));
    table.addHighlighter(new ColorHighlighter(new PatternPredicate(Pattern.compile(KNC_Terminal.wasSent), KNCTableModel.COLUMN_COMMENT), new Color(183,255,198), Color.BLACK, new Color(130,227,170), Color.BLACK));
    table.addHighlighter(new ColorHighlighter(new PatternPredicate(Pattern.compile(KNC_Terminal.wasGot), KNCTableModel.COLUMN_COMMENT), new Color(255,209,155), Color.BLACK, new Color(210,200,179), Color.BLACK));
    table.addHighlighter(new ColorHighlighter(new PatternPredicate(Pattern.compile(KNC_Terminal.wasLoaded), KNCTableModel.COLUMN_COMMENT), new Color(255,225,190), Color.BLACK, new Color(210,200,179), Color.BLACK));
    new KNCTableRenderer1(table);
    new KNCTableRenderer2(table);
    new KNCTableRenderer3(table);
    renderer4 = new KNCTableRenderer4(table);
    scrollPane = new JScrollPane(table);
    contentPane.add(scrollPane, java.awt.BorderLayout.CENTER);
    Object columnID = tableModel.getColumnName(8);
    table.getColumnExt(columnID).setVisible(false);//колонку "ошибки" теперь не показываем
    columnID = tableModel.getColumnName(KNCTableModel.COLUMN_NET);
    table.getColumnExt(columnID).setVisible(false);//колонку "связь" пока не показываем
    jMenuItem1.setText("Получить реестр платежей");
    jMenuItem1.setActionCommand("GET_DBREESTR");
    jMenuItem2.setText("Получить реестр инкассаций");
    jMenuItem2.setActionCommand("GET_DBINCASS");
    jMenuItem3.setText("Отменить последний запрос");
    jMenuItem3.setActionCommand("UNDO_COMMAND");
    jMenuItem4.setText("Заблокировать киоск");
    jMenuItem4.setActionCommand("BLOCK_KIOSK");
    jMenuItem5.setText("Разблокировать киоск");
    jMenuItem5.setActionCommand("UNBLOCK_KIOSK");
    jMenuItem6.setText("Перезапустить киоск");
    jMenuItem6.setActionCommand("RESTART_KIOSK");
    subMenu1.setText("Услуги");
    jMenuItem11.setText("Задать задержку платежей");
    jMenuItem11.setActionCommand("PRODUCT_TROUBLE");
    jMenuItem12.setText("Заблокировать/разблокировать");
    jMenuItem12.setActionCommand("PRODUCT_AVAILABLE");
    subMenu2.setText("Дополнительно");
    jMenuItem20.setText("Запретить обслуживание");
    jMenuItem20.setActionCommand("FORBID_SERVICE");
    jMenuItem21.setText("Инициализировать КП");
    jMenuItem21.setActionCommand("ACCEPTOR_INIT");
    jMenuItem22.setText("Синхронизировать время");
    jMenuItem22.setActionCommand("SET_TIME");
    jMenuItem23.setText("Добавить в автозагрузку");
    jMenuItem23.setActionCommand("SET_AUTORUN");
    jMenuItem231.setText("Перезагрузить киоск");
    jMenuItem231.setActionCommand("REBOOT_KIOSK");
    jMenuItem24.setText("Получить логи");
    jMenuItem24.setActionCommand("GET_LOG");
    jMenuItem25.setText("Изменить конфигурацию");
    jMenuItem25.setActionCommand("EDIT_CONFIG");
    jMenuItem26.setText("Установить обновление");
    jMenuItem26.setActionCommand("SET_UPDATE");
    jMenuItem27.setText("Удалить киоск с сервера");
    jMenuItem27.setActionCommand("DEL_KIOSK");
    jMenuItem28.setText("Запретить управление");
    jMenuItem28.setActionCommand("FORBID_CONTROL");
    jMenuItem29.setText("Разрешить управление");
    jMenuItem29.setActionCommand("ALLOW_CONTROL");
    filterMenu = KNC_Terminal.filterMenuFactory.getMenu(KNC_Terminal.user.filters, true);
    jMenuItemData.setText("Просмотреть данные");
    jMenuItemData.setActionCommand("VIEW_DATA");
    jMenuItemClear.setText("Очистить результат");
    jMenuItemClear.setActionCommand("CLEAR_DATA");
    if (KNC_Terminal.user.level < 1) jMenuItem1.setEnabled(false);
    if (KNC_Terminal.user.level < 1) jMenuItem2.setEnabled(false);
    if (KNC_Terminal.user.level < 1) jMenuItem3.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem4.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem5.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem6.setEnabled(false);
    if (KNC_Terminal.user.level < 2) subMenu1.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem11.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem12.setEnabled(false);
    if (KNC_Terminal.user.level < 2) subMenu2.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem20.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem21.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem22.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem23.setEnabled(false);
    if (KNC_Terminal.user.level < 2) jMenuItem231.setEnabled(false);
    if (KNC_Terminal.user.level < 3) jMenuItem24.setEnabled(false);
    if (KNC_Terminal.user.level < 3) jMenuItem25.setEnabled(false);
    if (KNC_Terminal.user.level < 3) jMenuItem26.setEnabled(false);
    if (KNC_Terminal.user.level < 3) jMenuItem27.setEnabled(false);
    if (KNC_Terminal.user.level < 3) jMenuItem28.setEnabled(false);
    if (KNC_Terminal.user.level < 3) jMenuItem29.setEnabled(false);
    jMenuItemData.setEnabled(false);
    jMenuItemClear.setEnabled(false);
    jMenuItem1.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem2.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem3.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem4.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem5.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem6.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem11.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem12.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem20.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem21.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem22.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem23.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem231.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem24.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem25.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem26.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem27.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem28.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItem29.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    filterMenu.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItemData.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    jMenuItemClear.addActionListener(new TableFrame_popupMenu_actionAdapter(this));
    popupMenu.add(jMenuItem1);
    popupMenu.add(jMenuItem2);
    popupMenu.add(jMenuItem3);
    popupMenu.add(jMenuItem4);
    popupMenu.add(jMenuItem5);
    popupMenu.add(jMenuItem6);
    popupMenu.add(subMenu1);
    popupMenu.add(subMenu2);
    popupMenu.addSeparator();
    popupMenu.add(filterMenu.getSubMenu());
    popupMenu.addSeparator();
    popupMenu.add(jMenuItemData);
    popupMenu.add(jMenuItemClear);
    subMenu1.add(jMenuItem11);
    subMenu1.add(jMenuItem12);
    subMenu2.add(jMenuItem20);
    subMenu2.add(jMenuItem21);
    subMenu2.add(jMenuItem22);
    subMenu2.add(jMenuItem23);
    subMenu2.add(jMenuItem231);
    subMenu2.add(jMenuItem24);
    subMenu2.add(jMenuItem25);
    subMenu2.add(jMenuItem26);
    subMenu2.add(jMenuItem27);
    subMenu2.addSeparator();
    subMenu2.add(jMenuItem28);
    subMenu2.add(jMenuItem29);
  }

  public void this_componentShown(ComponentEvent e) {
    if (!machineMode) KNC_Terminal.dataUpdater.setIdle(false);
    if (KNC_Terminal.dataUpdater.getState() == Thread.State.NEW) {
      KNC_Terminal.dataUpdater.start();
    } else {
      KNC_Terminal.dataUpdater.wakeup();
    }
  }

  public void this_componentHidden(ComponentEvent e) {
    KNC_Terminal.dataUpdater.setIdle(true);
  }

  public void tableModel_tableChanged(TableModelEvent e) {//не графический поток
    if (e.getColumn() == TableModelEvent.ALL_COLUMNS)
      SwingUtilities.invokeLater(new Runnable() {public void run() {
          if (updateFlag < 2) {
            Object columnID = tableModel.getColumnName(KNCTableModel.COLUMN_BUFFER);
            if (tableModel.isBufferPresent()) table.getColumnExt(columnID).setVisible(true);
            else table.getColumnExt(columnID).setVisible(false);
            updateFlag++;
          }
          table.packAll();
        }});
  }

  public void table_keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_F5 && !machineMode) {
      KNC_Terminal.dataUpdater.wakeup();
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
      if (e.isPopupTrigger() && KNC_Terminal.user.level > 0) {
        if (table.convertRowIndexToModel(activeRow) == table.getModel().getRowCount()-1) return;
        activeKioskNum = tableModel.getValueAt(table.convertRowIndexToModel(activeRow), 1).toString();
        if (activeKioskNum.equals("")) return;
        if (!table.isRowSelected(activeRow)) table.setRowSelectionInterval(activeRow, activeRow);
        if (tableModel.isResultExists(activeKioskNum)) {
          jMenuItemData.setEnabled(true);
          jMenuItemClear.setEnabled(true);
        } else {
          jMenuItemData.setEnabled(false);
          jMenuItemClear.setEnabled(false);
        }
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
    String[] kioskAddresses = new String[rows.length];
    KioskProfile[] kioskProfiles = new KioskProfile[rows.length];
    String kioskList;
    StringBuilder kioskListB = new StringBuilder();
    for (int i=0; i<rows.length; i++) {
      kioskNumbers[i] = tableModel.getValueAt(table.convertRowIndexToModel(rows[i]), 1).toString();
      kioskAddresses[i] = tableModel.getValueAt(table.convertRowIndexToModel(rows[i]), 2).toString();
      kioskListB.append("№").append(kioskNumbers[i]).append(", ").append(kioskAddresses[i]).append("\n");
    }
    kioskList = kioskListB.toString();
    if (e.getActionCommand().equals(FilterMenu.CANCEL_FILTER)) {
      tableModel.removeFilter();
      filterCurrentName = null;
      filterTitle = FilterMenu.getTitle(null);
      updateTitle();
      filterMenu.updateAfterCommand(e.getActionCommand());
      return;
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
      return;
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
      return;
    }
    if (e.getActionCommand().startsWith(FilterMenu.SET_FILTERA) || e.getActionCommand().startsWith(FilterMenu.SET_FILTER)) {
      filterCurrentName = FilterMenu.getFilterNameFromCommand(e.getActionCommand());
      if (e.getActionCommand().startsWith(FilterMenu.SET_FILTERA)) tableModel.setAutoFilter(KNC_Terminal.frame.getAutoFilters().get(filterCurrentName));
      if (e.getActionCommand().startsWith(FilterMenu.SET_FILTER)) tableModel.setFilter(KNC_Terminal.user.filters.get(filterCurrentName));
      filterTitle = FilterMenu.getTitle(filterCurrentName);
      updateTitle();
      filterMenu.updateAfterCommand(e.getActionCommand());
      return;
    }
    boolean simpleCommand = false;
    String message = "";
    if (e.getActionCommand().equals("UNDO_COMMAND")) {
      simpleCommand = true;
      message = "Отменить последнюю посланную команду для следующих киосков?\n"+kioskList;
    }
    if (e.getActionCommand().equals("BLOCK_KIOSK")) {
      simpleCommand = true;
      message = "Заблокировать следующие киоски?\n"+kioskList;
    }
    if (e.getActionCommand().equals("UNBLOCK_KIOSK")) {
      simpleCommand = true;
      message = "Разблокировать следующие киоски?\n"+kioskList;
    }
    if (e.getActionCommand().equals("RESTART_KIOSK")) {
      simpleCommand = true;
      message = "Перезапустить следующие киоски?\n"+kioskList;
    }
    if (e.getActionCommand().equals("FORBID_SERVICE")) {
      simpleCommand = true;
      message = "Запретить обслуживание без клавиатуры на следующих киосках?\n"+kioskList;
    }
    if (e.getActionCommand().equals("ACCEPTOR_INIT")) {
      simpleCommand = true;
      message = "Переинициализировать купюроприемник на следующих киосках?\n"+kioskList;
    }
    if (e.getActionCommand().equals("SET_AUTORUN")) {
      simpleCommand = true;
      message = "Добавить в автозагрузку следующие киоски?\n"+kioskList;
    }
    if (e.getActionCommand().equals("REBOOT_KIOSK")) {
      simpleCommand = true;
      message = "Перезагрузить следующие киоски?\n"+kioskList;
    }
    if (e.getActionCommand().equals("DEL_KIOSK")) {
      simpleCommand = true;
      message = "Удалить следующие киоски с сервера?\n"+kioskList;
    }
    if (e.getActionCommand().equals("FORBID_CONTROL")) {
      simpleCommand = true;
      message = "Запретить управление следующими киосками?\n"+kioskList;
    }
    if (e.getActionCommand().equals("ALLOW_CONTROL")) {
      simpleCommand = true;
      message = "Разрешить управление следующими киосками?\n"+kioskList;
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
        if (e.getActionCommand().equals("UNDO_COMMAND")) tableModel.clearInternalComments(kioskNumbers, KNC_Terminal.wasSent);
      }
    } else {
      for (int i=0; i<rows.length; i++) {
        kioskProfiles[i] = tableModel.getKiosks()[table.convertRowIndexToModel(rows[i])];
      }
      if (e.getActionCommand().equals("SET_TIME")) {
        if (machineMode) {RPCMessage.showMessageDialog(this, KNC_Terminal.machineNotAllow);return;}
        if (RPCDialog.showMessageDialog(this, "Синхронизировать время следующих киосков со временем сервера?\n"+kioskList, "Вопрос", SwingConstants.LEFT) == RPCDialog.Result.OK) {
          AdminCommand[] commands = new AdminCommand[rows.length];
          for (int i=0; i<commands.length; i++) {
            commands[i] = new AdminCommand(kioskNumbers[i], KNC_Terminal.user.name);
            commands[i].setObjectArray(new Object[] {e.getActionCommand(), null, null});
          }
          new At_net("ADMIN_COMMAND_START", new Request(KNC_Terminal.user, commands), this).start();
        }
      }
      if (e.getActionCommand().equals("GET_DBREESTR")) {
        new GetRegistryDialog(this, Dialog.ModalityType.DOCUMENT_MODAL, kioskProfiles, tableModel).setVisible(true);
      }
      if (e.getActionCommand().equals("GET_DBINCASS")) {
        new GetIncassDialog(this, Dialog.ModalityType.DOCUMENT_MODAL, kioskProfiles, tableModel).setVisible(true);
      }
      if (e.getActionCommand().equals("GET_LOG")) {
        if (machineMode) {RPCMessage.showMessageDialog(this, KNC_Terminal.machineNotAllow);return;}
        new GetLogDialog(this, Dialog.ModalityType.DOCUMENT_MODAL, kioskProfiles, tableModel).setVisible(true);
      }
      if (e.getActionCommand().equals("EDIT_CONFIG")) {
        if (machineMode) {RPCMessage.showMessageDialog(this, KNC_Terminal.machineNotAllow);return;}
        new EditConfigDialog(this, Dialog.ModalityType.DOCUMENT_MODAL, kioskProfiles).setVisible(true);
      }
      if (e.getActionCommand().equals("PRODUCT_TROUBLE")) {
        if (machineMode) {RPCMessage.showMessageDialog(this, KNC_Terminal.machineNotAllow);return;}
        new ProductTroubleDialog(this, Dialog.ModalityType.DOCUMENT_MODAL, kioskProfiles).setVisible(true);
      }
      if (e.getActionCommand().equals("PRODUCT_AVAILABLE")) {
        if (machineMode) {RPCMessage.showMessageDialog(this, KNC_Terminal.machineNotAllow);return;}
        new ProductAvailableDialog(this, Dialog.ModalityType.DOCUMENT_MODAL, kioskProfiles).setVisible(true);
      }
      if (e.getActionCommand().equals("SET_UPDATE")) {
        if (machineMode) {RPCMessage.showMessageDialog(this, KNC_Terminal.machineNotAllow);return;}
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
      if (e.getActionCommand().equals("VIEW_DATA")) {
        new ResultsFrame(tableModel.getResults(kioskNumbers)).setVisible(true);
      }
      if (e.getActionCommand().equals("CLEAR_DATA")) {
        if (RPCDialog.showMessageDialog(this, "Очистить результаты запросов для следующих киосков?\n"+kioskList, "Вопрос", SwingConstants.LEFT) == RPCDialog.Result.OK) {
          tableModel.clearResults(kioskNumbers);
          tableModel.clearInternalComments(kioskNumbers, KNC_Terminal.wasGot);
          tableModel.clearInternalComments(kioskNumbers, KNC_Terminal.wasLoaded);
        }
      }
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("comment_updated")) {
      SwingUtilities.invokeLater(new Runnable() {public void run() {table.packAll();}});
    }
    if (e.getActionCommand().equals("finished_ADMIN_COMMAND_START")) {
      if (((At_net)e.getSource()).getResult().equals("OK")) {
        String command = ((AdminCommand)((Request)((At_net)e.getSource()).getObject()).data[0]).getObjectArray()[0].toString();
        if (command.equals("DEL_KIOSK")) KNC_Terminal.dataUpdater.wakeup();
        if (command.equals("FORBID_CONTROL")) KNC_Terminal.dataUpdater.wakeup();
        if (command.equals("ALLOW_CONTROL")) KNC_Terminal.dataUpdater.wakeup();
        if (command.equals("UNDO_COMMAND")) RPCMessage.showMessageDialog(this, "Команда отмены последнего запроса успешно отправлена на сервер");
        if (command.equals("BLOCK_KIOSK")) RPCMessage.showMessageDialog(this, "Команда блокировки успешно отправлена на сервер");
        if (command.equals("UNBLOCK_KIOSK")) RPCMessage.showMessageDialog(this, "Команда разблокировки успешно отправлена на сервер");
        if (command.equals("RESTART_KIOSK")) RPCMessage.showMessageDialog(this, "Команда перезапуска успешно отправлена на сервер");
        if (command.equals("FORBID_SERVICE")) RPCMessage.showMessageDialog(this, "Команда запрета обслуживания успешно отправлена на сервер");
        if (command.equals("ACCEPTOR_INIT")) RPCMessage.showMessageDialog(this, "Команда переинициализации купюроприемника успешно отправлена на сервер");
        if (command.equals("SET_TIME")) RPCMessage.showMessageDialog(this, "Команда синхронизации успешно отправлена на сервер");
        if (command.equals("SET_AUTORUN")) RPCMessage.showMessageDialog(this, "Команда добавления киоска в автозагрузку успешно отправлена на сервер");
        if (command.equals("REBOOT_KIOSK")) RPCMessage.showMessageDialog(this, "Команда перезагрузки успешно отправлена на сервер");
        if (command.equals("SET_UPDATE")) RPCMessage.showMessageDialog(this, "Обновление успешно отправлено на сервер. Установка будет проведена после перезапуска киоска");
        if (command.equals("DEL_KIOSK")) RPCMessage.showMessageDialog(this, "Команда удаления успешно отправлена на сервер");
        if (command.equals("FORBID_CONTROL")) RPCMessage.showMessageDialog(this, "Управление киосками успешно запрещено");
        if (command.equals("ALLOW_CONTROL")) RPCMessage.showMessageDialog(this, "Управление киосками успешно разрешено");
      } else {
        String command = ((AdminCommand)((Request)((At_net)e.getSource()).getObject()).data[0]).getObjectArray()[0].toString();
        if (command.equals("FORBID_CONTROL")) RPCMessage.showMessageDialog(this, "Невозможно запретить управление киосками");
        if (command.equals("ALLOW_CONTROL")) RPCMessage.showMessageDialog(this, "Невозможно разрешить управление киосками");
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
      if (!machineMode) KNC_Terminal.dataUpdater.wakeup();
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
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_MACHINE_ON)) {
      KNC_Terminal.dataUpdater.setMachineMode(true);
      machineMode = true;
      updateTitle();
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_MACHINE_OFF)) {
      KNC_Terminal.dataUpdater.setMachineMode(false);
      machineMode = false;
      machineData = null;
      actualFlag = true;
      KNC_Terminal.dataUpdater.wakeup();
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_MACHINE_DATE)) {
      toolBarPanel1.setLockSlider(true);
      Date date = toolBarPanel1.getMachineDate();
      setUpdatingStart();
      new At_net("GET_MACHINE", new Request(KNC_Terminal.user, new Object[] {0, date}), this).start();
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_MACHINE_TIME)) {
      if (machineData == null) return;
      long time = toolBarPanel1.getMachineTime().getTime();
      ArrayList<KioskProfile> kiosks = getMachineKiosks(time);
      renderer4.setMachineTime(time);
      renderer4.setMachineMode(true);
      tableModel.putKiosks(kiosks, new ArrayList<AdminCommand>());
    }
    if (e.getActionCommand().equals("finished_GET_MACHINE")) {
      if (!machineMode) return;
      machineData = (TreeMap<String, ArrayList<KioskProfile>>)((At_net)e.getSource()).getResult();
      toolBarPanel1.setLockSlider(false);
      this.actionPerformed(new ActionEvent(this, 0, ToolBarPanel1.BUTTON_MACHINE_TIME));
      actualFlag = true;
      setUpdatingFinish();
    }
    if (e.getActionCommand().equals("fail_GET_MACHINE")) {
      actualFlag = false;
      setUpdatingFinish();
      RPCMessage.showMessageDialog(this, "Ошибка соединения с центральным сервером");
    }
  }

  public void setUpdatingStart() {
    updatingFlag = true;
    updateTitle();
  }

  public void setUpdatingFinish() {
    updatingFlag = false;
    updateTitle();
  }

  public void interrupt() {
    this.setTitle(this.getTitle()+KNC_Terminal.interruptTitle);
  }

  private void exportTable(boolean toFile) {
    if (toFile) {
      KNC_Terminal.saveRegistryFileDialog.setDialogTitle("Сохранить таблицу киосков");
      KNC_Terminal.saveRegistryFileDialog.setSelectedFile(new File("Таблица киосков от "+new SimpleDateFormat("dd.MM.yyyy").format(new Date())));
      if (KNC_Terminal.saveRegistryFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = KNC_Terminal.saveRegistryFileDialog.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) file = new File(file.getAbsolutePath()+".xls");
        if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \""+file.getName()+"\"?") == RPCDialog.Result.OK) {
          HSSFWorkbook wb = new HSSFWorkbook();
          wb = new ExcelCreater(wb, "Таблица киосков", table).createTableSheet();
          try (FileOutputStream out = new FileOutputStream(file)) {
            wb.write(out);
          } catch (IOException ex) {
            RPCMessage.showMessageDialog(this, "Не удалось сохранить таблицу киосков: " + ex.getMessage());
          }
        }
      }
    } else {
      try {
        File file = File.createTempFile("Таблица киосков от " + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + " ", ".xls");
        file.deleteOnExit();
        HSSFWorkbook wb = new HSSFWorkbook();
        wb = new ExcelCreater(wb, "Таблица киосков", table).createTableSheet();
        try (FileOutputStream out = new FileOutputStream(file)) {
          wb.write(out);
        }
        Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
      } catch (IOException ex) {
        RPCMessage.showMessageDialog(this, "Не удалось экспортировать таблицу киосков: " + ex.getMessage());
      }
    }
  }

  private ArrayList<KioskProfile> getMachineKiosks(long time) {
    ArrayList<KioskProfile> kiosks = new ArrayList<>();
    Set<String> keys = machineData.keySet();
    for (String number : keys) {
      ArrayList<KioskProfile> stat = machineData.get(number);
      int index = -1;
      for (int i=stat.size()-1; i>=0; i--) {
        KioskProfile k = stat.get(i);
        if (k.lat <= time) {
          index = i;
          break;
        }
      }
      if (index == -1) continue;
      KioskProfile k = stat.get(0);
      KioskProfile kiosk = new KioskProfile(k.data.clone(), null, k.productList.clone(), k.delay);
      for (int i=1; i<=index; i++) {
        k = stat.get(i);
        if (k.data[0] != null) kiosk.data[0] = k.data[0];
        if (k.data[1] != null) kiosk.data[1] = k.data[1];
        if (k.data[2] != null) kiosk.data[2] = k.data[2];
        if (k.data[3] != null) kiosk.data[3] = k.data[3];
        if (k.data[4] != null) kiosk.data[4] = k.data[4];
        if (k.data[5] != null) kiosk.data[5] = k.data[5];
        if (k.data[6] != null) kiosk.data[6] = k.data[6];
        if (k.data[7] != null) kiosk.data[7] = k.data[7];
        if (k.data[8] != null) kiosk.data[8] = k.data[8];
        if (k.data[9] != null) kiosk.data[9] = k.data[9];
        if (k.data[10] != null) kiosk.data[10] = k.data[10];
        if (k.data[11] != null) kiosk.data[11] = k.data[11];
        if (k.data.length >=13 && k.data[12] != null && kiosk.data.length >=13) kiosk.data[12] = k.data[12];
      }
      k = stat.get(index);
      kiosk.delay = k.delay;
      kiosk.lat = k.lat;
      if (time-kiosk.lat > kiosk.delay*2.2+120000) kiosk.setTimeout(true);
      else kiosk.setTimeout(false);
      kiosks.add(kiosk);
    }
    return kiosks;
  }
}

class TableFrame_popupMenu_actionAdapter implements ActionListener {
  private TableFrame adaptee;
  TableFrame_popupMenu_actionAdapter(TableFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.popupMenu_actionPerformed(e);
  }
}

class TableFrame_table_popupAdapter extends MouseAdapter {
  private TableFrame adaptee;
  TableFrame_table_popupAdapter(TableFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mousePressed(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
}

class TableFrame_table_keyAdapter extends KeyAdapter {
  private TableFrame adaptee;
  TableFrame_table_keyAdapter(TableFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void keyPressed(KeyEvent e) {
    adaptee.table_keyPressed(e);
  }
}

class TableFrame_this_componentAdapter extends ComponentAdapter {
  private TableFrame adaptee;
  TableFrame_this_componentAdapter(TableFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void componentShown(ComponentEvent e) {
    adaptee.this_componentShown(e);
  }
  public void componentHidden(ComponentEvent e) {
    adaptee.this_componentHidden(e);
  }
}

class TableFrame_tableModel_tableModelAdapter implements TableModelListener {
  private TableFrame adaptee;
  TableFrame_tableModel_tableModelAdapter(TableFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel_tableChanged(e);
  }
}
