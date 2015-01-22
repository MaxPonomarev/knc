

import java.awt.*;
import javax.swing.border.BevelBorder;

class SlimBevelBorder extends BevelBorder {

  @SuppressWarnings("MagicConstant")
  public SlimBevelBorder(int bevelType) {
    super(bevelType);
  }

  public SlimBevelBorder(int bevelType, Color highlight, Color shadow) {
    super(bevelType, highlight, shadow);
  }

  public SlimBevelBorder(int bevelType, Color highlightOuterColor,
                     Color highlightInnerColor, Color shadowOuterColor,
                     Color shadowInnerColor) {
    super(bevelType, highlightOuterColor, highlightInnerColor, shadowOuterColor, shadowInnerColor);
  }

  protected void paintRaisedBevel(Component c, Graphics g, int x, int y, int w, int h)  {
    Color oldColor = g.getColor();

    g.translate(x, y);

    g.setColor(getHighlightOuterColor(c));
    g.drawLine(0, 0, 0, h-2);
    g.drawLine(1, 0, w-2, 0);

    g.setColor(getShadowOuterColor(c));
    g.drawLine(0, h-1, w-1, h-1);
    g.drawLine(w-1, 0, w-1, h-2);

    g.translate(-x, -y);
    g.setColor(oldColor);
  }

  protected void paintLoweredBevel(Component c, Graphics g, int x, int y, int w, int h)  {
    Color oldColor = g.getColor();

    g.translate(x, y);

    g.setColor(getShadowInnerColor(c));
    g.drawLine(0, 0, 0, h-1);
    g.drawLine(1, 0, w-1, 0);

    g.setColor(getHighlightOuterColor(c));
    g.drawLine(1, h-1, w-1, h-1);
    g.drawLine(w-1, 1, w-1, h-2);

    g.translate(-x, -y);
    g.setColor(oldColor);
  }
}
