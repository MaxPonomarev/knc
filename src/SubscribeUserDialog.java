

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SubscribeUserDialog extends JDialog implements ActionListener {
  private final Window owner;
  private JPanel panel = new JPanel();
  private JLabel background = new JLabel();
  private JTextField mailField = new JTextField();
  private JButton okButton = new JButton();
  private JButton cancelButton = new JButton();

  public SubscribeUserDialog(Window owner, Dialog.ModalityType modalityType) {
    super(owner, modalityType);
    this.owner = owner;
    try {
      jbInit();
      this.setLocationRelativeTo(owner);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() {
    panel.setLayout(null);
    getContentPane().add(panel);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setSize(new Dimension(300, 190));
    this.setTitle("Подписаться на рассылку");
    background.setBounds(0, 0, 300, 190);
    background.setIcon(new ImageIcon("images/mailuser.png"));
    mailField.setBounds(80, 61, 200, 25);
    mailField.setFont(new Font("Dialog", Font.PLAIN, 12));
    mailField.setToolTipText("Список адресов через запятую");
    if (KNC_Terminal.user.mailList == null) mailField.setText(""); else mailField.setText(KNC_Terminal.user.mailList);
    okButton.setBounds(48, 100, 98, 44);
    okButton.setIcon(new ImageIcon("images/but1.png"));
    okButton.setPressedIcon(new ImageIcon("images/but1pressed.png"));
    okButton.setBorderPainted(false);
    okButton.setContentAreaFilled(false);
    okButton.addActionListener(new SubscribeUserDialog_okButton_actionAdapter(this));
    cancelButton.setBounds(154, 101, 98, 44);
    cancelButton.setIcon(new ImageIcon("images/but2.png"));
    cancelButton.setPressedIcon(new ImageIcon("images/but2pressed.png"));
    cancelButton.setBorderPainted(false);
    cancelButton.setContentAreaFilled(false);
    cancelButton.addActionListener(new SubscribeUserDialog_cancelButton_actionAdapter(this));
    panel.add(mailField);
    panel.add(okButton);
    panel.add(cancelButton);
    panel.add(background);
  }

  void okButton_actionPerformed(ActionEvent e) {
    String[] parts = mailField.getText().split(",");
    for (String part : parts) {
      if (!part.matches(".+@.+\\..+|")) {
        RPCMessage.showMessageDialog(this, "Неправильно указан список адресов");
        mailField.requestFocus();
        return;
      }
    }
    this.setVisible(false);
    new At_net("SET_MAILLIST", new Request(KNC_Terminal.user, new Object[] {mailField.getText()}), this).start();
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_SET_MAILLIST")) {
      UserProfile newUser = (UserProfile)((At_net)e.getSource()).getResult();
      if (newUser.name.equals("CHANGEERROR")) {
        RPCMessage.showMessageDialog(owner, "Ошибка сервера: невозможно изменить подписку");
      } else {
        KNC_Terminal.user = newUser;
        RPCMessage.showMessageDialog(owner, "Изменение подписки выполнено успешно");
      }
      this.dispose();
    }
    if (e.getActionCommand().equals("fail_SET_MAILLIST")) {
      RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
      this.setVisible(true);
    }
  }
}

class SubscribeUserDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  private SubscribeUserDialog adaptee;
  SubscribeUserDialog_okButton_actionAdapter(SubscribeUserDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class SubscribeUserDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  private SubscribeUserDialog adaptee;
  SubscribeUserDialog_cancelButton_actionAdapter(SubscribeUserDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}
