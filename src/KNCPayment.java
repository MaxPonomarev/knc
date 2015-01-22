

import java.io.Serializable;

/**
 * Класс содержащий информацию о платеже для передачи в систему управления
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: RPC</p>
 * @author Maksim Ponomarev
 */

class KNCPayment implements Serializable {
  static final long serialVersionUID = 1917489823657535005L;
  int device;       //1+номер киоска
  int id;           //ID операции (платежа)
  Object[] data;
  /*
  Integer data[0]   ID операции (платежа)
  Date    data[1]   Дата и время создания операции (платежа)
  Integer data[2]   Артикул товара (услуги) в платежной системе
  String  data[3]   Платежная система
  String  data[4]   Наименование товара (услуги)
  String  data[5]   Субсчет
  String  data[6]   Номер телефона, лиц. счета, договора и т.д.
  Double  data[7]   Внесённая сумма
  Double  data[8]   Сумма пополнения (за вычетом комиссии)
  Double  data[9]   Комиссия, взятая для данной операции (платежа) в %
  Double  data[10]  Комиссия, взятая для данной операции (платежа) в руб.
  Boolean data[11]  Завершена ли работа с данным платежом
  Boolean data[12]  Успешно ли проведен платеж
  String  data[13]  Статус платежа (Действующий, Проведен, Отклонен)
  String  data[14]  Комментарий

  Статус платежа расчитывается следующим образом:
  if (data[11] == true && data[12] == true)   платеж проведен (успешный)
  if (data[11] == true && data[12] == false)  платеж отклонен (ошибочный)
  if (data[11] == false)                      платеж обрабатывается (действующий)
  */

  public KNCPayment() {} //безпараметровый конструктор
  public KNCPayment(Object[] data) {
    this.data = data;
  }

  public Object[] getData() {
    return data;
  }

  public void setData(Object[] data) {
    this.data = data;
  }

  public Object getDataAt(int index) {
    if (index < data.length) return data[index];
    else return null;
  }
}
