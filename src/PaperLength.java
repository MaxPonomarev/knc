

import java.text.DecimalFormat;

public final class PaperLength implements Comparable<PaperLength> {
  private final Integer paperLengthMM;
  private final String extRepresentation;
  private final String basicRepresentation;
  private boolean basicFormat;

  public PaperLength(Integer paperLengthMM, boolean basicFormat) {
    DecimalFormat mmFormat = new DecimalFormat("00");
    this.paperLengthMM = paperLengthMM;
    this.extRepresentation = mmFormat.format(paperLengthMM / 1000) + "м " + mmFormat.format(paperLengthMM % 1000 / 10) + "см";
    this.basicRepresentation = (paperLengthMM / 1000) + " м";
    this.basicFormat = basicFormat;
  }

  public boolean isBasicFormat() {
    return basicFormat;
  }

  public void setBasicFormat(boolean b) {
    basicFormat = b;
  }

  public int mmValue() {
    return paperLengthMM;
  }

  public int compareTo(PaperLength paperLength) {
    return paperLengthMM.compareTo(paperLength.paperLengthMM);
  }

  public String toString() {
    if (basicFormat) return basicRepresentation;
    else return extRepresentation;
  }
}
