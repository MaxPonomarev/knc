import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.*;

import org.jdesktop.swingx.JXSummaryTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

class IncassDataFrame extends JFrame {
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final Incass[] incass;
  private IncassDataTableModel tableModel;
  private JPanel contentPane;
  private JXSummaryTable table;
  private JScrollPane scrollPane;

  public IncassDataFrame(Incass[] incass) {
    this.incass = incass;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() {
    contentPane = (JPanel) this.getContentPane();
    this.setIconImages(KNC_Terminal.icons);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    if (incass.length == 1) {
      this.setTitle("Детализация инкассации от "+dateFormat.format(incass[0].date));
    } else {
      this.setTitle("Детализация инкассации");
    }
    this.setBounds(10, 25, Toolkit.getDefaultToolkit().getScreenSize().width-20, Toolkit.getDefaultToolkit().getScreenSize().height-60);
    tableModel = new IncassDataTableModel();
    tableModel.putIncass(incass);
    table = new JXSummaryTable(tableModel);
    table.setRowHeight(33);
    table.setColumnControlVisible(true);
    table.setHorizontalScrollEnabled(true);
    table.addHighlighter(HighlighterFactory.createSimpleStriping(new Color(240,240,224)));
    scrollPane = new JScrollPane(table);
    contentPane.add(scrollPane);
    table.packAll();
  }
}
