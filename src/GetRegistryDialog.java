import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXDatePicker;

class GetRegistryDialog extends JDialog implements ActionListener {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JCheckBox intervalCheckBox;
  private JCheckBox accountCheckBox;
  private JLabel startLabel;
  private JXDatePicker startDatePicker;
  private JLabel endLabel;
  private JXDatePicker endDatePicker;
  private JTextField accountTextField;
  private JComboBox<ProductID<String, String>> productComboBox;
  private JComboBox<ProductID<String, String>> statusComboBox;
  private JComboBox<ProductID<String, String>> gatewayComboBox;
  private JTextField filterTextField;
  private JCheckBox allCheckBox;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  private final Window owner;
  private final KioskProfile[] kiosks;
  private final KNCTableModel tableModel;
  private WaitingDialog waitingDialog;

  public GetRegistryDialog(Window owner, Dialog.ModalityType modalityType, KioskProfile[] kiosks, KNCTableModel tableModel) {
    super(owner, modalityType);
    this.owner = owner;
    this.kiosks = kiosks;
    this.tableModel = tableModel;

    ArrayList<ProductID<String, String>> productIDs = KNC_Terminal.getProductIDs(kiosks);
    Vector<ProductID<String, String>> products = new Vector<>(productIDs.size()+1);
    products.add(new ProductID<>("все услуги", "e"));
    products.addAll(productIDs);

    Vector<ProductID<String, String>> statuses = new Vector<>();
    Collections.addAll(statuses,
            new ProductID<>("любой статус", "e"),
            new ProductID<>("Проведен", "1"),
            new ProductID<>("Отклонен", "-1"),
            new ProductID<>("Действующий", "0")
    );

    Vector<ProductID<String, String>> gateways = new Vector<>();
    Collections.addAll(gateways,
            new ProductID<>("все платежные системы", "e"),
            new ProductID<>(KNC_Terminal.getGatewayName("RPC"), "RPC"),
            new ProductID<>(KNC_Terminal.getGatewayName("GORODK"), "GORODK"),
            new ProductID<>(KNC_Terminal.getGatewayName("GREENPOST"), "GREENPOST"),
            new ProductID<>(KNC_Terminal.getGatewayName("EPORT"), "EPORT"),
            new ProductID<>(KNC_Terminal.getGatewayName("CYBERPLAT"), "CYBERPLAT")
    );

    try {
      initComponents(products, statuses, gateways);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void initComponents(Vector<ProductID<String, String>> products, Vector<ProductID<String, String>> statuses, Vector<ProductID<String, String>> gateways) {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    JLabel topLabel = new JLabel();
    JPanel panel1 = new JPanel();
    intervalCheckBox = new JCheckBox();
    accountCheckBox = new JCheckBox();
    startLabel = new JLabel();
    startDatePicker = new JXDatePicker(new Date());
    endLabel = new JLabel();
    endDatePicker = new JXDatePicker(new Date());
    accountTextField = new JTextField();
    JPanel panel2 = new JPanel();
    productComboBox = new JComboBox<>();
    statusComboBox = new JComboBox<>();
    gatewayComboBox = new JComboBox<>();
    filterTextField = new JTextField();
    allCheckBox = new JCheckBox();
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();

    //======== this ========
    setTitle("\u0420\u0435\u0435\u0441\u0442\u0440 \u043f\u043b\u0430\u0442\u0435\u0436\u0435\u0439");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setResizable(false);
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 90, 100, 0};
    ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
    ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

    //---- topLabel ----
    topLabel.setText("\u041f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u0440\u0435\u0435\u0441\u0442\u0440 \u043f\u043b\u0430\u0442\u0435\u0436\u0435\u0439 \u0434\u043b\u044f \u0443\u043a\u0430\u0437\u0430\u043d\u043d\u044b\u0445 \u043a\u0438\u043e\u0441\u043a\u043e\u0432");
    topLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    contentPane.add(topLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
      new Insets(10, 10, 10, 10), 0, 0));

    //======== panel1 ========
    {
      panel1.setBorder(new EtchedBorder());
      panel1.setLayout(new GridBagLayout());
      ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 135, 0, 135, 0, 0};
      ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 40};
      ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};

      //---- intervalCheckBox ----
      intervalCheckBox.setText("\u041f\u043e\u043a\u0430\u0437\u0430\u0442\u044c \u0437\u0430 \u043f\u0435\u0440\u0438\u043e\u0434:");
      intervalCheckBox.setSelected(true);
      intervalCheckBox.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
          intervalCheckBoxItemStateChanged(e);
        }
      });
      panel1.add(intervalCheckBox, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(5, 10, 0, 0), 0, 0));

      //---- accountCheckBox ----
      accountCheckBox.setText("\u0422\u0435\u043b\u0435\u0444\u043e\u043d, \u043b/\u0441:");
      accountCheckBox.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
          accountCheckBoxItemStateChanged(e);
        }
      });
      panel1.add(accountCheckBox, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(5, 0, 0, 10), 0, 0));

      //---- startLabel ----
      startLabel.setText("\u0441");
      startLabel.setFont(new Font("Dialog", Font.BOLD, 12));
      panel1.add(startLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
        new Insets(5, 10, 10, 5), 0, 0));

      //---- startDatePicker ----
      startDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
      panel1.add(startDatePicker, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(5, 0, 10, 10), 0, 0));

      //---- endLabel ----
      endLabel.setText("\u043f\u043e");
      endLabel.setFont(new Font("Dialog", Font.BOLD, 12));
      panel1.add(endLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
        new Insets(5, 10, 10, 5), 0, 0));

      //---- endDatePicker ----
      endDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
      panel1.add(endDatePicker, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(5, 0, 10, 10), 0, 0));

      //---- accountTextField ----
      accountTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
      accountTextField.setEnabled(false);
      panel1.add(accountTextField, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(5, 0, 10, 10), 0, 0));
    }
    contentPane.add(panel1, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 10, 0, 10), 0, 0));

    //======== panel2 ========
    {
      panel2.setBorder(new EtchedBorder());
      panel2.setLayout(new GridBagLayout());
      ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0};
      ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};

      //---- productComboBox ----
      productComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      productComboBox.setBorder(null);
      productComboBox.setModel(new DefaultComboBoxModel<>(products));
      panel2.add(productComboBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(10, 10, 0, 0), 0, 0));

      //---- statusComboBox ----
      statusComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      statusComboBox.setBorder(null);
      statusComboBox.setModel(new DefaultComboBoxModel<>(statuses));
      panel2.add(statusComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(10, 10, 0, 10), 0, 0));

      //---- gatewayComboBox ----
      gatewayComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      gatewayComboBox.setBorder(null);
      gatewayComboBox.setModel(new DefaultComboBoxModel<>(gateways));
      panel2.add(gatewayComboBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(10, 10, 10, 0), 0, 0));

      //---- filterTextField ----
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
      panel2.add(filterTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(10, 10, 10, 10), 0, 0));
    }
    contentPane.add(panel2, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(10, 10, 0, 10), 0, 0));

    //---- allCheckBox ----
    allCheckBox.setText("\u043f\u043e \u0432\u0441\u0435\u043c \u0442\u0435\u0440\u043c\u0438\u043d\u0430\u043b\u0430\u043c");
    contentPane.add(allCheckBox, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(15, 10, 15, 50), 0, 0));

    //---- okButton ----
    okButton.setText("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        okButtonActionPerformed();
      }
    });
    contentPane.add(okButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(15, 0, 15, 10), 0, 0));

    //---- cancelButton ----
    cancelButton.setText("\u041e\u0442\u043c\u0435\u043d\u0430");
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cancelButtonActionPerformed();
      }
    });
    contentPane.add(cancelButton, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(15, 0, 15, 20), 0, 0));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  private void intervalCheckBoxItemStateChanged(ItemEvent e) {
    boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
    startLabel.setEnabled(selected);
    startDatePicker.setEnabled(selected);
    endLabel.setEnabled(selected);
    endDatePicker.setEnabled(selected);
  }

  private void accountCheckBoxItemStateChanged(ItemEvent e) {
    accountTextField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
  }

  private void okButtonActionPerformed() {
    Object[] objc = new Object[9];
    objc[0] = "GET_DBREESTR";
    if (intervalCheckBox.isSelected()) {
      Date date1 = startDatePicker.getDate();
      Date date2 = KNC_Terminal.endOfDay(endDatePicker.getDate());
      if (date1.after(date2)) {
        date1 = endDatePicker.getDate();
        date2 = KNC_Terminal.endOfDay(startDatePicker.getDate());
      }
      objc[2] = date1;
      objc[3] = date2;
    } else {
      objc[2] = "e";
      objc[3] = "e";
    }
    if (accountCheckBox.isSelected()) objc[4] = accountTextField.getText();
    else objc[4] = "e";
    objc[5] = productComboBox.getItemAt(productComboBox.getSelectedIndex()).id;
    objc[6] = statusComboBox.getItemAt(statusComboBox.getSelectedIndex()).id;
    objc[7] = gatewayComboBox.getItemAt(gatewayComboBox.getSelectedIndex()).id;
    String filter = filterTextField.getText();
    if (filter.equals(KNC_Terminal.queryFilter) || filter.equals("")) objc[8] = null;
    else objc[8] = filter;
    if (objc[2].equals("e") || (((Date) objc[3]).getTime() - ((Date) objc[2]).getTime() > 5446800001L)) {
      boolean allow = false;
      if (!objc[4].equals("e")) allow = true;
      if (!objc[5].equals("e")) allow = true;
      if (!objc[6].equals("e") && !objc[6].equals("1")) allow = true;
      if (!allow) {
        RPCMessage.showMessageDialog(owner, "Период больше 2 месяцев, уменьшите период или задайте дополнительные условия");
        return;
      }
    }
    this.setVisible(false);
    if (!allCheckBox.isSelected()) {
      AdminCommand[] commands = new AdminCommand[kiosks.length];
      for (int i = 0; i < commands.length; i++) {
        commands[i] = new AdminCommand(kiosks[i].getData()[1].toString(), KNC_Terminal.user.name);
        commands[i].setObjectArray(objc);
      }
      new At_net("ADMIN_COMMAND_START", new Request(KNC_Terminal.user, commands), this, 60000).start();
    } else {
      waitingDialog = new WaitingDialog(owner, ModalityType.DOCUMENT_MODAL);
      new At_net("GET_ALLREESTR", new Request(KNC_Terminal.user, objc), this, 120000).start();
      waitingDialog.setVisible(true);
    }
  }

  private void cancelButtonActionPerformed() {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("ret_ADMIN_COMMAND_START")) {
      if (e.getID() == 3) {
        AdminCommand[] commands = (AdminCommand[]) ((Request) ((At_net) e.getSource()).getObject()).data;
        String[] kioskNumbers = new String[commands.length];
        for (int i = 0; i < commands.length; i++) {
          kioskNumbers[i] = commands[i].kiosk_number;
        }
        tableModel.putInternalComments(kioskNumbers, KNC_Terminal.wasSent);
      }
    }
    if (e.getActionCommand().equals("finished_ADMIN_COMMAND_START")) {
      if (((At_net) e.getSource()).getResult().equals("OK")) {
        KNC_Terminal.dataUpdater.wakeup();
        this.dispose();
      }
      if (((At_net) e.getSource()).getResult().equals("WAIT")) {
        this.dispose();
      }
    }
    if (e.getActionCommand().equals("finished_GET_ALLREESTR")) {
      if (waitingDialog != null) waitingDialog.dispose();
      final Object[] result = (Object[]) ((At_net) e.getSource()).getResult();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          new ResultsFrame(result).setVisible(true);
          GetRegistryDialog.this.dispose();
        }
      });
    }
    if (e.getActionCommand().startsWith("fail_")) {
      if (waitingDialog != null) waitingDialog.dispose();
      if (((At_net) e.getSource()).getStatus())
        RPCMessage.showMessageDialog(owner, "Команда отправлена на сервер, но подтверждение не было получено");
      else
        RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
    }
  }

  private void filterTextFieldFocusGained() {
    if (filterTextField.getText().equals(KNC_Terminal.queryFilter)) filterTextField.setText("");
  }

  private void filterTextFieldFocusLost() {
    if (filterTextField.getText().equals("")) filterTextField.setText(KNC_Terminal.queryFilter);
  }
}
