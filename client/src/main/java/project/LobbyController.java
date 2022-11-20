package project;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;
import project.connection.LobbyServiceRequestSender;
import project.view.lobby.GameParameters;
import project.view.lobby.Session;
import project.view.lobby.User;

/**
 * login GUI controller.
 */
public class LobbyController {

  @FXML
  private ChoiceBox<String> gameChoices;

  @FXML
  private MenuItem adminZoneMenuItem;

  @FXML
  private ScrollPane sessionScrollPane;

  @FXML
  private VBox sessionVbox;


  @FXML
  protected void onCreateSessionButtonClick() throws UnirestException {
    // TODO: How to add a Session on GUI (with the buttons, everything)
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    User curUser = App.getUser();
    Map<String, String> gameNameMapping = lobbyRequestSender.getGameNameMapping();

    // Get the current game display name in the choice box
    String displayGameName = gameChoices.getValue();
    String gameName = gameNameMapping.get(displayGameName);
    String accessToken = curUser.getAccessToken();
    String creator = curUser.getUsername();
    JSONObject gameDetailsJson = lobbyRequestSender.getGameDetailsRequest(gameName);
    int maxSessionPlayers = gameDetailsJson.getInt("maxSessionPlayers");

    String sessionInfoStr = String.format(
        "%s, [%d/%d] players [%s]: \n",
        displayGameName, 1, maxSessionPlayers, creator) + String.format("creator: %s", creator);
    Label sessionInfo = new Label(sessionInfoStr);
    // TODO: Figure out where to get the saveGameName (maybe input by user? -> Later)
    String sessionId =
        lobbyRequestSender.sendCreateSessionRequest(creator, accessToken, gameName, "");


    Pane p = createSessionGui(accessToken, sessionId, sessionInfo, creator);

    Platform.runLater(() -> {
      sessionVbox.getChildren().add(p);
    });
  }


  @FXML
  protected void onLogOutFromLobbyMenu() throws IOException {
    // clean up local lobby session cache before logging out
    App.getLobbyServiceRequestSender().clearSessionIdMap();
    App.setRoot("start_page");
  }

  /**
   * Access admin zone page.
   */
  @FXML
  protected void onAdminZoneMenuClick() throws IOException {
    Stage curStage = (Stage) adminZoneMenuItem.getParentPopup().getOwnerWindow();
    App.setRootWithSizeTitle("admin_zone", 1000, 800, "Admin Zone");
    curStage.close();
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

  /**
   * create a GUI representation of session object.
   *
   * @param accessToken        access token
   * @param sessionId          session id
   * @param sessionInfoContent session info in string
   * @return a Pane that contains info about a session
   */
  private Pane createSessionGui(String accessToken,
                                String sessionId,
                                Label sessionInfoContent,
                                String creator) {
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();

    // give different buttons depending on username and creator name
    HBox hb;
    String sessionCreatorName;
    if (creator.equals("")) {
      sessionCreatorName = lobbyRequestSender.getSessionIdMap().get(sessionId).getCreator();
    } else {
      sessionCreatorName = creator;
    }

    // Can be changed for better visualization
    Region spaceBetween = new Region();
    spaceBetween.setPrefWidth(200);
    spaceBetween.setPrefHeight(30);
    User user = App.getUser();
    String curUserName = user.getUsername();
    // if the user is the creator, provides delete and launch button
    if (curUserName.equals(sessionCreatorName)) {
      EventHandler<ActionEvent> deleteSessionHandler = event -> {
        for (Node n : sessionVbox.getChildren()) {
          String paneSessionId = n.getAccessibleText();
          if (paneSessionId != null && paneSessionId.equals(sessionId)) {
            // TODO: If the onAction method involves GUI changes, defer this change by using
            //  Platform.runLater(() -> { methods_you_want_to_call })
            Platform.runLater(() -> {
              sessionVbox.getChildren().remove(n);
              try {
                lobbyRequestSender.sendDeleteSessionRequest(accessToken, sessionId);
                App.getLobbyServiceRequestSender().removeSessionIdMap(sessionId);
              } catch (UnirestException e) {
                throw new RuntimeException(e);
              }
            });
          }
        }

      };
      EventHandler<ActionEvent> launchSessionHandler = event -> {
        // TODO: add the request of launching session here
        //  (need to have the game server logic ready)
        System.out.println("Launch Session");
      };
      Button deleteButton = new Button("Delete");
      Button launchButton = new Button("Launch");
      deleteButton.setOnAction(deleteSessionHandler);
      launchButton.setOnAction(launchSessionHandler);
      hb = new HBox(sessionInfoContent, spaceBetween, deleteButton, launchButton);
    } else {
      EventHandler<ActionEvent> joinAndLeaveSessionHandler = event -> {
        // TODO: add the request of joining / leaving session here

        Button joinAndLeaveButton = (Button) event.getSource();

        // If the button says "Join", send join request

        if (joinAndLeaveButton.getText().equals("Join")) {
          // First thing, a request
          joinAndLeaveButton.setText("Leave");
          try {
            lobbyRequestSender.sendAddPlayerRequest(accessToken, sessionId, curUserName);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
        } else { // otherwise, send leave request
          joinAndLeaveButton.setText("Join");
          try {
            lobbyRequestSender.sendRemovePlayerRequest(accessToken, sessionId, curUserName);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
        }

        // Then, obtain the result from either join/leave request
        JSONObject getSessionDetailResponse;
        try {
          getSessionDetailResponse = lobbyRequestSender.sendGetSessionDetailRequest(sessionId);
        } catch (UnirestException e) {
          throw new RuntimeException(e);
        }
        Platform.runLater(() -> {
          Gson gson = new Gson();
          Session joinedSession = gson.fromJson(getSessionDetailResponse.toString(), Session.class);
          String newSessionInfo = formatSessionInfo(joinedSession);
          HBox infoHbox = (HBox) joinAndLeaveButton.getParent();
          Label infoLabel = (Label) infoHbox.getChildren().get(0);
          infoLabel.setText(newSessionInfo);
        });

      };
      Button joinAndLeaveButton = new Button("Join");
      joinAndLeaveButton.setOnAction(joinAndLeaveSessionHandler);
      hb = new HBox(sessionInfoContent, spaceBetween, joinAndLeaveButton);
    }

    Pane p = new Pane(hb);
    p.setAccessibleText(sessionId);
    return p;
  }

  private void addSessionsGui(Map<String, Session> localSessionIdMap, String accessToken,
                              VBox sessionVbox) {
    for (String sessionId : localSessionIdMap.keySet()) {
      Session curSession = localSessionIdMap.get(sessionId);
      String sessionInfo = formatSessionInfo(curSession);
      Label sessionInfoLabel = new Label(sessionInfo);
      Pane newPane = createSessionGui(accessToken, sessionId, sessionInfoLabel, "");
      // defer GUI change to lobby main page
      Platform.runLater(() -> {
        sessionVbox.getChildren().add(newPane);
      });
    }
  }

  private void removeSessionsGui(Map<String, Session> localSessionIdMap,
                                 Set<String> localSessionIds, VBox sessionVbox) {

    for (String sessionId : localSessionIds) {
      // remove session from local session id map
      localSessionIdMap.remove(sessionId);
      for (Node n : sessionVbox.getChildren()) {
        if (n.getAccessibleText().equals(sessionId)) {
          // defer GUI remove after
          Platform.runLater(() -> {
            sessionVbox.getChildren().remove(n);
          });
        }
      }
    }
  }

  private void updateSessionsGui(Map<String, Session> localSessionIdMap, Node inputNode) {
    String curSessionId = inputNode.getAccessibleText();
    HBox curSessionHbox = (HBox) inputNode;
    Label curSessionLabel = (Label) curSessionHbox.getChildren().get(0);
    Session curSession = localSessionIdMap.get(curSessionId);
    String sessionInfo = formatSessionInfo(curSession);
    // defer GUI change to lobby main page
    Platform.runLater(() -> {
      curSessionLabel.setText(sessionInfo);
    });
  }


  /**
   * Initializing info for local lobby.
   *
   * @throws UnirestException in case unirest failed to send a request
   */
  public void initialize() throws UnirestException {
    // Get all available games and pre-set the ChoiceBox
    // TODO: Some problem still remain unsolved for synchronization over diff clients...
    // mainly about display on session info
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    List<GameParameters> gameParameters = lobbyRequestSender.sendAllGamesRequest();
    List<String> gameDisplayNames = new ArrayList<>();

    for (GameParameters g : gameParameters) {
      gameDisplayNames.add(g.getDisplayName());
      lobbyRequestSender.addGameNameMapping(g.getDisplayName(), g.getName());
    }

    // Available games to choose from ChoiceBox
    ObservableList<String> gameOptionsList =
        FXCollections.observableArrayList(gameDisplayNames);
    gameChoices.setItems(gameOptionsList);

    // Busy waiting with backend javaFX thread to get lobby main page update
    // Similar thing with splendor game board
    // TODO: Deletion of Session is not visible on other client's side!
    Thread lobbyMainPageUpdateThread = new Thread(() -> {
      while (true) {
        // Start busy waiting by trying to update with the latest session info
        Map<String, Session> remoteSessionIdMap;
        Map<String, Session> localSessionIdMap = lobbyRequestSender.getSessionIdMap();
        try {
          remoteSessionIdMap = lobbyRequestSender.getRemoteSessions();
        } catch (UnirestException e) {
          throw new RuntimeException(e);
        }

        // if we have any remote sessions, then we need to sync local session ids
        // with the remote ones (either add or remove session from local sessionIdMap)
        User user = App.getUser();
        if (localSessionIdMap.isEmpty() && user != null) {
          // local has no record of any sessions, add all default ones
          // then copy the whole thing
          lobbyRequestSender.setSessionIdMap(remoteSessionIdMap);

          // generate all GUI if user logged in
          String accessToken = user.getAccessToken();
          localSessionIdMap = lobbyRequestSender.getSessionIdMap();
          addSessionsGui(localSessionIdMap, accessToken, sessionVbox);
        } else { // localSessionIdMap is not empty because it can only be
          int remoteSessionCount = remoteSessionIdMap.size();
          int localSessionCount = localSessionIdMap.size();
          // local already has a record of some sessions
          Set<String> remoteSessionIds = remoteSessionIdMap.keySet();
          Set<String> localSessionIds = localSessionIdMap.keySet();

          // TODO: Shallow copy might be a future problem....
          if (remoteSessionCount > localSessionCount) {
            // remote has more sessions, need to create ones locally
            remoteSessionIds.removeAll(localSessionIds);
            for (String sessionId : remoteSessionIds) {
              Session remoteSession = remoteSessionIdMap.get(sessionId);
              Session newLocalSession = new Session(remoteSession);
              // added new session to local session id map
              localSessionIdMap.put(sessionId, newLocalSession);
            }
            // update local session id map
            lobbyRequestSender.setSessionIdMap(localSessionIdMap);
            // GUI: add the new sessions
            // generate all GUI if user logged in
            if (user != null) {
              String accessToken = user.getAccessToken();
              addSessionsGui(localSessionIdMap, accessToken, sessionVbox);
            }

          } else if (localSessionCount > remoteSessionCount) {
            // local has more sessions, need to delete ones locally
            localSessionIds.removeAll(remoteSessionIds);
            removeSessionsGui(localSessionIdMap, localSessionIds, sessionVbox);
            // update local session id map
            lobbyRequestSender.setSessionIdMap(localSessionIdMap);
          }

          // TODO: After having the updated local sessions id map, we can update GUI
          // proceed with updating all local session ids' session info

          // the children of Vbox are Hbox, Hbox.get(0) contains the Label
          for (Node n : sessionVbox.getChildren()) {
            if (user != null) {
              updateSessionsGui(localSessionIdMap, n);
            }
          }
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }


      }
    });
    lobbyMainPageUpdateThread.start();
  }


}
