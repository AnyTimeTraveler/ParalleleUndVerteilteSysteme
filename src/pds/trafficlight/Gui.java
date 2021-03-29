package pds.trafficlight;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    setLayout(gridbag);

    c.fill = GridBagConstraints.BOTH;
    gridbag.setConstraints(north, c);
    add(north);

    c.fill = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(south, c);
    add(south);

    c.fill = GridBagConstraints.BOTH;
    gridbag.setConstraints(east, c);
    add(east);

    c.fill = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(west, c);
    add(west);

    JButton exit = new JButton("Exit");
    exit.addActionListener(e -> {
      System.exit(0);
    });
    c.gridwidth = 2;
    gridbag.setConstraints(exit, c);
    add(exit);

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
