import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

class AuthDialog extends JDialog {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JTextField nameField;
  private JPasswordField passField;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  private String name;
  private int pass;
  private int passStrength;

  public AuthDialog(Window owner, Dialog.ModalityType modalityType, String login) {
    super(owner, modalityType);
    name = login;
    try {
      initComponents();
      nameField.setText(name);
    }
    catch(Exception ex) {
      ex.printStackTrace();
      System.exit(-1);
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    JPanel panel = new JPanel();
    nameField = new JTextField();
    passField = new JPasswordField();
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();
    JLabel background = new JLabel();

    //======== this ========
    setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.283), (int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.273));
    setResizable(false);
    setTitle("\u0410\u0432\u0442\u043e\u0440\u0438\u0437\u0430\u0446\u0438\u044f");
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        thisComponentShown(e);
      }
    });
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== panel ========
    {
      panel.setLayout(null);

      //---- nameField ----
      nameField.setFont(new Font("Dialog", Font.PLAIN, 12));
      nameField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          okButtonActionPerformed(e);
        }
      });
      nameField.setActionCommand("name");
      panel.add(nameField);
      nameField.setBounds(92, 53, 130, 25);

      //---- passField ----
      passField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          okButtonActionPerformed(e);
        }
      });
      panel.add(passField);
      passField.setBounds(92, 90, 130, 25);

      //---- okButton ----
      okButton.setBorderPainted(false);
      okButton.setContentAreaFilled(false);
      okButton.setIcon(new ImageIcon("images/but1.png"));
      okButton.setPressedIcon(new ImageIcon("images/but1pressed.png"));
      okButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          okButtonActionPerformed(e);
        }
      });
      panel.add(okButton);
      okButton.setBounds(15, 127, 98, 44);

      //---- cancelButton ----
      cancelButton.setBorderPainted(false);
      cancelButton.setContentAreaFilled(false);
      cancelButton.setIcon(new ImageIcon("images/but2.png"));
      cancelButton.setPressedIcon(new ImageIcon("images/but2pressed.png"));
      cancelButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          cancelButtonActionPerformed(e);
        }
      });
      panel.add(cancelButton);
      cancelButton.setBounds(125, 128, 98, 44);

      //---- background ----
      background.setIcon(new ImageIcon("images/auth.png"));
      panel.add(background);
      background.setBounds(0, 0, 240, 215);

      { // compute preferred size
        Dimension preferredSize = new Dimension();
        for(int i = 0; i < panel.getComponentCount(); i++) {
          Rectangle bounds = panel.getComponent(i).getBounds();
          preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
          preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
        }
        Insets insets = panel.getInsets();
        preferredSize.width += insets.right;
        preferredSize.height += insets.bottom;
        panel.setMinimumSize(preferredSize);
        panel.setPreferredSize(preferredSize);
      }
    }
    contentPane.add(panel);
    setSize(240, 215);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  public String getUserName() {
    return name;
  }

  public int getUserPass() {
    return pass;
  }

  public int getPassStrength() {
    return passStrength;
  }

  public void thisComponentShown(ComponentEvent e) {
    if (!nameField.getText().equals("")) passField.requestFocus();
  }

  void okButtonActionPerformed(ActionEvent e) {
    if (nameField.getText().length() < 1) {
      RPCMessage.showMessageDialog(this, "Не указано имя для доступа к серверу");
      nameField.requestFocus();
      return;
    }
    if (e.getActionCommand().equals("name") && passField.getPassword().length < 1) {
      passField.requestFocus();
      return;
    }
    if (passField.getPassword().length < 1) {
      RPCMessage.showMessageDialog(this, "Не указан пароль для доступа к серверу");
      passField.requestFocus();
      return;
    }
    passStrength = PasswordChecker.getStrength(passField.getPassword());
    name = nameField.getText();
    char[] p = passField.getPassword();
    pass = String.valueOf(p).hashCode();
    Arrays.fill(p, (char)0);
    this.setVisible(false);
  }

  void cancelButtonActionPerformed(ActionEvent e) {
    System.exit(0);
  }
}
