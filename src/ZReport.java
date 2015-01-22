

import java.io.Serializable;
import java.util.Date;

class ZReport implements Serializable {
  int device;      //1+����� ������ (transient?)
  byte model;      //������ ����������
  boolean incass;  //true - ����� ��� ����������, false - �������������� ��������
  Date date;       //���� Z-������ �� ���������� �������
  long INN;        //��� �����������
  int serial;      //����� �����������
  int number;      //����� Z-������
  String timestamp;//���� Z-������ �� �����������
  double sum;      //����� Z-������
  double totalSum; //������������ ����� Z-������
  byte replicated; //0-�� ������������, 1-������������, 2-������������

  public ZReport() {
    date = new Date();
    replicated = 0;
  }

  public void setIncass(boolean incass) {
    this.incass = incass;
  }

  public void setReplicated(byte replicated) {
    this.replicated = replicated;
  }
}
