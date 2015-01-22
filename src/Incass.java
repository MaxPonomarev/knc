

import java.io.Serializable;
import java.util.Date;

class Incass implements Serializable {
  static final long serialVersionUID = 2433272952105299297L;
  int device;        //1+номер киоска
  IncassData[] data; //данные по каждой услуге
  Date date;         //дата инкассации
  int bills;         //количество купюр
  int[] billDivision;//10,50,100,500,1000,5000
  double sum;        //общая сумма инкассации
  double refund;     //сумма переплаты
  String cashier;    //инкассатор
  transient byte replicated;   //0-не реплицирован, 1-отправляется, 2-реплицирован

  public Incass() {
    replicated = 0;
  }

  public void setReplicated(byte replicated) {
    this.replicated = replicated;
  }
}
