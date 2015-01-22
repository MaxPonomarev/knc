import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import org.jdesktop.swingx.JXDatePicker;

public class ToolBarPanel1 extends ToolBarPanel {
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private SlimButton refreshButton;
  private SlimButton searchButton;
  private SlimButton exportButton;
  private SlimButton saveButton;
  private SlimToggleButton machineButton;
  private SlimToggleButton groupButton;
  private JXDatePicker datePicker;
  private JLabel timeLabel1;
  private JLabel timeLabel2;
  private JSlider timeSlider;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  public static final String BUTTON_MACHINE_ON = "BUTTON_MACHINE_ON";
  public static final String BUTTON_MACHINE_DATE = "BUTTON_MACHINE_DATE";
  public static final String BUTTON_MACHINE_TIME = "BUTTON_MACHINE_TIME";
  public static final String BUTTON_MACHINE_OFF = "BUTTON_MACHINE_OFF";
  public static final String BUTTON_GROUP_ON = "BUTTON_GROUP_ON";
  public static final String BUTTON_GROUP_OFF = "BUTTON_GROUP_OFF";
  private static final ImageIcon machineButtonIcon = new ImageIcon("images/machine.png");
  private static final ImageIcon groupButtonIcon = new ImageIcon("images/group.png");
  private final Date zeroDate = new Date(1308427200000L);
  private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
  private final byte mode; //0 - машина времени отсутствует, 1 - без выбора времени, 2 - все элементы, 3 - вместо машины времени 'группировка'
  private Date date1 = null; //для mode == 1
  private Date date2 = null; //для mode == 2
  private Date machineDate; //дата запрашиваемых суток, например 01.06.2011 0:00:00
  private Date machineTime; //время конкретного среза, например 01.06.2011 12:05:00
  private final GregorianCalendar cal = new GregorianCalendar();
  private boolean sendMachineEvent = false;

  public ToolBarPanel1(byte mode) {
    this.mode = mode;
    try {
      initComponents();
      refreshButton.setIcon(refreshButtonIcon);
      searchButton.setIcon(searchButtonIcon);
      exportButton.setIcon(exportButtonIcon);
      saveButton.setIcon(saveButtonIcon);
      machineButton.setIcon(machineButtonIcon);
      groupButton.setIcon(groupButtonIcon);
      datePicker.setVisible(false);
      timeLabel1.setVisible(false);
      timeLabel2.setVisible(false);
      timeSlider.setVisible(false);
      if (mode > 0 && mode <= 2) groupButton.setVisible(false);
      if (mode == 3) machineButton.setVisible(false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    refreshButton = new SlimButton();
    searchButton = new SlimButton();
    exportButton = new SlimButton();
    saveButton = new SlimButton();
    machineButton = new SlimToggleButton();
    groupButton = new SlimToggleButton();
    datePicker = new JXDatePicker();
    timeLabel1 = new JLabel();
    timeLabel2 = new JLabel();
    timeSlider = new JSlider();

    //======== this ========
    setBorder(new EmptyBorder(0, 5, 0, 5));
    setLayout(new GridBagLayout());
    ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    ((GridBagLayout)getLayout()).rowHeights = new int[] {25, 0};
    ((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
    ((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

    //---- refreshButton ----
    refreshButton.setToolTipText("\u041e\u0431\u043d\u043e\u0432\u0438\u0442\u044c (F5)");
    refreshButton.setPreferredSize(new Dimension(25, 25));
    refreshButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        refreshButtonActionPerformed();
      }
    });
    add(refreshButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //---- searchButton ----
    searchButton.setToolTipText("\u041f\u043e\u0438\u0441\u043a (Ctrl+F)");
    searchButton.setPreferredSize(new Dimension(25, 25));
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        searchButtonActionPerformed();
      }
    });
    add(searchButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //---- exportButton ----
    exportButton.setPreferredSize(new Dimension(25, 25));
    exportButton.setToolTipText("\u042d\u043a\u0441\u043f\u043e\u0440\u0442 \u0432 Excel (Ctrl+E)");
    exportButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        slimButton1ActionPerformed();
      }
    });
    add(exportButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //---- saveButton ----
    saveButton.setPreferredSize(new Dimension(25, 25));
    saveButton.setToolTipText("\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c... (Ctrl+S)");
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveButtonActionPerformed();
      }
    });
    add(saveButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //---- machineButton ----
    machineButton.setToolTipText("\u041c\u0430\u0448\u0438\u043d\u0430 \u0432\u0440\u0435\u043c\u0435\u043d\u0438");
    machineButton.setPreferredSize(new Dimension(25, 25));
    machineButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        machineButtonActionPerformed();
      }
    });
    add(machineButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //---- groupButton ----
    groupButton.setPreferredSize(new Dimension(25, 25));
    groupButton.setToolTipText("\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0430\u044f \u0433\u0440\u0443\u043f\u043f\u0438\u0440\u043e\u0432\u043a\u0430");
    groupButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        groupButtonActionPerformed();
      }
    });
    add(groupButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //---- datePicker ----
    datePicker.setPreferredSize(new Dimension(115, 25));
    datePicker.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent e) {
        datePickerPropertyChange();
      }
    });
    datePicker.setFormats("EEE dd.MM.yyyy","dd.MM.yyyy","dd.MM");
    add(datePicker, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 15, 5, 10), 0, 0));

    //---- timeLabel1 ----
    timeLabel1.setText("24:00");
    timeLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
    timeLabel1.setPreferredSize(new Dimension(35, 25));
    add(timeLabel1, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //---- timeLabel2 ----
    timeLabel2.setText("00:00");
    timeLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
    timeLabel2.setPreferredSize(new Dimension(35, 25));
    add(timeLabel2, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //---- timeSlider ----
    timeSlider.setMaximum(288);
    timeSlider.setValue(0);
    timeSlider.setPreferredSize(new Dimension(600, 33));
    timeSlider.setMinorTickSpacing(6);
    timeSlider.setMajorTickSpacing(12);
    timeSlider.setPaintTicks(true);
    timeSlider.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        timeSliderMousePressed(e);
      }
    });
    timeSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        timeSliderStateChanged();
      }
    });
    add(timeSlider, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 15, 0, 0), 0, 0));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  private void refreshButtonActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_REFRESH));
  }

  private void searchButtonActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_SEARCH));
  }

  private void slimButton1ActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_EXPORT));
  }

  private void saveButtonActionPerformed() {
    if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_SAVE));
  }

  private void machineButtonActionPerformed() {
    timeSlider.setEnabled(false);
    if (machineButton.isSelected()) {
      refreshButton.setEnabled(false);
      date2 = new Date();
      cal.setTime(date2);
      cal.add(Calendar.DATE, -1);
      date1 = cal.getTime();
      //устанавливаем текущую дату, срабатывает datePicker_propertyChange, который в свою очередь
      //меняет положение timeSlider, но если старый date и устанавливаемый одинаковы, то метод
      //datePicker_propertyChange не срабатывает, поэтому тут всё равно устанавливается timeSlider
      if (mode == 2) {
        datePicker.setDate(date2);
      } else if (mode == 1) {
        datePicker.setDate(date1);
      }
      datePicker.setVisible(true);
      changeDate(false);
      if (mode == 1) {
        timeLabel1.setVisible(true);
      }
      if (mode == 2) {
        long diff = System.currentTimeMillis()-datePicker.getDate().getTime();
        if (diff < 86400000) timeSlider.setValue((int)diff/1000/60/5);
        else timeSlider.setValue(288);
        timeLabel2.setVisible(true);
        timeSlider.setVisible(true);
        changeTime(false);
      }
      sendMachineEvent = true;
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_MACHINE_ON));
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_MACHINE_DATE));
    } else {
      refreshButton.setEnabled(true);
      datePicker.setVisible(false);
      timeLabel1.setVisible(false);
      timeLabel2.setVisible(false);
      timeSlider.setVisible(false);
      sendMachineEvent = false;
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_MACHINE_OFF));
    }
  }

  private void groupButtonActionPerformed() {
    if (groupButton.isSelected()) {
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_GROUP_ON));
    } else {
      if (listener != null) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_GROUP_OFF));
    }
  }

  private void datePickerPropertyChange() {
    if (datePicker.getDate() == null) return;
    Date maxDate = date2;
    if (mode == 1) maxDate = date1;
    if (datePicker.getDate().before(zeroDate)) datePicker.setDate(zeroDate);
    else if (datePicker.getDate().after(maxDate)) datePicker.setDate(maxDate);
    else {
      if (mode == 2) {
        changeDate(false);
        long diff = System.currentTimeMillis()-datePicker.getDate().getTime();
        boolean oldMachineEvent = sendMachineEvent;
        sendMachineEvent = false;
        if (diff < 86400000) timeSlider.setValue((int)diff/1000/60/5);
        else timeSlider.setValue(288);
        sendMachineEvent = oldMachineEvent;
        changeTime(false);
      }
      changeDate(true);
    }
  }

  private void timeSliderMousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON3) {
      long diff = System.currentTimeMillis()-datePicker.getDate().getTime();
      if (diff < 86400000) timeSlider.setValue((int)diff/1000/60/5);
    }
  }

  private void timeSliderStateChanged() {
    cal.setTime(datePicker.getDate());
    int minutes = timeSlider.getValue()*5;
    cal.add(Calendar.MINUTE, minutes);
    if (minutes < 1440) timeLabel2.setText(timeFormat.format(cal.getTime()));
    else timeLabel2.setText("24:00");
    if (!timeSlider.getValueIsAdjusting()) {
      changeTime(true);
    }
  }

  public Date getMachineDate() {
    return machineDate;
  }

  public Date getMachineTime() {
    return machineTime;
  }

  public void setLockSlider(boolean b) {
    timeSlider.setEnabled(!b);
  }

  private void changeDate(boolean sendEvent) {
    machineDate = datePicker.getDate();
    if (mode == 1) {
      cal.setTime(machineDate);
      cal.add(Calendar.DATE, 1);
      machineDate = cal.getTime();
    }
    if (listener != null && sendMachineEvent && sendEvent) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_MACHINE_DATE));
  }

  private void changeTime(boolean sendEvent) {
    cal.setTime(machineDate);
    int minutes = timeSlider.getValue()*5;
    cal.add(Calendar.MINUTE, minutes);
    machineTime = cal.getTime();
    if (listener != null && sendMachineEvent && sendEvent) listener.actionPerformed(new ActionEvent(this, 0, BUTTON_MACHINE_TIME));
  }
}
