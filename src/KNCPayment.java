

import java.io.Serializable;

/**
 * ����� ���������� ���������� � ������� ��� �������� � ������� ����������
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: RPC</p>
 * @author Maksim Ponomarev
 */

class KNCPayment implements Serializable {
  static final long serialVersionUID = 1917489823657535005L;
  int device;       //1+����� ������
  int id;           //ID �������� (�������)
  Object[] data;
  /*
  Integer data[0]   ID �������� (�������)
  Date    data[1]   ���� � ����� �������� �������� (�������)
  Integer data[2]   ������� ������ (������) � ��������� �������
  String  data[3]   ��������� �������
  String  data[4]   ������������ ������ (������)
  String  data[5]   �������
  String  data[6]   ����� ��������, ���. �����, �������� � �.�.
  Double  data[7]   �������� �����
  Double  data[8]   ����� ���������� (�� ������� ��������)
  Double  data[9]   ��������, ������ ��� ������ �������� (�������) � %
  Double  data[10]  ��������, ������ ��� ������ �������� (�������) � ���.
  Boolean data[11]  ��������� �� ������ � ������ ��������
  Boolean data[12]  ������� �� �������� ������
  String  data[13]  ������ ������� (�����������, ��������, ��������)
  String  data[14]  �����������

  ������ ������� ������������� ��������� �������:
  if (data[11] == true && data[12] == true)   ������ �������� (��������)
  if (data[11] == true && data[12] == false)  ������ �������� (���������)
  if (data[11] == false)                      ������ �������������� (�����������)
  */

  public KNCPayment() {} //��������������� �����������
  public KNCPayment(Object[] data) {
    this.data = data;
  }

  public Object[] getData() {
    return data;
  }

  public void setData(Object[] data) {
    this.data = data;
  }

  public Object getDataAt(int index) {
    if (index < data.length) return data[index];
    else return null;
  }
}
