import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXDatePicker;

class GetIncassDialog extends JDialog implements ActionListener {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JXDatePicker startDatePicker;
  private JXDatePicker endDatePicker;
  private JCheckBox allCheckBox;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  private final Window owner;
  private final KioskProfile[] kiosks;
  private final KNCTableModel tableModel;
  private WaitingDialog waitingDialog;

  public GetIncassDialog(Window owner, Dialog.ModalityType modalityType, KioskProfile[] kiosks, KNCTableModel tableModel) {
    super(owner, modalityType);
    this.owner = owner;
    this.kiosks = kiosks;
    this.tableModel = tableModel;
    try {
      initComponents();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    JLabel topLabel = new JLabel();
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel();
    startDatePicker = new JXDatePicker(new Date());
    JLabel label2 = new JLabel();
    endDatePicker = new JXDatePicker(new Date());
    allCheckBox = new JCheckBox();
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();

    //======== this ========
    setTitle("\u0420\u0435\u0435\u0441\u0442\u0440 \u0438\u043d\u043a\u0430\u0441\u0441\u0430\u0446\u0438\u0439");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setResizable(false);
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 90, 100, 0};
    ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
    ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

    //---- topLabel ----
    topLabel.setText("\u041f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u0440\u0435\u0435\u0441\u0442\u0440 \u0438\u043d\u043a\u0430\u0441\u0441\u0430\u0446\u0438\u0439 \u0434\u043b\u044f \u0443\u043a\u0430\u0437\u0430\u043d\u043d\u044b\u0445 \u043a\u0438\u043e\u0441\u043a\u043e\u0432");
    topLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    contentPane.add(topLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
      new Insets(10, 10, 10, 10), 0, 0));

    //======== panel1 ========
    {
      panel1.setBorder(new EtchedBorder());
      panel1.setLayout(new GridBagLayout());
      ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 135, 0, 135, 0};
      ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {61};
      ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0, 0.0, 1.0E-4};

      //---- label1 ----
      label1.setText("\u0441");
      label1.setFont(new Font("Dialog", Font.BOLD, 12));
      panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
        new Insets(18, 10, 18, 5), 0, 0));

      //---- startDatePicker ----
      startDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
      panel1.add(startDatePicker, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(18, 0, 18, 10), 0, 0));

      //---- label2 ----
      label2.setText("\u043f\u043e");
      label2.setFont(new Font("Dialog", Font.BOLD, 12));
      panel1.add(label2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
        new Insets(18, 10, 18, 5), 0, 0));

      //---- endDatePicker ----
      endDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
      panel1.add(endDatePicker, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(18, 0, 18, 10), 0, 0));
    }
    contentPane.add(panel1, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 10, 0, 10), 0, 0));

    //---- allCheckBox ----
    allCheckBox.setText("\u043f\u043e \u0432\u0441\u0435\u043c \u0442\u0435\u0440\u043c\u0438\u043d\u0430\u043b\u0430\u043c");
    contentPane.add(allCheckBox, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(15, 10, 15, 50), 0, 0));

    //---- okButton ----
    okButton.setText("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        okButtonActionPerformed();
      }
    });
    contentPane.add(okButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(15, 0, 15, 10), 0, 0));

    //---- cancelButton ----
    cancelButton.setText("\u041e\u0442\u043c\u0435\u043d\u0430");
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cancelButtonActionPerformed();
      }
    });
    contentPane.add(cancelButton, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(15, 0, 15, 20), 0, 0));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  private void okButtonActionPerformed() {
    this.setVisible(false);
    Date date1 = startDatePicker.getDate();
    Date date2 = KNC_Terminal.endOfDay(endDatePicker.getDate());
    if (date1.after(date2)) {
      date1 = endDatePicker.getDate();
      date2 = KNC_Terminal.endOfDay(startDatePicker.getDate());
    }
    Object[] objc = new Object[4];
    objc[0] = "GET_DBINCASS";
    objc[2] = date1;
    objc[3] = date2;
    if (!allCheckBox.isSelected()) {
      AdminCommand[] commands = new AdminCommand[kiosks.length];
      for (int i = 0; i < commands.length; i++) {
        commands[i] = new AdminCommand(kiosks[i].getData()[1].toString(), KNC_Terminal.user.name);
        commands[i].setObjectArray(objc);
      }
      new At_net("ADMIN_COMMAND_START", new Request(KNC_Terminal.user, commands), this, 60000).start();
    } else {
      waitingDialog = new WaitingDialog(owner, ModalityType.DOCUMENT_MODAL);
      new At_net("GET_ALLINCASS", new Request(KNC_Terminal.user, objc), this, 60000).start();
      waitingDialog.setVisible(true);
    }
  }

  private void cancelButtonActionPerformed() {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("ret_ADMIN_COMMAND_START")) {
      if (e.getID() == 3) {
        AdminCommand[] commands = (AdminCommand[]) ((Request) ((At_net) e.getSource()).getObject()).data;
        String[] kioskNumbers = new String[commands.length];
        for (int i = 0; i < commands.length; i++) {
          kioskNumbers[i] = commands[i].kiosk_number;
        }
        tableModel.putInternalComments(kioskNumbers, KNC_Terminal.wasSent);
      }
    }
    if (e.getActionCommand().equals("finished_ADMIN_COMMAND_START")) {
      if (((At_net) e.getSource()).getResult().equals("OK")) {
        KNC_Terminal.dataUpdater.wakeup();
        this.dispose();
      }
    }
    if (e.getActionCommand().equals("finished_GET_ALLINCASS")) {
      if (waitingDialog != null) waitingDialog.dispose();
      final Object[] result = (Object[]) ((At_net) e.getSource()).getResult();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          new ResultsFrame(result).setVisible(true);
          GetIncassDialog.this.dispose();
        }
      });
    }
    if (e.getActionCommand().startsWith("fail_")) {
      if (waitingDialog != null) waitingDialog.dispose();
      if (((At_net) e.getSource()).getStatus())
        RPCMessage.showMessageDialog(owner, "Команда отправлена на сервер, но подтверждение не было получено");
      else
        RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
    }
  }
}
