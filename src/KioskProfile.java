

import java.io.*;

class KioskProfile implements Serializable {
  static final long serialVersionUID = 1186076506737438521L;
  int delay; //��������������� ������� ��� ��������
  long lat;  //��������������� �������� ��� ���������
  boolean timeout = false; //��������������� �������� ��� ������ ������� ���������
  //data[0]  ������ ������: 0-�����, 1-������������ ����������������, 2-��������� ��, 3-����������� ������
  //data[1]  ����� ������
  //data[2]  ����� ��������� ������
  //data[3]  ���� ��������� ����������
  //data[4]  ����� � ����� � ��������� ����������
  //data[5]  ���������� ����� � ����� � ��������� ����������
  //data[6]  ��������� ����� � �����
  //data[7]  ���������� �������� � ��������� ����������
  //data[8]  ���������� ��������� �������� � ��������� ����������
  //data[9]  ������� ������
  //data[10] ����� ��������� ����������� ��������
  //data[11] �����������
  Object[] data;
  Object[][] data2;
  Object[][] productList;

  public KioskProfile(Object[] data, Object[][] data2, Object[][] productList, int delay) {
    this.data = data;
    this.data2 = data2;
    this.productList = productList;
    this.delay = delay;
  }

  public Object[] getData() {
    return data;
  }

  public Object[] getData2() {
    return data2;
  }

  public Object[][] getProductList() {
    return productList;
  }

  public void setLAT(long time) {
    lat = time;
  }

  public void setTimeout(boolean b) {
    timeout = b;
  }
}
