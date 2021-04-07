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
        // du bist rot, also musst du auf der erlaubten axe liegen, damit du weiter schalten darfst
        synchronized (lock) {
          // cycle trackt, ob du schon einmal durch gruen und gelb gelaufen bist
          if (locationOnAxis() && cycle[cd.ordinal()] == CAN_RUN) {
            cycle[cd.ordinal()] = RUNNING;
            nextState();
          } else {
            cycle[cd.ordinal()] = ENDED;
          }
        }
      } else {
        // du bist nicht rot, also darfst du in den naechsten Zustand
        nextState();

        if (state == RED) {
          // falls du auf rot gewechselt hast, checke ob du der letzte bist, der auf rot wechselt
          synchronized (lock) {
            cycle[cd.ordinal()] = ENDED;
            if (allEnded()) {
              // du bist als letzes auf rot gewechselt
              // (alle ampeln sind auf rot und haben nicht mehr vor, von rot weg zu wechseln)

              dir = CardinalDirection.next(dir);
              // resette das cycle array
              Arrays.fill(cycle, CAN_RUN);
              // neue Zeile, da es die uebersiche erhoeht
              System.out.println();
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
