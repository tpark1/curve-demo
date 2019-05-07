
import java.util.Random;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

public class Demo {

  Graphics g;
  ArrayList<Point> points;
  float step = 0.001f;
  JFrame frame;
  int pointRadius = 1;

  public Demo() {
    points = new ArrayList<>();

    frame = new JFrame("Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // frame.setSize(250, 200);
    frame.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    frame.setLocationRelativeTo(null);

    addComponentsToPaneAndDisplay(frame.getContentPane());
  }

  private void addComponentsToPaneAndDisplay(Container contentPane) {
    JPanel display = new JPanel();

    JPanel buttons = new JPanel();
    buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
    JButton a = new JButton("Curve 1");
    JButton b = new JButton("Curve 2");
    buttons.add(a);
    buttons.add(b);

    // add everything to contentPane and display
    contentPane.add(display, BorderLayout.CENTER);
    contentPane.add(buttons, BorderLayout.EAST);
    frame.setVisible(true);

    // add event listeners and set g
    display.setFocusable(true);
    display.requestFocus();
    display.addKeyListener(new KeyLis());
    display.addMouseListener(new MouseLis());
    g = display.getGraphics();
  }

  // Key listener class for swing
  private class KeyLis extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyChar() == 'q') { // close window
        frame.dispose();
      }
      else if(e.getKeyChar() == 'g') {
        drawCurve();
      }
      else if(e.getKeyChar() == 'c') {
        clearFrame();
      }
    }
  }

  // Mouse listener class for swing
  private class MouseLis extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      addPoint(g, (int)e.getX(), (int)e.getY());
    }
  }

  public void addPoint(Graphics g, int x, int y) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawOval(x, y, 10, 10);
    points.add(new Point(x,y));
  }

  public void clearFrame() {
    points = new ArrayList<>();
    g.clearRect(0, 0, (int)frame.getSize().getWidth(), (int)frame.getSize().getHeight());
  }

  private void drawCurve(){
    if(points.size() > 1){
      float t = 0;
      while(t <= 1){
        // System.out.println(t);
        g.setColor(Color.red);
        computeBezier(t);
        t += step;
      }
    }
  }

  // Factorial function
  private static int factorial(int n) {
    int result = 1;
    for (int i = 1; i <= n; i++) {
      result *= i;
    }
    return result;
  }

  // Function that computes the binomial coefficent. Utilizes fact that in binomial coefficent, one of the factorials in denominator
  // will always cancel out with terms in numerator
  private static double binomialCoefficient(int n, int i) {
    int numerator = 1;
    for (int j = n; j>i; j--) {
      numerator *= j;
    }
    return numerator / factorial(n-i);
  }

  // Function that computes the location of point at time t
  // We use points.size()-1 for binomialCoefficient because there is one more point than segment
  private void computeBezier(float t) {
    double x = 0;
    double y = 0;
    double coefficients[] = new double[points.size()];
    for (int i = 0; i<points.size(); i++) {
      coefficients[i] = binomialCoefficient(points.size()-1, i) * Math.pow(1-t, points.size()-1-i) * Math.pow(t, i);
    }

    for(int i = 0; i < points.size();  i++){
      x += coefficients[i] * points.get(i).x;
      y += coefficients[i] * points.get(i).y;
    }
    g.drawOval((int)x, (int)y, pointRadius, pointRadius);
  }

  // public void paintComponent(Graphics g) {

    // System.out.println("Paint called");
    // super.paintComponent(g);

    // Graphics2D g2d = (Graphics2D) g;

    // g2d.setColor(Color.red);

  // }

  public static void main(String[] args) {
    Demo demo = new Demo();
  }
}
