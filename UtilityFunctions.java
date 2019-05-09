import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class UtilityFunctions {
  public UtilityFunctions() {}

    public static Point2D.Double convert(Point p) {
      return new Point2D.Double(p.x, p.y);
    }

    public static Point add(Point p1, Point p2) {
      return new Point(p1.x + p2.x, p1.y + p2.y);
    }

    public static Point subtract(Point2D.Double p1, Point2D.Double p2) {
      return new Point((int)p1.x - (int)p2.x, (int)p1.y - (int)p2.y);
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
    public static double crossProductMagnitude(Point2D.Double p1, Point2D.Double p2) {
      return p1.x * p2.y - p1.y * p2.x;
    }

    // Magnitude
    public static double magnitude(Point2D.Double p) {
      return Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
    }

    // Distance
    public static double distance(Point2D.Double p1, Point p2) {
      return magnitude(new Point2D.Double(p1.x - p2.x, p1.y - p2.y));
    }

    public static Point2D.Double[] rescale(Point2D.Double[] list, int max) {
      Point2D.Double output[] = new Point2D.Double[list.length];
      for (int i = 0; i<list.length; i++) {
        Point2D.Double oldP = list[i];
        output[i] = (new Point2D.Double((oldP.x/magnitude(oldP)) * max, (oldP.y/magnitude(oldP)) * max));
      }
      return output;
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

    public static double test(ArrayList<Point> curve2, int i) {
      int t = 20;
      Point p1 = null;
      Point p2 = null;
      if (i < t+1) {
        // slopes[i] = (UtilityFunctions.slope(curve2.get(curve2.size()-2), curve2.get(i)) + UtilityFunctions.slope(curve2.get(i), curve2.get(i+1)))/2;
        p1 = curve2.get(i+t);
        p2 = curve2.get(curve2.size()-1-i);
        // slopes[i] = (p1.y - p2.y)/(2 * (p1.x - p2.x));
      }
      else if (i > curve2.size()-1-t) {
        // slopes[i] = (UtilityFunctions.slope(curve2.get(i-1), curve2.get(i)) + UtilityFunctions.slope(curve2.get(i), curve2.get(1)))/2;
        p1 = curve2.get(i+t-curve2.size());
        p2 = curve2.get(i-t);
        // slopes[i] = (p1.y - p2.y)/(2 * (p1.x - p2.x));
      }
      else {
        // slopes[i] = (UtilityFunctions.slope(curve2.get(i-1), curve2.get(i)) + UtilityFunctions.slope(curve2.get(i), curve2.get(i+1)))/2;
        p1 = curve2.get(i+t);
        p2 = curve2.get(i-t);
        // slopes[i] = (p1.y - p2.y)/(2 * (p1.x - p2.x));
      }

      // System.out.println("p1: " + p1.x + " " + p1.y);
      // System.out.println("p2: " + p2.x + " " + p2.y);

      if ((p1.x - p2.x) == 0) {
        return Double.MAX_VALUE;
      }
      else {
        return (p1.y - p2.y)/(1.0 * (p1.x - p2.x));
      }

    }

  }
