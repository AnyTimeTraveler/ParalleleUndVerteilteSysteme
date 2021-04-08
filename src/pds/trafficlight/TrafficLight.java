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
  @SuppressWarnings("FieldMayBeFinal")
  private static volatile Cycle[] cycle = new Cycle[CardinalDirection.values().length];
  private static final Object lock = new Object();
  private final CardinalDirection cd;
  private Colour state;

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
          // Light is red
          if (locationOnAxis() && cycle[cd.ordinal()] == CAN_RUN) {
            // if light is on the active axis and hasn't run yet, set its state to running
            cycle[cd.ordinal()] = RUNNING;
            // switch to and report next state (green)
            nextState();
          } else {
            // otherwise mark as ended
            cycle[cd.ordinal()] = ENDED;
          }
        }
      } else {
        // if light is not red, you have to be on the active axis
        // so you can switch to the next state
        nextState();

        // if you just switched to red, mark your cycle as ended
        if (state == RED) {
          synchronized (lock) {
            cycle[cd.ordinal()] = ENDED;
            if (allEnded()) {
              // if all TrafficLights have marked their cycle as ended,
              // then you are the the last traffic light to do so,
              // as this happens in a synchronized block

              // update the active axis
              dir = CardinalDirection.next(dir);

              // reset the states of all TrafficLights
              Arrays.fill(cycle, CAN_RUN);
            }
          }
        }
      }
    }
  }

  /**
   * Sets next state and reports it.
   */
  private void nextState() {
    state = next(state);
    Reporter.show(cd, state);
  }

  /**
   * Checks if all TrafficLights are in the ENDED state.
   *
   * @return true if all TrafficLights are in ended state, false otherwise
   */
  private boolean allEnded() {
    for (Cycle c : cycle) {
      if (c != ENDED) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if cd is on the axis of dir.
   *
   * @return true if cd is equal or opposite to dir, false otherwise
   */
  private boolean locationOnAxis() {
    return cd == dir || cd == opposite(dir);
  }
}
