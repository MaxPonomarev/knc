

public class SelectID {
  public final Object name;
  public final int id;

  public SelectID(Object name, int id) {
    this.name = name;
    this.id = id;
  }

  public String toString() {
    return name.toString();
  }
}
