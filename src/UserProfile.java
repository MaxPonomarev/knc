

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
      0 - Наблюдатель
      1 - Пользователь
      2 - Куратор
      3 - Администратор
      4 - Супервизор
  */
  String access = ""; //""  "23003"  "23003,23005"  "2300301,2300302,2300303"  "!2300301,23003"
  String mailList = ""; //список адресов для рассылки оповещений через запятую
  TreeMap<String,HashSet<String>> filters = null; //список фильтров Название=>[HashSet.class]Фильтр
  TreeMap<Long,String> companies = null; //список организаций [Long]ИНН=>Название

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
