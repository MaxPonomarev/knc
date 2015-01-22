import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXDatePicker;

class GetLogDialog2 extends JDialog implements ActionListener {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JXDatePicker startDatePicker;
  private JXDatePicker endDatePicker;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  private final Window owner;

  public GetLogDialog2(Window owner, Dialog.ModalityType modalityType) {
    super(owner, modalityType);
    this.owner = owner;
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
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();

    //======== this ========
    setTitle("\u041b\u043e\u0433-\u0444\u0430\u0439\u043b \u0441\u0435\u0440\u0432\u0435\u0440\u0430");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setResizable(false);
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 90, 100, 0};
    ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
    ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

    //---- topLabel ----
    topLabel.setText("\u041f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u043b\u043e\u0433-\u0444\u0430\u0439\u043b \u0441\u0435\u0440\u0432\u0435\u0440\u0430");
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
    objc[0] = "GET_LOG";
    objc[2] = date1;
    objc[3] = date2;
    AdminCommand command = new AdminCommand("0", KNC_Terminal.user.name);
    command.setObjectArray(objc);
    new At_net("GET_LOG", new Request(KNC_Terminal.user, new Object[]{command}), this, 40000).start();
  }

  private void cancelButtonActionPerformed() {
    this.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("finished_GET_LOG")) {
      Request result = (Request) ((At_net) e.getSource()).getResult();
      ArrayList<AdminCommand> results = new ArrayList<>();
      results.add((AdminCommand) result.data[0]);
      new ResultsFrame(results).setVisible(true);
      this.dispose();
    }
    if (e.getActionCommand().equals("fail_GET_LOG")) {
      RPCMessage.showMessageDialog(owner, "Ошибка соединения с центральным сервером");
      this.setVisible(true);
    }
  }
}
