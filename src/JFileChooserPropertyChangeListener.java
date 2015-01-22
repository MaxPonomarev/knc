import java.beans.*;
import java.io.File;
import javax.swing.*;

//������ ����� �� ���� ��������� ���������� ����� ����� ��� ����� FileFilter
class JFileChooserPropertyChangeListener implements PropertyChangeListener {

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) && evt.getOldValue() != null && evt.getNewValue() == null) {
      ((JFileChooser) evt.getSource()).setSelectedFile((File) evt.getOldValue());
      ((JFileChooser) evt.getSource()).updateUI();
    }
  }
}
