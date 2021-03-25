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

    TrafficLight north = new TrafficLight(dir, startDirection);
    dir = next(dir);
    north.start();

    TrafficLight east = new TrafficLight(dir, startDirection);
    dir = next(dir);
    east.start();

    TrafficLight south = new TrafficLight(dir, startDirection);
    dir = next(dir);
    south.start();

    TrafficLight west = new TrafficLight(dir, startDirection);
    west.start();

  }

}
