

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

class ChPassDialog extends JDialog implements ActionListener {
  private final Window owner;
  private JPanel panel = new JPanel();
  private JLabel background = new JLabel();
  private JPasswordField passField = new JPasswordField();
  private JPasswordField pass1Field = new JPasswordField();
  private JPasswordField pass2Field = new JPasswordField();
  private JButton okButton = new JButton();
  private JButton cancelButton = new JButton();

  public ChPassDialog(Window owner, Dialog.ModalityType modalityType) {
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
    this.setSize(new Dimension(300, 297));
    this.setTitle("Изменить пароль");
    background.setBounds(0, 0, 300, 297);
    background.setIcon(new ImageIcon("images/chpass.png"));
    passField.setBounds(123, 60, 150, 25);
    passField.setActionCommand("pass");
    passField.addActionListener(new ChPassDialog_okButton_actionAdapter(this));
    pass1Field.setBounds(123, 105, 150, 25);
    pass1Field.setActionCommand("pass1");
    pass1Field.addActionListener(new ChPassDialog_okButton_actionAdapter(this));
    pass2Field.setBounds(123, 154, 150, 25);
    pass2Field.setActionCommand("pass2");
    pass2Field.addActionListener(new ChPassDialog_okButton_actionAdapter(this));
    okButton.setBounds(48, 207, 98, 44);
    okButton.setIcon(new ImageIcon("images/but1.png"));
    okButton.setPressedIcon(new ImageIcon("images/but1pressed.png"));
    okButton.setBorderPainted(false);
    okButton.setContentAreaFilled(false);
    okButton.addActionListener(new ChPassDialog_okButton_actionAdapter(this));
    cancelButton.setBounds(154, 208, 98, 44);
    cancelButton.setIcon(new ImageIcon("images/but2.png"));
    cancelButton.setPressedIcon(new ImageIcon("images/but2pressed.png"));
    cancelButton.setBorderPainted(false);
    cancelButton.setContentAreaFilled(false);
    cancelButton.addActionListener(new ChPassDialog_cancelButton_actionAdapter(this));
    panel.add(passField);
    panel.add(pass1Field);
    panel.add(pass2Field);
    panel.add(okButton);
    panel.add(cancelButton);
    panel.add(background);
  }

  void okButton_actionPerformed(ActionEvent e) {
    if (passField.getPassword().length < 1) {
      RPCMessage.showMessageDialog(this, "Не указан старый пароль учетной записи");
      passField.requestFocus();
      return;
    }
    if (new String(passField.getPassword()).hashCode() != KNC_Terminal.user.pass) {
      RPCMessage.showMessageDialog(this, "Неверно указан старый пароль");
      passField.requestFocus();
      return;
    }
    if (e.getActionCommand().equals("pass") && pass1Field.getPassword().length < 1) {
      pass1Field.requestFocus();
      return;
    }
    if (pass1Field.getPassword().length < 1) {
      RPCMessage.showMessageDialog(this, "Не указан новый пароль учетной записи");
      pass1Field.requestFocus();
      return;
    }
    if (pass1Field.getPassword().length < 5) {
      RPCMessage.showMessageDialog(this, "Новый пароль учетной записи меньше 5 символов");
      pass1Field.requestFocus();
      return;
    }
    if (e.getActionCommand().equals("pass1") && pass2Field.getPassword().length < 1) {
      pass2Field.requestFocus();
      return;
    }
    if (pass2Field.getPassword().length < 1) {
      RPCMessage.showMessageDialog(this, "Не указан новый пароль учетной записи");
      pass2Field.requestFocus();
      return;
    }
    if (!new String(pass1Field.getPassword()).equals(new String(pass2Field.getPassword()))) {
      RPCMessage.showMessageDialog(this, "Новые пароли не совпадают");
      pass1Field.setText("");
      pass2Field.setText("");
      pass1Field.requestFocus();
      return;
    }
    if (PasswordChecker.getStrength(pass1Field.getPassword()) < PasswordChecker.THRESHOLD) {
      RPCMessage.showMessageDialog(this, "Новый пароль учетной записи слишком простой");
      pass1Field.setText("");
      pass2Field.setText("");
      pass1Field.requestFocus();
      return;
    }
    this.setVisible(false);
    char[] p = pass2Field.getPassword();
    int newPass = String.valueOf(p).hashCode();
    Arrays.fill(p, (char)0);
    new At_net("CHANGE_PAASVT", new Request(KNC_Terminal.user, new Object[] {newPass}), this).start();
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_CHANGE_PAASVT")) {
      UserProfile newUser = (UserProfile)((At_net)e.getSource()).getResult();
      if (newUser.name.equals("CHANGEERROR")) {
        RPCMessage.showMessageDialog(owner, "Ошибка сервера: невозможно изменить пароль");
      } else {
        KNC_Terminal.user = newUser;
        RPCMessage.showMessageDialog(owner, "Изменение пароля выполнено успешно");
      }
      this.dispose();
    }
    if (e.getActionCommand().equals("fail_CHANGE_PAASVT")) {
      RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
      this.setVisible(true);
    }
  }
}

class ChPassDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  private ChPassDialog adaptee;
  ChPassDialog_okButton_actionAdapter(ChPassDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class ChPassDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  private ChPassDialog adaptee;
  ChPassDialog_cancelButton_actionAdapter(ChPassDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}
