import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.jdesktop.swingx.JXSummaryTable;

class FinanceHistoryFrame extends JFrame {
  private final ArrayList<FinanceProfile[]> financeHistory;
  private FinanceTableModel tableModel;
  private JPanel contentPane;
  private JXSummaryTable table;
  private JScrollPane scrollPane;

  public FinanceHistoryFrame(ArrayList<FinanceProfile[]> financeHistory) {
    this.financeHistory = financeHistory;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() {
    contentPane = (JPanel) this.getContentPane();
    this.setIconImages(KNC_Terminal.icons);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setTitle("Финансовая история");
    this.setBounds(10, 25, Toolkit.getDefaultToolkit().getScreenSize().width-20, Toolkit.getDefaultToolkit().getScreenSize().height-60);
    ArrayList<FinanceProfile> financeList = new ArrayList<>();
    for (FinanceProfile[] finances : financeHistory) {
      Collections.addAll(financeList, finances);
    }
    tableModel = new FinanceTableModel();
    tableModel.putFinances(financeList, true);
    table = new JXSummaryTable(tableModel);
    table.setRowHeight(33);
    table.setColumnControlVisible(true);
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
    for (int i=gatewaysCount-1; i>=0; i--) {
      table.getColumnExt(4+i).setVisible(false);
    }
    table.setHorizontalScrollEnabled(true);
    scrollPane = new JScrollPane(table);
    contentPane.add(scrollPane);
    table.packAll();
  }
}
