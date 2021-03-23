package pds.trafficlight;

import static pds.trafficlight.Colour.next;

/**
 * Representation of an autonomous traffic light. A set of four traffic lights are able to provide
 * 'safe' signalling at an intersection, by interacting with each other. A traffic light is
 * represented by a separate thread and all threads interact with each other using shared memory.
 * [PVS-21SS]
 */
public class TrafficLight extends Thread {

  private final CardinalDirection cd;
  private Colour light;

  /**
   * Basic constructor of the traffic light.
   *
   * @param cd  the cardinal direction of the location of this traffic light at the intersection
   * @param dir the scheduling will start (first green lights) along the axis between the cardinal
   *            direction 'dir' and its opposite
   */
  public TrafficLight(CardinalDirection cd, CardinalDirection dir) {
    this.cd = cd;
    light = Colour.RED;
  }

  /**
   * The main loop implementing the switching of the traffic light.
   */
  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      light = next(light);
      Reporter.show(cd, light);
    }
  }

  /**
   * Informs the traffic light to halt its operation. This information is recorded in a variable
   * shared by all traffic lights. Therefore all traffic lights will halt after one traffic light
   * has been informed to halt.
   */
  public void halt() {

  }
}
