
import java.util.Random;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class Demo {

  JFrame frame;
  JTextArea textArea;
  int curveNum = -1;
  Display display;
  Timer timer1;
  Timer timer2;
  Color sidePanelBackground = Color.orange;
  boolean editting = false;
  boolean holdingPoint = false;
  Point editPoint = null;

  public Demo() {
    frame = new JFrame("Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setLocationRelativeTo(null);
    timer1 = new Timer(40, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          if (display.shortenCalled(curveNum, 1)) {
            timer1.stop();
            textArea.setText("Shortening complete.\n");
          }
          display.repaint();
        }
        catch (Exception ex) {
          timer1.stop();
          textArea.setText("Shortening complete.\n");
        }
      }
    });
    timer2 = new Timer(40, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          if (display.shortenCalled(curveNum, 2)) {
            timer2.stop();
            textArea.setText("Shortening complete.\n");
          }
          display.repaint();
        }
        catch (Exception ex) {
          timer2.stop();
          textArea.setText("Shortening complete.\n");
        }
      }
    });

    addComponentsToPaneAndDisplay(frame.getContentPane());
  }

  private void addComponentsToPaneAndDisplay(Container contentPane) {
    // area to display curves
    display = new Display();

    // buttonPanel on the right
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
    buttonPanel.setBackground(sidePanelBackground);
    // check boxes
    JPanel checkBoxes = new JPanel();
    checkBoxes.setBackground(sidePanelBackground);
    JCheckBox curve1CheckBox = new JCheckBox("curve 1");
    JCheckBox curve2CheckBox = new JCheckBox("curve 2");
    curve1CheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          textArea.setText("Curve 1 selected.\n");
          curveNum = 1;
        }
      }
    });
    curve2CheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          textArea.setText("Curve 2 selected.\n");
          curveNum = 2;
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
    buttons.setLayout(new GridLayout(5,2, 10,10));
    buttons.setBackground(sidePanelBackground);
    JCheckBox editButton = new JCheckBox("Edit");
    editButton.setBackground(sidePanelBackground);
    editButton.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          editting = true;
          textArea.setText("Editting mode.\n");
        }
        else {
          editting = false;
          textArea.setText("Plotting mode.\n");
        }
      }
    });
    JButton drawButton = new JButton("Draw");
    drawButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(display.drawCurve(curveNum)) {
          textArea.setText("Select a curve first!\n");
        }
        else {
          textArea.setText("Curve drawn.\n");
        }
      }
    });
    JButton shortenButton1 = new JButton("Shorten - Resample Step");
    shortenButton1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Shortening with resampling.\n");
        try {
          display.shortenCalled(curveNum, 1);
        }
        catch (Exception ex) {
          System.out.println(ex.getMessage());
        }
      }
    });
    JButton shortenButton2 = new JButton("Shorten - Front Tracking Step");
    shortenButton2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Shortening with front tracking.\n");
        try {
          display.shortenCalled(curveNum, 2);
        }
        catch (Exception ex) {
          System.out.println(ex.getMessage());
          ex.printStackTrace();
        }
      }
    });
    JButton shortenButton3 = new JButton("Shorten - Resample");
    shortenButton3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Shortening with resampling.\n");
        try {
          display.shortenCalled(curveNum, 1);
        }
        catch (Exception ex) {
          System.out.println(ex.getMessage());
        }
        timer1.start();
      }
    });
    JButton shortenButton4 = new JButton("Shorten - Front Tracking");
    shortenButton4.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Shortening with front tracking.\n");
        try {
          display.shortenCalled(curveNum, 2);
        }
        catch (Exception ex) {
          System.out.println(ex.getMessage());
        }
        timer2.start();
      }
    });
    JButton convoluteButton = new JButton("Convolute");
    convoluteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Convoluting.\n");
        display.convoluteCurves();
      }
    });
    JButton refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        display.clearScreen();
        display.drawCurve(curveNum+3);
      }
    });
    JButton clearButton = new JButton("Clear");
    clearButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        display.clearFrame();
        textArea.setText("Points and curves cleared.\n");
      }
    });
    buttons.add(editButton);
    buttons.add(drawButton);
    buttons.add(shortenButton1);
    buttons.add(shortenButton2);
    buttons.add(shortenButton3);
    buttons.add(shortenButton4);
    buttons.add(convoluteButton);
    buttons.add(refreshButton);
    buttons.add(clearButton);
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

    // set focus and add event listeners
    display.setFocusable(true);
    curve1CheckBox.setFocusable(false);
    curve2CheckBox.setFocusable(false);
    checkBoxes.setFocusable(false);
    editButton.setFocusable(false);
    drawButton.setFocusable(false);
    shortenButton1.setFocusable(false);
    shortenButton2.setFocusable(false);
    shortenButton3.setFocusable(false);
    shortenButton4.setFocusable(false);
    convoluteButton.setFocusable(false);
    refreshButton.setFocusable(false);
    clearButton.setFocusable(false);
    buttons.setFocusable(false);
    textArea.setFocusable(false);
    buttonPanel.setFocusable(false);
    display.requestFocus();
    display.addKeyListener(new KeyLis());
    display.addMouseListener(new MouseLis());
    display.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        // only display a hand if the cursor is over the items
        try {
          if (editting) {
            if (display.onPoint(x, y, curveNum) != null) {
              display.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            else {
              display.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
          }
        }
        catch(NullPointerException ex) {
          // do nothing - this triggers when you move mouse but haven't selected a curve
        }
      }

      @Override
      public void mouseDragged(MouseEvent e) {}
    });
    display.setGraphics(display.getGraphics());
  }

  // Some keyboard shortcuts
  private class KeyLis extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyChar() == 'q') { // close window
        frame.dispose();
        System.exit(0);
      }
      else if(e.getKeyChar() == 'g') {
        if(display.drawCurve(curveNum)) {
          textArea.setText("Curve drawn.\n");
        }
        else {
          textArea.setText("Select a curve first!\n");
        }
      }
      else if(e.getKeyChar() == 'c') {
        display.clearFrame();
        textArea.setText("Points and curves cleared.\n");
      }
    }
  }

  // Mouse listener class for display
  private class MouseLis extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      if (!editting) {
        if(display.addPoint((int)e.getX(), (int)e.getY(), curveNum)) {
          textArea.setText("Added point.\n");
        }
        else {
          textArea.setText("Select a curve first!\n");
        }
      }
      else {
        try {
          if (editting && !holdingPoint) {
            Point candidate = display.onPoint((int)e.getX(), (int)e.getY(), curveNum);
            if (candidate != null) {
              textArea.setText("Click on this point's new location.\n");
              holdingPoint = true;
              editPoint = candidate;
            }
            else {
              textArea.setText("No point here to move.\n");
            }
          }
          else if (editting && holdingPoint) {
            display.updatePoints(e.getX(), e.getY(), curveNum, editPoint);
            holdingPoint = false;
            editPoint = null;
            textArea.setText("Point moved.\n");
          }
        }
        catch(NullPointerException ex) {
          // do nothing - this triggers when you move mouse but haven't selected a curve
        }
      }
    }
  }

  public static void main(String[] args) {
    Demo demo = new Demo();
  }
}
