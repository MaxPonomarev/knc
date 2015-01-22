import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import org.jdesktop.swingx.renderer.*;

class StatTableRenderer implements TableCellRenderer {
  private final HashMap<StatProfile, ImageIcon> icons = new HashMap<>();

  public StatTableRenderer(JTable table) {
    TableColumnModel columnModel = table.getColumnModel();
    columnModel.getColumn(StatTableModel.COLUMN_GRAPH).setCellRenderer(this);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    TableCellRenderer defaultTableCellRenderer = new DefaultTableRenderer(StringValues.NUMBER_TO_STRING, JLabel.RIGHT);
    Component component = defaultTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (component instanceof JRendererLabel) {
      if (value != null && (value instanceof StatProfile)) {
        ImageIcon icon = icons.get(value);
        if (icon == null) {
          icon = createIcon((StatProfile) value);
          icons.put((StatProfile) value, icon);
        }
        ((JRendererLabel) component).setIcon(icon);
        ((JRendererLabel) component).setIconTextGap(0);
        ((JRendererLabel) component).setText("");
        ((JRendererLabel) component).setHorizontalAlignment(SwingConstants.LEFT);
      }
    }
    return component;
  }

  public void clear() {
    icons.clear();
  }

  private ImageIcon createIcon(StatProfile stat) {
    Image image = StatTableRendererImagesFactory.createImage(stat);
    return new ImageIcon(image);
  }
}

class StatTableRendererImagesFactory {
  private static final Color col00 = new Color(112, 141, 224);
  private static final Color col01 = new Color(104, 135, 223);
  private static final Color col02 = new Color(96, 127, 220);
  private static final Color col03 = new Color(135, 162, 232);
  private static final Color col04 = new Color(133, 161, 231);
  private static final Color col05 = new Color(63, 95, 206);
  private static final Color col06 = new Color(29, 55, 184);
  private static final Color col07 = new Color(21, 42, 174);
  private static final Color col08 = new Color(23, 45, 177);
  private static final Color col09 = new Color(29, 54, 183);
  private static final Color col010 = new Color(57, 88, 203);
  private static final Color col011 = new Color(103, 134, 222);
  private static final Color col012 = new Color(91, 122, 218);
  private static final Color col013 = new Color(44, 73, 195);
  private static final Color col014 = new Color(90, 120, 216);
  private static final Color col10 = new Color(233, 234, 70);
  private static final Color col11 = new Color(232, 233, 59);
  private static final Color col12 = new Color(229, 231, 51);
  private static final Color col13 = new Color(238, 239, 89);
  private static final Color col14 = new Color(238, 239, 88);
  private static final Color col15 = new Color(219, 221, 26);
  private static final Color col16 = new Color(201, 204, 7);
  private static final Color col17 = new Color(194, 197, 4);
  private static final Color col18 = new Color(196, 199, 5);
  private static final Color col19 = new Color(201, 204, 7);
  private static final Color col110 = new Color(216, 219, 22);
  private static final Color col111 = new Color(232, 233, 58);
  private static final Color col112 = new Color(229, 229, 47);
  private static final Color col113 = new Color(211, 214, 14);
  private static final Color col114 = new Color(227, 228, 51);
  private static final Color col20 = new Color(216, 134, 169);
  private static final Color col21 = new Color(214, 127, 165);
  private static final Color col22 = new Color(211, 119, 158);
  private static final Color col23 = new Color(225, 155, 187);
  private static final Color col24 = new Color(225, 154, 186);
  private static final Color col25 = new Color(194, 86, 129);
  private static final Color col26 = new Color(167, 47, 89);
  private static final Color col27 = new Color(156, 36, 75);
  private static final Color col28 = new Color(159, 38, 78);
  private static final Color col29 = new Color(167, 47, 88);
  private static final Color col210 = new Color(190, 79, 123);
  private static final Color col211 = new Color(214, 126, 164);
  private static final Color col212 = new Color(209, 115, 155);
  private static final Color col213 = new Color(181, 64, 108);
  private static final Color col214 = new Color(206, 112, 151);
  private static final Color col30 = new Color(106, 106, 106);
  private static final Color col31 = new Color(98, 98, 98);
  private static final Color col32 = new Color(88, 88, 88);
  private static final Color col33 = new Color(135, 135, 135);
  private static final Color col34 = new Color(133, 133, 133);
  private static final Color col35 = new Color(66, 66, 66);
  private static final Color col36 = new Color(40, 40, 40);
  private static final Color col37 = new Color(30, 30, 30);
  private static final Color col38 = new Color(32, 32, 32);
  private static final Color col39 = new Color(40, 40, 40);
  private static final Color col310 = new Color(63, 63, 63);
  private static final Color col311 = new Color(97, 97, 97);
  private static final Color col312 = new Color(86, 86, 86);
  private static final Color col313 = new Color(57, 57, 57);
  private static final Color col314 = new Color(87, 87, 87);
  //Color[4][15] синий, желтый, красный, черный
  private static final Color[][] colors = new Color[][]{
          {col00, col01, col02, col03, col04, col05, col06, col07, col08, col09, col010, col011, col012, col013, col014},
          {col10, col11, col12, col13, col14, col15, col16, col17, col18, col19, col110, col111, col112, col113, col114},
          {col20, col21, col22, col23, col24, col25, col26, col27, col28, col29, col210, col211, col212, col213, col214},
          {col30, col31, col32, col33, col34, col35, col36, col37, col38, col39, col310, col311, col312, col313, col314},
  };
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
  private static final int hLine = colors[0].length;
  private static final int leftInset = 4;

  private StatTableRendererImagesFactory() {
  }

  static public BufferedImage createImage(StatProfile stat) {
    int w = stat.data.length + leftInset;
    int h = hLine + 8;
    BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics g = image.getGraphics();
    paint(g, stat);
    return image;
  }

  static private void paint(Graphics g, StatProfile stat) {
    byte[] data = stat.data;

    int dx = leftInset;
    int dy = 8;
    int color = 0;
    for (int i = 0; i < data.length; i++) {
      if (data[i] < 0) continue;
      if (data[i] == 0 || data[i] == 1) color = 0; //синий
      else if (data[i] == 2 || data[i] == 12) color = 1; //желтый
      else if (data[i] == 3 || data[i] == 13) color = 2; //красный
      else if (data[i] == 10 || data[i] == 11) color = 3; //черный
      for (int j = 0; j < hLine; j++) {
        g.setColor(colors[color][j]);
        g.drawLine(dx + i, dy + j, dx + i, dy + j);
      }
    }

    g.setColor(new Color(68, 219, 2));
    for (int i = 289; i < data.length; i = i + 288) {
      g.drawLine(dx + i, dy - 1, dx + i, dy - 2);
      g.drawLine(dx + i - 1, dy - 3, dx + i + 1, dy - 3);
      g.drawLine(dx + i - 1, dy - 4, dx + i + 1, dy - 4);
      g.drawLine(dx + i - 2, dy - 5, dx + i + 2, dy - 5);
      g.drawLine(dx + i - 2, dy - 6, dx + i + 2, dy - 6);
      g.drawLine(dx + i - 3, dy - 7, dx + i + 3, dy - 7);
      g.drawLine(dx + i - 3, dy - 8, dx + i + 3, dy - 8);

    }
    g.setFont(new Font("Dialog", Font.PLAIN, 9));
    g.setColor(Color.BLACK);
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(stat.from);
    for (int i = 1; i < data.length; i = i + 288) {
      g.drawString(dateFormat.format(cal.getTime()), dx + i + 8, dy - 1);
      cal.add(Calendar.DATE, 1);
    }
  }
}
