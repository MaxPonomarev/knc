import java.text.DecimalFormat;

public class DoubleTotal implements Comparable<DoubleTotal> {
  private static final DecimalFormat format = new DecimalFormat();
  private final Double value1;
  private final Double value2;
  private final boolean equals;

  public DoubleTotal(Double value1) {
    if (value1 == null) value1 = 0.0d;
    this.value1 = value1;
    this.value2 = value1;
    this.equals = true;
  }

  public DoubleTotal(Double value1, Double value2) {
    if (value1 == null) value1 = 0.0d;
    if (value2 == null) value2 = 0.0d;
    this.value1 = value1;
    this.value2 = value2;
    this.equals = false;
  }

  public Double getValue1() {
    return value1;
  }

  public Double getValue2() {
    return value2;
  }

  @Override
  public int compareTo(DoubleTotal o) {
    return value1.compareTo(o.value1);
  }

  @Override
  public String toString() {
    if (equals) return format.format(value1);
    else return format.format(value1) + " [" + format.format(value2) + "]";
  }
}
