

import java.awt.*;
import javax.swing.*;

public class LevelPanel extends JPanel {
  JPanel accessPanel = new JPanel();
  ButtonGroup buttonGroup = new ButtonGroup();
  public JRadioButton levelButton[] = new JRadioButton[5];
  JLabel accessLabel = new JLabel();
  public JTextField accessField = new JTextField();

  public LevelPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    for (int i=0; i<5; i++) {
      levelButton[i] = new JRadioButton();
      buttonGroup.add(levelButton[i]);
      levelButton[i].setFont(new Font("Dialog", Font.BOLD, 12));
      levelButton[i].setOpaque(false);
      this.add(levelButton[i]);
    }
    levelButton[0].setSelected(true);
    levelButton[0].setText("�����������");
    levelButton[1].setText("������������");
    levelButton[2].setText("�������");
    levelButton[3].setText("�������������");
    levelButton[4].setText("����������");
    levelButton[0].setToolTipText("<HTML>���������: �������� �������<br>���������: ��������� ��������, ���������� � ���������</HTML>");
    levelButton[1].setToolTipText("<HTML>���������: �������� �������, ��������� ��������<br>���������: ���������� � ���������</HTML>");
    levelButton[2].setToolTipText("<HTML>���������: �������� �������, ��������� ��������, ���������� ��������<br>���������: ��������� �������</HTML>");
    levelButton[3].setToolTipText("<HTML>����������/�������� ������������� � ����� �������,<br>��������� ������ ������� ������������� � ����� �������</HTML>");
    levelButton[4].setToolTipText("<HTML>����������/�������� ������������� � ����� �������,<br>��������� ������ ������� ������������� � ����� �������,<br>���������� ��������</HTML>");
    accessPanel.setLayout(null);
    accessLabel.setText("����:");
    accessLabel.setBounds(5, 5, 30, 25);
    accessLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    accessField.setBounds(40, 5, 199, 25);
    accessField.setFont(new Font("Dialog", Font.PLAIN, 12));
    accessField.setToolTipText("������ ����� ����� �������");
    accessPanel.add(accessLabel);
    accessPanel.add(accessField);
    this.add(accessPanel);
  }

  public void setEnabled(boolean enabled) {
    for (int i=0; i<5; i++) {
      levelButton[i].setEnabled(enabled);
    }
    accessLabel.setEnabled(enabled);
    accessField.setEditable(enabled);
    if (enabled) accessField.setForeground(UIManager.getColor("TextField.foreground"));
    else accessField.setForeground(UIManager.getColor("TextField.inactiveForeground"));
    super.setEnabled(enabled);
  }
}
