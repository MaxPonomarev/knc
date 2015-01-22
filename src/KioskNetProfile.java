

import java.io.*;

class KioskNetProfile implements Serializable {
  //Всего доступно киосков
  //Производит операции
  // остановлено киосков
  // необходима инкассация (ТО)
  // в результате сбоев
  public Object[] data;

  public KioskNetProfile(Object[] data) {
    this.data = data;
  }

  Object[] getData() {
    return data;
  }
}
