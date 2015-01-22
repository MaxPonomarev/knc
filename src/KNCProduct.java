import java.text.DecimalFormat;
import java.util.Date;

public class KNCProduct implements Comparable<KNCProduct> {
  private static final DecimalFormat intFormat = new DecimalFormat("############.##");
  private int id;
  private String name;
  private boolean isVisible;
  private boolean isEnabled;
  private double cost;
  private double[][] commission;
  private Date troubleBefore;

  public KNCProduct(Object[] product) {
    id = Integer.parseInt((String) product[1]);
    name = (String) product[0];
    isVisible = (Boolean) product[2];
    isEnabled = (Boolean) product[3];
    cost = (Double) product[4];
    commission = (double[][]) product[5];
    troubleBefore = (Date) product[6];
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public double getCost() {
    return cost;
  }

  public double[][] getCommission() {
    return commission;
  }

  public Date getTroubleBefore() {
    return troubleBefore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return id == ((KNCProduct) o).id;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();
    int lines = 0;
    if (cost > 0.001) {
      res.append("цена ").append(intFormat.format(cost)).append(" руб.");
      if (commission[0][2] > 0.001) res.append(", комиссия ").append(intFormat.format(commission[0][2])).append(" руб.");
      lines++;
    } else if (commission.length == 1) {
      res.append(intFormat.format(commission[0][1])).append("%");
      if (commission[0][2] > 0.001) res.append(" минимум ").append(intFormat.format(commission[0][2])).append(" руб.");
      lines++;
    } else if (commission.length > 1) {
      int clength = commission.length;
      for (int i = 0; i < commission.length; i++) {//определение длины массива, исключая то что после Double.POSITIVE_INFINITY
        if (commission[i][0] == Double.POSITIVE_INFINITY) {
          clength = i + 1;
          break;
        }
      }
      int part1 = (int) Math.floor(clength / 2);
      for (int i = 0; i < clength; i++) {
        if (commission[i][0] < Double.POSITIVE_INFINITY) {
          res.append("[до ").append(intFormat.format(commission[i][0])).append(" руб. - ");
        } else res.append("[свыше - ");
        if (commission[i][1] > 0.001) res.append(intFormat.format(commission[i][1])).append("%");
        if (commission[i][2] > 0.001) {
          if (commission[i][1] > 0.001) res.append(" минимум ").append(intFormat.format(commission[i][2])).append(" руб.");
          else res.append(intFormat.format(commission[i][2])).append(" руб.");
        }
        res.append("]");
        if (i == part1 - 1) {
          res.append("<br>");
          lines++;
        }
      }
      lines++;
    }
    if (!isEnabled || !isVisible || troubleBefore != null) {
      res.append("<br>");
      if (!isEnabled) {
        res.append("<font color=red>отключена</font>");
        if (!isVisible) res.append(", ");
      }
      if (!isVisible) res.append("<font color=red>скрыта</font>");
      if (isEnabled && isVisible && troubleBefore != null) res.append("<font color=blue>задержка платежей</font>");
      lines++;
    }
    if (lines > 2) {
      res.insert(0, "<html><font style=\"font-size: 70%\">");
      res.append("</font></html>");
    } else {
      res.insert(0, "<html>");
      res.append("</html>");
    }
    return res.toString();
  }

  public int compareTo(KNCProduct o) {
    return name.compareTo(o.name);
  }
}
