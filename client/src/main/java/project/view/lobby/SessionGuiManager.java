package project.view.lobby;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.VBox;

public class SessionGuiManager extends VBox {

  private static SessionGuiManager sessionsVbox = null;
  private static final Map<Long, SessionGui> sessionIdGuiMap = new HashMap<>();

  private SessionGuiManager() {
  }

  public static SessionGuiManager getInstance() {
    if (sessionsVbox == null) {
      sessionsVbox = new SessionGuiManager();
    }
    return sessionsVbox;
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


}

