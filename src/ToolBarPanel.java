import java.awt.event.ActionListener;
import javax.swing.*;

abstract class ToolBarPanel extends JPanel {
  static public final String BUTTON_REFRESH = "BUTTON_REFRESH";
  static public final String BUTTON_SEARCH = "BUTTON_SEARCH";
  static public final String BUTTON_EXPORT = "BUTTON_EXPORT";
  static public final String BUTTON_SAVE = "BUTTON_SAVE";
  static protected ImageIcon refreshButtonIcon = new ImageIcon("images/refresh.png");
  static protected ImageIcon searchButtonIcon = new ImageIcon("images/search.png");
  static protected ImageIcon exportButtonIcon = new ImageIcon("images/export.png");
  static protected ImageIcon saveButtonIcon = new ImageIcon("images/save.png");
  protected ActionListener listener = null;

  public void setActionListener(ActionListener l) {
    listener = l;
  }
}
