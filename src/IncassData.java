

import java.io.Serializable;
import java.util.Date;

/**
 * Класс описывает данные инкассации по данному товару
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: RPC</p>
 * @author Maksim Ponomarev
 */

class IncassData implements Serializable {
  String productName; //Наименование товара (услуги)
  int fromOperation;  //ID первой операции. 0 - не было операций
  int toOperation;    //ID последней операции. 0 - не было операций
  Date fromDate;      //Дата первого платежа. Может быть null
  Date toDate;        //Дата последнего платежа. Может быть null
  int operQuantity;   //Количество операций
  int errQuantity;    //Количество ошибочных операций
  double sumByOpers;  //Внесённая сумма
  double paySum;      //Сумма пополнения (за вычетом комиссии)
  double errSum;      //Сумма отклоненных операций
  IncassData_subAccount[] subAccounts = null; //Если не null, то разбивка по субсчетам

  public IncassData() {}
}

class IncassData_subAccount implements Serializable {
  String subAccountUID; //Наименование оператора
  int operQuantity;     //Количество операций
  double sumByOpers;    //Сумма всех операций

  public IncassData_subAccount() {}
  public IncassData_subAccount(String subAccountUID, int operQuantity, double sumByOpers) {
    this.subAccountUID = subAccountUID;
    this.operQuantity = operQuantity;
    this.sumByOpers = sumByOpers;
  }
}
