package pds.trafficlight;

public class TrafficLight extends Thread {
  CardinalDirection cd;
  CardinalDirection dir;
  Colour colour;

  private static boolean running = true;

  TrafficLight(CardinalDirection cd, CardinalDirection dir) {
    this.cd = cd;
    this.dir = dir;
    colour = Colour.RED;
  }

  @Override
  public void run() {
    while (running) {
      try {
        CardinalDirection rightSide = CardinalDirection.next(cd);
        Colour rightSideColour = Intersection.farben.get(rightSide.ordinal());

        CardinalDirection leftSide = CardinalDirection.opposite(rightSide);
        Colour leftSideColour = Intersection.farben.get(leftSide.ordinal());

        if ((cd == CardinalDirection.NORTH || cd == CardinalDirection.SOUTH)
            && (cd == Intersection.sharedDirection || dir == Intersection.sharedDirection)) {
          if (rightSideColour == Colour.RED
              && leftSideColour == Colour.RED) {

            Intersection.sem.acquire();
            colour = Colour.next(colour);
            Intersection.farben.set(cd.ordinal(), colour); // 2 parameters, index, new value
            Reporter.show(cd, colour);

            if (colour == Colour.RED && Intersection.farben.get(dir.ordinal()) == Colour.RED) {
              Intersection.sharedDirection = CardinalDirection.next(Intersection.sharedDirection);

            }
            Intersection.sem.release();
          }
        } else if ((cd == CardinalDirection.EAST || cd == CardinalDirection.WEST)
            && (cd == Intersection.sharedDirection || dir == Intersection.sharedDirection)) {


          if (rightSideColour == Colour.RED
              && leftSideColour == Colour.RED) {

            Intersection.sem.acquire();
            colour = Colour.next(colour);
            Reporter.show(cd, colour);
            Intersection.farben.set(cd.ordinal(), colour); // 2 parameters, index, new value



            if (colour == Colour.RED && Intersection.farben.get(dir.ordinal()) == Colour.RED) {
              Intersection.sharedDirection = CardinalDirection.next(Intersection.sharedDirection);
            }
            Intersection.sem.release();

          }

        }

        Thread.sleep(100); //testing

      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

  public void halt() {
    running = false;
  }
}