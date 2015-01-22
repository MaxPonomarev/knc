

import java.io.Serializable;
import java.util.Date;

class StatProfile implements Serializable {
  Date from, to; //������
  int interval;
  String number;
  String address;
  //������ ������: -1-��� ������ � ���� �������, 0-�����, 1-������������ ����������������, 2-��������� ��, 3-����������� ������, 10-13-��� �����
  byte[] data;

  public StatProfile() {
  }

  public StatProfile(Date from, Date to, int interval, String number, String address, byte[] data) {
    this.from = from;
    this.to = to;
    this.interval = interval;
    this.number = number;
    this.address = address;
    this.data = data;
  }
}
