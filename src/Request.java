

import java.io.*;

class Request implements Serializable {
  UserProfile user;
  Object[] data;

  public Request(UserProfile user) {
    this.user = user;
  }

  public Request(UserProfile user, Object[] data) {
    this.user = user;
    this.data = data;
  }

  public void setData(Object[] data) {
    this.data = data;
  }
}
