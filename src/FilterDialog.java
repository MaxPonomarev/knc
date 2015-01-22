import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FilterDialog extends JDialog {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JComboBox<String> filterComboBox;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  private enum Mode {SAVE, DEL, ADD}
  private final Mode mode;
  private String returnVal = null;

  private FilterDialog(Window owner, Mode mode) {
    super(owner, ModalityType.DOCUMENT_MODAL);
    this.mode = mode;
    String[] filters;
    if (KNC_Terminal.user.filters != null) {
      filters = new String[KNC_Terminal.user.filters.size()];
      filters = KNC_Terminal.user.filters.keySet().toArray(filters);
    } else {
      filters = new String[0];
    }
    try {
      initComponents(filters);
      setMinimumSize(getSize());
      switch (mode) {
        case SAVE:
          setTitle("Сохранить фильтр");
          filterComboBox.setEditable(true);
          filterComboBox.setSelectedItem("");
          break;
        case DEL:
          setTitle("Удалить фильтр");
          break;
        case ADD:
          setTitle("Добавить в фильтр");
          break;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void initComponents(String[] filters) {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel();
    filterComboBox = new JComboBox<>();
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();

    //======== this ========
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 115, 115, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0, 1.0E-4};
    ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

    //======== panel1 ========
    {
      panel1.setLayout(new GridBagLayout());
      ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 150, 0};
      ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0};
      ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
      ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

      //---- label1 ----
      label1.setText("\u041d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 \u0444\u0438\u043b\u044c\u0442\u0440\u0430:");
      label1.setFont(new Font("Dialog", Font.BOLD, 12));
      panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 5), 0, 0));

      //---- filterComboBox ----
      filterComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
      filterComboBox.setBorder(null);
      filterComboBox.setModel(new DefaultComboBoxModel<>(filters));
      panel1.add(filterComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    }
    contentPane.add(panel1, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(10, 10, 10, 10), 0, 0));

    //---- okButton ----
    okButton.setText("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        okButtonActionPerformed();
      }
    });
    contentPane.add(okButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 10, 15), 0, 0));

    //---- cancelButton ----
    cancelButton.setText("\u041e\u0442\u043c\u0435\u043d\u0430");
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cancelButtonActionPerformed();
      }
    });
    contentPane.add(cancelButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 15, 10, 0), 0, 0));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  public static String showSaveDialog(Frame frame) {
    FilterDialog saveFilterDialog = new FilterDialog(frame, Mode.SAVE);
    saveFilterDialog.setVisible(true);
    return saveFilterDialog.returnVal;
  }

  public static String showDelDialog(Frame frame) {
    FilterDialog delFilterDialog = new FilterDialog(frame, Mode.DEL);
    delFilterDialog.setVisible(true);
    return delFilterDialog.returnVal;
  }

  public static String showAddDialog(Frame frame) {
    FilterDialog addFilterDialog = new FilterDialog(frame, Mode.ADD);
    addFilterDialog.setVisible(true);
    return addFilterDialog.returnVal;
  }

  private void okButtonActionPerformed() {
    String candidate = (String)filterComboBox.getSelectedItem();
    if (mode == Mode.SAVE) {
      if (candidate.length() == 0) {
        RPCMessage.showMessageDialog(this, "Необходимо указать название фильтра");
        filterComboBox.requestFocus();
        return;
      }
      if (candidate.length() > 30) {
        RPCMessage.showMessageDialog(this, "Название фильтра не может быть больше 30 символов");
        filterComboBox.requestFocus();
        return;
      }
      if (KNC_Terminal.user.filters != null && KNC_Terminal.user.filters.containsKey(candidate)) {
        if (RPCDialog.showMessageDialog(this, "Такое название уже существует, заменить?") == RPCDialog.Result.CANCEL) {
          return;
        }
      }
    }
    returnVal = candidate;
    this.dispose();
  }

  private void cancelButtonActionPerformed() {
    returnVal = null;
    this.dispose();
  }
}
