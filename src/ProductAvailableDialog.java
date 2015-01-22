import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class ProductAvailableDialog extends JDialog implements ActionListener {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JComboBox<ProductID<String, String>> productComboBox;
  private JComboBox<ProductID<String, Boolean>> availableComboBox;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  private final Window owner;
  private final KioskProfile[] kiosks;

  public ProductAvailableDialog(Window owner, Dialog.ModalityType modalityType, KioskProfile[] kiosks) {
    super(owner, modalityType);
    this.owner = owner;
    this.kiosks = kiosks;

    ArrayList<ProductID<String, String>> productIDs = KNC_Terminal.getProductIDs(kiosks);
    Vector<ProductID<String, String>> products = new Vector<>(productIDs.size());
    products.addAll(productIDs);

    Vector<ProductID<String, Boolean>> values = new Vector<>(2);
    Collections.addAll(values,
            new ProductID<>("Заблокировать", Boolean.FALSE),
            new ProductID<>("Разблокировать", Boolean.TRUE)
    );

    try {
      initComponents(products, values);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void initComponents(Vector<ProductID<String, String>> products, Vector<ProductID<String, Boolean>> values) {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    JLabel topLabel = new JLabel();
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel();
    JLabel label2 = new JLabel();
    productComboBox = new JComboBox<>();
    availableComboBox = new JComboBox<>();
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();

    //======== this ========
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("\u0411\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u043a\u0430/\u0440\u0430\u0437\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u043a\u0430 \u0443\u0441\u043b\u0443\u0433");
    setResizable(false);
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 90, 100, 0};
    ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
    ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

    //---- topLabel ----
    topLabel.setText("\u0417\u0430\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u0442\u044c/\u0440\u0430\u0437\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0443\u0441\u043b\u0443\u0433\u0443 \u043d\u0430 \u0443\u043a\u0430\u0437\u0430\u043d\u043d\u044b\u0445 \u043a\u0438\u043e\u0441\u043a\u0430\u0445");
    topLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    contentPane.add(topLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
      new Insets(10, 10, 10, 10), 0, 0));

    //======== panel1 ========
    {
      panel1.setBorder(new EtchedBorder());
      panel1.setLayout(new GridBagLayout());
      ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {200, 150, 0};
      ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};

      //---- label1 ----
      label1.setText("\u0423\u0441\u043b\u0443\u0433\u0430");
      label1.setFont(new Font("Dialog", Font.BOLD, 12));
      panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(15, 10, 0, 0), 0, 0));

      //---- label2 ----
      label2.setText("\u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0435");
      label2.setFont(new Font("Dialog", Font.BOLD, 12));
      panel1.add(label2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(15, 10, 0, 10), 0, 0));

      //---- productComboBox ----
      productComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      productComboBox.setBorder(null);
      productComboBox.setModel(new DefaultComboBoxModel<>(products));
      panel1.add(productComboBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 10, 15, 0), 0, 0));

      //---- availableComboBox ----
      availableComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      availableComboBox.setBorder(null);
      availableComboBox.setModel(new DefaultComboBoxModel<>(values));
      panel1.add(availableComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 10, 15, 10), 0, 0));
    }
    contentPane.add(panel1, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 10, 0, 10), 0, 0));

    //---- okButton ----
    okButton.setText("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        okButtonActionPerformed();
      }
    });
    contentPane.add(okButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
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
    contentPane.add(cancelButton, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(15, 0, 15, 20), 0, 0));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  private void okButtonActionPerformed() {
    this.setVisible(false);
    Object[] objc = new Object[4];
    objc[0] = "PRODUCT_AVAILABLE";
    objc[2] = productComboBox.getItemAt(productComboBox.getSelectedIndex()).id;
    objc[3] = availableComboBox.getItemAt(availableComboBox.getSelectedIndex()).id;
    AdminCommand[] commands = new AdminCommand[kiosks.length];
    for (int i = 0; i < commands.length; i++) {
      commands[i] = new AdminCommand(kiosks[i].getData()[1].toString(), KNC_Terminal.user.name);
      commands[i].setObjectArray(objc);
    }
    new At_net("ADMIN_COMMAND_START", new Request(KNC_Terminal.user, commands), this).start();
  }

  private void cancelButtonActionPerformed() {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_ADMIN_COMMAND_START")) {
      if (((At_net) e.getSource()).getResult().equals("OK")) {
        RPCMessage.showMessageDialog(owner, "Команда блокировки/разблокировки услуг успешно отправлена на сервер");
        this.dispose();
      }
    }
    if (e.getActionCommand().equals("fail_ADMIN_COMMAND_START")) {
      if (((At_net) e.getSource()).getStatus())
        RPCMessage.showMessageDialog(owner, "Команда отправлена на сервер, но подтверждение не было получено");
      else
        RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
    }
  }
}

