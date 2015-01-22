import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.*;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.renderer.*;

class Settings9TableRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
  private JXHyperlink renderHyperlink;
  private JXHyperlink editHyperlink;

  public Settings9TableRenderer(JTable table) {
    renderHyperlink = new JXHyperlink();
    renderHyperlink.setText("показать");
    renderHyperlink.setFont(table.getFont());
    renderHyperlink.setOpaque(true);
    editHyperlink = new JXHyperlink();
    editHyperlink.setText("показать");
    editHyperlink.setFont(table.getFont());
    editHyperlink.setOpaque(true);
    editHyperlink.setClickedColor(editHyperlink.getUnclickedColor());
    editHyperlink.setBackground(table.getSelectionBackground());
    editHyperlink.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        try {
          Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + e.getActionCommand());
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });

    TableColumnModel columnModel = table.getColumnModel();
    columnModel.getColumn(5).setCellRenderer(this);
    columnModel.getColumn(6).setCellRenderer(this);
    columnModel.getColumn(7).setCellRenderer(this);
    columnModel.getColumn(5).setCellEditor(this);
    columnModel.getColumn(6).setCellEditor(this);
    columnModel.getColumn(7).setCellEditor(this);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    String val = (value == null) ? "" : value.toString();
    if (val.length() > 0) {
      if (hasFocus) {
        renderHyperlink.setBackground(table.getSelectionBackground());
      } else if (isSelected) {
        renderHyperlink.setBackground(table.getSelectionBackground());
      } else {
        renderHyperlink.setBackground(table.getBackground());
      }
      return renderHyperlink;
    } else {
      TableCellRenderer defaultTableCellRenderer = new DefaultTableRenderer(StringValues.TO_STRING, JLabel.LEFT);
      return defaultTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    if (value != null) editHyperlink.setActionCommand(value.toString());
    return editHyperlink;
  }

  public Object getCellEditorValue() {//хз
    return "показать";
  }

}
