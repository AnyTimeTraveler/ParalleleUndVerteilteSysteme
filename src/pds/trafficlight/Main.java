package pds.trafficlight;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

/**
 * Entrypoint.
 */
public class Main {
  public static Gui gui = new Gui();

  public static void main(String[] args) throws InvocationTargetException, InterruptedException {
    SwingUtilities.invokeAndWait(new Gui());
  }

}
