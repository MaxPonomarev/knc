

import java.io.*;

class KioskNetProfile implements Serializable {
  //����� �������� �������
  //���������� ��������
  // ����������� �������
  // ���������� ���������� (��)
  // � ���������� �����
  public Object[] data;

  public KioskNetProfile(Object[] data) {
    this.data = data;
  }

  Object[] getData() {
    return data;
  }
}
