import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

class RPCDialog extends JDialog {
  public enum Result {CANCEL, OK}
  private final String message;
  private final int horizontalAlignment;
  private Result returnVal = Result.CANCEL;

  private JPanel panel = new JPanel();
  private JLabel background = new JLabel();
  private JLabel messageLabel = new JLabel();
  private JScrollPane scrollPane;
  private JButton okButton = new JButton();
  private JButton cancelButton = new JButton();

  private RPCDialog(Window owner, String message, String title, int horizontalAlignment) {
    super(owner, title, ModalityType.DOCUMENT_MODAL);
    this.message = message.replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
    this.horizontalAlignment = horizontalAlignment;
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
    this.setSize(new Dimension(400, 225));
    background.setBounds(0, 0, 400, 225);
    background.setIcon(new ImageIcon("images/dlg.png"));
    messageLabel.setSize(375, 85);
    messageLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
    messageLabel.setForeground(Color.WHITE);
    if (horizontalAlignment == SwingConstants.LEFT) messageLabel.setText("<HTML><TABLE cellpadding=0 width=358><TR><TD ALIGN=LEFT>"+message+"</TD></TR></TABLE></HTML>");
    if (horizontalAlignment == SwingConstants.CENTER) messageLabel.setText("<HTML><TABLE cellpadding=0 width=358><TR><TD ALIGN=CENTER>"+message+"</TD></TR></TABLE></HTML>");
    if (horizontalAlignment == SwingConstants.RIGHT) messageLabel.setText("<HTML><TABLE cellpadding=0 width=358><TR><TD ALIGN=RIGHT>"+message+"</TD></TR></TABLE></HTML>");
    scrollPane = new JScrollPane(messageLabel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBounds(15, 52, 375, 85);
    scrollPane.setBorder(null);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    okButton.setBounds(96, 142, 98, 44);
    okButton.setIcon(new ImageIcon("images/but1.png"));
    okButton.setPressedIcon(new ImageIcon("images/but1pressed.png"));
    okButton.setBorderPainted(false);
    okButton.setContentAreaFilled(false);
    okButton.addActionListener(new RPCDialog_okButton_actionAdapter(this));
    cancelButton.setBounds(206, 143, 98, 44);
    cancelButton.setIcon(new ImageIcon("images/but2.png"));
    cancelButton.setPressedIcon(new ImageIcon("images/but2pressed.png"));
    cancelButton.setBorderPainted(false);
    cancelButton.setContentAreaFilled(false);
    cancelButton.addActionListener(new RPCDialog_cancelButton_actionAdapter(this));
    panel.add(okButton);
    panel.add(cancelButton);
    panel.add(scrollPane);
    panel.add(background);
  }

  public static Result showMessageDialog(Window owner, String message) {
    return showMessageDialog(owner, message, "Вопрос", SwingConstants.CENTER);
  }

  public static Result showMessageDialog(Window owner, String message, String title) {
    return showMessageDialog(owner, message, title, SwingConstants.CENTER);
  }

  public static Result showMessageDialog(Window owner, String message, String title, int horizontalAlignment) {
    RPCDialog rpcDialog = new RPCDialog(owner, message, title, horizontalAlignment);
    rpcDialog.setVisible(true);
    return rpcDialog.returnVal;
  }

  void okButton_actionPerformed(ActionEvent e) {
    returnVal = Result.OK;
    this.dispose();
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    returnVal = Result.CANCEL;
    this.dispose();
  }
}

class RPCDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  private RPCDialog adaptee;
  RPCDialog_okButton_actionAdapter(RPCDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class RPCDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  private RPCDialog adaptee;
  RPCDialog_cancelButton_actionAdapter(RPCDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}
