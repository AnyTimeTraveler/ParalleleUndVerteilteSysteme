package pds.trafficlight;

import static pds.trafficlight.CardinalDirection.opposite;
import static pds.trafficlight.Colour.RED;
import static pds.trafficlight.Colour.next;

/**
 * Representation of an autonomous traffic light. A set of four traffic lights are able to provide
 * 'safe' signalling at an intersection, by interacting with each other. A traffic light is
 * represented by a separate thread and all threads interact with each other using shared memory.
 * [PVS-21SS]
 */
public class TrafficLight extends Thread {

  private static volatile boolean running = true;
  private static volatile CardinalDirection currentAxis;
  private static volatile int redLightCount = 0;
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
    redLightCount = CardinalDirection.cardinality;
  }

  /**
   * Informs the traffic light to halt its operation. This information is recorded in a variable
   * shared by all traffic lights. Therefore all traffic lights will halt after one traffic light
   * has been informed to halt.
   */
  public void halt() {
    running = false;
  }

  private void setLight(Colour colour) {
    if (state == colour) {
      return;
    }
    if (state == RED) {
      synchronized (lock) {
        redLightCount--;
      }
    }
    state = colour;
    Reporter.show(location, state);
    if (colour == RED) {
      synchronized (lock) {
        redLightCount++;
      }
    }
  }

  /**
   * The main loop implementing the switching of the traffic light.
   */
  @Override
  public void run() {
    while (running) {
      if (state == RED) {
        synchronized (lock) {
          if (location == currentAxis || location == opposite(currentAxis)) {
            setLight(next(state));
          }
        }
      } else if (next(state) == RED) {
        synchronized (lock) {
          if (location == currentAxis || location == opposite(currentAxis)) {
            setLight(next(state));
          }
          if (redLightCount == CardinalDirection.cardinality) {
            currentAxis = CardinalDirection.next(currentAxis);
          }
        }
      } else {
        setLight(next(state));
      }
    }
  }
}
