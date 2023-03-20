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
import project.App;
import project.GameBoardLayoutConfig;
import project.connection.LobbyRequestSender;
import project.controllers.stagecontrollers.GameController;
import project.view.lobby.communication.Session;
import project.view.lobby.communication.User;

/**
 * TODO: Assigned the functionality to the buttons (launch, delete, play, ...) in here
 */
public class SessionGui extends HBox {

  private final Long curSessionId;
  private final User curUser;
  private Session curSession;

  private final Thread lobbyUpdateThread;


  // TODO: Needs to add a field -> String saveGameId, constructed in constructor
  //private final String saveGameId;

  /**
   * This constructs a new SessionGUI.
   *
   * @param curSession        provides the current Session object.
   * @param curSessionId      provides the current Session's ID.
   * @param curUser           provides the current User object.
   * @param lobbyUpdateThread the thread that controls the lobby page GUI udpates
   */
  public SessionGui(Session curSession, Long curSessionId, User curUser, Thread lobbyUpdateThread) {
    this.curSession = curSession;
    this.curSessionId = curSessionId;
    this.curUser = curUser;
    this.lobbyUpdateThread = lobbyUpdateThread;
    FXMLLoader fxmlLoader = new FXMLLoader(getClass()
        .getResource("/project/session_gui.fxml"));
    fxmlLoader.setRoot(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setup() {
    setSessionButtons();
    setSessionInfoText();
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

  public void setCurSession(Session newSession) {
    this.curSession = newSession;
  }

  public Long getCurSessionId() {
    return curSessionId;
  }

  public User getCurUser() {
    return curUser;
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
      // we are loading a game play page for a watcher, who has only access to QUIT the game
      // the board is updating pretending this watcher is the first player name
      // but there is no functionality provided to one but the quit button
      App.getGameRequestSender().setGameServiceName(curSession.getGameParameters().getName());
      App.loadNewSceneToPrimaryStage("splendor_base_game_board.fxml",
          new GameController(curSessionId, null));
      lobbyUpdateThread.interrupt();
    };
  }

  private EventHandler<ActionEvent> createPlayGameHandler() {
    return event -> {
      // display the GUI with some basic information needed
      // whenever the user clicks play button, we will reset the game request sender to
      // send correct REST requests to our backend in a right path name (splendorbase, city...)
      App.getGameRequestSender().setGameServiceName(curSession.getGameParameters().getName());

      // pass a meaningful username, so that we know that person can play
      App.loadNewSceneToPrimaryStage("splendor_base_game_board.fxml",
          new GameController(curSessionId, App.getUser().getUsername()));
      // when we click Play, we need to stop the lobby thread from keep monitoring
      // the changes
      lobbyUpdateThread.interrupt();
    };
  }

  private EventHandler<ActionEvent> createDeleteSessionHandler() {
    return event -> {
      SessionGuiManager sessionsVbox = SessionGuiManager.getInstance();
      LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
      sessionsVbox.getChildren().remove(this);
      String accessToken = curUser.getAccessToken();
      lobbyRequestSender.sendDeleteSessionRequest(accessToken, curSessionId);
    };
  }

  // TODO: For server side, they need to handle
  //  this POST request and send a PUT request to GameServer (DONE)
  private EventHandler<ActionEvent> createLaunchSessionHandler() {
    return event -> {
      try {
        LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
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
      LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
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
   * To updateSessionGui.
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

}
