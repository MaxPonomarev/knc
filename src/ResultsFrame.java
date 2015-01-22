import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.decorator.*;

class ResultsFrame extends JFrame implements ActionListener {
  private final static String summaryPaymentsTabName = "сводный реестр платежей";
  private final static String summaryIncassTabName = "сводный реестр инкассаций";
  private final static String INCASSES = "GET_DBINCASS";
  private final static String PAYMENTS = "GET_DBREESTR";
  private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
  private final ArrayList<AdminCommand> results;
  private final Object[] dbResults;
  private final ArrayList<String[]> tabbedMap = new ArrayList<>();
  private final ArrayList<Integer> summaryPaymentsMap = new ArrayList<>();
  private final ArrayList<Integer> summaryIncassMap = new ArrayList<>();
  private FilterMenu filterMenu1;
  private FilterMenu filterMenu2;
  private JPanel contentPane;
  private BorderLayout borderLayout = new BorderLayout();
  private ToolBarPanel2 toolBarPanel = new ToolBarPanel2((byte) 1);
  private JPopupMenu summaryMenu = new JPopupMenu();
  private JMenu paymentsMenu = new JMenu();
  private JMenu incassMenu = new JMenu();
  private JTabbedPane jTabbedPane = new JTabbedPane();
  private JPopupMenu popupMenu1 = new JPopupMenu();
  private JPopupMenu popupMenu2 = new JPopupMenu();
  private JMenuItem jPopupMenu2Item1 = new JMenuItem();

  public ResultsFrame(ArrayList<AdminCommand> results) {
    this.results = results;
    this.dbResults = null;
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public ResultsFrame(Object[] dbResults) {
    this.dbResults = dbResults;
    this.results = null;
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() {
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout);
    this.setIconImages(KNC_Terminal.icons);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setTitle("Результаты запроса");
    this.setBounds(10, 25, Toolkit.getDefaultToolkit().getScreenSize().width - 20, Toolkit.getDefaultToolkit().getScreenSize().height - 60);
    paymentsMenu.setText("Реестры платежей");
    incassMenu.setText("Реестры инкассаций");
    summaryMenu.add(paymentsMenu);
    summaryMenu.add(incassMenu);
    filterMenu1 = KNC_Terminal.filterMenuFactory.getMenu();
    filterMenu1.addActionListener(new ResultsFrame_popupMenu1_actionAdapter(this));
    filterMenu1.addToPopupMenu(popupMenu1);
    jPopupMenu2Item1.setText("Детализация...");
    jPopupMenu2Item1.setActionCommand("DETAL");
    jPopupMenu2Item1.addActionListener(new ResultsFrame_popupMenu2_actionAdapter(this));
    filterMenu2 = KNC_Terminal.filterMenuFactory.getMenu();
    filterMenu2.addActionListener(new ResultsFrame_popupMenu2_actionAdapter(this));
    popupMenu2.add(jPopupMenu2Item1);
    popupMenu2.addSeparator();
    filterMenu2.addToPopupMenu(popupMenu2);
    toolBarPanel.setActionListener(this);
    jTabbedPane.addChangeListener(new ResultsFrame_jTabbedPane_changeAdapter(this));
    jTabbedPane.addKeyListener(new ResultsFrame_table_keyAdapter(this));
    contentPane.add(toolBarPanel, java.awt.BorderLayout.NORTH);
    contentPane.add(jTabbedPane, java.awt.BorderLayout.CENTER);
    if (results != null) {
      for (int i = 0; i < results.size(); i++) {
        AdminCommand result = results.get(i);
        if (result.getObjectArray()[0].equals(PAYMENTS) && KNC_Terminal.user.level > 0) {//--РЕЕСТР-----
          RegistryTableModel tableModel = new RegistryTableModel();
          Object[] obj = result.getResponse();
          KNCPayment[] payments = new KNCPayment[obj.length];
          for (int j = 0; j < obj.length; j++) {
            payments[j] = (KNCPayment) obj[j];
          }
          tableModel.putPayments(payments);
          JXSummaryTable table = createRegistryTable(tableModel, false);
          JScrollPane tableScrollPane = new JScrollPane(table);
          String range;
          if (result.getObjectArray()[2].equals("e") || result.getObjectArray()[3].equals("e")) {
            range = "полный";
          } else {
            String from = dateFormat.format((Date) result.getObjectArray()[2]);
            String to = dateFormat.format((Date) result.getObjectArray()[3]);
            if (from.equals(to)) range = from;
            else range = from + "-" + to;
          }
          jTabbedPane.addTab("реестр " + range + " киоск №" + result.kiosk_number, tableScrollPane);
          table.packAll();
          tabbedMap.add(new String[]{PAYMENTS, range, result.kiosk_number});
          JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(range + " киоск №" + result.kiosk_number);
          menuItem.setState(true);
          menuItem.addActionListener(new ResultsFrame_summaryMenuItem_actionAdapter(this));
          paymentsMenu.add(menuItem);
          summaryPaymentsMap.add(i);
        }
        if (result.getObjectArray()[0].equals(INCASSES) && KNC_Terminal.user.level > 0) {//--ИНКАССАЦИЯ-
          IncassTableModel tableModel = new IncassTableModel();
          Object[] obj = result.getResponse();
          Incass[] incass = new Incass[obj.length];
          for (int j = 0; j < obj.length; j++) {
            incass[j] = (Incass) obj[j];
          }
          tableModel.putIncass(incass);
          JXSummaryTable table = createIncassTable(tableModel, false);
          JScrollPane tableScrollPane = new JScrollPane(table);
          String from = dateFormat.format((Date) result.getObjectArray()[2]);
          String to = dateFormat.format((Date) result.getObjectArray()[3]);
          String range;
          if (from.equals(to)) range = from;
          else range = from + "-" + to;
          jTabbedPane.addTab("реестр " + range + " киоск №" + result.kiosk_number, tableScrollPane);
          table.packAll();
          tabbedMap.add(new String[]{INCASSES, range, result.kiosk_number});
          JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(range + " киоск №" + result.kiosk_number);
          menuItem.setState(true);
          menuItem.addActionListener(new ResultsFrame_summaryMenuItem_actionAdapter(this));
          incassMenu.add(menuItem);
          summaryIncassMap.add(i);
        }
        if (result.getObjectArray()[0].equals("GET_LOG") && KNC_Terminal.user.level > 2) {//--ЛОГ-----
          JXFiltrableEditorPane log1EditorPane = new JXFiltrableEditorPane();
          JXFiltrableEditorPane log2EditorPane = new JXFiltrableEditorPane();
          log1EditorPane.setText("" + result.getResponse()[0]);
          log2EditorPane.setText("" + result.getResponse()[1]);
          log1EditorPane.setFont(new Font("Dialog", Font.PLAIN, 12));
          log2EditorPane.setFont(new Font("Dialog", Font.PLAIN, 12));
          log1EditorPane.addKeyListener(new ResultsFrame_table_keyAdapter(this));
          log2EditorPane.addKeyListener(new ResultsFrame_table_keyAdapter(this));
          JScrollPane log1ScrollPane = new JScrollPane(log1EditorPane);
          JScrollPane log2ScrollPane = new JScrollPane(log2EditorPane);
          String from = dateFormat.format((Date) result.getObjectArray()[2]);
          String to = dateFormat.format((Date) result.getObjectArray()[3]);
          String range;
          if (from.equals(to)) range = from;
          else range = from + "-" + to;
          jTabbedPane.addTab("лог событий " + range + " киоск №" + result.kiosk_number, log1ScrollPane);
          jTabbedPane.addTab("лог ошибок " + range + " киоск №" + result.kiosk_number, log2ScrollPane);
          tabbedMap.add(new String[]{"GET_LOG1", range, result.kiosk_number});
          tabbedMap.add(new String[]{"GET_LOG2", range, result.kiosk_number});
        }
      }
      if (summaryPaymentsMap.size() > 1) {
        paymentsMenu.addSeparator();
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem("Все");
        menuItem.setState(true);
        menuItem.addActionListener(new ResultsFrame_paymentsMenuItem_actionAdapter(this));
        paymentsMenu.add(menuItem);
      } else {
        paymentsMenu.setEnabled(false);
      }
      if (summaryIncassMap.size() > 1) {
        incassMenu.addSeparator();
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem("Все");
        menuItem.setState(true);
        menuItem.addActionListener(new ResultsFrame_incassMenuItem_actionAdapter(this));
        incassMenu.add(menuItem);
      } else {
        incassMenu.setEnabled(false);
      }
    } else if (dbResults != null && dbResults[0].equals(INCASSES)) {
      IncassTableModel tableModel = new IncassTableModel();
      tableModel.putIncass((Incass[]) dbResults[1]);
      JXSummaryTable table = createIncassTable(tableModel, true);
      JScrollPane tableScrollPane = new JScrollPane(table);
      String from = dateFormat.format((Date) dbResults[2]);
      String to = dateFormat.format((Date) dbResults[3]);
      String range;
      if (from.equals(to)) range = from;
      else range = from + "-" + to;
      jTabbedPane.addTab("реестр инкассаций " + range, tableScrollPane);
      table.packAll();
      tabbedMap.add(new String[]{INCASSES, range, ""});
    } else if (dbResults != null && dbResults[0].equals(PAYMENTS)) {
      RegistryTableModel tableModel = new RegistryTableModel();
      tableModel.putPayments((KNCPayment[]) dbResults[1]);
      JXSummaryTable table = createRegistryTable(tableModel, true);
      JScrollPane tableScrollPane = new JScrollPane(table);
      String range;
      if (dbResults[2].equals("e") || dbResults[3].equals("e")) {
        range = "полный";
      } else {
        String from = dateFormat.format((Date) dbResults[2]);
        String to = dateFormat.format((Date) dbResults[3]);
        if (from.equals(to)) range = from;
        else range = from + "-" + to;
      }
      jTabbedPane.addTab("реестр платежей " + range, tableScrollPane);
      table.packAll();
      tabbedMap.add(new String[]{PAYMENTS, range, ""});
    }
    if (summaryPaymentsMap.size() > 1 || summaryIncassMap.size() > 1) toolBarPanel.summaryButton.setEnabled(true);
    else toolBarPanel.summaryButton.setEnabled(false);
    summaryMenuItem_actionPerformed(null);
    jTabbedPane_stateChanged(null);
  }

  public void table_keyPressed(KeyEvent e) {
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F && e.getSource().equals(jTabbedPane)) {
      int i = jTabbedPane.getSelectedIndex();
      String command = tabbedMap.get(i)[0];
      if (command.equals(PAYMENTS) || command.equals(INCASSES)) {
        JXTable table = (JXTable) ((JScrollPane) jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
        table.getActionMap().get("find").actionPerformed(null);
      } else if (command.startsWith("GET_LOG")) {
        JXEditorPane editorPane = (JXEditorPane) ((JScrollPane) jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
        editorPane.getActionMap().get("find").actionPerformed(null);
      }
    }
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E) {
      exportTable(false);
    }
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
      exportTable(true);
    }
    if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_A) {
      saveTables();
    }
  }

  public void jTabbedPane_stateChanged(ChangeEvent e) {
    int i = jTabbedPane.getSelectedIndex();
    if (tabbedMap.size() == 0) return;
    String command = tabbedMap.get(i)[0];
    if (command.equals(PAYMENTS) || command.equals(INCASSES)) {
      toolBarPanel.setFilterMode(false);
    } else if (command.startsWith("GET_LOG")) {
      toolBarPanel.setFilterMode(true);
    }
  }

  public void table1_maybeShowPopup(MouseEvent e) {//меню в реестре платежей
    int activeRow;
    JXTable table = (JXTable) e.getSource();
    activeRow = table.rowAtPoint(new Point(e.getX(), e.getY()));
    if (activeRow == -1) {
      table.removeRowSelectionInterval(0, table.getRowCount() - 1);
    } else {
      if (e.isPopupTrigger()) {
        if (table.convertRowIndexToModel(activeRow) == table.getModel().getRowCount() - 1) return;
        if (!table.isRowSelected(activeRow)) table.setRowSelectionInterval(activeRow, activeRow);
        popupMenu1.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  public void table2_maybeShowPopup(MouseEvent e) {//меню в реестре инкассаций
    int activeRow;
    JXTable table = (JXTable) e.getSource();
    activeRow = table.rowAtPoint(new Point(e.getX(), e.getY()));
    if (activeRow == -1) {
      table.removeRowSelectionInterval(0, table.getRowCount() - 1);
    } else {
      if (e.isPopupTrigger()) {
        if (table.convertRowIndexToModel(activeRow) == table.getModel().getRowCount() - 1) return;
        if (!table.isRowSelected(activeRow)) table.setRowSelectionInterval(activeRow, activeRow);
        popupMenu2.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  public void popupMenu1_actionPerformed(ActionEvent e) {//меню в реестре платежей
    Component menuItem = (Component) e.getSource();
    JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
    JXTable table = (JXTable) popupMenu.getInvoker();
    int[] rows = table.getSelectedRows();
    int[] modelRows = new int[rows.length];
    for (int i = 0; i < rows.length; i++) {
      modelRows[i] = table.convertRowIndexToModel(rows[i]);
    }
    if (e.getActionCommand().equals(FilterMenu.CANCEL_FILTER)) {
      ((FiltrableCustomTableModel) table.getModel()).revertRows();
      filterMenu1.updateAfterCommand(e.getActionCommand());
      table.packAll();
    }
    if (e.getActionCommand().equals(FilterMenu.INCLUDE)) {
      ((FiltrableCustomTableModel) table.getModel()).leaveRows(modelRows);
      filterMenu1.updateAfterCommand(e.getActionCommand());
      table.packAll();
    }
    if (e.getActionCommand().equals(FilterMenu.EXCLUDE)) {
      try {
        ((FiltrableCustomTableModel) table.getModel()).deleteRows(modelRows);
        filterMenu1.updateAfterCommand(e.getActionCommand());
        table.packAll();
      } catch (FiltrableTableModelException ex) {
        RPCMessage.showMessageDialog(this, "Нельзя исключить все строки");
      }
    }
  }

  public void popupMenu2_actionPerformed(ActionEvent e) {//меню в реестре инкассаций
    Component menuItem = (Component) e.getSource();
    JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
    JXTable table = (JXTable) popupMenu.getInvoker();
    int[] rows = table.getSelectedRows();
    int[] modelRows = new int[rows.length];
    for (int i = 0; i < rows.length; i++) {
      modelRows[i] = table.convertRowIndexToModel(rows[i]);
    }
    if (e.getActionCommand().equals("DETAL")) {
      new IncassDataFrame(((IncassTableModel) table.getModel()).getIncass(modelRows)).setVisible(true);
    }
    if (e.getActionCommand().equals(FilterMenu.CANCEL_FILTER)) {
      ((FiltrableCustomTableModel) table.getModel()).revertRows();
      filterMenu2.updateAfterCommand(e.getActionCommand());
      table.packAll();
    }
    if (e.getActionCommand().equals(FilterMenu.INCLUDE)) {
      ((FiltrableCustomTableModel) table.getModel()).leaveRows(modelRows);
      filterMenu2.updateAfterCommand(e.getActionCommand());
      table.packAll();
    }
    if (e.getActionCommand().equals(FilterMenu.EXCLUDE)) {
      try {
        ((FiltrableCustomTableModel) table.getModel()).deleteRows(modelRows);
        filterMenu2.updateAfterCommand(e.getActionCommand());
        table.packAll();
      } catch (FiltrableTableModelException ex) {
        RPCMessage.showMessageDialog(this, "Нельзя исключить все строки");
      }
    }
  }

  public void summaryMenuItem_actionPerformed(ActionEvent e) {
    int current = jTabbedPane.getSelectedIndex();
    if (summaryPaymentsMap.size() > 1) {
      RegistryTableModel tableModel = new RegistryTableModel();
      ArrayList<KNCPayment> paymentsList = new ArrayList<>();
      int includeCount = 0;
      for (int i = 0; i < summaryPaymentsMap.size(); i++) {
        if (!((JCheckBoxMenuItem) paymentsMenu.getItem(i)).getState()) continue;
        includeCount++;
        AdminCommand result = results.get(summaryPaymentsMap.get(i).intValue());
        Object[] obj = result.getResponse();
        for (Object o : obj) {
          paymentsList.add((KNCPayment) o);
        }
      }
      int tabIndex = jTabbedPane.indexOfTab(summaryPaymentsTabName);
      if (includeCount > 1) {
        KNCPayment[] payments = new KNCPayment[paymentsList.size()];
        payments = paymentsList.toArray(payments);
        tableModel.putPayments(payments);
        JXSummaryTable table = createRegistryTable(tableModel, true);
        JScrollPane tableScrollPane = new JScrollPane(table);
        if (tabIndex == -1) {
          jTabbedPane.addTab(summaryPaymentsTabName, tableScrollPane);
          tabbedMap.add(new String[]{PAYMENTS, "сводный", ""});
        } else {
          jTabbedPane.remove(tabIndex);
          jTabbedPane.insertTab(summaryPaymentsTabName, null, tableScrollPane, null, tabIndex);
          tabbedMap.set(tabIndex, new String[]{PAYMENTS, "сводный", ""});
        }
        table.packAll();
      } else if (tabIndex >= 0) { //если реестров мало, то надо убрать закладку со сводкой
        jTabbedPane.remove(tabIndex);
        tabbedMap.remove(tabIndex);
      }
    }
    if (summaryIncassMap.size() > 1) {
      IncassTableModel tableModel = new IncassTableModel();
      ArrayList<Incass> incassList = new ArrayList<>();
      int includeCount = 0;
      for (int i = 0; i < summaryIncassMap.size(); i++) {
        if (!((JCheckBoxMenuItem) incassMenu.getItem(i)).getState()) continue;
        includeCount++;
        AdminCommand result = results.get(summaryIncassMap.get(i).intValue());
        Object[] obj = result.getResponse();
        for (Object o : obj) {
          incassList.add((Incass) o);
        }
      }
      int tabIndex = jTabbedPane.indexOfTab(summaryIncassTabName);
      if (includeCount > 1) {
        Incass[] incass = new Incass[incassList.size()];
        incass = incassList.toArray(incass);
        tableModel.putIncass(incass);
        JXSummaryTable table = createIncassTable(tableModel, true);
        JScrollPane tableScrollPane = new JScrollPane(table);
        if (tabIndex == -1) {
          jTabbedPane.addTab(summaryIncassTabName, tableScrollPane);
          tabbedMap.add(new String[]{INCASSES, "сводный", ""});
        } else {
          jTabbedPane.remove(tabIndex);
          jTabbedPane.insertTab(summaryIncassTabName, null, tableScrollPane, null, tabIndex);
          tabbedMap.set(tabIndex, new String[]{INCASSES, "сводный", ""});
        }
        table.packAll();
      } else if (tabIndex >= 0) { //если реестров мало, то надо убрать закладку со сводкой
        jTabbedPane.remove(tabIndex);
        tabbedMap.remove(tabIndex);
      }
    }
    if (current < jTabbedPane.getTabCount()) jTabbedPane.setSelectedIndex(current);
  }

  public void paymentsMenuItem_actionPerformed(ActionEvent e) {
    boolean state = ((JCheckBoxMenuItem) e.getSource()).getState();
    for (int i = 0; i < summaryPaymentsMap.size(); i++) {
      ((JCheckBoxMenuItem) paymentsMenu.getItem(i)).setState(state);
    }
    summaryMenuItem_actionPerformed(null);
  }

  public void incassMenuItem_actionPerformed(ActionEvent e) {
    boolean state = ((JCheckBoxMenuItem) e.getSource()).getState();
    for (int i = 0; i < summaryIncassMap.size(); i++) {
      ((JCheckBoxMenuItem) incassMenu.getItem(i)).setState(state);
    }
    summaryMenuItem_actionPerformed(null);
  }

  public void actionPerformed(ActionEvent e) {
    int tabIndex = jTabbedPane.getSelectedIndex();
    String tabType = tabbedMap.get(tabIndex)[0];
    if (e.getActionCommand().equals(ToolBarPanel2.BUTTON_SEARCH)) {
      if (tabType.equals(PAYMENTS) || tabType.equals(INCASSES)) {
        JXTable table = (JXTable) ((JScrollPane) jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
        table.getActionMap().get("find").actionPerformed(null);
      } else if (tabType.startsWith("GET_LOG")) {
        JXEditorPane editorPane = (JXEditorPane) ((JScrollPane) jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
        editorPane.getActionMap().get("find").actionPerformed(null);
      }
    }
    if (e.getActionCommand().equals(ToolBarPanel2.BUTTON_EXPORT)) {
      exportTable(false);
    }
    if (e.getActionCommand().equals(ToolBarPanel2.BUTTON_SAVE)) {
      exportTable(true);
    }
    if (e.getActionCommand().equals(ToolBarPanel2.BUTTON_SAVE_ALL)) {
      saveTables();
    }
    if (e.getActionCommand().equals(ToolBarPanel2.BUTTON_SUMMARY)) {
      summaryMenu.show(toolBarPanel.summaryButton, 0, toolBarPanel.summaryButton.getHeight());
    }
    if (e.getActionCommand().startsWith(ToolBarPanel2.FILTER_INCLUDE)) {
      if (tabType.startsWith("GET_LOG")) {
        JXFiltrableEditorPane editorPane = (JXFiltrableEditorPane) ((JScrollPane) jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
        editorPane.leaveLines(ToolBarPanel2.getFilterFromCommand(e.getActionCommand()));
      }
    }
    if (e.getActionCommand().startsWith(ToolBarPanel2.FILTER_EXCLUDE)) {
      if (tabType.startsWith("GET_LOG")) {
        JXFiltrableEditorPane editorPane = (JXFiltrableEditorPane) ((JScrollPane) jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
        editorPane.deleteLines(ToolBarPanel2.getFilterFromCommand(e.getActionCommand()));
      }
    }
    if (e.getActionCommand().startsWith(ToolBarPanel2.FILTER_HIGHLIGHT)) {
      if (tabType.startsWith("GET_LOG")) {
        JXFiltrableEditorPane editorPane = (JXFiltrableEditorPane) ((JScrollPane) jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
        try {
          editorPane.highlightLines(ToolBarPanel2.getFilterFromCommand(e.getActionCommand()), Color.YELLOW);
        } catch (BadLocationException ex) {
          RPCMessage.showMessageDialog(this, "Ошибка при выделении");
        }
      }
    }
    if (e.getActionCommand().equals(ToolBarPanel2.FILTER_CANCEL)) {
      if (tabType.startsWith("GET_LOG")) {
        JXFiltrableEditorPane editorPane = (JXFiltrableEditorPane) ((JScrollPane) jTabbedPane.getSelectedComponent()).getViewport().getComponent(0);
        editorPane.revertLines();
      }
    }
  }

  private JXSummaryTable createRegistryTable(RegistryTableModel tableModel, boolean summary) {
    JXSummaryTable table = new JXSummaryTable(tableModel);
    table.setRowHeight(33);
    table.setColumnControlVisible(true);
    table.getColumnModel().getColumn(0).setMinWidth(54);
    table.getColumnModel().getColumn(0).setMaxWidth(58);
    table.getColumnModel().getColumn(1).setMinWidth(47);
    table.getColumnModel().getColumn(2).setMinWidth(120);
    table.getColumnModel().getColumn(7).setMinWidth(75);
    table.getColumnModel().getColumn(8).setMinWidth(35);
    table.getColumnModel().getColumn(9).setMinWidth(53);
    table.getColumnModel().getColumn(11).setMinWidth(43);
    table.getColumnModel().getColumn(14).setMinWidth(63);
    table.getColumnExt(13).setVisible(false);
    table.getColumnExt(12).setVisible(false);
    if (!tableModel.isSubAccounts()) {
      table.getColumnExt(6).setVisible(false);
    }
    table.getColumnExt(4).setVisible(false);
    table.getColumnExt(3).setVisible(false);
    if (!summary) table.getColumnExt(0).setVisible(false);
    table.setHorizontalScrollEnabled(true);
    table.addMouseListener(new ResultsFrame_table1_popupAdapter(this));
    table.addKeyListener(new ResultsFrame_table_keyAdapter(this));
    table.addHighlighter(HighlighterFactory.createSimpleStriping(new Color(240, 240, 224)));
    table.addHighlighter(new ColorHighlighter(new PatternPredicate(Pattern.compile("Действующий"), 14), new Color(183, 255, 198), Color.BLACK, new Color(130, 227, 170), Color.BLACK));
    table.addHighlighter(new ColorHighlighter(new PatternPredicate(Pattern.compile("Отклонен"), 14), new Color(255, 209, 155), Color.BLACK, new Color(210, 200, 179), Color.BLACK));
    return table;
  }

  private JXSummaryTable createIncassTable(IncassTableModel tableModel, boolean summary) {
    JXSummaryTable table = new JXSummaryTable(tableModel);
    table.setRowHeight(33);
    table.setColumnControlVisible(true);
    table.getColumnModel().getColumn(0).setMinWidth(54);
    table.getColumnModel().getColumn(0).setMaxWidth(58);
    table.getColumnModel().getColumn(1).setMinWidth(120);
    table.getColumnModel().getColumn(2).setMinWidth(50);
    table.getColumnModel().getColumn(3).setMinWidth(50);
    table.getColumnModel().getColumn(4).setMinWidth(35);
    table.getColumnModel().getColumn(5).setMinWidth(35);
    table.getColumnModel().getColumn(6).setMinWidth(35);
    table.getColumnModel().getColumn(7).setMinWidth(35);
    table.getColumnModel().getColumn(8).setMinWidth(35);
    table.getColumnModel().getColumn(9).setMinWidth(35);
    table.getColumnModel().getColumn(10).setMinWidth(35);
    if (!summary) table.getColumnExt(0).setVisible(false);
    table.setHorizontalScrollEnabled(true);
    table.addMouseListener(new ResultsFrame_table2_popupAdapter(this));
    table.addKeyListener(new ResultsFrame_table_keyAdapter(this));
    table.addHighlighter(HighlighterFactory.createSimpleStriping(new Color(240, 240, 224)));
    return table;
  }

  private void exportTable(boolean toFile) {
    int i = jTabbedPane.getSelectedIndex();
    JViewport currentViewport = ((JScrollPane) jTabbedPane.getSelectedComponent()).getViewport();
    String command = tabbedMap.get(i)[0];
    String range = tabbedMap.get(i)[1];
    String kiosk = tabbedMap.get(i)[2];
    if (!kiosk.equals("")) kiosk = " №" + kiosk;
    if (toFile) {
      if (command.equals(PAYMENTS)) {
        KNC_Terminal.saveRegistryFileDialog.setDialogTitle("Сохранить реестр платежей");
        KNC_Terminal.saveRegistryFileDialog.setSelectedFile(new File("Реестр платежей " + range));
        if (KNC_Terminal.saveRegistryFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
          File file = KNC_Terminal.saveRegistryFileDialog.getSelectedFile();
          if (!file.getAbsolutePath().endsWith(".xls")) file = new File(file.getAbsolutePath() + ".xls");
          if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \"" + file.getName() + "\"?") == RPCDialog.Result.OK) {
            HSSFWorkbook wb = new HSSFWorkbook();
            JXTable table = (JXTable) currentViewport.getComponent(0);
            wb = new ExcelCreater(wb, range + kiosk, table).createRegistrySheet();
            try (FileOutputStream out = new FileOutputStream(file)) {
              wb.write(out);
            } catch (IOException ex) {
              RPCMessage.showMessageDialog(this, "Не удалось сохранить реестр: " + ex.getMessage());
            }
          }
        }
      }
      if (command.equals(INCASSES)) {
        KNC_Terminal.saveIncassFileDialog.setDialogTitle("Сохранить реестр инкассаций");
        KNC_Terminal.saveIncassFileDialog.setSelectedFile(new File("Реестр инкассаций " + range));
        if (KNC_Terminal.saveIncassFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
          File file = KNC_Terminal.saveIncassFileDialog.getSelectedFile();
          String[] exts = ((FileNameExtensionFilter) KNC_Terminal.saveIncassFileDialog.getFileFilter()).getExtensions();
          if (Arrays.asList(exts).contains("xls") && !file.getAbsolutePath().endsWith(".xls"))
            file = new File(file.getAbsolutePath() + ".xls");
          if (Arrays.asList(exts).contains("txt") && !file.getAbsolutePath().endsWith(".txt"))
            file = new File(file.getAbsolutePath() + ".txt");
          if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \"" + file.getName() + "\"?") == RPCDialog.Result.OK) {
            if (Arrays.asList(exts).contains("xls")) {
              HSSFWorkbook wb = new HSSFWorkbook();
              JXTable table = (JXTable) currentViewport.getComponent(0);
              wb = new ExcelCreater(wb, range + kiosk, table).createIncassSheet();
              try (FileOutputStream out = new FileOutputStream(file)) {
                wb.write(out);
              } catch (IOException ex) {
                RPCMessage.showMessageDialog(this, "Не удалось сохранить реестр: " + ex.getMessage());
              }
            }
            if (Arrays.asList(exts).contains("txt")) {
              try (FileWriter out = new FileWriter(file, false)) {
                JXTable table = (JXTable) currentViewport.getComponent(0);
                String reg = ((IncassTableModel) table.getModel()).getFilteredIncass1C();
                out.write(reg);
              } catch (IOException ex) {
                RPCMessage.showMessageDialog(this, "Не удалось сохранить реестр: " + ex.getMessage());
              }
            }
          }
        }
      }
      if (command.equals("GET_LOG1")) KNC_Terminal.saveLogFileDialog.setSelectedFile(new File("operation" + range));
      if (command.equals("GET_LOG2")) KNC_Terminal.saveLogFileDialog.setSelectedFile(new File("error" + range));
      if (command.startsWith("GET_LOG")) {
        if (KNC_Terminal.saveLogFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
          File file = KNC_Terminal.saveLogFileDialog.getSelectedFile();
          if (!file.getAbsolutePath().endsWith(".log")) file = new File(file.getAbsolutePath() + ".log");
          if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \"" + file.getName() + "\"?") == RPCDialog.Result.OK) {
            String text = ((JXEditorPane) currentViewport.getComponent(0)).getText();
            try (FileOutputStream out = new FileOutputStream(file)) {
              out.write(text.getBytes("windows-1251"));
            } catch (IOException ex) {
              RPCMessage.showMessageDialog(this, "Не удалось сохранить лог: " + ex.getMessage());
            }
          }
        }
      }
    } else {
      if (command.equals(PAYMENTS)) {
        try {
          File file = File.createTempFile("Реестр платежей " + range + " ", ".xls");
          file.deleteOnExit();
          HSSFWorkbook wb = new HSSFWorkbook();
          JXTable table = (JXTable) currentViewport.getComponent(0);
          wb = new ExcelCreater(wb, range + kiosk, table).createRegistrySheet();
          try (FileOutputStream out = new FileOutputStream(file)) {
            wb.write(out);
          }
          Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
        } catch (IOException ex) {
          RPCMessage.showMessageDialog(this, "Не удалось экспортировать реестр: " + ex.getMessage());
        }
      }
      if (command.equals(INCASSES)) {
        try {
          File file = File.createTempFile("Реестр инкассаций " + range + " ", ".xls");
          file.deleteOnExit();
          HSSFWorkbook wb = new HSSFWorkbook();
          JXTable table = (JXTable) currentViewport.getComponent(0);
          wb = new ExcelCreater(wb, range + kiosk, table).createIncassSheet();
          try (FileOutputStream out = new FileOutputStream(file)) {
            wb.write(out);
          }
          Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
        } catch (IOException ex) {
          RPCMessage.showMessageDialog(this, "Не удалось экспортировать реестр: " + ex.getMessage());
        }
      }
      if (command.startsWith("GET_LOG")) {
        try {
          File file = File.createTempFile("log ", ".log");
          file.deleteOnExit();
          String text = ((JXEditorPane) currentViewport.getComponent(0)).getText();
          try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(text.getBytes("windows-1251"));
          }
          Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
        } catch (IOException ex) {
          RPCMessage.showMessageDialog(this, "Не удалось экспортировать лог: " + ex.getMessage());
        }
      }
    }
  }

  private void saveTables() {
    int i = jTabbedPane.getSelectedIndex();
    String command = tabbedMap.get(i)[0];
    if (command.equals(PAYMENTS)) {
      KNC_Terminal.saveRegistryFileDialog.setDialogTitle("Сохранить реестр платежей");
      KNC_Terminal.saveRegistryFileDialog.setSelectedFile(new File("Реестр платежей"));
      if (KNC_Terminal.saveRegistryFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = KNC_Terminal.saveRegistryFileDialog.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) file = new File(file.getAbsolutePath() + ".xls");
        if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \"" + file.getName() + "\"?") == RPCDialog.Result.OK) {
          HSSFWorkbook wb = new HSSFWorkbook();
          for (int j = 0; j < jTabbedPane.getTabCount(); j++) {
            command = tabbedMap.get(j)[0];
            if (command.equals(PAYMENTS)) {
              JViewport currentViewport = ((JScrollPane) jTabbedPane.getComponentAt(j)).getViewport();
              String range = tabbedMap.get(j)[1];
              String kiosk = tabbedMap.get(j)[2];
              if (!kiosk.equals("")) kiosk = " №" + kiosk;
              JXTable table = (JXTable) currentViewport.getComponent(0);
              wb = new ExcelCreater(wb, range + kiosk, table).createRegistrySheet();
            }
          }
          try (FileOutputStream out = new FileOutputStream(file)) {
            wb.write(out);
          } catch (IOException ex) {
            RPCMessage.showMessageDialog(this, "Не удалось сохранить реестр: " + ex.getMessage());
          }
        }
      }
    } else if (command.equals(INCASSES)) {
      KNC_Terminal.saveRegistryFileDialog.setDialogTitle("Сохранить реестр инкассаций");
      KNC_Terminal.saveRegistryFileDialog.setSelectedFile(new File("Реестр инкассаций"));
      if (KNC_Terminal.saveRegistryFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = KNC_Terminal.saveRegistryFileDialog.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) file = new File(file.getAbsolutePath() + ".xls");
        if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \"" + file.getName() + "\"?") == RPCDialog.Result.OK) {
          HSSFWorkbook wb = new HSSFWorkbook();
          for (int j = 0; j < jTabbedPane.getTabCount(); j++) {
            command = tabbedMap.get(j)[0];
            if (command.equals(INCASSES)) {
              JViewport currentViewport = ((JScrollPane) jTabbedPane.getComponentAt(j)).getViewport();
              String range = tabbedMap.get(j)[1];
              String kiosk = tabbedMap.get(j)[2];
              if (!kiosk.equals("")) kiosk = " №" + kiosk;
              JXTable table = (JXTable) currentViewport.getComponent(0);
              wb = new ExcelCreater(wb, range + kiosk, table).createIncassSheet();
            }
          }
          try (FileOutputStream out = new FileOutputStream(file)) {
            wb.write(out);
          } catch (IOException ex) {
            RPCMessage.showMessageDialog(this, "Не удалось сохранить реестр: " + ex.getMessage());
          }
        }
      }
    }
  }
}

class ResultsFrame_summaryMenuItem_actionAdapter implements ActionListener {
  private ResultsFrame adaptee;

  ResultsFrame_summaryMenuItem_actionAdapter(ResultsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.summaryMenuItem_actionPerformed(e);
  }
}

class ResultsFrame_paymentsMenuItem_actionAdapter implements ActionListener {
  private ResultsFrame adaptee;

  ResultsFrame_paymentsMenuItem_actionAdapter(ResultsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.paymentsMenuItem_actionPerformed(e);
  }
}

class ResultsFrame_incassMenuItem_actionAdapter implements ActionListener {
  private ResultsFrame adaptee;

  ResultsFrame_incassMenuItem_actionAdapter(ResultsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.incassMenuItem_actionPerformed(e);
  }
}

class ResultsFrame_popupMenu1_actionAdapter implements ActionListener {
  private ResultsFrame adaptee;

  ResultsFrame_popupMenu1_actionAdapter(ResultsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.popupMenu1_actionPerformed(e);
  }
}

class ResultsFrame_popupMenu2_actionAdapter implements ActionListener {
  private ResultsFrame adaptee;

  ResultsFrame_popupMenu2_actionAdapter(ResultsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.popupMenu2_actionPerformed(e);
  }
}

class ResultsFrame_table1_popupAdapter extends MouseAdapter {
  private ResultsFrame adaptee;

  ResultsFrame_table1_popupAdapter(ResultsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void mousePressed(MouseEvent e) {
    adaptee.table1_maybeShowPopup(e);
  }

  public void mouseReleased(MouseEvent e) {
    adaptee.table1_maybeShowPopup(e);
  }
}

class ResultsFrame_table2_popupAdapter extends MouseAdapter {
  private ResultsFrame adaptee;

  ResultsFrame_table2_popupAdapter(ResultsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void mousePressed(MouseEvent e) {
    adaptee.table2_maybeShowPopup(e);
  }

  public void mouseReleased(MouseEvent e) {
    adaptee.table2_maybeShowPopup(e);
  }
}

class ResultsFrame_table_keyAdapter extends KeyAdapter {
  private ResultsFrame adaptee;

  ResultsFrame_table_keyAdapter(ResultsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
    adaptee.table_keyPressed(e);
  }
}

class ResultsFrame_jTabbedPane_changeAdapter implements ChangeListener {
  private ResultsFrame adaptee;

  ResultsFrame_jTabbedPane_changeAdapter(ResultsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void stateChanged(ChangeEvent e) {
    adaptee.jTabbedPane_stateChanged(e);
  }
}
