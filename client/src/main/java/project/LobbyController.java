package project;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import project.connection.LobbyServiceRequestSender;
import project.view.lobby.GameParameters;
import project.view.lobby.Session;
import project.view.lobby.SessionList;
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
    // TODO: saveGameName is an optional argument FOR NOW, implement it later
    String sessionId =
        lobbyRequestSender.sendCreateSessionRequest(creator, accessToken, gameName, "");
    Session newSession = new Gson()
        .fromJson(lobbyRequestSender
                .sendGetOneSessionDetailRequest(Long.parseLong(sessionId), "").getBody(),
            Session.class);
    Pane p = createSessionGui(accessToken, Long.parseLong(sessionId), newSession, sessionInfo);
    Platform.runLater(() -> {
      sessionVbox.getChildren().add(p);
    });
  }


  @FXML
  protected void onLogOutFromLobbyMenu() throws IOException {
    // clean up local lobby session cache before logging out
    App.setUser(null);
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


  private EventHandler<ActionEvent> createDeleteSessionHandler(
      VBox sessionVbox, Long sessionId,
      LobbyServiceRequestSender lobbyRequestSender,
      String accessToken) {
    return event -> {
      for (Node n : sessionVbox.getChildren()) {
        String paneSessionId = n.getAccessibleText();
        String sessionIdStr = sessionId.toString();
        if (paneSessionId != null && paneSessionId.equals(sessionIdStr)) {
          // TODO: If the onAction method involves GUI changes, defer this change by using
          //  Platform.runLater(() -> { methods_you_want_to_call })
          Platform.runLater(() -> {
            sessionVbox.getChildren().remove(n);
          });
          try {
            lobbyRequestSender.sendDeleteSessionRequest(accessToken, sessionId);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
        }
      }
    };
  }

  private EventHandler<ActionEvent> createJoinLeaveSessionHandler(
      String curUserName, Long sessionId,
      LobbyServiceRequestSender lobbyRequestSender, String accessToken) {
    return event -> {
      // TODO: add the request of joining / leaving session here
      // anonymous class of EventHandler instance

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
      } else if (joinAndLeaveButton.getText().equals("Leave")) { // otherwise, send leave request
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
      Gson gson = new Gson();
      Session joinedSession = gson.fromJson(getSessionDetailResponse.toString(), Session.class);
      String newSessionInfo = formatSessionInfo(joinedSession);
      HBox infoHbox = (HBox) joinAndLeaveButton.getParent();
      Label infoLabel = (Label) infoHbox.getChildren().get(0);
      Platform.runLater(() -> {
        infoLabel.setText(newSessionInfo);
      });

    };
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
                                Long sessionId,
                                Session curSession,
                                Label sessionInfoContent) {
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();

    // give different buttons depending on username and creator name
    HBox hb;
    String sessionCreatorName;
    sessionCreatorName = curSession.getCreator();
    // Can be changed for better visualization
    Region spaceBetween = new Region();
    spaceBetween.setPrefWidth(200);
    spaceBetween.setPrefHeight(30);
    //
    User user = App.getUser();
    String curUserName = user.getUsername();
    // if the user is the creator, provides delete and launch button
    if (curUserName.equals(sessionCreatorName)) {
      EventHandler<ActionEvent> deleteSessionHandler =
          createDeleteSessionHandler(sessionVbox, sessionId, lobbyRequestSender, accessToken);
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
      EventHandler<ActionEvent> joinAndLeaveSessionHandler =
          createJoinLeaveSessionHandler(curUserName, sessionId, lobbyRequestSender, accessToken);
      List<String> players = curSession.getPlayers();
      String buttonContent;
      if (players.contains(curUserName)) {
        buttonContent = "Leave";
      } else {
        buttonContent = "Join";
      }

      Button joinAndLeaveButton = new Button(buttonContent);
      joinAndLeaveButton.setOnAction(joinAndLeaveSessionHandler);
      hb = new HBox(sessionInfoContent, spaceBetween, joinAndLeaveButton);
    }

    Pane p = new Pane(hb);
    p.setAccessibleText(sessionId.toString());
    return p;
  }

  protected void addOneSessionGui(String accessToken,
                                  Long sessionId,
                                  Session curSession,
                                  VBox sessionVbox) {
    String sessionInfo = formatSessionInfo(curSession);
    Label sessionInfoLabel = new Label(sessionInfo);
    Pane newPane = createSessionGui(accessToken, sessionId, curSession, sessionInfoLabel);
    // defer GUI change to lobby main page
    Platform.runLater(() -> {
      sessionVbox.getChildren().add(newPane);
    });
  }

  protected void removeSessionsGui(Long sessionId, VBox sessionVbox) {

    for (Iterator<Node> iterator = sessionVbox.getChildren().iterator(); iterator.hasNext(); ) {
      Node n = iterator.next();
      if (n.getAccessibleText().equals(sessionId.toString())) {
        // defer GUI remove after
        Platform.runLater(iterator::remove);
      }
    }
  }

  protected void updateSessionsGui(Map<String, Session> localSessionIdMap, Node inputNode) {
    String curSessionId = inputNode.getAccessibleText();
    Pane childPane = (Pane) inputNode;
    HBox inputHbox = (HBox) childPane.getChildren().get(0);
    Label curSessionLabel = (Label) inputHbox.getChildren().get(0);
    Session curSession = localSessionIdMap.get(curSessionId);
    String sessionInfo = formatSessionInfo(curSession);
    // defer GUI change to lobby main page
    Platform.runLater(() -> {
      curSessionLabel.setText(sessionInfo);
    });
  }

  private Thread getUpdateOneSessionGuiThread(Long sessionId,
                                              LobbyServiceRequestSender lobbyRequestSender,
                                              VBox sessionVbox) {

    return new Thread(() -> {
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;
      Session localSession;

      while (true) {
        int responseCode = 408;
        while (responseCode == 408) {
          try {
            longPullResponse =
                lobbyRequestSender.sendGetOneSessionDetailRequest(sessionId, hashedResponse);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
          responseCode = longPullResponse.getStatus();
        }
        if (responseCode == 200) {
          hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
          localSession = new Gson().fromJson(longPullResponse.getBody(), Session.class);

          for (Node n : sessionVbox.getChildren()) {
            if (n.getAccessibleText().equals(sessionId.toString())) {
              Pane sessionPane = (Pane) n;
              HBox sessionHbox = (HBox) sessionPane.getChildren().get(0);
              Label sessionInfoLabel = (Label) sessionHbox.getChildren().get(0);
              String newSessionInfo = formatSessionInfo(localSession);
              // defer updating session info
              Platform.runLater(() -> {
                sessionInfoLabel.setText(newSessionInfo);
              });
            }
          }
        }
      }
    });
  }


  /**
   * Initializing info for local lobby.
   *
   * @throws UnirestException in case unirest failed to send a request
   */
  public void initialize() throws UnirestException {
    // Get all available games and pre-set the ChoiceBox
    // mainly about display on session info
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    List<GameParameters> gameParameters = lobbyRequestSender.sendAllGamesRequest();
    List<String> gameDisplayNames = new ArrayList<>();

    for (GameParameters g : gameParameters) {
      gameDisplayNames.add(g.getDisplayName());
      lobbyRequestSender.addGameNameMapping(g.getDisplayName(), g.getName());
    }
    // Available games to choose from ChoiceBox
    ObservableList<String> gameOptionsList = FXCollections.observableArrayList(gameDisplayNames);
    gameChoices.setItems(gameOptionsList);

    Thread updateAddRemoveSessionThread = new Thread(() -> {
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;
      SessionList localSessionList = null;
      boolean isFirstCheck = true;

      while (true) {
        int responseCode = 408;
        User user = App.getUser();
        // if there is no user logged in, do not do anything
        if (user == null) {
          continue;
        }
        while (responseCode == 408) {
          try {
            longPullResponse =
                lobbyRequestSender.sendGetAllSessionDetailRequest(hashedResponse);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
          responseCode = longPullResponse.getStatus();
        }

        if (responseCode == 200) {
          hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
          if (isFirstCheck) {
            localSessionList = new Gson().fromJson(longPullResponse.getBody(), SessionList.class);
            isFirstCheck = false;
            Map<Long, Session> localSessionsMap = localSessionList.getSessions();
            for (Long sessionId : localSessionsMap.keySet()) {
              Session curSession = localSessionsMap.get(sessionId);
              addOneSessionGui(user.getAccessToken(), sessionId, curSession, sessionVbox);
              getUpdateOneSessionGuiThread(sessionId, lobbyRequestSender, sessionVbox);
              Thread updateSessionInfoThread =
                  getUpdateOneSessionGuiThread(sessionId, lobbyRequestSender, sessionVbox);
              updateSessionInfoThread.start();
            }
          } else {
            // TODO: localSession has been set, check the diff between remote and local
            SessionList
                remoteSessionList =
                new Gson().fromJson(longPullResponse.getBody(), SessionList.class);

            Set<Long> remoteSessionIds = remoteSessionList.getSessionIds();
            int remoteSessionCount = remoteSessionList.getSessionsCount();

            Set<Long> localSessionIds = localSessionList.getSessionIds();
            int localSessionCount = localSessionList.getSessionsCount();

            // local more than remote -> delete Session GUI
            if (localSessionCount > remoteSessionCount) {
              localSessionIds.removeAll(remoteSessionIds);
              for (Long diffSessionId : localSessionIds) {
                removeSessionsGui(diffSessionId, sessionVbox);
              }
              localSessionList = remoteSessionList;
            } else if (remoteSessionCount > localSessionCount) {
              // remote more than local -> add Session GUI
              remoteSessionIds.removeAll(localSessionIds);
              for (Long diffSessionId : remoteSessionIds) {
                String accessToken = user.getAccessToken();
                Session curSession = remoteSessionList.getSessionById(diffSessionId);
                addOneSessionGui(accessToken, diffSessionId, curSession, sessionVbox);
                // create and start the thread that is responsible for updating this newly
                // created Session Pane
                Thread updateSessionInfoThread =
                    getUpdateOneSessionGuiThread(diffSessionId, lobbyRequestSender, sessionVbox);
                updateSessionInfoThread.start();
              }
              localSessionList = remoteSessionList;
            }
          }
        }
      }
    });
    updateAddRemoveSessionThread.start();

  }
}

