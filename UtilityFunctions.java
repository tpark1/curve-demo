import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javafx.util.Pair;

public class UtilityFunctions {
  static int r = 10;
  static int t = 20;
  static double epsilon = 0.001f;
  static final double MAX_VALUE = Double.MAX_VALUE;

  public UtilityFunctions() {
    // do nothing
  }

  public static Point2D.Double convert(Point p) {
    return new Point2D.Double(p.x, p.y);
  }

  public static Point toPoint(Point2D.Double p) {
    return new Point((int)p.x, (int)p.y);
  }

  public static Point add(Point p1, Point p2) {
    return new Point(p1.x + p2.x, p1.y + p2.y);
  }

  // public static Point2D.Double add2(Point p1, Point2D.Double p2) {
  //   return new Point2D.Double(p1.x + p2.x, p1.y + p2.y);
  // }

  public static Point2D.Double subtract(Point2D.Double p1, Point2D.Double p2) {
    return new Point2D.Double(p1.x - p2.x, p1.y - p2.y);
  }

  public static Point subtract2(Point p1, Point p2) {
    return new Point(p1.x - p2.x, p1.y - p2.y);
  }

  public static Point scale(Point p, double n) {
    return new Point((int)Math.round(p.x * n), (int)Math.round(p.y * n));
  }

  public static Point2D.Double scale2(Point2D.Double p, double n) {
    // if (n == 0) {
    //   return new Point2D.Double();
    // }
    return new Point2D.Double(p.x/n, p.y/n);
  }

  public static Point2D.Double getVector(double slope, double range) {
    if (slope == MAX_VALUE) {
      return new Point2D.Double(0, 10);
    }
    Point2D.Double vector = new Point2D.Double(1.0, slope);
    Point2D.Double unitVector = scale2(vector, UtilityFunctions.magnitude(vector));
    return new Point2D.Double(unitVector.x * range, unitVector.y * range);
  }

  // public static Point divide(Point p1, double x) {
  //   return new Point((int)p1.x/x, (int)p1.y/x);
  // }

  // Factorial function
  public static int factorial(int n) {
    int result = 1;
    for (int i = 1; i <= n; i++) {
      result *= i;
    }
    return result;
  }

  // Function that computes the binomial coefficent. Utilizes fact that in binomial coefficent, one of the factorials in denominator
  // will always cancel out with terms in numerator
  public static double binomialCoefficient(int n, int i) {
    int numerator = 1;
    for (int j = n; j>i; j--) {
      numerator *= j;
    }
    return numerator / factorial(n-i);
  }

  // Magnitude of cross product
  public static double crossProductMagnitude(Point p1, Point p2) {
    return p1.x * p2.y - p1.y * p2.x;
  }

  public static double crossProductMagnitude(Point2D.Double p1, Point2D.Double p2) {
    return p1.x * p2.y - p1.y * p2.x;
  }

  // Magnitude
  public static double magnitude(Point2D.Double p) {
    return Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
  }

  // Magnitude
  public static double magnitude2(Point p) {
    return Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
  }

  // Distance
  public static double distance(Point2D.Double p1, Point p2) {
    return magnitude(new Point2D.Double(p1.x - p2.x, p1.y - p2.y));
  }

  public static double[] rescale(double[] list, double max) {
    double output[] = new double[list.length];
    // find max of list
    double localMax = 0;
    for (int i = 0; i<list.length; i++) {
      if (list[i] > localMax) {
        localMax = list[i];
      }
    }
    // rescale so output has range [0, max]
    for (int i = 0; i<list.length; i++) {
      output[i] = (list[i] / localMax) * max;
    }

    return output;
  }

  public static double perpendicular(double x) {
    if (Math.abs(x) < epsilon) {
      return MAX_VALUE;
    }
    return -1.0/x;
  }

  public static double curvature(ArrayList<Point> curve, int x) {
    Point twiceDisplacement;
    Point laplacian;
    if (x < (r + 1)) {
      twiceDisplacement = subtract2(curve.get(x+r),curve.get(curve.size()-1-x));
      laplacian = add(scale(curve.get(r), 2), add(curve.get(x+r), curve.get(curve.size()-1-x)));
    }
    else if (x > curve.size()-1-r) {
      twiceDisplacement = subtract2(curve.get(x+r-curve.size()),curve.get(x-r));
      laplacian = add(scale(curve.get(r), 2), add(curve.get(x+r-curve.size()), curve.get(x-r)));
    }
    else {
      twiceDisplacement = subtract2(curve.get(x+r),curve.get(x-r));
      laplacian = add(scale(curve.get(r), 2), add(curve.get(x+r), curve.get(x-r)));
    }
    double dr2 = Math.pow(magnitude2(twiceDisplacement), 2) * 0.25;
    return Math.abs(0.5 * crossProductMagnitude(twiceDisplacement, laplacian)) * Math.pow(dr2, -1.5);
  }

  public static Point2D.Double center(ArrayList<Point> points) {
    double x = 0;
    double y = 0;
    for (Point p : points) {
      x += p.x;
      y += p.y;
    }
    return new Point2D.Double(x/points.size(), y/points.size());
  }

  // Computes slope of two points. Assumes p1 -> p2
  public static double slope(Point p1, Point p2) {
    int run = p2.x - p1.x;
    if (run == 0) {
      return p2.y - p1.y;
    }
    else {
      return (double)(p2.y - p1.y) / (double)run;
    }
  }

  public static Pair<Double, Boolean> computeTangentVector(ArrayList<Point> curve, int i) {
    Point p1 = null;
    Point p2 = null;
    if (i < t+1) {
      p1 = curve.get(i+t);
      p2 = curve.get(curve.size()-1-i);
    }
    else if (i > curve.size()-1-t) {
      p1 = curve.get(i+t-curve.size());
      p2 = curve.get(i-t);
    }
    else {
      p1 = curve.get(i+t);
      p2 = curve.get(i-t);
    }
    boolean b = (p1.x - p2.x) <= 0;
    if ((p1.x - p2.x) == 0) {
      return new Pair<Double, Boolean>(MAX_VALUE, b);
    }
    else {
      return new Pair<Double, Boolean>((p1.y - p2.y)/(1.0 * (p1.x - p2.x)), b);
    }
  }
}
