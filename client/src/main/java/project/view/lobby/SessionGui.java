package project.view.lobby;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.App;
import project.GameBoardLayoutConfig;
import project.GameController;
import project.connection.LobbyServiceRequestSender;
import project.view.lobby.communication.Session;
import project.view.lobby.communication.User;

/**
 * TODO: Assigned the functionality to the buttons (launch, delete, play, ...) in here
 */
public class SessionGui extends HBox {

  private Session curSession;

  private final Long curSessionId;

  private final User curUser;

  /**
   * This constructs a new SessionGUI.
   *
   * @param curSession   provides the current Session object.
   * @param curSessionId provides the current Session's ID.
   * @param curUser      provides the current User object.
   */
  public SessionGui(Session curSession, Long curSessionId, User curUser) {
    this.curSession = curSession;
    this.curSessionId = curSessionId;
    this.curUser = curUser;
    FXMLLoader fxmlLoader = new FXMLLoader(getClass()
        .getResource("/project/session_template.fxml"));
    fxmlLoader.setRoot(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String formatSessionInfo(Session session) {
    List<String> curPlayers = session.getPlayers();
    String curPlayerStr = curPlayers.toString();
    String creatorName = session.getCreator();
    int curPlayersCount = curPlayers.size();
    int maxPlayerCount = session.getGameParameters().getMaxSessionPlayers();
    String displayGameName = session.getGameParameters().getDisplayName();
    return String.format(
        "%s, [%d/%d] players %s: \n",
        displayGameName,
        curPlayersCount,
        maxPlayerCount,
        curPlayerStr)
        + String.format("creator: %s", creatorName);
  }

  public Session getCurSession() {
    return curSession;
  }

  public Long getCurSessionId() {
    return curSessionId;
  }

  public User getCurUser() {
    return curUser;
  }

  public void setCurSession(Session newSession) {
    this.curSession = newSession;
  }

  /**
   * Sets the text information for the current session.
   */
  public void setSessionInfoText() {
    String sessionInfo = formatSessionInfo(curSession);
    Label sessionInfoLabel = (Label) this.getChildren().get(0);
    sessionInfoLabel.setText(sessionInfo);
  }

  private EventHandler<ActionEvent> createWatchGameHandler() {
    return event -> {
      // just load the board to this user, nothing else should be done
      try {
        App.setRoot("splendor_base_game_board");
        //stopAndClearThreads(); TODO: Might need a way to clean up the threads later ...
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  private EventHandler<ActionEvent> createPlayGameHandler() {
    return event -> {
      try {
        // display the GUI with some basic information needed
        // 0. sessionId needs to be passed to this controller, the other info
        // I can get from based on this sessionId (gameId)
        GameBoardLayoutConfig config = App.getGuiLayouts();
        App.loadPopUpWithController("splendor_base_game_board.fxml",
            new GameController(curSessionId), config.getAppWidth(), config.getAppHeight());

        Button playButton = (Button) event.getSource();
        Stage lobbyWindow = (Stage) playButton.getScene().getWindow();
        lobbyWindow.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  private EventHandler<ActionEvent> createDeleteSessionHandler() {
    return event -> {
      SessionGuiManager sessionsVbox = SessionGuiManager.getInstance();
      LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
      sessionsVbox.getChildren().remove(this);
      try {
        String accessToken = curUser.getAccessToken();
        lobbyRequestSender.sendDeleteSessionRequest(accessToken, curSessionId);
      } catch (UnirestException e) {
        throw new RuntimeException(e);
      }
    };
  }

  // TODO: For server side, they need to handle
  //  this POST request and send a PUT request to GameServer (DONE)
  private EventHandler<ActionEvent> createLaunchSessionHandler() {
    return event -> {
      try {
        LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
        String accessToken = curUser.getAccessToken();
        lobbyRequestSender.sendLaunchSessionRequest(curSessionId, accessToken);
      } catch (UnirestException e) {
        throw new RuntimeException(e);
      }
    };
  }


  private EventHandler<ActionEvent> createJoinLeaveSessionHandler() {
    return event -> {
      Button joinAndLeaveButton = (Button) event.getSource();
      LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
      String accessToken = curUser.getAccessToken();
      String curUserName = curUser.getUsername();
      // If the button says "Join", send join request
      if (joinAndLeaveButton.getText().equals("Join")) {
        // First thing, a request
        joinAndLeaveButton.setText("Leave");
        try {
          lobbyRequestSender.sendAddPlayerRequest(accessToken, curSessionId, curUserName);
        } catch (UnirestException e) {
          throw new RuntimeException(e);
        }
      } else if (joinAndLeaveButton.getText().equals("Leave")) { // otherwise, send leave request
        joinAndLeaveButton.setText("Join");
        try {
          lobbyRequestSender.sendRemovePlayerRequest(accessToken, curSessionId, curUserName);
        } catch (UnirestException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  /**
   * Sets the available buttons for the sessions.
   */
  public void setSessionButtons() {
    String curUserName = curUser.getUsername();
    String sessionCreatorName = curSession.getCreator();
    List<String> curSessionPlayers = curSession.getPlayers();
    VBox buttonsVbox = (VBox) this.getChildren().get(2);
    Button topButton = (Button) buttonsVbox.getChildren().get(0);
    Button btmButton = (Button) buttonsVbox.getChildren().get(1);
    // make sure everytime before updating, these 2 buttons are not visible
    topButton.setVisible(false);
    btmButton.setVisible(false);

    // if the session is launched, add a Play Button for both creator and player
    if (curSession.isLaunched()) {
      // if the current user is not in the session, then give a Watch Button
      if (!curSessionPlayers.contains(curUserName)) {
        topButton.setVisible(true);
        topButton.setText("Watch");
        topButton.setOnAction(createWatchGameHandler());
      } else {
        topButton.setVisible(true);
        topButton.setText("Play");
        topButton.setOnAction(createPlayGameHandler());
      }
    } else {
      // otherwise, add diff buttons for creator OR player
      // if the user is the creator, provides delete and launch button
      if (curUserName.equals(sessionCreatorName)) {
        // set up Delete button
        topButton.setVisible(true);
        topButton.setText("Delete");
        topButton.setOnAction(createDeleteSessionHandler());

        // set up Launch button
        btmButton.setVisible(true);
        btmButton.setText("Launch");
        btmButton.setOnAction(createLaunchSessionHandler());
        // launchButton greyed out depends on if curPlayers > minPlayers or not
        int curSessionPlayersCount = curSessionPlayers.size();
        int minSessionPlayerCount = curSession.getGameParameters().getMinSessionPlayers();
        btmButton.setDisable(curSessionPlayersCount < minSessionPlayerCount);
      } else {
        String buttonContent;
        if (curSessionPlayers.contains(curUserName)) {
          buttonContent = "Leave";
        } else {
          buttonContent = "Join";
        }
        topButton.setVisible(true);
        topButton.setText(buttonContent);
        topButton.setOnAction(createJoinLeaveSessionHandler());
      }
    }
  }

  /**
   * TODO.
   */
  public void updateSessionGui() {
    // if user is in this game, then we only need to delete the Leave button to Play
    // if user is NOT in this game, change the Join button to Watch
    // either way, we are just removing one button and create a new one
    String curUserName = curUser.getUsername();
    List<String> curPlayers = curSession.getPlayers();
    VBox buttonsVbox = (VBox) this.getChildren().get(2);
    Button topButton = (Button) buttonsVbox.getChildren().get(0);
    Button btmButton = (Button) buttonsVbox.getChildren().get(1);
    topButton.setVisible(false);
    btmButton.setVisible(false);

    if (curSession.isLaunched()) {
      topButton.setVisible(true);
      if (curPlayers.contains(curUserName)) {
        // Play button case
        topButton.setText("Play");
        topButton.setOnAction(createPlayGameHandler());
      } else {
        // Watch button case
        topButton.setText("Watch");
        topButton.setOnAction(createWatchGameHandler());
      }
    } else {
      String sessionCreator = curSession.getCreator();
      if (curUserName.equals(sessionCreator)) {
        topButton.setVisible(true);
        btmButton.setVisible(true);
        topButton.setText("Delete");
        btmButton.setText("Launch");
        topButton.setOnAction(createDeleteSessionHandler());
        btmButton.setOnAction(createLaunchSessionHandler());

        int curPlayersCount = curPlayers.size();
        int minPlayersCount = curSession.getGameParameters().getMinSessionPlayers();

        // delete and launch (clickable or not depends on curPlayer < minPlayer or not)
        btmButton.setDisable(curPlayersCount < minPlayersCount);
      } else {
        topButton.setVisible(true);
        topButton.setOnAction(createJoinLeaveSessionHandler());
        if (curPlayers.contains(curUserName)) {
          // leave button case
          topButton.setText("Leave");
        } else {
          // join button case
          topButton.setText("Join");
        }
      }
    }
  }

  public void setup() {
    setSessionButtons();
    setSessionInfoText();
  }

}
