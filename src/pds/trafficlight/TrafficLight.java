package pds.trafficlight;

import static pds.trafficlight.CardinalDirection.opposite;
import static pds.trafficlight.Colour.GREEN;
import static pds.trafficlight.Colour.RED;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Representation of an autonomous traffic light. A set of four traffic lights are able to provide
 * 'safe' signalling at an intersection, by interacting with each other. A traffic light is
 * represented by a separate thread and all threads interact with each other using shared memory.
 * [PVS-21SS]
 */
public class TrafficLight extends Thread {

  private static final Colour[] state = new Colour[4];
  private static final ReentrantLock lock = new ReentrantLock();
  private final CardinalDirection locationDir;
  private final CardinalDirection startAxis;

  /**
   * Basic constructor of the traffic light.
   *
   * @param cd  the cardinal direction of the location of this traffic light at the intersection
   * @param dir the scheduling will start (first green lights) along the axis between the cardinal
   *            direction 'dir' and its opposite
   */
  public TrafficLight(CardinalDirection cd, CardinalDirection dir) {
    this.locationDir = cd;
    this.startAxis = dir;

    lock.lock();
    if (locationDir == startAxis || locationDir == opposite(startAxis)) {
      setLightState(locationDir, GREEN);
    } else {
      setLightState(locationDir, RED);
    }

    Reporter.show(locationDir, RED);
    lock.unlock();
  }

  static Colour getLightState(CardinalDirection dir) {

    return state[dir.ordinal()];

  }

  static void setLightState(CardinalDirection dir, Colour colour) {

    state[dir.ordinal()] = colour;


  }

  /**
   * Informs the traffic light to halt its operation. This information is recorded in a variable
   * shared by all traffic lights. Therefore all traffic lights will halt after one traffic light
   * has been informed to halt.
   */
  public void halt() {

  }

  /**
   * The main loop implementing the switching of the traffic light.
   */
  @Override
  public void run() {
    while (true) {
      lock.lock();

      Colour current = getLightState(locationDir);

      if (current == RED) {
        if (getLightState(CardinalDirection.next(locationDir)) == RED
            && getLightState(CardinalDirection.next(opposite(locationDir)))
            == RED) {
          setLightState(locationDir, GREEN);
        }
      } else {
        setLightState(locationDir, Colour.next(current));
      }

      Reporter.show(locationDir, getLightState(locationDir));
      lock.unlock();
      sleep(100);


    }
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
