

import java.io.*;
import java.util.*;

class AdminCommand implements Serializable {
  Object[] obj;
  String kiosk_number;
  int id;
  String name;
  Object[] response;
  boolean done = false;

  public AdminCommand(String kioskNumber, String userName) {
    kiosk_number = kioskNumber;
    name = userName;
    id = new Random().nextInt();
  }

  public void setObjectArray(Object[] obj) {
    this.obj = obj;
  }

  public Object[] getObjectArray() {
    return obj;
  }

  public void setDone() {
    done = true;
  }

  public void setResponse(Object[] response) {
    this.response = response;
  }

  public Object[] getResponse() {
    return response;
  }
}
