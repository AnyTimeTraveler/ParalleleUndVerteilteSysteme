package pds.trafficlight;

import static pds.trafficlight.CardinalDirection.next;
import static pds.trafficlight.CardinalDirection.opposite;
import static pds.trafficlight.Colour.GREEN;
import static pds.trafficlight.Colour.RED;

import java.util.Arrays;

/**
 * Representation of an autonomous traffic light. A set of four traffic lights are able to provide
 * 'safe' signalling at an intersection, by interacting with each other. A traffic light is
 * represented by a separate thread and all threads interact with each other using shared memory.
 * [PVS-21SS]
 */
public class TrafficLight extends Thread {

  private static volatile boolean running = true;
  private static volatile CardinalDirection currentAxis;
  private static final boolean[] lightsDone = new boolean[CardinalDirection.cardinality];
  private static final Object lock = new Object();
  private Colour state;
  private final CardinalDirection location;

  /**
   * Basic constructor of the traffic light.
   *
   * @param cd  the cardinal direction of the location of this traffic light at the intersection
   * @param dir the scheduling will start (first green lights) along the axis between the cardinal
   *            direction 'dir' and its opposite
   */
  public TrafficLight(CardinalDirection cd, CardinalDirection dir) {
    currentAxis = dir;
    this.location = cd;
    running = true;
    state = RED;
    Reporter.show(location, RED);
  }

  /**
   * Informs the traffic light to halt its operation. This information is recorded in a variable
   * shared by all traffic lights. Therefore all traffic lights will halt after one traffic light
   * has been informed to halt.
   */
  public void halt() {
    running = false;
  }

  /**
   * The main loop implementing the switching of the traffic light.
   */
  @Override
  public void run() {
    while (running) {
      if (state == RED) {
        if (location == currentAxis || location == opposite(currentAxis)) {
          synchronized (lock) {
            lightsDone[location.ordinal()] = true;
            if (allLightsDone()) {
              currentAxis = next(currentAxis);
              state = GREEN;
              Reporter.show(location, state);
              Arrays.fill(lightsDone, false);
            }
          }
        } else {
          synchronized (lock) {
            lightsDone[location.ordinal()] = true;
          }
        }
      } else {
        state = Colour.next(state);
        Reporter.show(location, state);
      }
    }
  }

  private boolean allLightsDone() {
    for (boolean lightDone : lightsDone) {
      if (!lightDone) {
        return false;
      }
    }
    return true;
  }
}
