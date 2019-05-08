import java.awt.*;

public class UtilityFunctions {
  public UtilityFunctions() {}

    public static Point add(Point p1, Point p2) {
      return new Point(p1.x + p2.x, p1.y + p2.y);
    }

    public static Point subtract(Point p1, Point p2) {
      return new Point(p1.x - p2.x, p1.y - p2.y);
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
    public static int crossProductMagnitude(Point p1, Point p2) {
      return p1.x * p2.y - p1.y * p2.x;
    }

    // Magnitude
    public static double magnitude(Point p1, Point p2) {
      return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y-p2.y, 2));
    }

    // Computes slope of two points. Assumes p1 -> p2
    // public static double slope(Point p1, Point p2) {
    //   return (p2.y - p1.y)/(p2.x - p1.x);
    // }

  }
