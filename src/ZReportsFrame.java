import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdesktop.swingx.JXSummaryTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

class ZReportsFrame extends JFrame implements ActionListener {
  private final String MAIN_TITLE = "Реестр Z-отчетов";
  private final ZReport[] zReports;

  private ZReportsTableModel tableModel;
  private FilterMenu filterMenu;
  private JPanel contentPane;
  private BorderLayout borderLayout = new BorderLayout();
  private ToolBarPanel2 toolBarPanel2;
  private JXSummaryTable table;
  private JScrollPane scrollPane;
  private JPopupMenu popupMenu = new JPopupMenu();

  public ZReportsFrame(ZReport[] zReports) {
    this.zReports = zReports;
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() {
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout);
    this.setIconImages(KNC_Terminal.icons);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setTitle(MAIN_TITLE);
    this.setBounds(10, 25, Toolkit.getDefaultToolkit().getScreenSize().width-20, Toolkit.getDefaultToolkit().getScreenSize().height-60);
    if (KNC_Terminal.defaultTableMaximized > 0) this.setExtendedState(Frame.MAXIMIZED_BOTH);
    toolBarPanel2 = new ToolBarPanel2((byte)0);
    toolBarPanel2.setActionListener(this);
    contentPane.add(toolBarPanel2, java.awt.BorderLayout.NORTH);
    tableModel = new ZReportsTableModel();
    tableModel.putZReports(zReports);
    table = new JXSummaryTable(tableModel);
    table.setRowHeight(33);
    table.setColumnControlVisible(true);
    table.setHorizontalScrollEnabled(true);
    table.addMouseListener(new ZReportsFrame_table_popupAdapter(this));
    table.addKeyListener(new ZReportsFrame_table_keyAdapter(this));
    table.addHighlighter(HighlighterFactory.createSimpleStriping(new Color(240,240,224)));
    scrollPane = new JScrollPane(table);
    contentPane.add(scrollPane, java.awt.BorderLayout.CENTER);
    table.packAll();
    filterMenu = KNC_Terminal.filterMenuFactory.getMenu();
    filterMenu.addActionListener(new ZReportsFrame_popupMenu_actionAdapter(this));
    filterMenu.addToPopupMenu(popupMenu);
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
    int[] rows = table.getSelectedRows();
    int[] modelRows = new int[rows.length];
    for (int i=0; i<rows.length; i++) {
      modelRows[i] = table.convertRowIndexToModel(rows[i]);
    }
    if (e.getActionCommand().equals(FilterMenu.CANCEL_FILTER)) {
      ((FiltrableCustomTableModel)table.getModel()).revertRows();
      filterMenu.updateAfterCommand(e.getActionCommand());
      table.packAll();
    }
    if (e.getActionCommand().equals(FilterMenu.INCLUDE)) {
      ((FiltrableCustomTableModel)table.getModel()).leaveRows(modelRows);
      filterMenu.updateAfterCommand(e.getActionCommand());
      table.packAll();
    }
    if (e.getActionCommand().equals(FilterMenu.EXCLUDE)) {
      try {
        ((FiltrableCustomTableModel)table.getModel()).deleteRows(modelRows);
        filterMenu.updateAfterCommand(e.getActionCommand());
        table.packAll();
      }
      catch (FiltrableTableModelException ex) {
        RPCMessage.showMessageDialog(this, "Нельзя исключить все строки");
      }
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_SEARCH)) {
      table.getActionMap().get("find").actionPerformed(null);
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_EXPORT)) {
      exportTable(false);
    }
    if (e.getActionCommand().equals(ToolBarPanel1.BUTTON_SAVE)) {
      exportTable(true);
    }
  }

  private void exportTable(boolean toFile) {
    if (toFile) {
      KNC_Terminal.saveZReportsFileDialog.setDialogTitle("Сохранить реестр Z-отчетов");
      KNC_Terminal.saveZReportsFileDialog.setSelectedFile(new File("Реестр Z-отчетов от "+new SimpleDateFormat("dd.MM.yyyy").format(new Date())));
      if (KNC_Terminal.saveZReportsFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = KNC_Terminal.saveZReportsFileDialog.getSelectedFile();
        String[] exts = ((FileNameExtensionFilter) KNC_Terminal.saveZReportsFileDialog.getFileFilter()).getExtensions();
        if (Arrays.asList(exts).contains("xls") && !file.getAbsolutePath().endsWith(".xls"))
          file = new File(file.getAbsolutePath() + ".xls");
        if (Arrays.asList(exts).contains("txt") && !file.getAbsolutePath().endsWith(".txt"))
          file = new File(file.getAbsolutePath() + ".txt");
        if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \""+file.getName()+"\"?") == RPCDialog.Result.OK) {
          if (Arrays.asList(exts).contains("xls")) {
            HSSFWorkbook wb = new HSSFWorkbook();
            wb = new ExcelCreater(wb, "Реестр Z-отчетов", table).createZReportsSheet();
            try (FileOutputStream out = new FileOutputStream(file)) {
              wb.write(out);
            } catch (IOException ex) {
              RPCMessage.showMessageDialog(this, "Не удалось сохранить реестр Z-отчетов: " + ex.getMessage());
            }
          }
          if (Arrays.asList(exts).contains("txt")) {
            try (FileWriter out = new FileWriter(file, false)) {
              String reg = tableModel.getFilteredZReports1C();
              out.write(reg);
            } catch (IOException ex) {
              RPCMessage.showMessageDialog(this, "Не удалось сохранить реестр Z-отчетов: " + ex.getMessage());
            }
          }
        }
      }
    } else {
      try {
        File file = File.createTempFile("Реестр Z-отчетов от " + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + " ", ".xls");
        file.deleteOnExit();
        HSSFWorkbook wb = new HSSFWorkbook();
        wb = new ExcelCreater(wb, "Реестр Z-отчетов", table).createZReportsSheet();
        try (FileOutputStream out = new FileOutputStream(file)) {
          wb.write(out);
        }
        Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
      } catch (IOException ex) {
        RPCMessage.showMessageDialog(this, "Не удалось экспортировать реестр Z-отчетов: " + ex.getMessage());
      }
    }
  }
}

class ZReportsFrame_popupMenu_actionAdapter implements ActionListener {
  private ZReportsFrame adaptee;
  ZReportsFrame_popupMenu_actionAdapter(ZReportsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.popupMenu_actionPerformed(e);
  }
}

class ZReportsFrame_table_popupAdapter extends MouseAdapter {
  private ZReportsFrame adaptee;
  ZReportsFrame_table_popupAdapter(ZReportsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mousePressed(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.table_maybeShowPopup(e);
  }
}

class ZReportsFrame_table_keyAdapter extends KeyAdapter {
  private ZReportsFrame adaptee;
  ZReportsFrame_table_keyAdapter(ZReportsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void keyPressed(KeyEvent e) {
    adaptee.table_keyPressed(e);
  }
}
