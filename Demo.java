
import java.util.Random;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

public class Demo {

  Graphics g;
  ArrayList<Point> points1;
  ArrayList<Point> points2;
  float step = 0.001f;
  JFrame frame;
  JTextArea textArea;
  int curCurve = -1;
  int pointRadius = 1;

  public Demo() {
    points1 = new ArrayList<>();
    points2 = new ArrayList<>();

    frame = new JFrame("Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // frame.setSize(250, 200);
    // frame.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setLocationRelativeTo(null);

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
      }
    });
    buttons.add(editButton);
    buttons.add(drawButton);
    buttonPanel.add(buttons);

    // text area to display messages at the bottom
    textArea = new JTextArea("Welcome! \nPress Curve 1 or Curve 2 and start plotting points!");
    JScrollPane scrollPane = new JScrollPane(textArea);
    textArea.setEditable(false);

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
    buttons.setFocusable(false);
    textArea.setFocusable(false);
    buttonPanel.setFocusable(false);
    display.requestFocus();
    display.addKeyListener(new KeyLis());
    display.addMouseListener(new MouseLis());
    g = display.getGraphics();
  }

  // Key listener class for swing
  private class KeyLis extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      // e.consume();
      if (e.getKeyChar() == 'q') { // close window
        frame.dispose();
      }
      else if(e.getKeyChar() == 'g') {
        if (curCurve == 1) {
          drawCurve(points1);
        }
        else if (curCurve == 2) {
          drawCurve(points2);
        }
        else {
          textArea.setText("Select a curve first!");
        }
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

  public void addPoint(Graphics g, int x, int y, ArrayList<Point> points) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawOval(x, y, 10, 10);
    points.add(new Point(x,y));
  }

  public void clearFrame() {
    points1 = new ArrayList<>();
    points2 = new ArrayList<>();
    g.clearRect(0, 0, (int)frame.getSize().getWidth(), (int)frame.getSize().getHeight());
  }

  private void drawCurve(ArrayList<Point> points){
    if(points.size() > 1){
      float t = 0;
      while(t <= 1){
        computeBezier(t, points);
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
  private void computeBezier(float t, ArrayList<Point> points) {
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

  public static void main(String[] args) {
    Demo demo = new Demo();
  }
}
