package project.view.lobby;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.App;
import project.connection.LobbyServiceRequestSender;

public class SessionGui extends HBox {

  private Session curSession;

  private final Long curSessionId;

  private final User curUser;

  public SessionGui(Session curSession, Long curSessionId, User curUser) {
    this.curSession = curSession;
    this.curSessionId = curSessionId;
    this.curUser = curUser;
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/session_template.fxml"));
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
        // TODO: Adjust this fxml file later when game server is done
        App.setRoot("splendor_base_game_board");
        // anything involving reloading this page, we need to clear the thread pool
        //stopAndClearThreads();
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
  //  this POST request and send a PUT request to GameServer
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
        // launchButton greyed out
        btmButton.setDisable(true);

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

    if (curPlayers.contains(curUserName)) {
      // creator must be in curPlayers
      int curPlayersCount = curPlayers.size();
      int minPlayersCount = curSession.getGameParameters().getMinSessionPlayers();
      if (curUserName.equals(curSession.getCreator()) && curPlayersCount == minPlayersCount) {
        // if curUser is the creator, and we have enough players, we should change
        // launch to disable(false)
        topButton.setVisible(true);
        topButton.setDisable(false);
        btmButton.setVisible(true);
      }
      if (curSession.isLaunched()) {
        topButton.setVisible(true);
        topButton.setText("Play");
        topButton.setOnAction(createPlayGameHandler());
      }
    } else {
      if (curSession.isLaunched()) {
        topButton.setVisible(true);
        topButton.setText("Watch");
        topButton.setOnAction(createPlayGameHandler());
      }
    }

    //String creatorName = curSession.getCreator();
    //int curPlayersCount = curPlayers.size();
    //int minPlayersCount = localSession.getGameParameters().getMinSessionPlayers();
    //if (curUserName.equals(creatorName)) {
    //  Button launchButton = (Button) sessionHbox.getChildren().get(3);
    //  // If it's curUser, then we must check for set Enable for him/her
    //  if (launchButton.isDisabled()) {
    //    // only setDisable(false) for the launch button if we have enough players
    //    if (curPlayersCount == minPlayersCount) {
    //      Platform.runLater(() -> {
    //        launchButton.setDisable(false);
    //      });
    //    }
    //  } else {
    //    // if launchButton.isDisabled() == false, means we can click it
    //    // if it's clickable, then we should replace it here
    //    Platform.runLater(() -> {
    //      sessionHbox.getChildren().remove(2);
    //      sessionHbox.getChildren().remove(2);
    //      sessionHbox.getChildren().add(replaceButton);
    //    });
    //  }
    //} else {
    //  // if the user is not creator, there is no point updating the launch for one
    //  // thus ONLY the remove / replace button logic here for user
    //  Platform.runLater(() -> {
    //    // remove the Button on index 2 (0: Label, 1: Region, 2: Button)
    //    sessionHbox.getChildren().remove(2);
    //    sessionHbox.getChildren().add(replaceButton);
    //  });
    //}

  }

  public void setup() {
    setSessionButtons();
    setSessionInfoText();
  }

}
