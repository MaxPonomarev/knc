import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ChUserDialog extends JDialog implements ActionListener {
  private final Window owner;
  private JPanel panel = new JPanel();
  private LevelPanel levelPanel = new LevelPanel();
  private JLabel background = new JLabel();
  private JTextField nameField = new JTextField();
  private JButton okButton = new JButton();
  private JButton cancelButton = new JButton();

  public ChUserDialog(Window owner, Dialog.ModalityType modalityType) {
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
    this.setSize(new Dimension(300, 395));
    this.setTitle("Изменить права пользователя");
    background.setBounds(0, 0, 300, 395);
    background.setIcon(new ImageIcon("images/chuser.png"));
    nameField.setBounds(100, 61, 150, 25);
    nameField.setFont(new Font("Dialog", Font.PLAIN, 12));
    levelPanel.setBounds(25, 125, 244, 165);
    if (KNC_Terminal.user.level < 4) {
      levelPanel.levelButton[4].setEnabled(false);
      levelPanel.accessField.setEditable(false);
    }
    {levelPanel.accessField.setText(KNC_Terminal.user.access);}
    okButton.setBounds(48, 305, 98, 44);
    okButton.setIcon(new ImageIcon("images/but1.png"));
    okButton.setPressedIcon(new ImageIcon("images/but1pressed.png"));
    okButton.setBorderPainted(false);
    okButton.setContentAreaFilled(false);
    okButton.addActionListener(new ChUserDialog_okButton_actionAdapter(this));
    cancelButton.setBounds(154, 306, 98, 44);
    cancelButton.setIcon(new ImageIcon("images/but2.png"));
    cancelButton.setPressedIcon(new ImageIcon("images/but2pressed.png"));
    cancelButton.setBorderPainted(false);
    cancelButton.setContentAreaFilled(false);
    cancelButton.addActionListener(new ChUserDialog_cancelButton_actionAdapter(this));
    panel.add(nameField);
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
    this.setVisible(false);
    UserProfile newUser = new UserProfile(nameField.getText(), 0);
    for (int i=0; i<5; i++) {
      if (levelPanel.levelButton[i].isSelected()) {
        newUser.level = (byte)i;
        break;
      }
    }
    newUser.access = levelPanel.accessField.getText();
    /*/для добавления прав пока нет интерфейса
    newUser.companies = new TreeMap();
    newUser.companies.put(new Long(2308081782L), "ООО РПЦ");
    //newUser.companies.put(new Long(2312175338L), "ООО ДСН");
    //newUser.companies.put(new Long(230804494851L), "ИП Маркова Е.А.");
    //newUser.companies.put(new Long(2320140000L), "ООО Единая Платежная Система");
    /*/
    new At_net("SET_RIGHTS", new Request(KNC_Terminal.user, new Object[] {newUser}), this).start();
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_SET_RIGHTS")) {
      if (((At_net)e.getSource()).getResult().equals("NOTFOUND")) RPCMessage.showMessageDialog(owner, "Пользователь с таким именем не найден");
      if (((At_net)e.getSource()).getResult().equals("SETERROR")) RPCMessage.showMessageDialog(owner, "Ошибка сервера: невозможно изменить права данного пользователя");
      if (((At_net)e.getSource()).getResult().equals("SETDENIED")) RPCMessage.showMessageDialog(owner, "Вы не можете изменить права этому пользователю");
      if (((At_net)e.getSource()).getResult().equals("SETSUCCESS")) RPCMessage.showMessageDialog(owner, "Изменение прав выполнено успешно");
      this.dispose();
    }
    if (e.getActionCommand().equals("fail_SET_RIGHTS")) {
      RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
      this.setVisible(true);
    }
  }
}

class ChUserDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  private ChUserDialog adaptee;
  ChUserDialog_okButton_actionAdapter(ChUserDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class ChUserDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  private ChUserDialog adaptee;
  ChUserDialog_cancelButton_actionAdapter(ChUserDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}
