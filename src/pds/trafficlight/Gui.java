package pds.trafficlight;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * GUI to represent the current state of all traffic lights better.
 */
public class Gui extends JFrame implements Runnable {

  private static final JButton north = new JButton("North");
  private static final JButton south = new JButton("South");
  private static final JButton east = new JButton("East");
  private static final JButton west = new JButton("West");

  /**
   * Create GUI Layout.
   */
  public Gui() {
    setAlwaysOnTop(true);
    setUndecorated(true);
    setLayout(new GridLayout(2, 2, 5, 5));

    add(north);
    add(south);
    add(east);
    add(west);

    setSize(300, 300);
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
    getLight(dir).setBackground(colour.toAwtColor());
  }

  /**
   * Translate CardinalDirection to a button in the GUI.
   *
   * @param dir Direction
   * @return Button representing that direction
   */
  private static JButton getLight(CardinalDirection dir) {
    return switch (dir) {
      case NORTH -> north;
      case EAST -> east;
      case SOUTH -> south;
      case WEST -> west;
    };
  }

  @Override
  public void run() {
    setVisible(true);
  }
}
