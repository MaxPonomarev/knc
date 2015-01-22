import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ToolBarPanel2 extends ToolBarPanel {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private SlimButton searchButton;
  private SlimButton exportButton;
  private SlimButton saveButton;
  private SlimButton saveAllButton;
  SlimButton summaryButton;
  private JComboBox<String> filterComboBox;
  private JTextField filterTextField;
  private SlimButton applyFilterButton;
  private SlimButton clearFilterButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  public static final String BUTTON_SAVE_ALL = "BUTTON_SAVE_ALL";
  public static final String BUTTON_SUMMARY = "BUTTON_SUMMARY";
  public static final String FILTER_INCLUDE = "TFILTER_INCLUDE_";
  public static final String FILTER_EXCLUDE = "TFILTER_EXCLUDE_";
  public static final String FILTER_HIGHLIGHT = "TFILTER_HIGHLIGHT_";
  public static final String FILTER_CANCEL = "TFILTER_CANCEL";
  private static final ImageIcon saveAllButtonIcon = new ImageIcon("images/saveall.png");
  private final byte mode; //0 - для реестров Z-отчетов, 1 - для реестров платежей и инкассаций

  public ToolBarPanel2(byte mode) {
    this.mode = mode;
    try {
      initComponents();
      searchButton.setIcon(searchButtonIcon);
      exportButton.setIcon(exportButtonIcon);
      saveButton.setIcon(saveButtonIcon);
      saveAllButton.setIcon(saveAllButtonIcon);
      if (mode == 0) {
        saveAllButton.setVisible(false);
        summaryButton.setVisible(false);
        filterComboBox.setVisible(false);
        filterTextField.setVisible(false);
        applyFilterButton.setVisible(false);
        clearFilterButton.setVisible(false);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    searchButton = new SlimButton();
    exportButton = new SlimButton();
    saveButton = new SlimButton();
    saveAllButton = new SlimButton();
    summaryButton = new SlimButton();
    filterComboBox = new JComboBox<>();
    filterTextField = new JTextField();
    applyFilterButton = new SlimButton();
    clearFilterButton = new SlimButton();

    //======== this ========
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setLayout(new GridBagLayout());
    ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    ((GridBagLayout)getLayout()).rowHeights = new int[] {25, 0};
    ((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};
    ((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

    //---- searchButton ----
    searchButton.setToolTipText("\u041f\u043e\u0438\u0441\u043a (Ctrl+F)");
    searchButton.setPreferredSize(new Dimension(25, 25));
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        searchButtonActionPerformed();
      }
    });
    add(searchButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));

    //---- exportButton ----
    exportButton.setPreferredSize(new Dimension(25, 25));
    exportButton.setToolTipText("\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u0432 Excel (Ctrl+E)");
    exportButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        exportButtonActionPerformed();
      }
    });
    add(exportButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));

    //---- saveButton ----
    saveButton.setPreferredSize(new Dimension(25, 25));
    saveButton.setToolTipText("\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c... (Ctrl+S)");
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveButtonActionPerformed();
      }
    });
    add(saveButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));

    //---- saveAllButton ----
    saveAllButton.setToolTipText("\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0432\u0441\u0435 \u0440\u0435\u0435\u0441\u0442\u0440\u044b... (Ctrl+Shift+A)");
    saveAllButton.setPreferredSize(new Dimension(25, 25));
    saveAllButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveAllButtonActionPerformed();
      }
    });
    add(saveAllButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));

    //---- summaryButton ----
    summaryButton.setText("\u0421\u0432\u043e\u0434\u043a\u0430");
    summaryButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        summaryButtonActionPerformed();
      }
    });
    add(summaryButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));

    //---- filterComboBox ----
    filterComboBox.setBorder(null);
    filterComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
      "\u0444\u0438\u043b\u044c\u0442\u0440:",
      "\u0432\u043a\u043b\u044e\u0447\u0438\u0442\u044c",
      "\u0438\u0441\u043a\u043b\u044e\u0447\u0438\u0442\u044c",
      "\u0432\u044b\u0434\u0435\u043b\u0438\u0442\u044c"
    }));
    filterComboBox.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        ToolBarPanel2.this.keyPressed(e);
      }
    });
    add(filterComboBox, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 10, 0, 0), 0, 0));

    //---- filterTextField ----
    filterTextField.setFont(new Font("Dialog", Font.PLAIN, 12));
    filterTextField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        ToolBarPanel2.this.keyPressed(e);
      }
    });
    add(filterTextField, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 10, 0, 10), 0, 0));

    //---- applyFilterButton ----
    applyFilterButton.setText("\u041f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c");
    applyFilterButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        applyFilterButtonActionPerformed();
      }
    });
    add(applyFilterButton, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));

    //---- clearFilterButton ----
    clearFilterButton.setText("\u041e\u0447\u0438\u0441\u0442\u0438\u0442\u044c");
    clearFilterButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        clearFilterButtonActionPerformed();
      }
    });
    add(clearFilterButton, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  public void setFilterMode(boolean b) {
    if (mode > 0) {
      saveAllButton.setVisible(!b);
      summaryButton.setVisible(!b);
      filterComboBox.setVisible(b);
      filterTextField.setVisible(b);
      applyFilterButton.setVisible(b);
      clearFilterButton.setVisible(b);
    }
  }

  static public String getFilterFromCommand(String command) {
    if (command.startsWith(FILTER_INCLUDE)) {
      return command.replaceAll(FILTER_INCLUDE, "");
    } else if (command.startsWith(FILTER_EXCLUDE)) {
      return command.replaceAll(FILTER_EXCLUDE, "");
    } else if (command.startsWith(FILTER_HIGHLIGHT)) {
      return command.replaceAll(FILTER_HIGHLIGHT, "");
    } else return null;
  }

  private void searchButtonActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_SEARCH));
  }

  private void exportButtonActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_EXPORT));
  }

  private void saveButtonActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_SAVE));
  }

  private void saveAllButtonActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_SAVE_ALL));
  }

  private void summaryButtonActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_SUMMARY));
  }

  private void keyPressed(KeyEvent e) {
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
      searchButtonActionPerformed();
    }
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E) {
      exportButtonActionPerformed();
    }
    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
      saveButtonActionPerformed();
    }
    if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_A) {
      saveAllButtonActionPerformed();
    }
  }

  private void applyFilterButtonActionPerformed() {
    int index = filterComboBox.getSelectedIndex();
    if (index == 0) return;
    String str = filterTextField.getText();
    if (str.length() == 0) return;
    if (index == 1) {
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, FILTER_INCLUDE+str));
    } else if (index == 2) {
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, FILTER_EXCLUDE+str));
    } else if (index == 3) {
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, FILTER_HIGHLIGHT+str));
    }
  }

  private void clearFilterButtonActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, FILTER_CANCEL));
  }
}
