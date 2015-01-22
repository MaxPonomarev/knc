

import java.io.*;
import java.util.*;

class UserProfile implements Serializable {
  static final long serialVersionUID = -6782226737229406849L;
  String name;
  int pass;
  int id = 0;
  long time = 0;
  byte level = 0;
  /*  level:
      0 - �����������
      1 - ������������
      2 - �������
      3 - �������������
      4 - ����������
  */
  String access = ""; //""  "23003"  "23003,23005"  "2300301,2300302,2300303"  "!2300301,23003"
  String mailList = ""; //������ ������� ��� �������� ���������� ����� �������
  TreeMap<String,HashSet<String>> filters = null; //������ �������� ��������=>[HashSet.class]������
  TreeMap<Long,String> companies = null; //������ ����������� [Long]���=>��������

  public UserProfile(String name, int pass) {
    this.name = name;
    this.pass = pass;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String toString() {
    return name;
  }
}
