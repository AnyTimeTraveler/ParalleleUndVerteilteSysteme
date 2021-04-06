package pds.trafficlight;

import static pds.trafficlight.CardinalDirection.opposite;
import static pds.trafficlight.Colour.RED;
import static pds.trafficlight.Colour.next;
import static pds.trafficlight.TrafficLight.Cycle.CAN_RUN;
import static pds.trafficlight.TrafficLight.Cycle.ENDED;
import static pds.trafficlight.TrafficLight.Cycle.RUNNING;

import java.util.Arrays;

/**
 * Representation of an autonomous traffic light. A set of four traffic lights are able to provide
 * 'safe' signalling at an intersection, by interacting with each other. A traffic light is
 * represented by a separate thread and all threads interact with each other using shared memory.
 * [PVS-21SS]
 */
public class TrafficLight extends Thread {

  enum Cycle {
    CAN_RUN,
    RUNNING,
    ENDED,
  }

  private static volatile boolean running = true;
  private static volatile CardinalDirection dir;
  private static final Cycle[] cycle = new Cycle[CardinalDirection.cardinality];
  private static final Object lock = new Object();
  private Colour state;
  private final CardinalDirection cd;

  /**
   * Basic constructor of the traffic light.
   *
   * @param cd  the cardinal direction of the location of this traffic light at the intersection
   * @param dir the scheduling will start (first green lights) along the axis between the cardinal
   *            direction 'dir' and its opposite
   */
  public TrafficLight(CardinalDirection cd, CardinalDirection dir) {
    TrafficLight.dir = dir;
    this.cd = cd;
    running = true;
    state = RED;
    Reporter.show(cd, RED);
    cycle[cd.ordinal()] = CAN_RUN;
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
        synchronized (lock) {
          if (locationOnAxis() && cycle[cd.ordinal()] == CAN_RUN) {
            cycle[cd.ordinal()] = RUNNING;
            nextState();
          }
        }
      } else {
        nextState();

        if (state == RED) {
          synchronized (lock) {
            cycle[cd.ordinal()] = ENDED;
            if (noneRunning()) {
              dir = CardinalDirection.next(dir);
              Arrays.fill(cycle, CAN_RUN);
              System.out.println();
            }
          }
        }
      }
    }
  }

  private void nextState() {
    state = next(state);
    Reporter.show(cd, state);
  }

  private boolean noneRunning() {
    for (Cycle c : cycle) {
      if (c == RUNNING) {
        return false;
      }
    }
    return true;
  }

  private boolean locationOnAxis() {
    return cd == dir || cd == opposite(dir);
  }
}
