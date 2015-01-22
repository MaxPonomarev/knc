

public class ProductID<N, I> implements Comparable<ProductID<?, ?>> {
  public N name;
  public I id;

  public ProductID(N name, I id) {
    this.name = name;
    this.id = id;
  }

  public String toString() {
    return name.toString();
  }

  public int compareTo(ProductID<?, ?> o) {
    return this.name.toString().compareTo(o.name.toString());
  }
}
