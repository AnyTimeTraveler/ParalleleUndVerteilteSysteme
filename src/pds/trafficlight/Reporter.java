/*
 * Reporter.java
 *
 * Copyright (c) 2021-2021 HS Emden/Leer
 * All Rights Reserved.
 *
 * @version 1.10 - 18 Mar 2021 - GJV - added msg for debugging purposes
 * @version 1.00 - 17 Mar 2021 - GJV - initial version
 */

package pds.trafficlight;

/**
 * A very simple reporter for the intersection.
 * <br><code><b>[PVS-21SS]</b></code>
 *
 * @author Gert Veltink
 * @version 1.10 - 18 Mar 2021
 */
public class Reporter {

  /**
   * Shows the cardinal direction and the colour of a traffic light.
   *
   * @param cd  the cardinal direction
   * @param c   the colour
   */
  public static void show(final CardinalDirection cd, final Colour c) {
    Gui.updateLight(cd, c);
    msg(cd + ":\t" + c);
  }

  /**
   * Shows a general message.
   *
   * @param s   the message string
   */
  public static void msg(final String s) {
    System.out.println(s);
    System.out.flush();
  }

}
