package pds.trafficlight;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Intersection {


  public static CardinalDirection sharedDirection = CardinalDirection.NORTH;


  public volatile static Semaphore sem = new Semaphore(1);

  public static List<TrafficLight> ampeln = new ArrayList<>();

  public static List<Colour> farben = new ArrayList<>();


  public static void main(String[] args) throws InterruptedException {
    //creating threads:
    CardinalDirection first = CardinalDirection.NORTH;
    for (int i = 0; i < 4; i++) {
      ampeln.add(new TrafficLight(first, CardinalDirection.opposite(first)));//thread created
      farben.add(ampeln.get(i).colour);//Coulour init red
      first = CardinalDirection.next(first);
    }
    for (int i = 0; i < 4; i++) {
      ampeln.get(i).start();
    }

    Thread.sleep(10000);

    for (int i = 0; i < 4; i++) {
      ampeln.get(i).halt();
    }


  }
}