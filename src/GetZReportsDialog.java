import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import javax.swing.*;

import org.jdesktop.swingx.JXDatePicker;

class GetZReportsDialog extends JDialog implements ActionListener {
  private final Window owner;
  private JPanel panel = new JPanel();
  private JLabel topLabel = new JLabel();
  private JButton okButton = new JButton();
  private JButton cancelButton = new JButton();
  private JComboBox<ProductID<String,Long>> companiesComboBox = new JComboBox<>();
  private JPanel intervalPanel = new JPanel();
  private JCheckBox intervalCheckBox = new JCheckBox();
  private JLabel startLabel = new JLabel();
  private JLabel endLabel = new JLabel();
  private JXDatePicker startDatePicker = new JXDatePicker(new Date());
  private JXDatePicker endDatePicker = new JXDatePicker(new Date());
  private JPanel serialPanel = new JPanel();
  private JCheckBox serialCheckBox = new JCheckBox();
  private JTextField serialField = new JTextField();

  public GetZReportsDialog(Window owner, Dialog.ModalityType modalityType) {
    super(owner, modalityType);
    this.owner = owner;
    try {
      jbInit();
      this.setLocationRelativeTo(owner);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() {
    panel.setLayout(null);
    getContentPane().add(panel);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setSize(new Dimension(410, 275));
    this.setTitle("Реестр Z-отчетов");
    topLabel.setText("Получить реестр Z-отчетов для указанного предприятия");
    topLabel.setBounds(10, 0, 400, 40);
    topLabel.setFont(new Font("Dialog", Font.BOLD, 12));

    companiesComboBox.setBounds(17, 35, 365, 25);
    companiesComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
    companiesComboBox.setBorder(null);
    {
      for (Long inn : KNC_Terminal.user.companies.keySet()) {
        String cname = KNC_Terminal.user.companies.get(inn);
        companiesComboBox.addItem(new ProductID<>(cname, inn));
      }
    }

    intervalPanel.setLayout(null);
    intervalPanel.setBorder(BorderFactory.createEtchedBorder());
    intervalPanel.setBounds(7, 80, 390, 70);
    intervalCheckBox.setText("Показать за период:");
    intervalCheckBox.setBounds(10, 5, 200, 25);
    intervalCheckBox.setSelected(true);
    intervalCheckBox.addActionListener(new GetZReportsDialog_intervalCheckBox_actionAdapter(this));
    startLabel.setText("с");
    startLabel.setBounds(30, 32, 15, 25);
    startLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    endLabel.setText("по");
    endLabel.setBounds(230, 32, 15, 25);
    endLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    startDatePicker.setBounds(50, 32, 125, 25);
    startDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
    endDatePicker.setBounds(250, 32, 125, 25);
    endDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
    intervalPanel.add(intervalCheckBox);
    intervalPanel.add(startDatePicker);
    intervalPanel.add(endDatePicker);
    intervalPanel.add(startLabel);
    intervalPanel.add(endLabel);

    serialPanel.setLayout(null);
    serialPanel.setBorder(BorderFactory.createEtchedBorder());
    serialPanel.setBounds(7, 160, 190, 70);
    serialCheckBox.setText("Серийный номер ФР:");
    serialCheckBox.setBounds(10, 5, 175, 25);
    serialCheckBox.addActionListener(new GetZReportsDialog_serialCheckBox_actionAdapter(this));
    serialField.setText("");
    serialField.setBounds(30, 32, 150, 25);
    serialField.setFont(new Font("Dialog", Font.PLAIN, 12));
    serialField.setEnabled(false);
    serialPanel.add(serialCheckBox);
    serialPanel.add(serialField);

    okButton.setText("OK");
    okButton.setBounds(217, 192, 80, 25);
    okButton.addActionListener(new GetZReportsDialog_okButton_actionAdapter(this));
    cancelButton.setText("Отмена");
    cancelButton.setBounds(307, 192, 80, 25);
    cancelButton.addActionListener(new GetZReportsDialog_cancelButton_actionAdapter(this));
    panel.add(topLabel);
    panel.add(companiesComboBox);
    panel.add(intervalPanel);
    panel.add(serialPanel);
    panel.add(okButton);
    panel.add(cancelButton);
  }

  public void intervalCheckBox_actionPerformed(ActionEvent e) {
    boolean selected = intervalCheckBox.isSelected();
    startLabel.setEnabled(selected);
    startDatePicker.setEnabled(selected);
    endLabel.setEnabled(selected);
    endDatePicker.setEnabled(selected);
  }

  public void serialCheckBox_actionPerformed(ActionEvent e) {
    boolean selected = serialCheckBox.isSelected();
    serialField.setEnabled(selected);
  }

  public void okButton_actionPerformed(ActionEvent e) {
    Object[] objc = new Object[5];
    objc[0] = "GET_ZREPORTS";
    objc[1] = companiesComboBox.getItemAt(companiesComboBox.getSelectedIndex()).id;
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
    if (serialCheckBox.isSelected()) {
      try {
        Integer serial = Integer.parseInt(serialField.getText());
        objc[4] = serial;
      } catch (NumberFormatException ex) {
        serialField.requestFocus();
        RPCMessage.showMessageDialog(this, "Серийный номер должен состоять из цифр", "Ошибка");
        return;
      }
    } else objc[4] = "e";
    this.setVisible(false);
    new At_net("GET_ZREPORTS", new Request(KNC_Terminal.user, objc), this, 60000).start();
  }

  public void cancelButton_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_GET_ZREPORTS")) {
      ZReport[] result = (ZReport[])((At_net)e.getSource()).getResult();
      if (result.length == 1 && result[0].INN == 0) RPCMessage.showMessageDialog(owner, "Ошибка базы данных");
      else new ZReportsFrame(result).setVisible(true);
      this.dispose();
    }
    if (e.getActionCommand().equals("fail_GET_ZREPORTS")) {
      RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
      this.setVisible(true);
    }
  }
}

class GetZReportsDialog_cancelButton_actionAdapter implements ActionListener {
  private GetZReportsDialog adaptee;
  GetZReportsDialog_cancelButton_actionAdapter(GetZReportsDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class GetZReportsDialog_okButton_actionAdapter implements ActionListener {
  private GetZReportsDialog adaptee;
  GetZReportsDialog_okButton_actionAdapter(GetZReportsDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class GetZReportsDialog_serialCheckBox_actionAdapter implements ActionListener {
  private GetZReportsDialog adaptee;
  GetZReportsDialog_serialCheckBox_actionAdapter(GetZReportsDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.serialCheckBox_actionPerformed(e);
  }
}

class GetZReportsDialog_intervalCheckBox_actionAdapter implements ActionListener {
  private GetZReportsDialog adaptee;
  GetZReportsDialog_intervalCheckBox_actionAdapter(GetZReportsDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.intervalCheckBox_actionPerformed(e);
  }
}
