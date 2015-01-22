

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class DelUserDialog extends JDialog implements ActionListener {
  private final Window owner;
  private JPanel panel = new JPanel();
  private JLabel background = new JLabel();
  private JTextField nameField = new JTextField();
  private JButton okButton = new JButton();
  private JButton cancelButton = new JButton();

  public DelUserDialog(Window owner, Dialog.ModalityType modalityType) {
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
    this.setTitle("Удалить пользователя");
    background.setBounds(0, 0, 300, 190);
    background.setIcon(new ImageIcon("images/deluser.png"));
    nameField.setBounds(100, 61, 150, 25);
    nameField.setFont(new Font("Dialog", Font.PLAIN, 12));
    okButton.setBounds(48, 100, 98, 44);
    okButton.setIcon(new ImageIcon("images/but1.png"));
    okButton.setPressedIcon(new ImageIcon("images/but1pressed.png"));
    okButton.setBorderPainted(false);
    okButton.setContentAreaFilled(false);
    okButton.addActionListener(new DelUserDialog_okButton_actionAdapter(this));
    cancelButton.setBounds(154, 101, 98, 44);
    cancelButton.setIcon(new ImageIcon("images/but2.png"));
    cancelButton.setPressedIcon(new ImageIcon("images/but2pressed.png"));
    cancelButton.setBorderPainted(false);
    cancelButton.setContentAreaFilled(false);
    cancelButton.addActionListener(new DelUserDialog_cancelButton_actionAdapter(this));
    panel.add(nameField);
    panel.add(okButton);
    panel.add(cancelButton);
    panel.add(background);
  }

  void okButton_actionPerformed(ActionEvent e) {
    if (nameField.getText().length() < 1) {
      RPCMessage.showMessageDialog(this, "Не указано имя учетной записи");
      nameField.requestFocus();
      return;
    }
    this.setVisible(false);
    if (RPCDialog.showMessageDialog(owner, "Вы действительно хотите удалить пользователя "+nameField.getText()+"?") == RPCDialog.Result.OK) {
      new At_net("DEL_USER", new Request(KNC_Terminal.user, new Object[] {nameField.getText()}), this).start();
    } else this.dispose();
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_DEL_USER")) {
      if (((At_net)e.getSource()).getResult().equals("NOTFOUND")) RPCMessage.showMessageDialog(owner, "Пользователь с таким именем не найден");
      if (((At_net)e.getSource()).getResult().equals("DELERROR")) RPCMessage.showMessageDialog(owner, "Ошибка сервера: невозможно удалить данного пользователя");
      if (((At_net)e.getSource()).getResult().equals("DELDENIED")) RPCMessage.showMessageDialog(owner, "Вы не можете удалить этого пользователя");
      if (((At_net)e.getSource()).getResult().equals("DELSUCCESS")) RPCMessage.showMessageDialog(owner, "Удаление пользователя выполнено успешно");
      this.dispose();
    }
    if (e.getActionCommand().equals("fail_DEL_USER")) {
      RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
      this.setVisible(true);
    }
  }
}

class DelUserDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  private DelUserDialog adaptee;
  DelUserDialog_okButton_actionAdapter(DelUserDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class DelUserDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  private DelUserDialog adaptee;
  DelUserDialog_cancelButton_actionAdapter(DelUserDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}
