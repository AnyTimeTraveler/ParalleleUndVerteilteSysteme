package pds.trafficlight;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.WindowConstants;

/**
 * GUI to represent the current state of all traffic lights better.
 */
public class Gui extends JFrame implements Runnable {

  private static final JButton[] buttons = {
      new JButton("North"),
      new JButton("East"),
      new JButton("South"),
      new JButton("West")
  };
  private static final int[][] buttonPositions = {
      {2, 0},
      {0, 2},
      {2, 4},
      {4, 2},
  };

  private static final ArrayList<Color[]> history = new ArrayList<>(100_000);
  private static JSlider slider;

  /**
   * Create GUI Layout.
   */
  public Gui() {
    setAlwaysOnTop(true);

    for (int i = 0; i < 4; i++) {
      buttons[i].setBackground(Color.BLACK);
    }

    JButton startButton = new JButton("Start");
    JButton clearButton = new JButton("Clear History");
    JButton stopButton = new JButton("Stop");

    startButton.setEnabled(false);
    clearButton.setEnabled(true);
    stopButton.setEnabled(true);

    startButton.addActionListener(e -> {
      Intersection.start();
      startButton.setEnabled(false);
      stopButton.setEnabled(true);
    });
    clearButton.addActionListener(e -> resetHistory());
    stopButton.addActionListener(e -> {
      Intersection.stop();
      startButton.setEnabled(true);
      stopButton.setEnabled(false);
    });

    GridBagConstraints gbc = new GridBagConstraints();
    setLayout(new GridBagLayout());

    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.gridwidth = 2;
    gbc.gridheight = 2;
    gbc.fill = GridBagConstraints.BOTH;

    for (int i = 0; i < 4; i++) {
      gbc.gridx = buttonPositions[i][0];
      gbc.gridy = buttonPositions[i][1];
      add(buttons[i], gbc);
    }

    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridheight = 2;
    gbc.gridwidth = 2;
    gbc.weighty = 0.5;
    add(stopButton, gbc);
    gbc.gridx = 3;
    add(clearButton, gbc);
    gbc.gridx = 5;
    add(startButton, gbc);

    slider = new JSlider(0, 0, 0);
    slider.addChangeListener(e -> {
      Color[] colors = history.get(slider.getValue());
      for (int i = 0; i < 4; i++) {
        buttons[i].setBackground(colors[i]);
      }
    });
    resetHistory();
    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.gridwidth = 6;
    gbc.gridheight = 1;
    gbc.weighty = 0.2;
    add(slider, gbc);

    setSize(300 * 2, 350 * 2);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  private void resetHistory() {
    history.clear();
    history.add(new Color[]{Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK});
    slider.setValue(0);
    slider.setMaximum(0);
  }

  /**
   * Sets the background colour of the button corresponding to the traffic light in the cardinal
   * direction.
   *
   * @param dir    Direction of the traffic light
   * @param colour Colour of the traffic light
   */
  public static synchronized void updateLight(CardinalDirection dir, Colour colour) {
    Color[] colors = new Color[]{
        buttons[0].getBackground(),
        buttons[1].getBackground(),
        buttons[2].getBackground(),
        buttons[3].getBackground(),
    };
    colors[dir.ordinal()] = toAwtColor(colour);
    history.add(colors);

    slider.setMaximum(history.size() - 1);
    slider.setValue(history.size() - 1);
  }

  @Override
  public void run() {
    setVisible(true);
  }


  /**
   * Returns the equivalent AWT color.
   *
   * @return the equivalent AWT color
   */
  public static Color toAwtColor(Colour colour) {
    return switch (colour) {
      case RED -> Color.RED;
      case GREEN -> Color.GREEN;
      case YELLOW -> Color.YELLOW;
    };
  }
}
