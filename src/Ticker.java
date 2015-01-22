

import java.awt.event.*;

class Ticker extends Thread implements ActionListener {

  public Ticker() {
    this.setPriority(Thread.MIN_PRIORITY);
    this.setName("Ticker");
  }

  public void run() {
    while (true) {
      try {
        sleep(KNC_Terminal.tickerDelay);
      } catch (InterruptedException ex) {
        return;
      }
      new At_net("TICK", KNC_Terminal.user, this).start();
      if (isInterrupted()) return;
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_TICK")) {
      String result = ((At_net) e.getSource()).getResult().toString();
      if (result.equals("DESINTEGRATED")) {
        this.interrupt();
        if (KNC_Terminal.dataUpdater != null) KNC_Terminal.dataUpdater.interrupt();
        RPCMessage.showMessageDialog(KNC_Terminal.frame, "—есси€ прервана, зайдите в терминал снова");
      }
    }
  }
}
