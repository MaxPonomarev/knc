

import java.io.Serializable;
import java.util.Date;

class Incass implements Serializable {
  static final long serialVersionUID = 2433272952105299297L;
  int device;        //1+����� ������
  IncassData[] data; //������ �� ������ ������
  Date date;         //���� ����������
  int bills;         //���������� �����
  int[] billDivision;//10,50,100,500,1000,5000
  double sum;        //����� ����� ����������
  double refund;     //����� ���������
  String cashier;    //����������
  transient byte replicated;   //0-�� ������������, 1-������������, 2-������������

  public Incass() {
    replicated = 0;
  }

  public void setReplicated(byte replicated) {
    this.replicated = replicated;
  }
}
