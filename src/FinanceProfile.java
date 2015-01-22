

import java.io.Serializable;
import java.util.Date;

class FinanceProfile implements Serializable {
  Date from, to; //период
  //HashMap data[0] выручка по шлюзам Шлюз=>Выручка
  //String  data[1] номер киоска
  //String  data[2] адрес установки киоска
  //Double  data[3] выручка, руб
  //Double  data[4] доход, руб
  //Integer data[5] количество операций
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
