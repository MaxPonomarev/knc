import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.jdesktop.swingx.renderer.*;

class KNCTableRenderer2 implements TableCellRenderer {
  private static final int YELLOW = 25000;
  private static final int RED = 10000;

  public KNCTableRenderer2(JTable table) {
    TableColumnModel columnModel = table.getColumnModel();
    columnModel.getColumn(KNCTableModel.COLUMN_PAPER).setCellRenderer(this);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    TableCellRenderer defaultTableCellRenderer = new DefaultTableRenderer();
    Component component = defaultTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (component instanceof JRendererLabel) {
      if (value != null && (value instanceof PaperLength)) {
        JRendererLabel label = (JRendererLabel) component;
        int val = ((PaperLength) value).mmValue();
        if (val < YELLOW) {
          label.setHorizontalTextPosition(SwingConstants.RIGHT);
          label.setIconTextGap(2);
        }
        if (val < RED) label.setIcon(KNC_Terminal.alertRedIcon);
        else if (val < YELLOW) label.setIcon(KNC_Terminal.alertYellowIcon);
      }
    }
    return component;
  }
}
