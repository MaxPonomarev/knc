

import java.awt.*;
import javax.swing.*;

class RPCProgress extends JDialog {
  private JPanel panel = new JPanel();
  private JLabel background = new JLabel();
  private JProgressBar progressBar = new JProgressBar(0, 100);

  public RPCProgress(Window owner, String title, Dialog.ModalityType modalityType) {
    super(owner, title, modalityType);
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() {
    panel.setLayout(null);
    getContentPane().add(panel);
    this.setResizable(false);
    this.setBounds(400, 240, 240, 131);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    background.setBounds(0, 0, 240, 131);
    background.setIcon(new ImageIcon("images/progress.png"));
    progressBar.setBounds(20, 55, 200, 30);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);
    panel.add(progressBar);
    panel.add(background);
  }

  public int getProgressValue() {
    return progressBar.getValue();
  }

  public void setProgressValue(int current) {
    progressBar.setValue(current);
  }
}
