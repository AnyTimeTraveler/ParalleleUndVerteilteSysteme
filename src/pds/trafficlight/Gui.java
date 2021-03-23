package pds.trafficlight;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 * TODO: Summary.
 */
public class Gui extends JDialog implements Runnable {

  private static final JButton north = new JButton("North");
  private static final JButton south = new JButton("South");
  private static final JButton east = new JButton("East");
  private static final JButton west = new JButton("West");

  /**
   * TODO: Summary.
   */
  public Gui() {
    setLayout(new GridLayout(3, 2, 5, 5));

    add(north);
    add(south);
    add(east);
    add(west);

    JButton step = new JButton("Step");
    step.addActionListener(e -> {
    });
    add(step);
    JButton exit = new JButton("Exit");
    exit.addActionListener(e -> {
      System.exit(0);
    });
    add(exit);

    setSize(300, 400);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

  }

  /**
   * TODO: Summary.
   */
  public static synchronized void updateLight(CardinalDirection dir, Colour colour) {
    getLight(dir).setBackground(colour.toAwtColor());
  }

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
