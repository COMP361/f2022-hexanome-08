import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;
import project.view.splendor.PlayerPosition;

public class TestBoardGui {

  ReentrantLock lock = new ReentrantLock();
  @Test
  public void testThreadCommunication() {
    Thread t1 = new Thread(() -> {
      while (true) {
        lock.lock();
        try {
          System.out.println("This is: " + Thread.currentThread().getName());
        } finally {
          lock.unlock();
        }

      }
    });
    Thread t2 = new Thread(() -> {
      while (true) {
        lock.lock();
        try {
          System.out.println("This is: " + Thread.currentThread().getName());
        } finally {
          lock.unlock();
        }

      }
    });

    t1.start();
    t2.start();


  }
}
