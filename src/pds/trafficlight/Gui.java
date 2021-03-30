package pds.trafficlight;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

  public static final int HISTORY_MAX = 1_000;
  private static int historyPos = 1;
  private static final Color[][] history = new Color[HISTORY_MAX][4];
  private static JSlider slider;

  /**
   * Create GUI Layout.
   */
  public Gui() {
    setAlwaysOnTop(true);

    for (int i = 0; i < HISTORY_MAX; i++) {
      history[i][0] = Color.BLACK;
      history[i][1] = Color.BLACK;
      history[i][2] = Color.BLACK;
      history[i][3] = Color.BLACK;
    }

    for (int i = 0; i < 4; i++) {
      buttons[i].setBackground(Color.BLACK);
    }

    JButton startButton = new JButton("Start");
    JButton stopButton = new JButton("Stop");

    startButton.setEnabled(false);
    stopButton.setEnabled(true);

    startButton.addActionListener(e -> {
      Intersection.start();
      startButton.setEnabled(false);
      stopButton.setEnabled(true);
    });
    stopButton.addActionListener(e -> {
      Intersection.stop();
      startButton.setEnabled(true);
      stopButton.setEnabled(false);
    });

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    setLayout(gridbag);

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
    gbc.gridwidth = 3;
    gbc.weighty = 0.5;
    add(stopButton, gbc);
    gbc.gridx = 4;
    add(startButton, gbc);

    slider = new JSlider(0, HISTORY_MAX - 1, 0);
    slider.addChangeListener(e -> {
      for (int i = 0; i < 4; i++) {
        buttons[i].setBackground(history[slider.getValue()][i]);
      }
    });
    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.gridwidth = 6;
    gbc.gridheight = 1;
    gbc.weighty = 0.2;
    add(slider, gbc);

    setSize(300 * 2, 350 * 2);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  /**
   * Sets the background colour of the button corresponding to the traffic light in the cardinal
   * direction.
   *
   * @param dir    Direction of the traffic light
   * @param colour Colour of the traffic light
   */
  public static synchronized void updateLight(CardinalDirection dir, Colour colour) {
    int lastPos = historyPos == 0 ? HISTORY_MAX - 1 : historyPos - 1;
    System.arraycopy(history[lastPos], 0, history[historyPos], 0, 4);
    history[historyPos][dir.ordinal()] = colour.toAwtColor();
    slider.setValue(historyPos);
    historyPos = historyPos == HISTORY_MAX - 1 ? 0 : historyPos + 1;
  }

  @Override
  public void run() {
    setVisible(true);
  }
}
