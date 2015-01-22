

import java.io.Serializable;
import java.util.Date;

class ZReport implements Serializable {
  int device;      //1+номер киоска (transient?)
  byte model;      //модель устройства
  boolean incass;  //true - отчет при инкассации, false - автоматическое закрытие
  Date date;       //дата Z-отчета по системному времени
  long INN;        //ИНН фискальника
  int serial;      //номер фискальника
  int number;      //номер Z-отчета
  String timestamp;//дата Z-отчета по фискальнику
  double sum;      //сумма Z-отчета
  double totalSum; //необнуляемая сумма Z-отчета
  byte replicated; //0-не реплицирован, 1-отправляется, 2-реплицирован

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
