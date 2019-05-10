
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
  Timer timer;

  public Demo() {
    frame = new JFrame("Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setLocationRelativeTo(null);
    timer = new Timer(40, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          display.shortenCalled(curveNum);
          // display.repaint();
        }
        catch (Exception ex) {
          System.out.println("fail");
          System.out.println(ex.getMessage());
          ex.printStackTrace();
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
    // check boxes
    JPanel checkBoxes = new JPanel();
    JCheckBox curve1CheckBox = new JCheckBox("curve 1");
    JCheckBox curve2CheckBox = new JCheckBox("curve 2");
    curve1CheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          textArea.setText("Curve 1 selected");
          // g.setColor(Color.red);
          curveNum = 1;
        }
      }
    });
    curve2CheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          textArea.setText("Curve 2 selected");
          // g.setColor(Color.blue);
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
    JButton editButton = new JButton("Edit");
    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Editting");
      }
    });
    JButton drawButton = new JButton("Draw");
    drawButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        display.drawPoints(1);
      //   if(display.drawCurve(curveNum)) {
      //     textArea.setText("Select a curve first!");
      //   }
      //   else {
      //     textArea.setText("Curve drawn");
      //   }
      }
    });
    JButton shortenButton = new JButton("Shorten");
    shortenButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Shortening");
        try {
          display.shortenCalled(curveNum);
        }
        catch (Exception ex) {
          System.out.println(ex.getMessage());
        }

        // timer.start();
      }
    });
    JButton convoluteButton = new JButton("Convolute");
    convoluteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        textArea.setText("Convoluting");
        display.convoluteCurves();
      }
    });
    JButton refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
        display.drawCurve(4);
        // display.drawPoints(4)
      }
    });
    buttons.add(editButton);
    buttons.add(drawButton);
    buttons.add(shortenButton);
    buttons.add(convoluteButton);
    buttons.add(refreshButton);
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
    shortenButton.setFocusable(false);
    convoluteButton.setFocusable(false);
    refreshButton.setFocusable(false);
    buttons.setFocusable(false);
    textArea.setFocusable(false);
    buttonPanel.setFocusable(false);
    display.requestFocus();
    display.addKeyListener(new KeyLis());
    display.addMouseListener(new MouseLis());
    display.setGraphics(display.getGraphics());
  }

  // Key listener class for swing
  private class KeyLis extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyChar() == 'q') { // close window
        frame.dispose();
        System.exit(0);
      }
      else if(e.getKeyChar() == 'g') {
        if(display.drawCurve(curveNum)) {
          textArea.setText("Curve drawn");
        }
        else {
          textArea.setText("Select a curve first!");
        }
      }
      else if(e.getKeyChar() == 'c') {
        display.clearFrame();
        textArea.setText("Points and curves cleared");
      }
    }
  }

  // Mouse listener class for swing
  private class MouseLis extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      if(display.addPoint((int)e.getX(), (int)e.getY(), curveNum)) {
        textArea.setText("Added point.");
      }
      else {
        textArea.setText("Select a curve first!");
      }
    }
  }


  public static void main(String[] args) {
    Demo demo = new Demo();
  }
}
