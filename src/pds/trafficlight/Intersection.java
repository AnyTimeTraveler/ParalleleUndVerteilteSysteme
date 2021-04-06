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
//    SwingUtilities.invokeLater(new Gui());
    start();
  }

  /**
   * Start all traffic lights.
   */
  public static void start() {
    final CardinalDirection startDirection = NORTH;
    CardinalDirection dir = startDirection;

    for (int i = 0; i < 4; i++) {
      lights[i] = new TrafficLight(dir, startDirection);
      dir = next(dir);
    }
    for (int i = 0; i < 4; i++) {
      lights[i].start();
    }
  }

  /**
   * Stop all traffic light threads.
   */
  public static void stop() {
    lights[0].halt();
  }

}
