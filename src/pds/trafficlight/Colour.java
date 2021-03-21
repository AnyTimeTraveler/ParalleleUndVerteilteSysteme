/*
 * Colour.java
 *
 * Copyright (c) 2021-2021 HS Emden-Leer
 * All Rights Reserved.
 *
 * @version 1.00 - 17 Mar 2021 - GJV - initial version
 */

package pds.trafficlight;

import java.awt.Color;

/**
 * Representation of the colours of a traffic light.
 * <br><code><b>[PVS-21SS]</b></code>
 *
 * @author Gert Veltink
 * @version 1.00 - 17 Mar 2021
 */
public enum Colour {
  /** the colour red. */
  RED,
  /** the colour green. */
  GREEN,
  /** the colour yellow. */
  YELLOW;


  /**
   * Returns the colour of the next phase of a traffic light.
   * Order: red - green - yellow - red - ...
   *
   * @param c   a colour
   * @return    the colour of the next phase
   */
  public static Colour next(Colour c) {
    switch (c) {
      case RED:
        return GREEN;
      case GREEN:
        return YELLOW;
      default:
        return RED; // catch all
    }
  }

  /**
   * Returns the equivalent AWT color.
   *
   * @return    the equivalent AWT color
   */
  public Color toAwtColor() {
    return switch (this) {
      case RED -> Color.RED;
      case GREEN -> Color.GREEN;
      case YELLOW -> Color.YELLOW;
    };
  }
}
