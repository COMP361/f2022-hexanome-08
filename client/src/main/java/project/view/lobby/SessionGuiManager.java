package project.view.lobby;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

/**
 * Customized GUI object that extends VBox.
 */
public class SessionGuiManager extends VBox {

  private static final Map<Long, SessionGui> sessionIdGuiMap = new HashMap<>();
  private static SessionGuiManager sessionsVbox = null;


  private SessionGuiManager() {
  }


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
  public static void clearSessionsRecorded() {
    Platform.runLater(() -> {
      sessionsVbox.getChildren().clear();
    });
    sessionIdGuiMap.clear();
  }

  public static void removeSessionGui(SessionGui newSessionGui) {
    sessionsVbox.getChildren().remove(newSessionGui);
  }

  public static void removeSessionIdGuiMap(Long sessionId) {
    sessionIdGuiMap.remove(sessionId);
  }


  public static void addSessionGui(SessionGui newSessionGui) {
    Platform.runLater(() -> {
      sessionsVbox.getChildren().add(newSessionGui);
    });

  }


}

