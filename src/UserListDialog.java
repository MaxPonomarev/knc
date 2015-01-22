

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class UserListDialog extends JDialog implements ActionListener {
  private JPanel panel = new JPanel();
  private LevelPanel levelPanel = new LevelPanel();
  private JLabel background = new JLabel();
  private JComboBox<UserProfile> userComboBox = new JComboBox<>();
  private JButton okButton = new JButton();

  public UserListDialog(Window owner, Dialog.ModalityType modalityType) {
    super(owner, modalityType);
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
    this.setTitle("Список пользователей");
    background.setBounds(0, 0, 300, 395);
    background.setIcon(new ImageIcon("images/chuser.png"));
    userComboBox.setBounds(100, 61, 150, 25);
    userComboBox.setFont(new Font("Dialog", Font.PLAIN, 12));
    userComboBox.setBorder(null);
    userComboBox.addItemListener(new UserListDialog_userComboBox_itemAdapter(this));
    levelPanel.setBounds(25, 125, 244, 165);
    levelPanel.setEnabled(false);
    okButton.setBounds(101, 305, 98, 44);
    okButton.setIcon(new ImageIcon("images/but1.png"));
    okButton.setPressedIcon(new ImageIcon("images/but1pressed.png"));
    okButton.setBorderPainted(false);
    okButton.setContentAreaFilled(false);
    okButton.addActionListener(new UserListDialog_okButton_actionAdapter(this));
    panel.add(userComboBox);
    panel.add(levelPanel);
    panel.add(okButton);
    panel.add(background);
  }

  public void setVisible(boolean b) {
    if (b) {
      userComboBox.removeAllItems();
      userComboBox.addItem(new UserProfile("Получение...",0));
      userComboBox.setEnabled(false);
      new At_net("GET_USERLIST", KNC_Terminal.user, this).start();
    }
    super.setVisible(b);
  }

  void userComboBox_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      levelPanel.levelButton[((UserProfile)e.getItem()).level].setSelected(true);
      levelPanel.accessField.setText(((UserProfile)e.getItem()).access);
    }
  }

  void okButton_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_GET_USERLIST")) {
      Request result = (Request)((At_net)e.getSource()).getResult();
      userComboBox.removeAllItems();
      for (int i=0; i<result.data.length; i++) {
        userComboBox.addItem((UserProfile) result.data[i]);
      }
      userComboBox.setEnabled(true);
    }
    if (e.getActionCommand().equals("fail_GET_USERLIST")) {
      RPCMessage.showMessageDialog(this, "Ошибка соединения с центральным сервером");
      userComboBox.removeAllItems();
      userComboBox.addItem(new UserProfile("Ошибка",0));
      userComboBox.setEnabled(false);
    }
  }
}

class UserListDialog_userComboBox_itemAdapter implements java.awt.event.ItemListener {
  private UserListDialog adaptee;
  UserListDialog_userComboBox_itemAdapter(UserListDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.userComboBox_itemStateChanged(e);
  }
}

class UserListDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  private UserListDialog adaptee;
  UserListDialog_okButton_actionAdapter(UserListDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}
