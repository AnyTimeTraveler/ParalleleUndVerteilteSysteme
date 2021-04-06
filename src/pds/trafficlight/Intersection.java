package pds.trafficlight;

import static pds.trafficlight.CardinalDirection.NORTH;
import static pds.trafficlight.CardinalDirection.next;

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
  public static void main(String[] args) throws InterruptedException {
    final CardinalDirection startDirection = NORTH;
    CardinalDirection dir = startDirection;

    for (int i = 0; i < 4; i++) {
      lights[i] = new TrafficLight(dir, startDirection);
      dir = next(dir);
    }
    System.out.println();
    for (int i = 0; i < 4; i++) {
      lights[i].start();
    }
    Thread.sleep(5);
    lights[0].halt();
  }
}
