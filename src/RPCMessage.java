

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class RPCMessage extends JDialog {
  private final String message;
  private final int horizontalAlignment;
  private final boolean canClosed;
  private JPanel panel = new JPanel();
  private JLabel background = new JLabel();
  private JLabel messageLabel = new JLabel();
  private JScrollPane scrollPane;
  private JButton okButton = new JButton();

  private RPCMessage(Window owner, String message, String title, int horizontalAlignment, boolean canClosed) {
    super(owner, title, ModalityType.DOCUMENT_MODAL);
    this.message = message.replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
    this.horizontalAlignment = horizontalAlignment;
    this.canClosed = canClosed;
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
    if (canClosed) this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    else {
      this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      this.setModalityType(ModalityType.MODELESS);
    }
    this.setResizable(false);
    this.setSize(new Dimension(350, 200));
    background.setBounds(0, 0, 350, 200);
    background.setIcon(new ImageIcon("images/msg.png"));
    messageLabel.setSize(325, 65);
    messageLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
    messageLabel.setForeground(Color.WHITE);
    if (horizontalAlignment == SwingConstants.LEFT) messageLabel.setText("<HTML><TABLE cellpadding=0 width=308><TR><TD ALIGN=LEFT>"+message+"</TD></TR></TABLE></HTML>");
    if (horizontalAlignment == SwingConstants.CENTER) messageLabel.setText("<HTML><TABLE cellpadding=0 width=308><TR><TD ALIGN=CENTER>"+message+"</TD></TR></TABLE></HTML>");
    if (horizontalAlignment == SwingConstants.RIGHT) messageLabel.setText("<HTML><TABLE cellpadding=0 width=308><TR><TD ALIGN=RIGHT>"+message+"</TD></TR></TABLE></HTML>");
    scrollPane = new JScrollPane(messageLabel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBounds(15, 50, 325, 65);
    scrollPane.setBorder(null);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    okButton.setBounds(126, 120, 98, 44);
    okButton.setIcon(new ImageIcon("images/but1.png"));
    okButton.setPressedIcon(new ImageIcon("images/but1pressed.png"));
    okButton.setBorderPainted(false);
    okButton.setContentAreaFilled(false);
    okButton.addKeyListener(new RPCMessage_okButton_keyAdapter(this));
    okButton.addActionListener(new RPCMessage_okButton_actionAdapter(this));
    if (canClosed) panel.add(okButton);
    panel.add(scrollPane);
    panel.add(background);
  }

  public static RPCMessage showMessage(Frame frame, String message, String title, int horizontalAlignment) {
    RPCMessage rpcMessage = new RPCMessage(frame, message, title, horizontalAlignment, false);
    rpcMessage.setVisible(true);
    return rpcMessage;
  }

  public static void showMessageDialog(Window owner, String message) {
    showMessageDialog(owner, message, "Сообщение", SwingConstants.CENTER);
  }

  public static void showMessageDialog(Window owner, String message, String title) {
    showMessageDialog(owner, message, title, SwingConstants.CENTER);
  }

  public static void showMessageDialog(Window owner, String message, String title, int horizontalAlignment) {
    RPCMessage rpcMessage = new RPCMessage(owner, message, title, horizontalAlignment, true);
    rpcMessage.setVisible(true);
    rpcMessage.dispose();
  }

  public void okButton_keyPressed(KeyEvent e) {
    if (e.getKeyCode() == 10) okButton.doClick();
  }

  void okButton_actionPerformed(ActionEvent e) {
    this.dispose();
  }
}

class RPCMessage_okButton_keyAdapter extends KeyAdapter {
  private RPCMessage adaptee;
  RPCMessage_okButton_keyAdapter(RPCMessage adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
    adaptee.okButton_keyPressed(e);
  }
}

class RPCMessage_okButton_actionAdapter implements java.awt.event.ActionListener {
  private RPCMessage adaptee;
  RPCMessage_okButton_actionAdapter(RPCMessage adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}
