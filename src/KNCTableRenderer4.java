import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import org.jdesktop.swingx.renderer.*;

class KNCTableRenderer4 implements TableCellRenderer {
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private boolean machineMode = false;
  private long machineTime;

  public KNCTableRenderer4(JTable table) {
    TableColumnModel columnModel = table.getColumnModel();
    columnModel.getColumn(KNCTableModel.COLUMN_LASTOPER).setCellRenderer(this);
  }

  public void setMachineMode(boolean b) {
    machineMode = b;
  }

  public void setMachineTime(long time) {
    machineTime = time;
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    TableCellRenderer defaultTableCellRenderer = new DefaultTableRenderer();
    Component component = defaultTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (component instanceof JRendererLabel) {
      if (value != null && (value instanceof String)) {
        JRendererLabel label = (JRendererLabel) component;
        try {
          boolean yellowFlag = false;
          boolean redFlag = false;
          Date val = dateFormat.parse((String) value);
          int dh;
          if (machineMode) dh = (int) ((machineTime - val.getTime()) / 1000 / 60 / 60);//разница в полных часах
          else dh = (int) ((new Date().getTime() - val.getTime()) / 1000 / 60 / 60);//разница в полных часах
          if (dh < 0) dh = 0;
          if (dh >= 24) {
            redFlag = true;
          } else {
            GregorianCalendar last = new GregorianCalendar();
            last.setTime(val);
            GregorianCalendar now = new GregorianCalendar();
            int lastHour = last.get(Calendar.HOUR_OF_DAY);
            int nowHour = now.get(Calendar.HOUR_OF_DAY);
            if (lastHour >= 15 && dh >= 15 && nowHour >= 15) {
              redFlag = true;
            } else if (lastHour >= 15 && dh >= 13 && nowHour >= 13) {
              yellowFlag = true;
            }
            if (lastHour <= 8 && ((dh >= 6 && nowHour >= 15) || dh >= 15)) {//dh >= 15 добавил, т.к. после 0 часов nowHour >= 15 не выполняется. Было так: if (lastHour <= 8 && dh >= 6 && nowHour >= 15) {
              redFlag = true;
            } else if (lastHour <= 8 && dh >= 4 && nowHour >= 13) {
              yellowFlag = true;
            }
            if (lastHour >= 9 && lastHour <= 14) {
              if (dh > 6) redFlag = true;
              else if (dh > 4) yellowFlag = true;
            }
          }
          if (redFlag || yellowFlag) {
            label.setHorizontalTextPosition(SwingConstants.RIGHT);
            label.setIconTextGap(2);
          }
          if (redFlag) label.setIcon(KNC_Terminal.alertRedIcon);
          else if (yellowFlag) label.setIcon(KNC_Terminal.alertYellowIcon);
        } catch (ParseException ex) {
          return component;
        }
      }
    }
    return component;
  }
}
