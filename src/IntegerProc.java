

class IntegerProc implements Comparable<IntegerProc> {
  private final int value;

  public IntegerProc(int i) {
    this.value = i;
  }

  public int intValue() {
    return value;
  }

  public int compareTo(IntegerProc compObj) {
    return Integer.valueOf(value).compareTo(compObj.value);
  }

  public String toString() {
    return value + "%";
  }
}
