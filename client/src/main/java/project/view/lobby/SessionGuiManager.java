package project.view.lobby;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.VBox;

public class SessionGuiManager extends VBox {

  private static SessionGuiManager sessionsVbox = null;
  private static final Map<Long, SessionGui> sessionIdGuiMap = new HashMap<>();
  private static final Map<Long, Session> sessionIdDataMap = new HashMap<>();
  private SessionGuiManager() {
  }

  public static SessionGuiManager getInstance() {
    if (sessionsVbox == null) {
      sessionsVbox = new SessionGuiManager();
    }
    return sessionsVbox;
  }

  public static void removeSessionDataAndGui(SessionGui newSessionGui, Long sessionId) {
    sessionsVbox.getChildren().remove(newSessionGui);
    sessionIdGuiMap.remove(sessionId);
    sessionIdDataMap.remove(sessionId);

  }

  public static void addSessionDataAndGui(SessionGui newSessionGui,
                                          Session newSessionData,
                                          Long sessionId) {
    sessionsVbox.getChildren().add(newSessionGui);
    sessionIdGuiMap.put(sessionId, newSessionGui);
    sessionIdDataMap.put(sessionId, newSessionData);
  }

  public static Map<Long, SessionGui> getSessionIdGuiMap() {
    return sessionIdGuiMap;
  }

  public static Map<Long, Session> getSessionIdDataMap() {
    return sessionIdDataMap;
  }




}

