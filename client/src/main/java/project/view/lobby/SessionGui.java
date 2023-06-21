package project.view.lobby;

import ca.mcgill.comp361.splendormodel.model.GameInfo;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import project.App;
import project.connection.LobbyRequestSender;
import project.controllers.guielementcontroller.SessionGuiController;
import project.controllers.popupcontrollers.lobbypopup.GameConfigPopUpController;
import project.controllers.stagecontrollers.GameController;
import project.view.lobby.communication.Session;
import project.view.lobby.communication.User;

/**
 * TODO: Assigned the functionality to the buttons (launch, delete, play, ...) in here
 */
public class SessionGui extends HBox {

  private final Long curSessionId;
  private final User curUser;
  private final Thread lobbyUpdateThread;
  private Session curSession;


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
        .getResource("/project/fxml_files/session_gui.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(new SessionGuiController(curSession));
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * set up.
   */
  public void setup() {
    setSessionButtons();

  }

  /**
   * getCurSession.
   *
   * @return session
   */
  public Session getCurSession() {
    return curSession;
  }

  /**
   * setCurSession.
   *
   * @param newSession newSession
   */
  public void setCurSession(Session newSession) {
    this.curSession = newSession;
  }

  /**
   * getCurSessionId.
   *
   * @return id
   */
  public Long getCurSessionId() {
    return curSessionId;
  }

  /**
   * get user.
   *
   * @return user
   */
  public User getCurUser() {
    return curUser;
  }


  private EventHandler<ActionEvent> createGameConfigHandler() {
    return event -> {
      // display the GUI with some basic information needed
      // whenever the user clicks play button, we will reset the game request sender to
      // send correct REST requests to our backend in a right path name (splendorbase, city...)
      String gameServiceName = curSession.getGameParameters().getName();
      App.getGameRequestSender().setGameServiceName(gameServiceName);
      GameInfo curGameInfo = App.getGameRequestSender().getInstantGameInfo(curSessionId);

      if (gameServiceName.equals("splendorcity")
          || curGameInfo.getWinningPoints() > 0) {
        // if it's city game OR the game has been configured, load the game page immediately
        App.loadNewSceneToPrimaryStage("game_board.fxml",
            new GameController(curSessionId, App.getUser().getUsername()));

      } else {
        // otherwise, make creator to decide the max points
        App.loadPopUpWithController(
            "game_config.fxml",
            new GameConfigPopUpController(curSessionId, App.getUser().getUsername()),
            App.getGuiLayouts().getLargePopUpWidth(),
            App.getGuiLayouts().getLargePopUpHeight(),
            StageStyle.UTILITY
        );

      }

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
        topButton.setOnAction(createGameConfigHandler());
      } else {
        topButton.setVisible(true);
        topButton.setText("Play");
        topButton.setOnAction(createGameConfigHandler());
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
}
