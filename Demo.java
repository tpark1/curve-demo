
import java.util.Random;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class Demo {

  Graphics g;
  Timer timer;
  ArrayList<Point> points1;
  ArrayList<Point> points2;
  ArrayList<Point> points3;
  ArrayList<Point> curve1;
  ArrayList<Point> curve2;
  ArrayList<Point> curve3;
  float step = 0.001f;
  JFrame frame;
  JTextArea textArea;
  int curCurve = -1;
  int pointRadius = 1;
  int controlPointRadius = 10;
  double epsilon = 3.0f;

  public Demo() {
    points1 = new ArrayList<>();
    points2 = new ArrayList<>();
    curve1 = new ArrayList<>();
    curve2 = new ArrayList<>();

    frame = new JFrame("Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setLocationRelativeTo(null);

    timer = new Timer(40, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // g.clearRect(0, 0, (int)frame.getSize().getWidth(), (int)frame.getSize().getHeight());
        // redrawCurve(curve1);
        frame.getContentPane().validate();

        frame.getContentPane().repaint();
      }
    });

    addComponentsToPaneAndDisplay(frame.getContentPane());
  }

  private void addComponentsToPaneAndDisplay(Container contentPane) {
    // area to display curves
    JPanel display = new JPanel();

    // buttonPanel on the right
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
    // check boxes
    JPanel checkBoxes = new JPanel();
    JCheckBox curve1CheckBox = new JCheckBox("curve 1");
    JCheckBox curve2CheckBox = new JCheckBox("curve 2");
    curve1CheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          textArea.setText("Curve 1 selected");
          g.setColor(Color.red);
          curCurve = 1;
        }
      }
    });
    curve2CheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          textArea.setText("Curve 2 selected");
          g.setColor(Color.blue);
          curCurve = 2;
        }
      }
    });
    checkBoxes.add(curve1CheckBox);
    checkBoxes.add(curve2CheckBox);
    ButtonGroup checkBoxGroup = new ButtonGroup(); // to have only one selected at a time
    checkBoxGroup.add(curve1CheckBox);
    checkBoxGroup.add(curve2CheckBox);
    buttonPanel.add(checkBoxes);
    // buttons
    JPanel buttons = new JPanel();
    JButton editButton = new JButton("Edit");
    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Editting");
      }
    });
    JButton drawButton = new JButton("Draw");
    drawButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Curve drawn");
        if (curCurve == 1) {
          points1.add(points1.get(0)); // to wrap around
          curve1 = drawCurve(points1);
        }
        else if (curCurve == 2) {
          points2.add(points2.get(0)); // to wrap around
          curve2 = drawCurve(points2);
        }
        else {
          textArea.setText("Select a curve first!");
        }
      }
    });
    JButton shortenButton = new JButton("Shorten");
    shortenButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Shortening");
        timer.start();
        if (curCurve == 1) {
          for(int i = 0; i < 100; i++) {
            curve1 = trackFront(curve1);

          }
        }
        else if (curCurve == 2) {
          for(int i = 0; i < 100; i++) {
            curve2 = trackFront(curve2);

          }
        }
        else {
          textArea.setText("Select a curve first!");
        }
      }
    });
    JButton convoluteButton = new JButton("Convolute");
    convoluteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Convoluting");
        if (curCurve == 1 || curCurve == 2) {
          try {
            points3 = convoluteCurves();

          }
          catch(Exception ex) {
            System.out.println(ex.getMessage());
          }
          System.out.println("Point3: " + points3.size());
          for (Point p : points3) {
            System.out.println(p.x + " " + p.y);
          }
          drawPoints(points3);
          drawCurve(points3);
        }
        else {
          textArea.setText("Select a curve first!");
        }
      }
    });
    buttons.add(editButton);
    buttons.add(drawButton);
    buttons.add(shortenButton);
    buttons.add(convoluteButton);
    buttonPanel.add(buttons);

    // text area to display messages at the bottom
    textArea = new JTextArea("Welcome! \nPress Curve 1 or Curve 2 and start plotting points!");
    JScrollPane scrollPane = new JScrollPane(textArea);
    textArea.setEditable(false);
    textArea.setFont(textArea.getFont().deriveFont(20f));

    // add everything to contentPane and display
    contentPane.add(display, BorderLayout.CENTER);
    contentPane.add(buttonPanel, BorderLayout.EAST);
    contentPane.add(textArea, BorderLayout.SOUTH);
    frame.setVisible(true);

    // add event listeners and set g
    display.setFocusable(true);
    curve1CheckBox.setFocusable(false);
    curve2CheckBox.setFocusable(false);
    checkBoxes.setFocusable(false);
    editButton.setFocusable(false);
    drawButton.setFocusable(false);
    shortenButton.setFocusable(false);
    convoluteButton.setFocusable(false);
    buttons.setFocusable(false);
    textArea.setFocusable(false);
    buttonPanel.setFocusable(false);
    display.requestFocus();
    display.addKeyListener(new KeyLis());
    display.addMouseListener(new MouseLis());
    g = display.getGraphics();


    // timer.start();
  }

  // Key listener class for swing
  private class KeyLis extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyChar() == 'q') { // close window
        frame.dispose();
      }
      else if(e.getKeyChar() == 'g') {
        if (curCurve == 1) {
          points1.add(points1.get(0)); // to wrap around
          curve1 = drawCurve(points1);
        }
        else if (curCurve == 2) {
          points2.add(points2.get(0)); // to wrap around
          curve2 = drawCurve(points2);
        }
        else {
          textArea.setText("Select a curve first!");
        }
      }
      else if(e.getKeyChar() == 'c') {
        clearFrame();
      }
      // frame.getContentPane().repaint();
    }
  }

  // Mouse listener class for swing
  private class MouseLis extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      if (curCurve == -1) {
        textArea.setText("Select a curve first!");
      }
      else if (curCurve == 1) {
        addPoint(g, (int)e.getX(), (int)e.getY(), points1);
      }
      else {
        addPoint(g, (int)e.getX(), (int)e.getY(), points2);
      }
    }
  }

  private void addPoint(Graphics g, int x, int y, ArrayList<Point> points) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.fillOval(x, y, controlPointRadius, controlPointRadius);
    points.add(new Point(x,y));
  }

  private void clearFrame() {
    points1 = new ArrayList<>();
    points2 = new ArrayList<>();
    g.clearRect(0, 0, (int)frame.getSize().getWidth(), (int)frame.getSize().getHeight());
  }

  private void drawPoints(ArrayList<Point> points) {
    Graphics2D g2d = (Graphics2D) g;
    for (Point p : points) {
      g2d.fillOval(p.x, p.y, controlPointRadius, controlPointRadius);
    }
  }

  private ArrayList<Point> drawCurve(ArrayList<Point> points){
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

  private void redrawCurve(ArrayList<Point> curve)  {
    // g.clearRect(0, 0, (int)frame.getSize().getWidth(), (int)frame.getSize().getHeight());
    for (Point p : curve) {
      g.drawOval(p.x, p.y, pointRadius, pointRadius);
    }
    // frame.getContentPane().validate();
    // frame.getContentPane().repaint();
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


  // CHECK INTERACCTIVE DEMO TO SEE HOW HE DID CURVE SHORTENING
  // Think I should just calculate derivatives without thinking about t
  private ArrayList<Point> trackFront(ArrayList<Point> curve) {
    System.out.println(curve.get(0));
    System.out.println(curve.get(1));
    System.out.println(curve.get(2));
    System.out.println(curve.get(3));
    System.out.println(curve.get(4));
    // approximate first derivative with Central Difference method
    // use Forward and Backward Differences for first and last points
    Point2D.Double firstD[] = new Point2D.Double[curve.size()];
    for (int i = 0; i<curve.size(); i++) {
      if (i == 0) {
        Point p1 = curve.get(i+1);
        Point p2 = curve.get(i);
        firstD[i] = new Point2D.Double((p2.x - p1.x)/step, (p2.y - p1.y)/step);
      }
      else if(i == curve.size()-1) {
        Point p1 = curve.get(i);
        Point p2 = curve.get(i-1);
        firstD[i] = new Point2D.Double((p2.x - p1.x)/step, (p2.y - p1.y)/step);
      }
      else {
        Point p1 = curve.get(i+1);
        Point p2 = curve.get(i-1);
        firstD[i] = new Point2D.Double((p2.x - p1.x)/(2*step), (p2.y - p1.y)/(2*step));
      }
    }
    int factor = 10;

    firstD = UtilityFunctions.rescale(firstD, factor);

    // approximate all second derivatives with Central Difference, approximating the numerator
    // Again, use Forward and Backward Differences for first and last points
    Point2D.Double secondD[] = new Point2D.Double[curve.size()];
    for (int i = 0; i<curve.size(); i++) {
      if (i == 0) {
        Point2D.Double p1 = firstD[i+1];
        Point2D.Double p2 = firstD[i];
        secondD[i] = new Point2D.Double((p2.x - p1.x)/step, (p2.y - p1.y)/step);
      }
      else if(i == curve.size()-1) {
        Point2D.Double p1 = firstD[i];
        Point2D.Double p2 = firstD[i-1];
        secondD[i] = new Point2D.Double((p2.x - p1.x)/step, (p2.y - p1.y)/step);
      }
      else {
        Point2D.Double p1 = firstD[i+1];
        Point2D.Double p2 = firstD[i-1];
        secondD[i] = new Point2D.Double((p2.x - p1.x)/(2*step), (p2.y - p1.y)/(2*step));
      }
    }

    secondD = UtilityFunctions.rescale(secondD, factor);


    double curvature[] = new double[curve.size()];
    for (int i = 0; i<curve.size(); i++) {
      curvature[i] = UtilityFunctions.crossProductMagnitude(firstD[i], secondD[i]) / Math.pow(UtilityFunctions.magnitude(firstD[i]), 3);
      // if (curvature[i] < 0) {
      //   curvature[i] = Math.floor(factor * curvature[i]);
      // }
      // else {
      //   curvature[i] = factor * Math.ceil(curvature[i]);
      // }
    }



    for (int i = 0;i<5 ; i++) {
      System.out.println(firstD[i]);
      System.out.println(secondD[i]);
      System.out.println(curvature[i]);

    }

    // we note that the tangent vector values have been calculated with firstD and the
    // derivative of the tangent vectors have been calculated with secondD, so secondD = normalVectors
    ArrayList<Point> newPoints = new ArrayList<>();
    for (int i = 0; i<curve.size(); i++) {
      // move point
      Point p = curve.get(i);
      newPoints.add(new Point((int)(p.x + secondD[i].x * curvature[i]), (int)(p.y + secondD[i].y * curvature[i])));
    }

    for (int i = 0; i<5; i++) {
      System.out.println(newPoints.get(i));
    }


    // redrawCurve(newPoints);
    return newPoints;
  }

  private ArrayList<Point> convoluteCurves() throws IOException {
    g.setColor(Color.green);

    // System.out.println("Control Points 1");
    // for (Point p : points1) {
    //   System.out.println(p.x + ", " + p.y);
    // }
    //
    // System.out.println("Control Points 2");
    // for (Point p : points2) {
    //   System.out.println(p.x + ", " + p.y);
    // }


    ArrayList<Point> convolution = new ArrayList<>();

    // first compute slopes of curve2
    double slopes[] = new double[curve2.size()];
    for (int i = 0; i<curve2.size(); i++) {
      slopes[i] = UtilityFunctions.test(curve2, i);
    }

    BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\New folder\\swarthmore\\senior\\geometry\\curve-demo\\temp.txt"));

    String fileContent = "";
    for (int i = 0; i<slopes.length; i++) {
      fileContent += "\n";
      fileContent += (Double.toString(curve2.get(i).x) + ", " + Double.toString(curve2.get(i).y));
      fileContent += "\n";
      fileContent += Double.toString(slopes[i]);
    }
    writer.write(fileContent);
    writer.close();


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
    return convolution;

  }

  public static void main(String[] args) {
    Demo demo = new Demo();
  }
}
