import java.text.DecimalFormat;

public class DoubleApproach implements Comparable<DoubleApproach> {
  private static final DecimalFormat format = new DecimalFormat();
  private final Double from;
  private final Double to;
  private final boolean equals;

  public DoubleApproach(Double from) {
    if (from == null) from = 0.0d;
    this.from = from;
    this.to = from;
    this.equals = true;
  }

  public DoubleApproach(Double from, Double to) {
    if (from == null) from = 0.0d;
    if (to == null) to = 0.0d;
    this.from = from;
    this.to = to;
    this.equals = false;
  }

  public Double getFrom() {
    return from;
  }

  public Double getTo() {
    return to;
  }

  @Override
  public int compareTo(DoubleApproach o) {
    return to.compareTo(o.to);
  }

  public String toString() {
    if (equals) return format.format(from);
    else return format.format(from) + " \u2192 " + format.format(to);
  }
}
