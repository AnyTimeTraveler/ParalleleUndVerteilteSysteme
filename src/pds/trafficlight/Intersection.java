package pds.trafficlight;

import static pds.trafficlight.CardinalDirection.NORTH;
import static pds.trafficlight.CardinalDirection.next;

import javax.swing.SwingUtilities;

/**
 * Entrypoint.
 */
public class Intersection {

  /**
   * Implements the main method for this class. Constructs the traffic lights and activates them
   * (threads).
   *
   * @param args not used
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Gui());
    CardinalDirection dir = NORTH;
    final CardinalDirection startDirection = NORTH;
    for (int i = 0; i < 4; i++) {
      new TrafficLight(dir, startDirection).start();
      dir = next(dir);
    }
  }

}
