package project.view.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import project.App;

/**
 * Customized GUI object that extends VBox.
 */
public class SessionGuiManager extends VBox {
  private static final List<SessionGui> sessionGuiList = new ArrayList<>();
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
    sessionGuiList.clear();
  }

  /**
   * add a Session GUI.
   *
   * @param newSessionGui newSessionGui
   */
  public void addSessionGui(SessionGui newSessionGui) {
    sessionGuiList.add(newSessionGui);
  }


  /**
   * setupSessionGuiOrder.
   */
  public void setupSessionGuiOrder() {
    List<SessionGui> containsUserSessionList = new ArrayList<>();
    List<SessionGui> otherUserSessionList = new ArrayList<>();
    for (SessionGui sessionGui : sessionGuiList) {
      // if the user who is using the app has one's name in the session's play list
      // we will put them at first
      // if is launched, the session GUI will be highlighted in GREEN
      if (sessionGui.getCurSession().getPlayers().contains(App.getUser().getUsername())) {
        containsUserSessionList.add(sessionGui);
      } else {
        otherUserSessionList.add(sessionGui);
      }
    }
    // now put every session gui together
    containsUserSessionList.addAll(otherUserSessionList);
    for (SessionGui sessionGui : containsUserSessionList) {
      List<String> playerNames = sessionGui.getCurSession().getPlayers();
      String curUserName = sessionGui.getCurUser().getUsername();

      if (playerNames.contains(curUserName)) {
        if (sessionGui.getCurSession().isLaunched()) {
          sessionGui.setStyle("-fx-border-width:6; -fx-border-color:#41de00");
        } else {
          sessionGui.setStyle("-fx-border-width:6; -fx-border-color:#ffb366");
        }
      }

      Platform.runLater(() -> {
        sessionsVbox.getChildren().add(sessionGui);
      });
    }

  }

}

