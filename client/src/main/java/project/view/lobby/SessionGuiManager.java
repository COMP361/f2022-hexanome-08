package project.view.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.VBox;

/**
 * Customized GUI object that extends VBox.
 */
public class SessionGuiManager extends VBox {

  private static SessionGuiManager sessionsVbox = null;
  private static final Map<Long, SessionGui> sessionIdGuiMap = new HashMap<>();

  private static final List<Thread> updateSessionThreads = new ArrayList<>();

  private SessionGuiManager() {}


  /**
   * Singleton get instance method.
   *
   * @return a SessionGuiManager singleton object
   */
  public static SessionGuiManager getInstance() {
    if (sessionsVbox == null) {
      sessionsVbox = new SessionGuiManager();
    }
    return sessionsVbox;
  }

  /**
   * Clean up everything after loading into another page
   */
  public static void resetManager() {
    sessionsVbox.getChildren().clear();
    sessionIdGuiMap.clear();
    for (Thread thread : updateSessionThreads) {
      thread.interrupt();
    }
    updateSessionThreads.clear();
  }

  public static void removeSessionGui(SessionGui newSessionGui) {
    sessionsVbox.getChildren().remove(newSessionGui);
  }

  public static void removeSessionIdGuiMap(Long sessionId) {
    sessionIdGuiMap.remove(sessionId);
  }


  public static void addSessionGui(SessionGui newSessionGui) {
    sessionsVbox.getChildren().add(newSessionGui);
  }

  public static void addSessionIdGuiMap(SessionGui newSessionGui, Long sessionId) {
    sessionIdGuiMap.put(sessionId, newSessionGui);

  }

  public static Map<Long, SessionGui> getSessionIdGuiMap() {
    return sessionIdGuiMap;
  }

  public static void addSessionUpdateThread(Thread thread) {
    updateSessionThreads.add(thread);
  }

}

