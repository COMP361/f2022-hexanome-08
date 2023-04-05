//import ca.mcgill.comp361.splendormodel.model.Colour;
//import ca.mcgill.comp361.splendormodel.model.Position;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.EnumMap;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.locks.ReentrantLock;
//import org.junit.Test;
//import project.App;
//import project.view.splendor.playergui.PlayerPosition;
//
//public class TestBoardGui {
//
//  //@Test
//  //public void test2() {
//  //  App.getPlayerImage("raneem");
//  //}
//
//  @Test
//  public void test1() {
//    String s = "abc: cde";
//    String s2 = "abc";
//    String[] parts = s2.split(":\\s*");
//    System.out.println(Arrays.toString(parts));
//  }
//  @Test
//  public void testPositionEquals() {
//    Position p1 = new Position(0,0);
//    Position p2 = new Position(0, 0);
//    Position p3 = new Position(0,1);
//    Position p4 = new Position(0,1);
//    Position p5 = new Position(0,1);
//    List<Position> allPositions = Arrays.asList(p1, p2,p3,p4,p5);
//
//    Map<Position, List<Integer>> map = new HashMap<>();
//    for (Position p: allPositions) {
//      if (!map.containsKey(p)) {
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        map.put(p, list);
//      } else {
//        List<Integer> curList = map.get(p);
//        curList.add(2);
//        map.put(p, curList);
//      }
//    }
//
//    System.out.println(map);
//
//  }
//
//  @Test
//  public void testEnum() {
//    EnumMap<Colour, Integer> totalGems = new EnumMap<>(Colour.class);
//    System.out.println(totalGems);
//  }
//
//  @Test
//  public void testCommandLine() throws IOException, InterruptedException {
//    // Run "ipconfig getifaddr en0" command on a Mac system
//    ProcessBuilder pb = new ProcessBuilder("ipconfig", "getifaddr", "en0");
//    Process process = pb.start();
//
//    // Read the output of the command and store it as a string
//    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//    StringBuilder output = new StringBuilder();
//    String line;
//    while ((line = reader.readLine()) != null) {
//      output.append(line);
//      output.append("\n");
//    }
//
//    // Wait for the command to finish and check the exit value
//    int exitCode = process.waitFor();
//    System.out.println("Exited with error code " + exitCode);
//
//    // Print the output string
//    System.out.println("Output:\n" + output.toString());
//  }
//  private volatile boolean shouldStop = false;
//  //@Test
//  //public void testThreadTerminating() {
//  //
//  //  Thread thread = new Thread(() -> {
//  //    while (!shouldStop) {
//  //      // Do some work here
//  //    }
//  //  });
//  //  thread.start();
//  //
//  //  // Wait for the thread to start
//  //  try {
//  //    Thread.sleep(1000); // Sleep for 1 second
//  //  } catch (InterruptedException e) {
//  //    e.printStackTrace();
//  //  }
//  //
//  //  // Print information about all running threads
//  //  Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
//  //  System.out.println("Number of running threads: " + allThreads.size());
//  //  for (Thread t : allThreads.keySet()) {
//  //    System.out.println("Thread name: " + t.getName() + ", Thread ID: " + t.getId());
//  //  }
//  //
//  //  // Request that the thread stop running
//  //  shouldStop = true;
//  //
//  //  // Wait for the thread to exit
//  //  //try {
//  //  //  thread.join();
//  //  //} catch (InterruptedException e) {
//  //  //  e.printStackTrace();
//  //  //}
//  //
//  //  // Print information about all running threads
//  //  allThreads = Thread.getAllStackTraces();
//  //  System.out.println("Number of running threads: " + allThreads.size());
//  //  for (Thread t : allThreads.keySet()) {
//  //    System.out.println("Thread name: " + t.getName() + ", Thread ID: " + t.getId());
//  //  }
//  //}
//}
//
//
