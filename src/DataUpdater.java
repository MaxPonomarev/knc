import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

class DataUpdater extends Thread implements ActionListener {
  private static final int RESPONSE = 2000; //допустимое время отклика потока в мс
  private final KNCTableModel model;
  private final KNCTableRenderer4 renderer4;
  private final File resFile;
  private boolean waking = false;
  private boolean idle = true;
  private boolean machineMode = false;
  private boolean waitReport = false;

  public DataUpdater(KNCTableModel model, KNCTableRenderer4 renderer4) {
    this.model = model;
    this.renderer4 = renderer4;
    resFile = new File(KNC_Terminal.userHome, "results.dat");
    this.setPriority(Thread.MIN_PRIORITY);
    this.setName("Data updater");
  }

  public void wakeup() {
    waking = true;
  }

  public void setIdle(boolean b) {
    idle = b;
  }

  public void setMachineMode(boolean b) {
    idle = b;
    machineMode = b;
  }

  public void setWaitReport(boolean b) {
    waitReport = b;
  }

  private void pause(int delay) throws InterruptedException {
    int ticks = 0;
    delay = delay / RESPONSE;
    while ((ticks++ < delay) && !waking && !isInterrupted()) {
      sleep(RESPONSE);
    }
    waking = false;
  }

  public void run() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(resFile))) {
      @SuppressWarnings("unchecked")
      HashMap<String, ArrayList<AdminCommand>> results = (HashMap<String, ArrayList<AdminCommand>>) in.readObject();
      model.setResults(results);
    } catch (Exception ignored) {
    }
    while (true) {
      if (isInterrupted()) return;
      if (!idle) {
        KNC_Terminal.frame.updatingKioskListStart();
        new At_net("GET_KIOSKLIST", KNC_Terminal.user, this).start();
        try {sleep(5000);}catch (InterruptedException ignored) {}
      } else {
        new At_net("KIOSKNET_STATISTIC", KNC_Terminal.user, this).start();
        try {sleep(5000);}catch (InterruptedException ignored) {}
      }
      if (waitReport) {
        new At_net("CHECK_REPORT", KNC_Terminal.user, this).start();
      }
      try {pause(KNC_Terminal.delay);}catch (InterruptedException ex) {return;}
    }
  }

  public synchronized void saveResults() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(resFile))) {
      HashMap<String, ArrayList<AdminCommand>> res = model.getResults();
      out.writeObject(res);
    } catch (Exception ignored) {
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_GET_KIOSKLIST")) {
      Request result = (Request) ((At_net) e.getSource()).getResult();
      @SuppressWarnings("unchecked")
      final ArrayList<KioskProfile> kiosks = (ArrayList<KioskProfile>) result.data[0];
      @SuppressWarnings("unchecked")
      final ArrayList<AdminCommand> results = (ArrayList<AdminCommand>) result.data[1];
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          KNC_Terminal.frame.updateAutoFilters(kiosks);
          if (!machineMode) {
            renderer4.setMachineMode(false);
            model.putKiosks(kiosks, results);
          }
          KNC_Terminal.frame.updatingKioskListFinish();
          new At_net("KIOSKNET_STATISTIC", KNC_Terminal.user, DataUpdater.this).start();
          if (results.size() > 0) saveResults();
        }
      });
    }
    if (e.getActionCommand().equals("fail_GET_KIOSKLIST")) {
      KNC_Terminal.frame.updatingKioskListFinish();
    }
    if (e.getActionCommand().equals("finished_KIOSKNET_STATISTIC")) {
      KioskNetProfile kioskNetProfile = (KioskNetProfile) ((At_net) e.getSource()).getResult();
      KNC_Terminal.frame.update(kioskNetProfile);
    }
    if (e.getActionCommand().equals("finished_CHECK_REPORT")) {
      Object[] result = (Object[]) ((At_net) e.getSource()).getResult();
      if (result != null) {
        setWaitReport(false);
        KNC_Terminal.frame.updateReportFrame(result);
      }
    }
  }

  public void interrupt() {
    super.interrupt();
    KNC_Terminal.frame.interrupt();
  }
}
