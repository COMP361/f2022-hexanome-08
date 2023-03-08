import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;
import project.view.splendor.PlayerPosition;

public class TestBoardGui {

  //ReentrantLock lock = new ReentrantLock();
  //@Test
  //public void testThreadCommunication() {
  //  Thread t1 = new Thread(() -> {
  //    while (true) {
  //      lock.lock();
  //      try {
  //        System.out.println("This is: " + Thread.currentThread().getName());
  //      } finally {
  //        lock.unlock();
  //      }
  //
  //    }
  //  });
  //  Thread t2 = new Thread(() -> {
  //    while (true) {
  //      lock.lock();
  //      try {
  //        System.out.println("This is: " + Thread.currentThread().getName());
  //      } finally {
  //        lock.unlock();
  //      }
  //
  //    }
  //  });
  //
  //  t1.start();
  //  t2.start();
  //
  //
  //}

  @Test
  public void testPositionEquals() {
    Position p1 = new Position(0,0);
    Position p2 = new Position(0, 0);
    Position p3 = new Position(0,1);
    Position p4 = new Position(0,1);
    Position p5 = new Position(0,1);
    List<Position> allPositions = Arrays.asList(p1, p2,p3,p4,p5);

    Map<Position, List<Integer>> map = new HashMap<>();
    for (Position p: allPositions) {
      if (!map.containsKey(p)) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        map.put(p, list);
      } else {
        List<Integer> curList = map.get(p);
        curList.add(2);
        map.put(p, curList);
      }
    }

    System.out.println(map);

  }

  @Test
  public void testEnum() {
    EnumMap<Colour, Integer> totalGems = new EnumMap<>(Colour.class);
    System.out.println(totalGems);
  }
}


