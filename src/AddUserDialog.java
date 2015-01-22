import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

class AddUserDialog extends JDialog implements ActionListener {
  private final Window owner;
  private JPanel panel = new JPanel();
  private LevelPanel levelPanel = new LevelPanel();
  private JLabel background = new JLabel();
  private JTextField nameField = new JTextField();
  private JPasswordField pass1Field = new JPasswordField();
  private JPasswordField pass2Field = new JPasswordField();
  private JButton okButton = new JButton();
  private JButton cancelButton = new JButton();

  public AddUserDialog(Window owner, Dialog.ModalityType modalityType) {
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
    this.setSize(new Dimension(300, 500));
    this.setTitle("Добавить пользователя");
    background.setBounds(0, 0, 300, 500);
    background.setIcon(new ImageIcon("images/adduser.png"));
    nameField.setBounds(107, 65, 150, 25);
    nameField.setFont(new Font("Dialog", Font.PLAIN, 12));
    nameField.setActionCommand("name");
    nameField.addActionListener(new AddUserDialog_okButton_actionAdapter(this));
    pass1Field.setBounds(107, 100, 150, 25);
    pass1Field.setActionCommand("pass1");
    pass1Field.addActionListener(new AddUserDialog_okButton_actionAdapter(this));
    pass2Field.setBounds(107, 135, 150, 25);
    pass2Field.setActionCommand("pass2");
    pass2Field.addActionListener(new AddUserDialog_okButton_actionAdapter(this));
    levelPanel.setBounds(25, 230, 244, 165);
    if (KNC_Terminal.user.level < 4) {
      levelPanel.levelButton[4].setEnabled(false);
      levelPanel.accessField.setEditable(false);
    }
    {levelPanel.accessField.setText(KNC_Terminal.user.access);}
    okButton.setBounds(48, 410, 98, 44);
    okButton.setIcon(new ImageIcon("images/but1.png"));
    okButton.setPressedIcon(new ImageIcon("images/but1pressed.png"));
    okButton.setBorderPainted(false);
    okButton.setContentAreaFilled(false);
    okButton.setActionCommand("ok");
    okButton.addActionListener(new AddUserDialog_okButton_actionAdapter(this));
    cancelButton.setBounds(154, 411, 98, 44);
    cancelButton.setIcon(new ImageIcon("images/but2.png"));
    cancelButton.setPressedIcon(new ImageIcon("images/but2pressed.png"));
    cancelButton.setBorderPainted(false);
    cancelButton.setContentAreaFilled(false);
    cancelButton.addActionListener(new AddUserDialog_cancelButton_actionAdapter(this));
    panel.add(nameField);
    panel.add(pass1Field);
    panel.add(pass2Field);
    panel.add(levelPanel);
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
    if (e.getActionCommand().equals("name") && pass1Field.getPassword().length < 1) {
      pass1Field.requestFocus();
      return;
    }
    if (pass1Field.getPassword().length < 1) {
      RPCMessage.showMessageDialog(this, "Не указан пароль учетной записи");
      pass1Field.requestFocus();
      return;
    }
    if (pass1Field.getPassword().length < 5) {
      RPCMessage.showMessageDialog(this, "Пароль учетной записи меньше 5 символов");
      pass1Field.requestFocus();
      return;
    }
    if (e.getActionCommand().equals("pass1") && pass2Field.getPassword().length < 1) {
      pass2Field.requestFocus();
      return;
    }
    if (pass2Field.getPassword().length < 1) {
      RPCMessage.showMessageDialog(this, "Не указан пароль учетной записи");
      pass2Field.requestFocus();
      return;
    }
    if (!Arrays.equals(pass1Field.getPassword(), pass2Field.getPassword())) {
      RPCMessage.showMessageDialog(this, "Пароли не совпадают");
      pass1Field.setText("");
      pass2Field.setText("");
      pass1Field.requestFocus();
      return;
    }
    if (e.getActionCommand().equals("ok")) { //по Enter не делаем, только по кнопке
      this.setVisible(false);
      char[] p = pass2Field.getPassword();
      UserProfile newUser = new UserProfile(nameField.getText(), String.valueOf(p).hashCode());
      Arrays.fill(p, (char)0);
      for (int i=0; i<5; i++) {
        if (levelPanel.levelButton[i].isSelected()) {
          newUser.level = (byte)i;
          break;
        }
      }
      newUser.access = levelPanel.accessField.getText();
      newUser.mailList = "";
      new At_net("ADD_USER", new Request(KNC_Terminal.user, new Object[] {newUser}), this).start();
    }
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_ADD_USER")) {
      if (((At_net)e.getSource()).getResult().equals("ALREADY_EXIST")) RPCMessage.showMessageDialog(owner, "Пользователь с таким именем существует");
      if (((At_net)e.getSource()).getResult().equals("ADDERROR")) RPCMessage.showMessageDialog(owner, "Ошибка сервера: невозможно добавить данного пользователя");
      if (((At_net)e.getSource()).getResult().equals("ADDSUCCESS")) RPCMessage.showMessageDialog(owner, "Добавление пользователя выполнено успешно");
      this.dispose();
    }
    if (e.getActionCommand().equals("fail_ADD_USER")) {
      RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
      this.setVisible(true);
    }
  }
}

class AddUserDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  private AddUserDialog adaptee;
  AddUserDialog_okButton_actionAdapter(AddUserDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class AddUserDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  private AddUserDialog adaptee;
  AddUserDialog_cancelButton_actionAdapter(AddUserDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}
