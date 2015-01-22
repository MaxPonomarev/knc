

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

class SlimToggleButton extends JToggleButton {
  private Border onMouseBorder;
  private Border pressedBorder;

  public SlimToggleButton() {
    super();
    try {jbInit();}catch (Exception ex) {ex.printStackTrace();}
  }

  public SlimToggleButton(Action a) {
    super(a);
    try {jbInit();}catch (Exception ex) {ex.printStackTrace();}
  }

  public SlimToggleButton(String text, Icon icon) {
    super(text, icon);
    try {jbInit();}catch (Exception ex) {ex.printStackTrace();}
  }

  public SlimToggleButton (String text, Icon icon, boolean selected) {
    super(text, icon, selected);
    try {jbInit();}catch (Exception ex) {ex.printStackTrace();}
  }

  private void jbInit() {
    setUI(new com.sun.java.swing.plaf.windows.WindowsToggleButtonUI());
    onMouseBorder = new SlimBevelBorder(BevelBorder.RAISED, Color.white, Color.white, new Color(116, 116, 116), new Color(166, 166, 166));
    pressedBorder = new SlimBevelBorder(BevelBorder.LOWERED, Color.white, Color.white, new Color(116, 116, 116), new Color(166, 166, 166));
    setBorder(onMouseBorder);
    setBorderPainted(false);
    setContentAreaFilled(false);
    setFocusable(false);
    addChangeListener(new SlimToggleButton_this_changeAdapter(this));
  }

  public void this_stateChanged(ChangeEvent e) {
    if (getModel().isEnabled() && (getModel().isRollover() || getModel().isArmed() || getModel().isSelected())) setBorderPainted(true);
    else setBorderPainted(false);
    if (getModel().isPressed() || getModel().isSelected()) setBorder(pressedBorder);
    else setBorder(onMouseBorder);
  }
}

class SlimToggleButton_this_changeAdapter implements ChangeListener {
  private SlimToggleButton adaptee;
  SlimToggleButton_this_changeAdapter(SlimToggleButton adaptee) {
    this.adaptee = adaptee;
  }

  public void stateChanged(ChangeEvent e) {
    adaptee.this_stateChanged(e);
  }
}

