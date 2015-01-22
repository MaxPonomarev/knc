import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.jdesktop.swingx.renderer.*;

class KNCTableRenderer3 implements TableCellRenderer {
  private static final int YELLOW = 2;
  private static final int RED = 1;

  public KNCTableRenderer3(JTable table) {
    TableColumnModel columnModel = table.getColumnModel();
    columnModel.getColumn(KNCTableModel.COLUMN_BUFFER).setCellRenderer(this);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    TableCellRenderer defaultTableCellRenderer = new DefaultTableRenderer(StringValues.NUMBER_TO_STRING, JLabel.RIGHT);
    Component component = defaultTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (component instanceof JRendererLabel) {
      if (value != null && (value instanceof Integer)) {
        JRendererLabel label = (JRendererLabel) component;
        int val = (Integer) value;
        if (val < YELLOW) {
          label.setHorizontalTextPosition(SwingConstants.LEFT);
          label.setIconTextGap(2);
        }
        if (val < RED) label.setIcon(KNC_Terminal.alertRedIcon);
        else if (val < YELLOW) label.setIcon(KNC_Terminal.alertYellowIcon);
      }
    }
    return component;
  }
}
