package pds.trafficlight;

import static pds.trafficlight.CardinalDirection.NORTH;
import static pds.trafficlight.CardinalDirection.next;

import javax.swing.SwingUtilities;

/**
 * Entrypoint.
 */
public class Intersection {

  private static final TrafficLight[] lights = new TrafficLight[4];

  /**
   * Implements the main method for this class. Constructs the traffic lights and activates them
   * (threads).
   *
   * @param args not used
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Gui());
    start();
  }

  /**
   * Start.
   */
  public static void start() {
    final CardinalDirection startDirection = NORTH;
    CardinalDirection dir = startDirection;

    for (int i = 0; i < 4; i++) {
      lights[i] = new TrafficLight(dir, startDirection);
      lights[i].start();
      dir = next(dir);
    }
  }

  public static void stop() {
    lights[0].halt();
  }

}
