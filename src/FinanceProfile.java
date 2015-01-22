

import java.io.Serializable;
import java.util.Date;

class FinanceProfile implements Serializable {
  Date from, to; //������
  //HashMap data[0] ������� �� ������ ����=>�������
  //String  data[1] ����� ������
  //String  data[2] ����� ��������� ������
  //Double  data[3] �������, ���
  //Double  data[4] �����, ���
  //Integer data[5] ���������� ��������
  Object[] data;

  public FinanceProfile() {
  }

  public FinanceProfile(Date from, Date to, Object[] data) {
    this.from = from;
    this.to = to;
    this.data = data;
  }

  public String getKioskID() {
    return data[1].toString();
  }
}
