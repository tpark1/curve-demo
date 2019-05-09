

import java.util.Random;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class Display extends JPanel {
  Graphics g;
  ArrayList<Point> points1 = new ArrayList<>();
  ArrayList<Point> points2 = new ArrayList<>();
  ArrayList<Point> points3 = new ArrayList<>();
  ArrayList<Point> curve1 = new ArrayList<>();
  ArrayList<Point> curve2 = new ArrayList<>();
  ArrayList<Point> curve3 = new ArrayList<>();
  Timer timer;
  float step = 0.001f;
  int pointRadius = 1;
  int controlPointRadius = 10;
  double epsilon = 3.0f;

  Color c1 = Color.red;
  Color c2 = Color.blue;
  Color c3 = Color.green;

  public Display() {
    super();
    // timer = new Timer(40, new ActionListener() {
    //   public void actionPerformed(ActionEvent e) {
    //     ArrayList<Point> newCurve = new ArrayList<>();
    //     Point a = new Point(0, 5);
    //     ArrayList<Point> curve = chooseCurve(1);
    //     for (int i = 0; i<curve.size(); i++) {
    //       newCurve.add(UtilityFunctions.add(a, curve.get(i)));
    //     }
    //     setCurve(newCurve, 1);
    //     g.clearRect(0, 0, (int)getSize().getWidth(), (int)getSize().getHeight());
    //     drawCurve(4);
    //   }
    // });
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawCurve(4);
  }

  public void setGraphics(Graphics g) {
    this.g = g;
  }

  private void setCurve(ArrayList<Point> newCurve, int curveNum) {
    if (curveNum == 1) {
      curve1 = newCurve;
    }
    else if (curveNum == 2) {
      curve2 = newCurve;
    }
    else if (curveNum == 3) {
      curve3 = newCurve;
    }
    else {
      // do nothing
    }
  }

  private ArrayList<Point> choosePoints(int curveNum) {
    if (curveNum == 1) {
      return points1;
    }
    else if (curveNum == 2) {
      return points2;
    }
    else if (curveNum == 3) {
      return points3;
    }
    else {
      return null;
    }
  }

  private ArrayList<Point> chooseCurve(int curveNum) {
    if (curveNum == 1) {
      return curve1;
    }
    else if (curveNum == 2) {
      return curve2;
    }
    else if (curveNum == 3) {
      return curve3;
    }
    else {
      return null;
    }
  }

  private Color chooseColor(int curveNum) {
    if (curveNum == 1) {
      return c1;
    }
    else if (curveNum == 2) {
      return c2;
    }
    else if (curveNum == 3) {
      return c3;
    }
    else {
      return null;
    }
  }

  public boolean addPoint(int x, int y, int curveNum) {
    Graphics2D g2d = (Graphics2D) g;
    if (curveNum == 1) {
      g2d.setColor(c1);
      points1.add(new Point(x,y));
    }
    else if (curveNum == 2) {
      g2d.setColor(c2);
      points2.add(new Point(x,y));
    }
    else {
      return false;
    }
    g2d.fillOval(x, y, controlPointRadius, controlPointRadius);
    return true;
  }

  public void clearFrame() {
    points1 = new ArrayList<>();
    points2 = new ArrayList<>();
    points3 = new ArrayList<>();
    curve1 = new ArrayList<>();
    curve2 = new ArrayList<>();
    curve3 = new ArrayList<>();
    g.clearRect(0, 0, (int)this.getSize().getWidth(), (int)this.getSize().getHeight());
  }

  private void drawPoints(int curveNum) {
    Graphics2D g2d = (Graphics2D) g;
    g.setColor(chooseColor(curveNum));
    for (Point p : choosePoints(curveNum)) {
      g2d.fillOval(p.x, p.y, controlPointRadius, controlPointRadius);
    }
  }

  private void closePoints(int curveNum) {
    if (curveNum == 1) {
      points1.add(points1.get(0));
    }
    else if (curveNum == 2) {
      points2.add(points2.get(0));
    }
    else if (curveNum == 3) {
      points3.add(points3.get(0));
    }
  }

  public boolean drawCurve(int curveNum) {
    closePoints(curveNum);
    if (curveNum == 1) {
      curve1 = computeCurve(curveNum);
    }
    else if (curveNum == 2) {
      curve2 = computeCurve(curveNum);
    }
    else if (curveNum == 4) {
      // provide an option to skip computation
      curveNum = 1;
    }
    else if (curveNum == 5) {
      // provide an option to skip computation
      curveNum = 2;
    }
    else {
      return false;
    }

    // Graphics2D g2d = (Graphics2D) g;
    g.setColor(chooseColor(curveNum));
    for (Point p : chooseCurve(curveNum)) {
      g.drawOval(p.x, p.y, pointRadius, pointRadius);
    }
    return true;
  }

  public ArrayList<Point> computeCurve(int curveNum){
    // calculating curve
    ArrayList<Point> points = choosePoints(curveNum);
    ArrayList<Point> curve = new ArrayList<>();
    if(points.size() > 1){
      float t = 0;
      while(t <= 1){
        Point p = computeBezier(t, points, curve);
        curve.add(p);
        t += step;
      }
    }
    return curve;
  }

  // private void redrawCurve(ArrayList<Point> curve)  {
  // g.clearRect(0, 0, (int)frame.getSize().getWidth(), (int)frame.getSize().getHeight());
  // for (Point p : curve) {
  //   g.drawOval(p.x, p.y, pointRadius, pointRadius);
  // }
  // frame.getContentPane().validate();
  // frame.getContentPane().repaint();
  // }

  // Function that computes the location of point at time t
  // We use points.size()-1 for binomialCoefficient because there is one more point than segment
  private Point computeBezier(float t, ArrayList<Point> points, ArrayList<Point> curve) {
    double x = 0;
    double y = 0;
    double coefficients[] = new double[points.size()];
    for (int i = 0; i<points.size(); i++) {
      coefficients[i] = UtilityFunctions.binomialCoefficient(points.size()-1, i) * Math.pow(1-t, points.size()-1-i) * Math.pow(t, i);
    }

    for(int i = 0; i < points.size();  i++){
      x += coefficients[i] * points.get(i).x;
      y += coefficients[i] * points.get(i).y;
    }
    g.drawOval((int)x, (int)y, pointRadius, pointRadius);
    return new Point((int)x, (int)y);
  }

  public void shortenCalled(int curveNum) {
    ArrayList<Point> newCurve = new ArrayList<>();
    Point a = new Point(0, 5);
    ArrayList<Point> curve = chooseCurve(1);
    for (int i = 0; i<curve.size(); i++) {
      newCurve.add(UtilityFunctions.add(a, curve.get(i)));
    }
    setCurve(newCurve, 1);
    // g.clearRect(0, 0, (int)getSize().getWidth(), (int)getSize().getHeight());
    // drawCurve(4);
    repaint();
    // timer.start();
    // return output;
  }

  // CHECK INTERACCTIVE DEMO TO SEE HOW HE DID CURVE SHORTENING
  // Think I should just calculate derivatives without thinking about t
  private ArrayList<Point> trackFront(ArrayList<Point> curve) {

    ArrayList<Point> output = new ArrayList<>();
    Point a = new Point(0, 5);
    for (int i = 0; i<curve.size(); i++) {
      output.add(UtilityFunctions.add(a, curve.get(i)));
    }
    return output;

    // System.out.println(curve.get(0));
    // System.out.println(curve.get(1));
    // System.out.println(curve.get(2));
    // System.out.println(curve.get(3));
    // System.out.println(curve.get(4));
    // // approximate first derivative with Central Difference method
    // // use Forward and Backward Differences for first and last points
    // Point2D.Double firstD[] = new Point2D.Double[curve.size()];
    // for (int i = 0; i<curve.size(); i++) {
    //   if (i == 0) {
    //     Point p1 = curve.get(i+1);
    //     Point p2 = curve.get(i);
    //     firstD[i] = new Point2D.Double((p2.x - p1.x)/step, (p2.y - p1.y)/step);
    //   }
    //   else if(i == curve.size()-1) {
    //     Point p1 = curve.get(i);
    //     Point p2 = curve.get(i-1);
    //     firstD[i] = new Point2D.Double((p2.x - p1.x)/step, (p2.y - p1.y)/step);
    //   }
    //   else {
    //     Point p1 = curve.get(i+1);
    //     Point p2 = curve.get(i-1);
    //     firstD[i] = new Point2D.Double((p2.x - p1.x)/(2*step), (p2.y - p1.y)/(2*step));
    //   }
    // }
    // int factor = 10;
    //
    // firstD = UtilityFunctions.rescale(firstD, factor);
    //
    // // approximate all second derivatives with Central Difference, approximating the numerator
    // // Again, use Forward and Backward Differences for first and last points
    // Point2D.Double secondD[] = new Point2D.Double[curve.size()];
    // for (int i = 0; i<curve.size(); i++) {
    //   if (i == 0) {
    //     Point2D.Double p1 = firstD[i+1];
    //     Point2D.Double p2 = firstD[i];
    //     secondD[i] = new Point2D.Double((p2.x - p1.x)/step, (p2.y - p1.y)/step);
    //   }
    //   else if(i == curve.size()-1) {
    //     Point2D.Double p1 = firstD[i];
    //     Point2D.Double p2 = firstD[i-1];
    //     secondD[i] = new Point2D.Double((p2.x - p1.x)/step, (p2.y - p1.y)/step);
    //   }
    //   else {
    //     Point2D.Double p1 = firstD[i+1];
    //     Point2D.Double p2 = firstD[i-1];
    //     secondD[i] = new Point2D.Double((p2.x - p1.x)/(2*step), (p2.y - p1.y)/(2*step));
    //   }
    // }
    //
    // secondD = UtilityFunctions.rescale(secondD, factor);
    //
    //
    // double curvature[] = new double[curve.size()];
    // for (int i = 0; i<curve.size(); i++) {
    //   curvature[i] = UtilityFunctions.crossProductMagnitude(firstD[i], secondD[i]) / Math.pow(UtilityFunctions.magnitude(firstD[i]), 3);
    //   // if (curvature[i] < 0) {
    //   //   curvature[i] = Math.floor(factor * curvature[i]);
    //   // }
    //   // else {
    //   //   curvature[i] = factor * Math.ceil(curvature[i]);
    //   // }
    // }
    //
    //
    //
    // for (int i = 0;i<5 ; i++) {
    //   System.out.println(firstD[i]);
    //   System.out.println(secondD[i]);
    //   System.out.println(curvature[i]);
    //
    // }
    //
    // // we note that the tangent vector values have been calculated with firstD and the
    // // derivative of the tangent vectors have been calculated with secondD, so secondD = normalVectors
    // ArrayList<Point> newPoints = new ArrayList<>();
    // for (int i = 0; i<curve.size(); i++) {
    //   // move point
    //   Point p = curve.get(i);
    //   newPoints.add(new Point((int)(p.x + secondD[i].x * curvature[i]), (int)(p.y + secondD[i].y * curvature[i])));
    // }
    //
    // for (int i = 0; i<5; i++) {
    //   System.out.println(newPoints.get(i));
    // }
    //
    //
    // // redrawCurve(newPoints);
    // return newPoints;
  }

  public void convoluteCurves() {
    // public void convoluteCurves() throws IOException {
    // g.setColor(Color.green);

    // System.out.println("Control Points 1");
    // for (Point p : points1) {
    //   System.out.println(p);
    // }
    //
    // System.out.println("Control Points 2");
    // for (Point p : points2) {
    //   System.out.println(p);
    // }


    ArrayList<Point> convolution = new ArrayList<>();

    // first compute slopes of curve2
    double slopes[] = new double[curve2.size()];
    for (int i = 0; i<curve2.size(); i++) {
      slopes[i] = UtilityFunctions.test(curve2, i);
    }

    // BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\New folder\\swarthmore\\senior\\geometry\\curve-demo\\temp.txt"));

    // String fileContent = "";
    // for (int i = 0; i<slopes.length; i++) {
    //   fileContent += "\n";
    //   fileContent += (Double.toString(curve2.get(i).x) + ", " + Double.toString(curve2.get(i).y));
    //   fileContent += "\n";
    //   fileContent += Double.toString(slopes[i]);
    // }
    // writer.write(fileContent);
    // writer.close();


    // now jump into convolution
    for (int i = 0; i<points1.size(); i++) {
      // find slope of cur ctrl point.
      double slope;
      if (i == 0) {
        slope = UtilityFunctions.slope(points1.get(points1.size()-2), points1.get(i+1));
      }
      else if (i == points1.size()-1) {
        slope = UtilityFunctions.slope(points1.get(i-1), points1.get(1));
      }
      else {
        slope = UtilityFunctions.slope(points1.get(i-1), points1.get(i+1));
      }
      // System.out.println("The slope of the " + i + "th point is: " + slope);

      // find points in curve2 that are parallel
      ArrayList<Point> candidates = new ArrayList<>();
      for (int j = 0; j<curve2.size(); j++) {
        if (j == 0) {
          if ((slopes[slopes.length-1] > slope) && (slopes[j] < slope)) {
            if ((slopes[j] - slope < epsilon)) {
              candidates.add(curve2.get(j));
            }
          }
          else if ((slopes[slopes.length-1] < slope) && (slopes[j] > slope)) {
            if ((slopes[j] - slope < epsilon)) {
              candidates.add(curve2.get(j));
            }
          }
        }
        else {
          if ((slopes[j-1] > slope) && (slopes[j] < slope)) {
            if ((slopes[j] - slope < epsilon)) {
              candidates.add(curve2.get(j));
            }
          }
          else if ((slopes[j-1] < slope) && (slopes[j] > slope)) {
            if ((slopes[j] - slope < epsilon)) {
              candidates.add(curve2.get(j));
            }
          }
        }
      }
      // System.out.println("Candidates size: " + candidates.size());
      // for (Point c : candidates) {
      //   System.out.println(c.x + ", " + c.y);
      // }
      // find best point
      Point2D.Double center1 = UtilityFunctions.center(points1);
      Point2D.Double center2 = UtilityFunctions.center(points2);
      double max = 0.0f;
      Point n = null;
      for (Point cand : candidates) {
        Point displacementVector = UtilityFunctions.subtract(center2, UtilityFunctions.convert(cand));
        Point simulatedCtrlPointLocation = UtilityFunctions.add(points1.get(i), displacementVector);
        double distance = UtilityFunctions.distance(center1, simulatedCtrlPointLocation);
        Point realCtrlPointLocation = UtilityFunctions.add(points1.get(i), cand);
        if (distance > max) {
          max = distance;
          n = realCtrlPointLocation;
        }
      }

      // System.out.println("We added: " + n);
      convolution.add(n);
    }
    // remove any null entries - couldn't find parallel places
    Iterator it = convolution.iterator();
    while(it.hasNext()) {
      if (it.next() == null) {
        it.remove();
      }
    }
    points3 = convolution;
    drawPoints(3);
    curve3 = computeCurve(3);
    drawCurve(3);
  }


}
