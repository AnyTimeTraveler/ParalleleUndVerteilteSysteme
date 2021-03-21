package pds.trafficlight;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 * TODO: Summary.
 */
public class Gui extends JDialog implements Runnable {

  private final JButton north = new JButton("North");
  private final JButton south = new JButton("South");
  private final JButton east = new JButton("East");
  private final JButton west = new JButton("West");

  /**
   * TODO: Summary.
   */
  public Gui() {
    setLayout(new GridLayout(3, 2, 5, 5));

    north.setBackground(Color.RED);
    add(north);

    south.setBackground(Color.GREEN);
    add(south);

    east.setBackground(Color.YELLOW);
    add(east);
    west.setBackground(Color.RED);
    add(west);

    JButton step = new JButton("Step");
    step.addActionListener(e -> {
    });
    add(step);
    JButton exit = new JButton("Exit");
    exit.addActionListener(e -> {
    });
    add(exit);

    setSize(300, 400);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

  }

  /**
   * TODO: Summary.
   */
  public synchronized void updateLight(CardinalDirection dir, Colour colour) {
    getLight(dir).setBackground(colour.toAwtColor());
  }

  private JButton getLight(CardinalDirection dir) {
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
