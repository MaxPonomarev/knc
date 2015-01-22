

import java.io.*;

class KioskProfile implements Serializable {
  static final long serialVersionUID = 1186076506737438521L;
  int delay; //устанавливаетс€ киоском при отправке
  long lat;  //устанавливаетс€ сервером при получении
  boolean timeout = false; //устанавливаетс€ сервером при каждом запросе терминала
  //data[0]  статус киоска: 0-норма, 1-ограниченна€ функциональность, 2-требуетс€ “ќ, 3-критическа€ ошибка
  //data[1]  номер киоска
  //data[2]  адрес установки киоска
  //data[3]  дата последней инкассации
  //data[4]  сумма в стеке с последней инкассации
  //data[5]  количество купюр в стеке с последней инкассации
  //data[6]  свободное место в стеке
  //data[7]  количество операций с последней инкассации
  //data[8]  количество неудачных операций с последней инкассации
  //data[9]  остаток бумаги
  //data[10] врем€ последней завершЄнной операции
  //data[11] комментарий
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
