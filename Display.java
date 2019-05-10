

import java.util.Random;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.util.Pair;
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

  int r = 5;

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
    if ((curveNum == 1) || (curveNum == 4)) {
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
    else if (curveNum == 4) {
      return c2;
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
      // curveNum = 1;
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

  // always points inwards
  private ArrayList<Point> naive(ArrayList<Point> curve) throws IOException {
    ArrayList<Point> newCurve = new ArrayList<>();

    double slopes[] = new double[curve.size()];
    boolean bools[] = new boolean[curve.size()];

    BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\New folder\\swarthmore\\senior\\geometry\\curve-demo\\temp.txt"));
    String fileContent = "";

    // scale curvatures
    double curvatures[] = new double[curve.size()];
    for (int i = 0; i<curve.size(); i++) {
      curvatures[i] = UtilityFunctions.curvature(curve, i);
      slopes[i] = (UtilityFunctions.computeTangentVector(curve, i)).getKey();
      bools[i] = (UtilityFunctions.computeTangentVector(curve, i)).getValue();
    }

    curvatures = UtilityFunctions.rescale(curvatures, 10.0);


    for (int i = 0; i<curve.size(); i++) {
      Point2D.Double tangent = UtilityFunctions.getVector(slopes[i], 10);

      Point2D.Double perp;
      if (bools[i]) {
         perp = new Point2D.Double(-1.0 * tangent.y, tangent.x);
      }
      else {
        perp = new Point2D.Double(tangent.y, -1.0 * tangent.x);
      }


      double scalar = 0.5;
      Point scaledNormal = new Point((int)(perp.x * scalar), (int)(perp.y * scalar));

      fileContent += "\n";
      fileContent += Math.round(slopes[i] * 100)/100.0;
      fileContent += " , ";
      fileContent += tangent;
      fileContent += " , ";
      fileContent += perp;
      fileContent += " , ";
      fileContent += scaledNormal;

      newCurve.add(UtilityFunctions.add(curve.get(i), scaledNormal));
    }
    writer.write(fileContent);
    writer.close();

    // double curvature = UtilityFunctions.curvature(curve, i);

    // check for redundant points
    Iterator it = newCurve.iterator();
    Point previous = null;
    while (it.hasNext()) {
      if (it.next() == previous) {
        previous = (Point)it.next();
        it.remove();
        break;
      }
    }
    // check for rogue points
    ArrayList<Point> finalCurve = new ArrayList<>();
    for (int i = 0; i<newCurve.size(); i++) {
      double distance1;
      double distance2;
      if (i == 0) {
        distance1 = UtilityFunctions.distance(UtilityFunctions.convert(newCurve.get(i)), newCurve.get(newCurve.size()-2));
        distance2 = UtilityFunctions.distance(UtilityFunctions.convert(newCurve.get(i)), newCurve.get(i+1));
      }
      else if (i == (newCurve.size()-1)) {
        distance1 = UtilityFunctions.distance(UtilityFunctions.convert(newCurve.get(i)), newCurve.get(i-1));
        distance2 = UtilityFunctions.distance(UtilityFunctions.convert(newCurve.get(i)), newCurve.get(1));
      }
      else {
        distance1 = UtilityFunctions.distance(UtilityFunctions.convert(newCurve.get(i)), newCurve.get(i-1));
        distance2 = UtilityFunctions.distance(UtilityFunctions.convert(newCurve.get(i)), newCurve.get(i+1));
      }
      if (!((Math.abs(distance1) > 10) || (Math.abs(distance2) > 10))) {
        finalCurve.add(newCurve.get(i));
      }
    }
    return finalCurve;
  }

  private ArrayList<Point> randomAttempt(ArrayList<Point> curve) throws IOException {
    ArrayList<Point> newCurve = new ArrayList<>();

    double slopes[] = new double[curve.size()];
    boolean bools[] = new boolean[curve.size()];

    BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\New folder\\swarthmore\\senior\\geometry\\curve-demo\\temp.txt"));
    String fileContent = "";

    // scale curvatures
    double curvatures[] = new double[curve.size()];
    for (int i = 0; i<curve.size(); i++) {
      curvatures[i] = UtilityFunctions.curvature(curve, i);
      slopes[i] = (UtilityFunctions.computeTangentVector(curve, i)).getKey();
      bools[i] = (UtilityFunctions.computeTangentVector(curve, i)).getValue();
    }

    curvatures = UtilityFunctions.rescale(curvatures, 10.0);


    for (int i = 0; i<curve.size(); i++) {
      double TaSlope = slopes[i];
      double TbSlope;
      if (i > curve.size()-1-r) {
        TbSlope = slopes[i-r];
      }
      else {
        TbSlope = slopes[i+r];
      }

      Point2D.Double Ta = UtilityFunctions.getVector(TaSlope, 10);
      Point2D.Double Tb = UtilityFunctions.getVector(TbSlope, 10);
      if (!bools[i]) {
        Ta = UtilityFunctions.scale2(Ta, -1.0);
        Tb = UtilityFunctions.scale2(Tb, -1.0);
      }

      Point2D.Double normal = UtilityFunctions.subtract(Ta, Tb);
      Point2D.Double unitNormal = UtilityFunctions.scale2(normal, UtilityFunctions.magnitude(normal));

      // double normalVectorSlope = UtilityFunctions.perpendicular(slopes[i]);
      // Point2D.Double normal = new Point2D.Double(1.0, normalVectorSlope);
      // Point2D.Double unitNormal = UtilityFunctions.scale2(normal, UtilityFunctions.magnitude(normal));
      // double scalar = curvatures[i];
      double scalar = 20;
      Point scaledNormal = new Point((int)(unitNormal.x * scalar), (int)(unitNormal.y * scalar));

      fileContent += "\n";
      fileContent += Math.round(slopes[i] * 100)/100.0;
      fileContent += " , ";
      fileContent += Ta;
      fileContent += " , ";
      fileContent += Tb;
      fileContent += " , ";
      fileContent += normal;
      fileContent += " , ";
      fileContent += scaledNormal;


      newCurve.add(UtilityFunctions.add(curve.get(i), scaledNormal));
    }
    writer.write(fileContent);
    writer.close();

    // double curvature = UtilityFunctions.curvature(curve, i);
    return newCurve;
  }

  public void shortenCalled(int curveNum) throws IOException {
    ArrayList<Point> newCurve = new ArrayList<>();
    // Point a = new Point(0, 5);
    // ArrayList<Point> curve = chooseCurve(1);
    // for (int i = 0; i<curve.size(); i++) {
    //   newCurve.add(UtilityFunctions.add(a, curve.get(i)));
    // }
    // setCurve(newCurve, 1);
    // g.clearRect(0, 0, (int)getSize().getWidth(), (int)getSize().getHeight());
    // drawCurve(4);
    // repaint();
    // timer.start();
    // return output;

    // BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\New folder\\swarthmore\\senior\\geometry\\curve-demo\\temp.txt"));
    // String fileContent = "";
    // writer.write(fileContent);
    //
    // ArrayList<Point> curve = chooseCurve(curveNum);
    // for (int i = 0; i<curve.size(); i++) {
    //   Point laplacian;
    //   double dr2;
    //   if (i < (r + 1)) {
    //     laplacian = UtilityFunctions.add(UtilityFunctions.scale(curve.get(r), 2), UtilityFunctions.add(curve.get(i+r), curve.get(curve.size()-1-i)));
    //     dr2 = UtilityFunctions.magnitude2(UtilityFunctions.subtract2(curve.get(i+r),curve.get(curve.size()-1-i))) * 0.25;
    //   }
    //   else if (i > curve.size()-1-r) {
    //     laplacian = UtilityFunctions.add(UtilityFunctions.scale(curve.get(r), 2), UtilityFunctions.add(curve.get(i+r-curve.size()), curve.get(i-r)));
    //     dr2 = UtilityFunctions.magnitude2(UtilityFunctions.subtract2(curve.get(i+r-curve.size()),curve.get(i-r))) * 0.25;
    //   }
    //   else {
    //     laplacian = UtilityFunctions.add(UtilityFunctions.scale(curve.get(r), 2), UtilityFunctions.add(curve.get(i+r), curve.get(i-r)));
    //     dr2 = UtilityFunctions.magnitude2(UtilityFunctions.subtract2(curve.get(i+r),curve.get(i-r))) * 0.25;
    //   }
    //   // Point laplacian = UtilityFunctions.add(UtilityFunctions.scale(curve.get(r), 2), UtilityFunctions.add(curve.get(i+r), curve.get(i-r)));
    //   // double dr2 = UtilityFunctions.magnitude2(UtilityFunctions.subtract2(curve.get(i+r),curve.get(i-r))) * 0.25;
    //   double curvature = UtilityFunctions.curvature(curve, i);
    //   Point scaledL = UtilityFunctions.scale(laplacian, 1.0/(dr2 * curvature));
    //   writer.write("\n");
    //   writer.write("laplacian: " + laplacian + " , dr2: " + dr2 + " , curvature: " + curvature + ", scaledL: " + scaledL);
    //   Point x = UtilityFunctions.add(curve.get(i), scaledL);
    //   newCurve.add(x);
    // }
    //
    //
    // for (int j = 0; j<curve.size(); j++) {
    //   fileContent += "\n";
    //   fileContent += (Double.toString(curve.get(j).x) + ", " + Double.toString(curve.get(j).y));
    //   fileContent += " --> ";
    //   fileContent += (Double.toString(newCurve.get(j).x) + ", " + Double.toString(newCurve.get(j).y));
    // }
    // writer.append(fileContent);
    // writer.close();

    newCurve = randomAttempt(chooseCurve(curveNum));
    curve1 = newCurve;
    drawCurve(4);
  }

  // Think I should just calculate derivatives without thinking about t
  private ArrayList<Point> trackFront(ArrayList<Point> curve) {

    ArrayList<Point> output = new ArrayList<>();
    // Point a = new Point(0, 5);
    // for (int i = 0; i<curve.size(); i++) {
    //   output.add(UtilityFunctions.add(a, curve.get(i)));
    // }
    // return output;

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
    //   if (curvature[i] < 0) {
    //     curvature[i] = Math.floor(factor * curvature[i]);
    //   }
    //   else {
    //     curvature[i] = factor * Math.ceil(curvature[i]);
    //   }
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
    return output;
  }

  public void convoluteCurves() {
    // public void convoluteCurves() throws IOException {

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
      slopes[i] = (UtilityFunctions.computeTangentVector(curve2, i)).getKey();
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
        Point displacementVector = UtilityFunctions.toPoint(UtilityFunctions.subtract(center2, UtilityFunctions.convert(cand)));
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
