import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXDatePicker;

public class ToolBarPanel3 extends ToolBarPanel {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private SlimButton searchButton;
  private SlimButton exportButton;
  private SlimButton saveButton;
  private SlimToggleButton groupButton;
  JXDatePicker startDatePicker;
  JXDatePicker endDatePicker;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  public static final String BUTTON_GET = "BUTTON_GET";
  public static final String BUTTON_GROUP_ON = "BUTTON_GROUP_ON";
  public static final String BUTTON_GROUP_OFF = "BUTTON_GROUP_OFF";
  private static final ImageIcon groupButtonIcon = new ImageIcon("images/group.png");
  private static final Date zeroDate = new Date(1308427200000L);
  private Date date = null;

  public ToolBarPanel3() {
    try {
      GregorianCalendar cal = new GregorianCalendar();
      cal.add(Calendar.DATE, -1);
      date = cal.getTime();
      initComponents();
      searchButton.setIcon(searchButtonIcon);
      exportButton.setIcon(exportButtonIcon);
      saveButton.setIcon(saveButtonIcon);
      groupButton.setIcon(groupButtonIcon);
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
    groupButton = new SlimToggleButton();
    JLabel startLabel = new JLabel();
    startDatePicker = new JXDatePicker();
    JLabel endLabel = new JLabel();
    endDatePicker = new JXDatePicker();
    SlimButton getButton = new SlimButton();

    //======== this ========
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setLayout(new GridBagLayout());
    ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    ((GridBagLayout)getLayout()).rowHeights = new int[] {25, 0};
    ((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
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

    //---- groupButton ----
    groupButton.setToolTipText("\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0430\u044f \u0433\u0440\u0443\u043f\u043f\u0438\u0440\u043e\u0432\u043a\u0430");
    groupButton.setPreferredSize(new Dimension(25, 25));
    groupButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        groupButtonActionPerformed();
        groupButtonActionPerformed();
      }
    });
    add(groupButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));

    //---- startLabel ----
    startLabel.setText("\u0441");
    startLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    add(startLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 20, 0, 5), 0, 0));

    //---- startDatePicker ----
    startDatePicker.setPreferredSize(new Dimension(115, 25));
    startDatePicker.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent e) {
        startDatePickerPropertyChange();
      }
    });
    startDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
    startDatePicker.getComponent(0).setFocusable(false);
    startDatePicker.setDate(date);
    add(startDatePicker, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));

    //---- endLabel ----
    endLabel.setText("\u043f\u043e");
    endLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    add(endLabel, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 10, 0, 5), 0, 0));

    //---- endDatePicker ----
    endDatePicker.setPreferredSize(new Dimension(115, 25));
    endDatePicker.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent e) {
        endDatePickerPropertyChange();
      }
    });
    endDatePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
    endDatePicker.getComponent(0).setFocusable(false);
    endDatePicker.setDate(date);
    add(endDatePicker, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));

    //---- getButton ----
    getButton.setText("\u041f\u043e\u043b\u0443\u0447\u0438\u0442\u044c");
    getButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getButtonActionPerformed();
      }
    });
    add(getButton, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 25, 0, 0), 0, 0));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
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

  private void groupButtonActionPerformed() {
    if (groupButton.isSelected()) {
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_GROUP_ON));
    } else {
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_GROUP_OFF));
    }
  }

  private void startDatePickerPropertyChange() {
    if (startDatePicker.getDate() == null) return;
    if (startDatePicker.getDate().before(zeroDate)) startDatePicker.setDate(zeroDate);
    else if (startDatePicker.getDate().after(date)) startDatePicker.setDate(date);
  }

  private void endDatePickerPropertyChange() {
    if (endDatePicker.getDate() == null) return;
    if (endDatePicker.getDate().before(zeroDate)) endDatePicker.setDate(zeroDate);
    else if (endDatePicker.getDate().after(date)) endDatePicker.setDate(date);
  }

  private void getButtonActionPerformed() {
    Date date1 = startDatePicker.getDate();
    Date date2 = endDatePicker.getDate();
    if (date1.after(date2)) {
      endDatePicker.setDate(date1);
      startDatePicker.setDate(date2);
    }
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_GET));
  }
}
