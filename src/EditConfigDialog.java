import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class EditConfigDialog extends JDialog implements ActionListener {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JLabel topLabel;
  private JComboBox<ProductID<String, String>> paramComboBox;
  private JComboBox<ProductID<String, String>> valueComboBox;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  private final Window owner;
  private final KioskProfile[] kiosks;
  private byte needReboot = -1; //-1-����������, 0-�� �����, 1-�����

  public EditConfigDialog(Window owner, Dialog.ModalityType modalityType, KioskProfile[] kiosks) {
    super(owner, modalityType);
    this.owner = owner;
    this.kiosks = kiosks;
    Vector<ProductID<String, String>> keys = new Vector<>();
    Collections.addAll(keys,
            new ProductID<>("������", ""),
            new ProductID<>("����� �����", "PaperLength"),
            new ProductID<>("����� ������ ������", "PaperRollLength"),
            new ProductID<>("�������", "Printer"),
            new ProductID<>("������������ ������ ����", "PrinterDelay"),
            new ProductID<>("������ �����", "MonitorOFF"),
            new ProductID<>("����� ������", "KioskAddress"),
            new ProductID<>("����� ���������� �����", "StackCapacity"),
            new ProductID<>("��������������", "AutoIncass")
    );
    try {
      initComponents(keys);
      setMinimumSize(getSize());
      topLabel.setText("�������� ������������ ��������� ������� (" + kiosks.length + " ��.)");
      paramComboBox.setSelectedIndex(-1);
      paramComboBox.setSelectedIndex(0);//��������� �������
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void initComponents(Vector<ProductID<String, String>> keys) {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    topLabel = new JLabel();
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel();
    JLabel label2 = new JLabel();
    paramComboBox = new JComboBox<>();
    valueComboBox = new JComboBox<>();
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();

    //======== this ========
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("\u041a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u044f \u043a\u0438\u043e\u0441\u043a\u0430");
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 90, 100, 0};
    ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
    ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

    //---- topLabel ----
    topLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    topLabel.setText("\u0418\u0437\u043c\u0435\u043d\u0438\u0442\u044c \u043a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u044e \u0443\u043a\u0430\u0437\u0430\u043d\u043d\u044b\u0445 \u043a\u0438\u043e\u0441\u043a\u043e\u0432");
    contentPane.add(topLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
      new Insets(10, 10, 10, 10), 0, 0));

    //======== panel1 ========
    {
      panel1.setBorder(new EtchedBorder());
      panel1.setLayout(new GridBagLayout());
      ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {195, 250, 0};
      ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};

      //---- label1 ----
      label1.setText("\u041f\u0430\u0440\u0430\u043c\u0435\u0442\u0440");
      label1.setFont(new Font("Dialog", Font.BOLD, 12));
      panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(15, 10, 0, 0), 0, 0));

      //---- label2 ----
      label2.setText("\u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435");
      label2.setFont(new Font("Dialog", Font.BOLD, 12));
      panel1.add(label2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(15, 10, 0, 10), 0, 0));

      //---- paramComboBox ----
      paramComboBox.setFont(new Font("Dialog", Font.PLAIN, 12));
      paramComboBox.setBorder(null);
      paramComboBox.setMaximumRowCount(9);
      paramComboBox.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
          paramComboBoxItemStateChanged(e);
        }
      });
      paramComboBox.setModel(new DefaultComboBoxModel<>(keys));
      panel1.add(paramComboBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 10, 15, 0), 0, 0));

      //---- valueComboBox ----
      valueComboBox.setFont(new Font("Dialog", Font.PLAIN, 12));
      valueComboBox.setBorder(null);
      valueComboBox.setMaximumRowCount(9);
      panel1.add(valueComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
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

  private void paramComboBoxItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      if (paramComboBox.getSelectedIndex() == 0) {
        paramComboBox.setEditable(true);
        paramComboBox.setSelectedItem("");
        valueComboBox.removeAllItems();
        valueComboBox.addItem(new ProductID<>("", ""));
        valueComboBox.setEditable(true);
        needReboot = -1;
      } else if (paramComboBox.getSelectedIndex() > 0) {
        paramComboBox.setEditable(false);
        needReboot = -1;
        if (paramComboBox.getItemAt(paramComboBox.getSelectedIndex()).id.equals("PaperLength")) {
          valueComboBox.removeAllItems();
          valueComboBox.addItem(new ProductID<>("0 �", "0"));
          valueComboBox.addItem(new ProductID<>("1 �", "1000"));
          valueComboBox.addItem(new ProductID<>("5 �", "5000"));
          for (int i = 10; i <= 500; i += 10) valueComboBox.addItem(new ProductID<>(i + " �", i + "000"));
          valueComboBox.setEditable(false);
          needReboot = 0;
        }
        if (paramComboBox.getItemAt(paramComboBox.getSelectedIndex()).id.equals("PaperRollLength")) {
          valueComboBox.removeAllItems();
          for (int i = 10; i <= 100; i += 10) valueComboBox.addItem(new ProductID<>(i + " �", i + "000"));
          for (int i = 150; i <= 500; i += 50) valueComboBox.addItem(new ProductID<>(i + " �", i + "000"));
          valueComboBox.setEditable(false);
          needReboot = 0;
        }
        if (paramComboBox.getItemAt(paramComboBox.getSelectedIndex()).id.equals("Printer")) {
          valueComboBox.removeAllItems();
          valueComboBox.addItem(new ProductID<>("EPSON EU-T500", "1"));
          valueComboBox.addItem(new ProductID<>("CUSTOM VKP-80", "3"));
          valueComboBox.addItem(new ProductID<>("CUSTOM TPT 52", "7"));
          valueComboBox.addItem(new ProductID<>("Citizen PPU-700", "2"));
          valueComboBox.addItem(new ProductID<>("Citizen PPU-700 ������� ���", "5"));
          valueComboBox.addItem(new ProductID<>("Citizen CT-S2000", "4"));
          valueComboBox.addItem(new ProductID<>("Star TUP 900", "6"));
          valueComboBox.addItem(new ProductID<>("�����-����", "8"));
          valueComboBox.addItem(new ProductID<>("�����-�����", "9"));
          valueComboBox.addItem(new ProductID<>("�����-�����", "10"));
          valueComboBox.addItem(new ProductID<>("�� Pay VKP-80K", "11"));
          valueComboBox.addItem(new ProductID<>("�� �����-����", "12"));
          valueComboBox.addItem(new ProductID<>("�� ����-01�", "14"));
          valueComboBox.addItem(new ProductID<>("�� ����-02�", "13"));
          valueComboBox.addItem(new ProductID<>("������� CUSTOM VKP-80", "21"));
          valueComboBox.setEditable(false);
          needReboot = 0;
        }
        if (paramComboBox.getItemAt(paramComboBox.getSelectedIndex()).id.equals("PrinterDelay")) {
          valueComboBox.removeAllItems();
          for (int i = 1; i <= 10; i++) valueComboBox.addItem(new ProductID<>(i + " �", i + "000"));
          valueComboBox.setEditable(false);
          needReboot = 0;
        }
        if (paramComboBox.getItemAt(paramComboBox.getSelectedIndex()).id.equals("KioskAddress")) {
          valueComboBox.removeAllItems();
          valueComboBox.addItem(new ProductID<>("", ""));
          valueComboBox.setEditable(true);
          needReboot = 0;
        }
        if (paramComboBox.getItemAt(paramComboBox.getSelectedIndex()).id.equals("MonitorOFF")) {
          valueComboBox.removeAllItems();
          valueComboBox.addItem(new ProductID<>("", ""));
          valueComboBox.setEditable(true);
          needReboot = 1;
        }
        if (paramComboBox.getItemAt(paramComboBox.getSelectedIndex()).id.equals("StackCapacity")) {
          valueComboBox.removeAllItems();
          valueComboBox.addItem(new ProductID<>("500 �����", "500"));
          valueComboBox.addItem(new ProductID<>("600 �����", "600"));
          valueComboBox.addItem(new ProductID<>("1000 �����", "1000"));
          valueComboBox.addItem(new ProductID<>("1200 �����", "1200"));
          valueComboBox.addItem(new ProductID<>("1500 �����", "1500"));
          valueComboBox.setEditable(false);
          needReboot = 0;
        }
        if (paramComboBox.getItemAt(paramComboBox.getSelectedIndex()).id.equals("AutoIncass")) {
          valueComboBox.removeAllItems();
          valueComboBox.addItem(new ProductID<>("���������", "0"));
          valueComboBox.addItem(new ProductID<>("��������", "1"));
          valueComboBox.setEditable(false);
          needReboot = 0;
        }
      }
    }
  }

  private void okButtonActionPerformed() {
    if (paramComboBox.getSelectedItem().equals("")) {
      paramComboBox.requestFocus();
      JOptionPane.showMessageDialog(this, "���������� ������� ��������", "������", JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.setVisible(false);
    Object[] objc = new Object[4];
    objc[0] = "EDIT_CONFIG";
    if (paramComboBox.getSelectedIndex() >= 0) objc[2] = paramComboBox.getItemAt(paramComboBox.getSelectedIndex()).id;
    else objc[2] = paramComboBox.getSelectedItem().toString();
    if (valueComboBox.getSelectedIndex() >= 0) objc[3] = valueComboBox.getItemAt(valueComboBox.getSelectedIndex()).id;
    else objc[3] = valueComboBox.getSelectedItem().toString();
    if (objc[2].equals("MonitorOFF") && objc[3].toString().length() > 0 && !objc[3].toString().matches("(([01]*\\d)|(2[0123]))\\.[012345]\\d-(([01]*\\d)|(2[0123]))\\.[012345]\\d")) {
      valueComboBox.requestFocus();
      JOptionPane.showMessageDialog(this, "�������� ������� ������ ������ �������\n������: 23.30-7.30", "������", JOptionPane.ERROR_MESSAGE);
      return;
    }
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
        if (needReboot == -1)
          RPCMessage.showMessageDialog(owner, "������� ��������� ������������ ������� ���������� �� ������. �������� ��������� ���������� ��� ���������� ��������� � ����");
        if (needReboot == 0)
          RPCMessage.showMessageDialog(owner, "������� ��������� ������������ ������� ���������� �� ������");
        if (needReboot == 1)
          RPCMessage.showMessageDialog(owner, "������� ��������� ������������ ������� ���������� �� ������. ��������� ���������� ��� ���������� ��������� � ����");
        this.dispose();
      }
    }
    if (e.getActionCommand().equals("fail_ADMIN_COMMAND_START")) {
      if (((At_net) e.getSource()).getStatus())
        RPCMessage.showMessageDialog(owner, "������� ���������� �� ������, �� ������������� �� ���� ��������");
      else
        RPCMessage.showMessageDialog(owner, "������ ���������� � ����������� ��������");
    }
  }
}
