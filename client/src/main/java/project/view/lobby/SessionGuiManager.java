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
   * Clean up everything after loading into another page.
   */
  public void clearSessionsRecorded() {
    Platform.runLater(() -> {
      sessionsVbox.getChildren().clear();
    });
    sessionIdGuiMap.clear();
  }

  /**
   * add a Session GUI.
   *
   * @param newSessionGui newSessionGui
   */
  public void addSessionGui(SessionGui newSessionGui) {
    Platform.runLater(() -> {
      sessionsVbox.getChildren().add(newSessionGui);
    });

  }


}

