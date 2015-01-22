import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.decorator.SortOrder;

class ReportFrame extends JFrame implements ActionListener {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JPanel reportPanel;
  private JLabel typeLabel;
  private JComboBox<ProductID<String, Integer>> typeComboBox;
  private JComboBox<ProductID<String, String>> kioskComboBox;
  private JComboBox<ProductID<String, String>> gatewayComboBox;
  private JTextField filterTextField;
  private JButton exportButton;
  private JLabel startLabel;
  private JXDatePicker startDatePicker;
  private JLabel endLabel;
  private JXDatePicker endDatePicker;
  private JComboBox<ProductID<String, ?>> productComboBox;
  private JComboBox<ProductID<String, String>> statusComboBox;
  private JButton requestButton;
  private JButton saveButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  private static final ImageIcon exportButtonIcon = new ImageIcon("images/export.png");
  private static final ImageIcon saveButtonIcon = new ImageIcon("images/save.png");
  private final Vector<ProductID<String, Integer>> REPORT_TYPES;
  private final String MAIN_TITLE = "Отчет";
  private final String[] conditions = new String[9];
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
  private Date date = new Date();
  private String filterCurrentName = null;
  private String filterTitle = "";
  private boolean reportDataFlag = false;
  private Object[] reportData = null;
  private Report1TableModel report1TableModel;
  private Report2TableModel report2TableModel;
  private Report3TableModel report3TableModel;
  private Report45TableModel report45TableModel;
  private FilterMenu filterMenu1;
  private FilterMenu filterMenu2;
  private JXSummaryTable table;
  private JScrollPane scrollPane = null;
  private JPopupMenu popupMenu1 = new JPopupMenu();
  private JPopupMenu popupMenu2 = new JPopupMenu();

  public ReportFrame() {
    REPORT_TYPES = new Vector<>();
    Collections.addAll(REPORT_TYPES,
            new ProductID<>("в разрезе киосков", 0),
            new ProductID<>("в разрезе услуг", 1),
            new ProductID<>("в разрезе дней", 2),
            new ProductID<>("выручка в разрезе киосков и дней", 3),
            new ProductID<>("доход в разрезе киосков и дней", 4)
    );

    Vector<ProductID<String, String>> statuses = new Vector<>(4);
    Collections.addAll(statuses,
            new ProductID<>("любой статус", "e"),
            new ProductID<>("Проведен", "1"),
            new ProductID<>("Отклонен", "-1"),
            new ProductID<>("Действующий", "0")
    );

    try {
      initComponents(statuses);
      this.setIconImages(KNC_Terminal.icons);
      this.setTitle(MAIN_TITLE);
      this.setBounds(10, 25, Toolkit.getDefaultToolkit().getScreenSize().width - 20, Toolkit.getDefaultToolkit().getScreenSize().height - 60);
      if (KNC_Terminal.defaultTableMaximized > 0) this.setExtendedState(Frame.MAXIMIZED_BOTH);
      setPanelEnabled(false);
      filterMenu1 = KNC_Terminal.filterMenuFactory.getMenu(KNC_Terminal.user.filters, false);
      filterMenu1.addActionListener(new ReportFrame_popupMenu1_actionAdapter(this));
      popupMenu1.add(filterMenu1.getSubMenu());
      filterMenu2 = KNC_Terminal.filterMenuFactory.getMenu();
      filterMenu2.addActionListener(new ReportFrame_popupMenu2_actionAdapter(this));
      filterMenu2.addToPopupMenu(popupMenu2);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void updateTitle() {
    String title = MAIN_TITLE;
    title += filterTitle;
    this.setTitle(title);
  }

  private void initComponents(Vector<ProductID<String, String>> statuses) {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    reportPanel = new JPanel();
    JPanel panel1 = new JPanel();
    typeLabel = new JLabel();
    typeComboBox = new JComboBox<>();
    kioskComboBox = new JComboBox<>();
    gatewayComboBox = new JComboBox<>();
    filterTextField = new JTextField();
    exportButton = new JButton();
    JPanel panel2 = new JPanel();
    startLabel = new JLabel();
    startDatePicker = new JXDatePicker();
    endLabel = new JLabel();
    endDatePicker = new JXDatePicker();
    productComboBox = new JComboBox<>();
    statusComboBox = new JComboBox<>();
    requestButton = new JButton();
    saveButton = new JButton();

    //======== this ========
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== reportPanel ========
    {
      reportPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      reportPanel.setLayout(new GridBagLayout());
      ((GridBagLayout)reportPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 160, 0, 0};
      ((GridBagLayout)reportPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
      ((GridBagLayout)reportPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
      ((GridBagLayout)reportPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

      //======== panel1 ========
      {
        panel1.setLayout(new GridBagLayout());
        ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0, 0};
        ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
        ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {1.0};

        //---- typeLabel ----
        typeLabel.setText("\u0422\u0438\u043f \u043e\u0442\u0447\u0435\u0442\u0430:");
        typeLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        panel1.add(typeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 5), 0, 0));

        //---- typeComboBox ----
        typeComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
        typeComboBox.setBorder(null);
        typeComboBox.setModel(new DefaultComboBoxModel<>(REPORT_TYPES));
        panel1.add(typeComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));
      }
      reportPanel.add(panel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 10, 10), 0, 0));

      //---- kioskComboBox ----
      kioskComboBox.setMaximumRowCount(16);
      kioskComboBox.setBorder(null);
      kioskComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      kioskComboBox.setPreferredSize(new Dimension(240, 25));
      kioskComboBox.addItem(new ProductID<>("Получение...", "e"));
      reportPanel.add(kioskComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 10, 10), 0, 0));

      //---- gatewayComboBox ----
      gatewayComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      gatewayComboBox.setBorder(null);
      gatewayComboBox.addItem(new ProductID<>("Получение...", "e"));
      reportPanel.add(gatewayComboBox, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 10, 10), 0, 0));

      //---- filterTextField ----
      filterTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
      filterTextField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
          filterTextFieldFocusGained();
        }
        @Override
        public void focusLost(FocusEvent e) {
          filterTextFieldFocusLost();
        }
      });
      filterTextField.setText(KNC_Terminal.queryFilter);
      reportPanel.add(filterTextField, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 10, 10), 0, 0));

      //---- exportButton ----
      exportButton.setEnabled(false);
      exportButton.setPreferredSize(new Dimension(25, 25));
      exportButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          exportButtonActionPerformed();
        }
      });
      exportButton.setIcon(exportButtonIcon);
      reportPanel.add(exportButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
        new Insets(0, 0, 10, 0), 0, 0));

      //======== panel2 ========
      {
        panel2.setLayout(new GridBagLayout());
        ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 115, 0, 115, 0};
        ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 0.0, 1.0E-4};
        ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {1.0};

        //---- startLabel ----
        startLabel.setText("\u0441");
        startLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        panel2.add(startLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
          GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
          new Insets(0, 0, 0, 5), 0, 0));

        //---- startDatePicker ----
        startDatePicker.addPropertyChangeListener(new PropertyChangeListener() {
          @Override
          public void propertyChange(PropertyChangeEvent e) {
            startDatePickerPropertyChange();
          }
        });
        startDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
        panel2.add(startDatePicker, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));

        //---- endLabel ----
        endLabel.setText("\u043f\u043e");
        endLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        panel2.add(endLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
          GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
          new Insets(0, 10, 0, 5), 0, 0));

        //---- endDatePicker ----
        endDatePicker.addPropertyChangeListener(new PropertyChangeListener() {
          @Override
          public void propertyChange(PropertyChangeEvent e) {
            endDatePickerPropertyChange();
          }
        });
        endDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
        panel2.add(endDatePicker, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));
      }
      reportPanel.add(panel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 10), 0, 0));

      //---- productComboBox ----
      productComboBox.setMaximumRowCount(16);
      productComboBox.setBorder(null);
      productComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      productComboBox.addItem(new ProductID<>("Получение...", "e"));
      reportPanel.add(productComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 10), 0, 0));

      //---- statusComboBox ----
      statusComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      statusComboBox.setBorder(null);
      statusComboBox.setModel(new DefaultComboBoxModel<>(statuses));
      reportPanel.add(statusComboBox, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 10), 0, 0));

      //---- requestButton ----
      requestButton.setText("\u041e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c \u0437\u0430\u043f\u0440\u043e\u0441");
      requestButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          requestButtonActionPerformed();
        }
      });
      reportPanel.add(requestButton, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 10), 0, 0));

      //---- saveButton ----
      saveButton.setEnabled(false);
      saveButton.setPreferredSize(new Dimension(25, 25));
      saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          saveButtonActionPerformed();
        }
      });
      saveButton.setIcon(saveButtonIcon);
      reportPanel.add(saveButton, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
        new Insets(0, 0, 0, 0), 0, 0));
    }
    contentPane.add(reportPanel, BorderLayout.NORTH);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  private void setPanelEnabled(boolean enabled) {
    typeLabel.setEnabled(enabled);
    typeComboBox.setEnabled(enabled);
    startLabel.setEnabled(enabled);
    endLabel.setEnabled(enabled);
    startDatePicker.setEnabled(enabled);
    endDatePicker.setEnabled(enabled);
    kioskComboBox.setEnabled(enabled);
    productComboBox.setEnabled(enabled);
    gatewayComboBox.setEnabled(enabled);
    statusComboBox.setEnabled(enabled);
    filterTextField.setEnabled(enabled);
    requestButton.setEnabled(enabled);
    requestButton.setFocusable(enabled);
  }

  public void setVisible(boolean b) {
    if (b) {
      if (!reportDataFlag) new At_net("GET_REPORTDATA", KNC_Terminal.user, this).start();
      else new At_net("CHECK_REPORT", KNC_Terminal.user, this).start();
    }
    super.setVisible(b);
  }

  private void setDate(Date date) {
    startDatePicker.setDate(date);
    endDatePicker.setDate(date);
    this.date = date;
  }

  private void setKiosks(HashMap<String, String> kiosks) {
    kioskComboBox.removeAllItems();
    kioskComboBox.addItem(new ProductID<>("все киоски", "e"));
    Set<String> keySet = kiosks.keySet();
    String[] kioskList = new String[keySet.size()];
    kioskList = keySet.toArray(kioskList);
    Arrays.sort(kioskList);
    for (String kiosk : kioskList) {
      String kioskAddress = kiosks.get(kiosk).replaceAll("(?i)<html>", "").replaceAll("(?i)</html>", "");
      kioskAddress = "<html><table cellpadding=0 cellspacing=0><tr><td>" + kiosk + "&nbsp;&nbsp;<td>" + kioskAddress + "</table></html>";
      kioskComboBox.addItem(new ProductID<>(kioskAddress, kiosk));
    }
  }

  private void setProducts(HashMap<Integer, String> products) {
    productComboBox.removeAllItems();
    productComboBox.addItem(new ProductID<>("все услуги", "e"));
    ArrayList<ProductID<String, Integer>> productList = new ArrayList<>(products.size());
    for (Integer id : products.keySet()) {
      productList.add(new ProductID<>(products.get(id) + " [" + KNC_Terminal.getGatewayName(KNC_Terminal.getGatewayByID(id)) + "]", id));
    }
    Collections.sort(productList);
    for (ProductID<String, Integer> productID : productList) {
      productComboBox.addItem(productID);
    }
  }

  private void setGateways(HashSet<String> gateways) {
    gatewayComboBox.removeAllItems();
    gatewayComboBox.addItem(new ProductID<>("все плат. системы", "e"));
    ArrayList<String> gatewayList = new ArrayList<>(gateways);
    KNC_Terminal.sortGateways(gatewayList);
    for (String gateway : gatewayList) {
      String gatewayName = KNC_Terminal.getGatewayName(gateway);
      gatewayComboBox.addItem(new ProductID<>(gatewayName, gateway));
    }
  }

  void update(Object[] report) {
    reportData = (Object[]) report[0];
    if (!isVisible()) setVisible(true);
    setState(Frame.NORMAL);
    Integer type = (Integer) reportData[1];
    for (ProductID<String, Integer> reportType : REPORT_TYPES) {
      if (reportType.id.equals(type)) {
        typeComboBox.setSelectedItem(reportType);
        conditions[0] = reportType.name;
        break;
      }
    }
    startDatePicker.setDate((Date) reportData[2]);
    endDatePicker.setDate(new Date(((Date) reportData[3]).getTime() - 86399999));
    conditions[1] = dateFormat.format(reportData[2]);
    conditions[2] = dateFormat.format(reportData[3]);
    for (int i = 0; i < kioskComboBox.getItemCount(); i++) {
      if (kioskComboBox.getItemAt(i).id.equals(reportData[4])) {
        kioskComboBox.setSelectedIndex(i);
        String kioskAddress = kioskComboBox.getItemAt(i).name;
        kioskAddress = kioskAddress.replaceAll("(?i)<br>", " ");
        kioskAddress = kioskAddress.replaceAll("<.+?>", "");
        kioskAddress = kioskAddress.replaceAll("&.+?;", "");
        conditions[3] = kioskAddress;
        break;
      }
    }
    for (int i = 0; i < productComboBox.getItemCount(); i++) {
      if (productComboBox.getItemAt(i).id.equals(reportData[5])) {
        productComboBox.setSelectedIndex(i);
        conditions[4] = productComboBox.getItemAt(i).name;
        break;
      }
    }
    for (int i = 0; i < gatewayComboBox.getItemCount(); i++) {
      if (gatewayComboBox.getItemAt(i).id.equals(reportData[6])) {
        gatewayComboBox.setSelectedIndex(i);
        conditions[5] = gatewayComboBox.getItemAt(i).name;
        break;
      }
    }
    for (int i = 0; i < statusComboBox.getItemCount(); i++) {
      if (statusComboBox.getItemAt(i).id.equals(reportData[7])) {
        statusComboBox.setSelectedIndex(i);
        conditions[6] = statusComboBox.getItemAt(i).name;
        break;
      }
    }
    if (reportData[8] != null) {
      filterTextField.setText(reportData[8].toString());
      conditions[7] = reportData[8].toString();
    } else conditions[7] = "нет";
    if (report[1].equals("e")) {
      RPCMessage.showMessageDialog(this, "Ошибка обработки запроса, проверьте дополнительный фильтр");
    } else {//данные получены
      Container contentPane = this.getContentPane();
      if (scrollPane != null) contentPane.remove(scrollPane);
      if (type == 0) {
        report1TableModel = new Report1TableModel();
        report1TableModel.putFinances(reportData, (ArrayList<FinanceProfile>) report[1]);
        report1TableModel.addTableModelListener(new ReportFrame_tableModel_tableModelAdapter(this));
        table = new JXSummaryTable(report1TableModel);
        table.setRowHeight(33);
        table.setColumnControlVisible(true);
        table.getColumnModel().getColumn(0).setMinWidth(20);
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        table.getColumnModel().getColumn(1).setMinWidth(54);
        table.getColumnModel().getColumn(1).setMaxWidth(58);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setMinWidth(58); //Выручка
        int gatewaysCount = report1TableModel.getGatewaysCount();
        for (int i = 0; i < gatewaysCount; i++) {
          table.getColumnModel().getColumn(4 + i).setMinWidth(58); //Выручка шлюза
        }
        table.getColumnModel().getColumn(4 + gatewaysCount).setMinWidth(65); //Доход
        table.getColumnModel().getColumn(5 + gatewaysCount).setMinWidth(48); //Рентабельность
        table.getColumnModel().getColumn(6 + gatewaysCount).setMinWidth(44); //Операций
        table.getColumnModel().getColumn(7 + gatewaysCount).setMinWidth(44); //Средний платеж
        table.getColumnModel().getColumn(8 + gatewaysCount).setMinWidth(48); //Доля в обороте
        table.getColumnModel().getColumn(9 + gatewaysCount).setMinWidth(48); //Доля в доходе
        table.setSortOrder(3, SortOrder.DESCENDING);
        table.setHorizontalScrollEnabled(true);
        table.addMouseListener(new ReportFrame_table1_popupAdapter(this));
        boolean filters = (reportData[4]).equals("e") && (reportData[8] == null || !((String) reportData[8]).contains("device"));//если по всем киоскам и фильтр не использует "device"
        filterMenu1.setFiltersEnabled(filters);
      }
      if (type == 1) {
        report2TableModel = new Report2TableModel();
        report2TableModel.putFinances(reportData, (ArrayList<FinanceProfile>) report[1]);
        report2TableModel.addTableModelListener(new ReportFrame_tableModel_tableModelAdapter(this));
        table = new JXSummaryTable(report2TableModel);
        table.setRowHeight(33);
        table.setColumnControlVisible(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(58); //Выручка
        table.getColumnModel().getColumn(2).setMinWidth(65); //Доход
        table.getColumnModel().getColumn(3).setMinWidth(48); //Рентабельность
        table.getColumnModel().getColumn(4).setMinWidth(44); //Операций
        table.getColumnModel().getColumn(5).setMinWidth(44); //Средний платеж
        table.getColumnModel().getColumn(6).setMinWidth(48); //Доля в обороте
        table.getColumnModel().getColumn(7).setMinWidth(48); //Доля в доходе
        table.setSortOrder(1, SortOrder.DESCENDING);
        table.setHorizontalScrollEnabled(true);
        table.addMouseListener(new ReportFrame_table2_popupAdapter(this));
      }
      if (type == 2) {
        report3TableModel = new Report3TableModel();
        report3TableModel.putFinances(reportData, (ArrayList<FinanceProfile>) report[1]);
        report3TableModel.addTableModelListener(new ReportFrame_tableModel_tableModelAdapter(this));
        table = new JXSummaryTable(report3TableModel);
        table.setRowHeight(33);
        table.setColumnControlVisible(true);
        table.getColumnModel().getColumn(0).setMinWidth(90);
        table.getColumnModel().getColumn(1).setMinWidth(58); //Выручка
        int gatewaysCount = report3TableModel.getGatewaysCount();
        for (int i = 0; i < gatewaysCount; i++) {
          table.getColumnModel().getColumn(2 + i).setMinWidth(58); //Выручка шлюза
        }
        table.getColumnModel().getColumn(2 + gatewaysCount).setMinWidth(65); //Доход
        table.getColumnModel().getColumn(3 + gatewaysCount).setMinWidth(48); //Рентабельность
        table.getColumnModel().getColumn(4 + gatewaysCount).setMinWidth(44); //Операций
        table.getColumnModel().getColumn(5 + gatewaysCount).setMinWidth(44); //Средний платеж
        table.getColumnModel().getColumn(6 + gatewaysCount).setMinWidth(48); //Доля в обороте
        table.getColumnModel().getColumn(7 + gatewaysCount).setMinWidth(48); //Доля в доходе
        table.setSortOrder(0, SortOrder.ASCENDING);
        table.setHorizontalScrollEnabled(true);
        table.addMouseListener(new ReportFrame_table2_popupAdapter(this));
      }
      if (type == 3 || type == 4) {
        report45TableModel = new Report45TableModel();
        report45TableModel.putFinances(reportData, (ArrayList<FinanceProfile>) report[1], type);
        report45TableModel.addTableModelListener(new ReportFrame_tableModel_tableModelAdapter(this));
        table = new JXSummaryTable(report45TableModel);
        table.setRowHeight(33);
        table.setColumnControlVisible(true);
        table.getColumnModel().getColumn(0).setMinWidth(20);
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        table.getColumnModel().getColumn(1).setMinWidth(54);
        table.getColumnModel().getColumn(1).setMaxWidth(58);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        for (int j = 3; j < table.getColumnModel().getColumnCount() - 1; j++) {
          table.getColumnModel().getColumn(j).setMinWidth(70);
        }
        table.setSortOrder(1, SortOrder.ASCENDING);
        table.setHorizontalScrollEnabled(true);
        table.addMouseListener(new ReportFrame_table1_popupAdapter(this));
        boolean filters = (reportData[4]).equals("e") && (reportData[8] == null || !((String) reportData[8]).contains("device"));//если по всем киоскам и фильтр не использует "device"
        filterMenu1.setFiltersEnabled(filters);
      }
      scrollPane = new JScrollPane(table);
      contentPane.add(scrollPane, java.awt.BorderLayout.CENTER);
      contentPane.validate();
      table.packAll();
    }
    requestButton.setText("Отправить запрос");
    setPanelEnabled(true);
    exportButton.setEnabled(true);
    saveButton.setEnabled(true);
  }

  private void filterTextFieldFocusGained() {
    if (filterTextField.getText().equals(KNC_Terminal.queryFilter)) filterTextField.setText("");
  }

  private void filterTextFieldFocusLost() {
    if (filterTextField.getText().equals("")) filterTextField.setText(KNC_Terminal.queryFilter);
  }

  private void startDatePickerPropertyChange() {
    if (startDatePicker.getDate() == null) return;
    if (startDatePicker.getDate().after(date)) startDatePicker.setDate(date);
  }

  private void endDatePickerPropertyChange() {
    if (endDatePicker.getDate() == null) return;
    if (endDatePicker.getDate().after(date)) endDatePicker.setDate(date);
  }

  private void requestButtonActionPerformed() {
    setPanelEnabled(false);
    exportButton.setEnabled(false);
    saveButton.setEnabled(false);
    Object[] objc = new Object[9];
    objc[0] = "GET_REPORT";
    objc[1] = ((ProductID<?, ?>) typeComboBox.getSelectedItem()).id;
    Date date1 = startDatePicker.getDate();
    Date date2 = KNC_Terminal.endOfDay(endDatePicker.getDate());
    if (date1.after(date2)) {
      date1 = endDatePicker.getDate();
      date2 = KNC_Terminal.endOfDay(startDatePicker.getDate());
    }
    objc[2] = date1;
    objc[3] = date2;
    objc[4] = kioskComboBox.getItemAt(kioskComboBox.getSelectedIndex()).id;
    objc[5] = productComboBox.getItemAt(productComboBox.getSelectedIndex()).id;
    objc[6] = gatewayComboBox.getItemAt(gatewayComboBox.getSelectedIndex()).id;
    objc[7] = statusComboBox.getItemAt(statusComboBox.getSelectedIndex()).id;
    String filter = filterTextField.getText();
    if (filter.equals(KNC_Terminal.queryFilter) || filter.equals("")) objc[8] = null;
    else objc[8] = filter;
    this.setTitle(MAIN_TITLE);
    filterMenu1.resetStatus();
    filterMenu2.resetStatus();
    filterCurrentName = null;
    new At_net("GET_REPORT", new Request(KNC_Terminal.user, objc), this, 100000).start();
  }

  private void exportButtonActionPerformed() {
    exportTable(false);
  }

  private void saveButtonActionPerformed() {
    exportTable(true);
  }

  public void tableModel_tableChanged(TableModelEvent e) {
    if (e.getColumn() == TableModelEvent.ALL_COLUMNS) SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        table.packAll();
      }
    });
  }

  public void table1_maybeShowPopup(MouseEvent e) {//для таблиц со списком киосков
    int activeRow;
    String activeKioskNum;
    activeRow = table.rowAtPoint(new Point(e.getX(), e.getY()));
    if (activeRow == -1) {
      table.removeRowSelectionInterval(0, table.getRowCount() - 1);
    } else {
      if (e.isPopupTrigger()) {
        if (table.convertRowIndexToModel(activeRow) == table.getModel().getRowCount() - 1) return;
        activeKioskNum = table.getModel().getValueAt(table.convertRowIndexToModel(activeRow), 1).toString();
        if (activeKioskNum.equals("")) return;
        if (!table.isRowSelected(activeRow)) table.setRowSelectionInterval(activeRow, activeRow);
        popupMenu1.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  public void table2_maybeShowPopup(MouseEvent e) {//для таблиц с любым содержимым
    int activeRow;
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

  public void popupMenu1_actionPerformed(ActionEvent e) {//для таблиц со списком киосков
    if (table == null) return;//если сработало меню от реконструкции, а таблицы не существует
    if (e.getActionCommand().equals(FilterMenu.RECONST_FILTERS)) {
      if (filterCurrentName != null) {
        for (int i = 0; i < filterMenu1.getSubMenu().getItemCount(); i++) {
          JMenuItem filterMenuItem = filterMenu1.getSubMenu().getItem(i);
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
    int j = 0;
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
      rows = new int[rows.length - 1];
      System.arraycopy(buffer, 0, rows, 0, rows.length);
    }
    String[] kioskNumbers = new String[rows.length];
    for (int i = 0; i < rows.length; i++) {
      kioskNumbers[i] = table.getModel().getValueAt(table.convertRowIndexToModel(rows[i]), 1).toString();
    }
    if (e.getActionCommand().equals(FilterMenu.CANCEL_FILTER)) {
      ((FiltrableTableModel) table.getModel()).removeFilter();
      filterCurrentName = null;
      filterTitle = FilterMenu.getTitle(null);
      updateTitle();
      conditions[8] = null;
      filterMenu1.updateAfterCommand(e.getActionCommand());
    }
    if (e.getActionCommand().equals(FilterMenu.INCLUDE)) {
      ((FiltrableTableModel) table.getModel()).setFilter(kioskNumbers);
      filterCurrentName = null;
      filterTitle = FilterMenu.getTitle("");
      updateTitle();
      conditions[8] = FilterMenu.getNoName();
      filterMenu1.updateAfterCommand(e.getActionCommand());
    }
    if (e.getActionCommand().equals(FilterMenu.EXCLUDE)) {
      try {
        ((FiltrableTableModel) table.getModel()).excludeFilter(kioskNumbers);
        filterCurrentName = null;
        filterTitle = FilterMenu.getTitle("");
        updateTitle();
        conditions[8] = FilterMenu.getNoName();
        filterMenu1.updateAfterCommand(e.getActionCommand());
      } catch (FiltrableTableModelException ex) {
        RPCMessage.showMessageDialog(this, "Нельзя исключить все киоски");
      }
    }
    if (e.getActionCommand().startsWith(FilterMenu.SET_FILTERA) || e.getActionCommand().startsWith(FilterMenu.SET_FILTER)) {
      filterCurrentName = FilterMenu.getFilterNameFromCommand(e.getActionCommand());
      if (e.getActionCommand().startsWith(FilterMenu.SET_FILTERA)) ((FiltrableTableModel)table.getModel()).setAutoFilter(KNC_Terminal.frame.getAutoFilters().get(filterCurrentName));
      if (e.getActionCommand().startsWith(FilterMenu.SET_FILTER)) ((FiltrableTableModel)table.getModel()).setFilter(KNC_Terminal.user.filters.get(filterCurrentName));
      filterTitle = FilterMenu.getTitle(filterCurrentName);
      updateTitle();
      conditions[8] = filterCurrentName;
      filterMenu1.updateAfterCommand(e.getActionCommand());
    }
  }

  public void popupMenu2_actionPerformed(ActionEvent e) {//для таблиц с любым содержимым
    int[] rows = table.getSelectedRows();
    int[] modelRows = new int[rows.length];
    for (int i = 0; i < rows.length; i++) {
      modelRows[i] = table.convertRowIndexToModel(rows[i]);
    }
    if (e.getActionCommand().equals(FilterMenu.CANCEL_FILTER)) {
      ((FiltrableCustomTableModel) table.getModel()).revertRows();
      filterTitle = FilterMenu.getTitle(null);
      updateTitle();
      conditions[8] = null;
      filterMenu2.updateAfterCommand(e.getActionCommand());
    }
    if (e.getActionCommand().equals(FilterMenu.INCLUDE)) {
      ((FiltrableCustomTableModel) table.getModel()).leaveRows(modelRows);
      filterTitle = FilterMenu.getTitle("");
      updateTitle();
      conditions[8] = FilterMenu.getNoName();
      filterMenu2.updateAfterCommand(e.getActionCommand());
    }
    if (e.getActionCommand().equals(FilterMenu.EXCLUDE)) {
      try {
        ((FiltrableCustomTableModel) table.getModel()).deleteRows(modelRows);
        filterTitle = FilterMenu.getTitle("");
        updateTitle();
        conditions[8] = FilterMenu.getNoName();
        filterMenu2.updateAfterCommand(e.getActionCommand());
      } catch (FiltrableTableModelException ex) {
        RPCMessage.showMessageDialog(this, "Нельзя исключить все строки");
      }
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_GET_REPORTDATA")) {
      final Object[] result = (Object[]) ((At_net) e.getSource()).getResult();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          setDate((Date) result[0]);
          setKiosks((HashMap<String, String>) result[1]);
          setProducts((HashMap<Integer, String>) result[2]);
          setGateways((HashSet<String>) result[3]);
          KNC_Terminal.frame.updateAutoFilters((ArrayList<String>) result[4]);
          Dimension preferredSize = reportPanel.getPreferredSize();
          setMinimumSize(new Dimension(preferredSize.width + 10, preferredSize.height + 10));
          reportDataFlag = true;
          setPanelEnabled(true);
          new At_net("CHECK_REPORT", KNC_Terminal.user, ReportFrame.this).start();
        }
      });
    }
    if (e.getActionCommand().equals("fail_GET_REPORTDATA")) {
      setPanelEnabled(false);
      RPCMessage.showMessageDialog(this, "Ошибка соединения с центральным сервером");
    }
    if (e.getActionCommand().equals("ret_GET_REPORT")) {
      if (e.getID() == 3) SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          requestButton.setText("Запрос отправлен");
        }
      });
    }
    if (e.getActionCommand().equals("finished_GET_REPORT")) {
      final Object[] result = (Object[]) ((At_net) e.getSource()).getResult();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          setTitle(MAIN_TITLE);
          update(result);
        }
      });
    }
    if (e.getActionCommand().equals("fail_GET_REPORT")) {
      if (((At_net) e.getSource()).getStatus()) {
        //отправили запрос но не дождались ответа, опрос сервера
        KNC_Terminal.dataUpdater.setWaitReport(true);
        if (KNC_Terminal.dataUpdater.getState() == Thread.State.NEW) KNC_Terminal.dataUpdater.start();
      } else {
        setPanelEnabled(true);
        RPCMessage.showMessageDialog(this, "Ошибка соединения с центральным сервером");
      }
    }
    if (e.getActionCommand().equals("finished_CHECK_REPORT")) {
      final Object[] result = (Object[]) ((At_net) e.getSource()).getResult();
      if (result != null) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            update(result);
          }
        });
      }
    }
  }

  private void exportTable(boolean toFile) {
    if (conditions[8] == null) conditions[8] = "нет";
    String range;
    String from = dateFormat.format((Date) reportData[2]);
    String to = dateFormat.format((Date) reportData[3]);
    if (from.equals(to)) range = from;
    else range = from + "-" + to;
    if (toFile) {
      KNC_Terminal.saveRegistryFileDialog.setDialogTitle("Сохранить отчет");
      KNC_Terminal.saveRegistryFileDialog.setSelectedFile(new File("Отчет " + typeComboBox.getItemAt((Integer) reportData[1]) + " " + range));
      if (KNC_Terminal.saveRegistryFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = KNC_Terminal.saveRegistryFileDialog.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) file = new File(file.getAbsolutePath() + ".xls");
        if (!file.exists() || RPCDialog.showMessageDialog(this, "Перезаписать файл \"" + file.getName() + "\"?") == RPCDialog.Result.OK) {
          HSSFWorkbook wb = new HSSFWorkbook();
          try {
            if ((Integer) reportData[1] == 0)
              wb = new ExcelCreater(wb, range, table).createReport0Sheet(conditions, ((Report1TableModel) table.getModel()).getGatewaysCount());
            if ((Integer) reportData[1] == 1)
              wb = new ExcelCreater(wb, range, table).createReport12Sheet(conditions, 0);
            if ((Integer) reportData[1] == 2)
              wb = new ExcelCreater(wb, range, table).createReport12Sheet(conditions, ((Report3TableModel) table.getModel()).getGatewaysCount());
            if ((Integer) reportData[1] == 3)
              wb = new ExcelCreater(wb, range, table).createReport34Sheet(conditions, (byte) 3);
            if ((Integer) reportData[1] == 4)
              wb = new ExcelCreater(wb, range, table).createReport34Sheet(conditions, (byte) 4);
            if ((Integer) reportData[1] > 4) return;
          } catch (ExcelCreaterException ex) {
            RPCMessage.showMessageDialog(this, "Не удалось создать отчет: " + ex.getMessage());
          }
          try (FileOutputStream out = new FileOutputStream(file)) {
            wb.write(out);
          } catch (IOException ex) {
            RPCMessage.showMessageDialog(this, "Не удалось сохранить отчет: " + ex.getMessage());
          }
        }
      }
    } else {
      try {
        File file = File.createTempFile("Отчет " + typeComboBox.getItemAt((Integer) reportData[1]) + " " + range + " ", ".xls");
        file.deleteOnExit();
        HSSFWorkbook wb = new HSSFWorkbook();
        if ((Integer) reportData[1] == 0)
          wb = new ExcelCreater(wb, range, table).createReport0Sheet(conditions, ((Report1TableModel) table.getModel()).getGatewaysCount());
        if ((Integer) reportData[1] == 1)
          wb = new ExcelCreater(wb, range, table).createReport12Sheet(conditions, 0);
        if ((Integer) reportData[1] == 2)
          wb = new ExcelCreater(wb, range, table).createReport12Sheet(conditions, ((Report3TableModel) table.getModel()).getGatewaysCount());
        if ((Integer) reportData[1] == 3)
          wb = new ExcelCreater(wb, range, table).createReport34Sheet(conditions, (byte) 3);
        if ((Integer) reportData[1] == 4)
          wb = new ExcelCreater(wb, range, table).createReport34Sheet(conditions, (byte) 4);
        if ((Integer) reportData[1] > 4) return;
        try (FileOutputStream out = new FileOutputStream(file)) {
          wb.write(out);
        }
        Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
      } catch (ExcelCreaterException ex) {
        RPCMessage.showMessageDialog(this, "Не удалось создать отчет: " + ex.getMessage());
      } catch (IOException ex) {
        RPCMessage.showMessageDialog(this, "Не удалось экспортировать отчет: " + ex.getMessage());
      }
    }
  }
}

class ReportFrame_tableModel_tableModelAdapter implements TableModelListener {
  private ReportFrame adaptee;
  ReportFrame_tableModel_tableModelAdapter(ReportFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void tableChanged(TableModelEvent e) {
    adaptee.tableModel_tableChanged(e);
  }
}

class ReportFrame_popupMenu1_actionAdapter implements ActionListener {//для таблиц со списком киосков
  private ReportFrame adaptee;
  ReportFrame_popupMenu1_actionAdapter(ReportFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.popupMenu1_actionPerformed(e);
  }
}

class ReportFrame_popupMenu2_actionAdapter implements ActionListener {//для таблиц с любым содержимым
  private ReportFrame adaptee;
  ReportFrame_popupMenu2_actionAdapter(ReportFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.popupMenu2_actionPerformed(e);
  }
}

class ReportFrame_table1_popupAdapter extends MouseAdapter {//для таблиц со списком киосков
  private ReportFrame adaptee;
  ReportFrame_table1_popupAdapter(ReportFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mousePressed(MouseEvent e) {
    adaptee.table1_maybeShowPopup(e);
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.table1_maybeShowPopup(e);
  }
}

class ReportFrame_table2_popupAdapter extends MouseAdapter {//для таблиц с любым содержимым
  private ReportFrame adaptee;
  ReportFrame_table2_popupAdapter(ReportFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mousePressed(MouseEvent e) {
    adaptee.table2_maybeShowPopup(e);
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.table2_maybeShowPopup(e);
  }
}
