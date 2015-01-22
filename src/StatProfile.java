

import java.io.Serializable;
import java.util.Date;

class StatProfile implements Serializable {
  Date from, to; //период
  int interval;
  String number;
  String address;
  //статус киоска: -1-нет киоска в этом периоде, 0-норма, 1-ограниченная функциональность, 2-требуется ТО, 3-критическая ошибка, 10-13-нет связи
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
