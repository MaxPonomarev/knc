

import java.io.Serializable;
import java.util.Date;

/**
 * ����� ��������� ������ ���������� �� ������� ������
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: RPC</p>
 * @author Maksim Ponomarev
 */

class IncassData implements Serializable {
  String productName; //������������ ������ (������)
  int fromOperation;  //ID ������ ��������. 0 - �� ���� ��������
  int toOperation;    //ID ��������� ��������. 0 - �� ���� ��������
  Date fromDate;      //���� ������� �������. ����� ���� null
  Date toDate;        //���� ���������� �������. ����� ���� null
  int operQuantity;   //���������� ��������
  int errQuantity;    //���������� ��������� ��������
  double sumByOpers;  //�������� �����
  double paySum;      //����� ���������� (�� ������� ��������)
  double errSum;      //����� ����������� ��������
  IncassData_subAccount[] subAccounts = null; //���� �� null, �� �������� �� ���������

  public IncassData() {}
}

class IncassData_subAccount implements Serializable {
  String subAccountUID; //������������ ���������
  int operQuantity;     //���������� ��������
  double sumByOpers;    //����� ���� ��������

  public IncassData_subAccount() {}
  public IncassData_subAccount(String subAccountUID, int operQuantity, double sumByOpers) {
    this.subAccountUID = subAccountUID;
    this.operQuantity = operQuantity;
    this.sumByOpers = sumByOpers;
  }
}
