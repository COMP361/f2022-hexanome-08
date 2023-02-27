package project;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import org.apache.commons.codec.digest.DigestUtils;
import project.connection.LobbyServiceRequestSender;
import project.view.lobby.communication.GameParameters;
import project.view.lobby.communication.Session;
import project.view.lobby.SessionGui;
import project.view.lobby.SessionGuiManager;
import project.view.lobby.communication.SessionList;
import project.view.lobby.communication.User;

/**
 * login GUI controller.
 */
public class LobbyController {

  @FXML
  private ChoiceBox<String> gameChoices;

  // TODO: Add the GUI to display the saved games for player to create load session from
  @FXML
  private ChoiceBox<String> savedGames;

  @FXML
  private ScrollPane allSessionScrollPane;

  @FXML
  protected void onCreateSessionButtonClick() throws UnirestException {
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    User curUser = App.getUser();
    Map<String, String> gameNameMapping = lobbyRequestSender.getGameNameMapping();

    // Get the current game display name in the choice box
    String displayGameName = gameChoices.getValue();
    String gameName = gameNameMapping.get(displayGameName);
    String accessToken = curUser.getAccessToken();
    String creator = curUser.getUsername();
    // TODO: Change the saveGameName to a input of this method, or somehow get the value
    //  as long as it's not "" (maybe should read it from a drop down select menu)
    //  the session will have this savegameid associated with it, and then we can launch it
    //  to make sure we are indeed launching a saved game before
    lobbyRequestSender.sendCreateSessionRequest(creator, accessToken, gameName, "");
  }


  @FXML
  protected void onLogOutFromLobbyMenu() throws IOException {
    // clean up local lobby session cache before logging out
    App.setUser(null);
    App.setRoot("start_page");
  }

  @FXML
  public void joinGameDev() throws IOException {
    // TODO: For debug usage
    App.setRoot("splendor_base_game_board");
  }

  private Thread getUpdateOneSessionGuiThread(Long sessionId) {
    return new Thread(() -> {
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;
      Session localSession;
      boolean isFirstCheck = true;
      LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
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
          // obtain the latest session info of one session
          localSession = new Gson().fromJson(longPullResponse.getBody(), Session.class);
          SessionGui sessionGuiNeedToUpdate = SessionGuiManager.getSessionIdGuiMap().get(sessionId);
          // bind the sessionGui to a different Session that contains diff info
          sessionGuiNeedToUpdate.setCurSession(localSession);

          if (!isFirstCheck) {
            // if it's NOT first check, we have to change buttons
            Platform.runLater(() -> {
              sessionGuiNeedToUpdate.updateSessionGui();
              sessionGuiNeedToUpdate.setSessionInfoText();
            });
          } else {
            // otherwise, always check to change text
            Platform.runLater(sessionGuiNeedToUpdate::setSessionInfoText);
            isFirstCheck = false;
          }

        }
      }
    });
  }

  private void createAndAddSessionGui(Session curSession, Long sessionId, User user) {
    // create the new GUI
    SessionGui curSessionGui = new SessionGui(curSession, sessionId, user);
    // set up the session gui
    curSessionGui.setup();

    // store the reference to the global singleton gui map
    SessionGuiManager.addSessionIdGuiMap(curSessionGui, sessionId);

    // create and start the thread that is responsible for updating this newly
    // created Session Pane, add the new sessionGui to the Singleton VBox
    Platform.runLater(() -> {
      SessionGuiManager.addSessionGui(curSessionGui);
    });

    Thread updateSessionInfoThread = getUpdateOneSessionGuiThread(sessionId);
    updateSessionInfoThread.start();
  }

  private Set<Long> findDifferentSessionIds(Set<Long> setA,
                                            Set<Long> setB) {
    HashSet<Long> resultSet = new HashSet<>();
    if (setA.size() > setB.size()) {
      for (Long l : setA) {
        if (!setB.contains(l)) {
          resultSet.add(l);
        }
      }

    } else if (setB.size() > setA.size()) {
      for (Long l : setB) {
        if (!setA.contains(l)) {
          resultSet.add(l);
        }
      }
    }

    return resultSet;
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
    // map from display name to actual game name
    for (GameParameters g : gameParameters) {
      gameDisplayNames.add(g.getDisplayName());
      lobbyRequestSender.addGameNameMapping(g.getDisplayName(), g.getName());
    }
    // Available games to choose from ChoiceBox
    ObservableList<String> gameOptionsList = FXCollections.observableArrayList(gameDisplayNames);
    gameChoices.setItems(gameOptionsList);


    // Set up the thread to keep updating sessions
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
            longPullResponse = lobbyRequestSender.sendGetAllSessionDetailRequest(hashedResponse);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
          responseCode = longPullResponse.getStatus();
        }

        if (responseCode == 200) {
          // long pulling ends in success, we obtain the list of new sessions
          hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
          if (isFirstCheck) {
            // the SessionList which contains Session objects from the remote side
            localSessionList = new Gson().fromJson(longPullResponse.getBody(), SessionList.class);
            isFirstCheck = false;

            Map<Long, Session> localSessionsMap = localSessionList.getSessions();
            // since it's the first check, we blindly add all sessions from remote side
            for (Long curSessionId : localSessionsMap.keySet()) {
              Session curSession = localSessionsMap.get(curSessionId);
              createAndAddSessionGui(curSession, curSessionId, user);
            }
          } else {
            SessionList remoteSessionList =
                new Gson().fromJson(longPullResponse.getBody(), SessionList.class);

            Set<Long> remoteSessionIds = remoteSessionList.getSessionIds();
            Set<Long> localSessionIds = localSessionList.getSessionIds();
            Set<Long> diffSessionIds = findDifferentSessionIds(remoteSessionIds, localSessionIds);
            if (diffSessionIds.isEmpty()) {
              continue;
            }
            // there are diff session ids, need to remove or add session gui
            int remoteSessionCount = remoteSessionList.getSessionsCount();
            int localSessionCount = localSessionList.getSessionsCount();
            // local more than remote -> delete Session GUI
            if (localSessionCount > remoteSessionCount) {
              for (Long diffSessionId : diffSessionIds) {
                SessionGui curSessionGui =
                    SessionGuiManager.getSessionIdGuiMap().get(diffSessionId);

                // remove the reference from the global reference sessionId -> GUI map
                SessionGuiManager.removeSessionIdGuiMap(diffSessionId);

                Platform.runLater(() -> {
                  SessionGuiManager.removeSessionGui(curSessionGui);
                });
              }
            } else if (remoteSessionCount > localSessionCount) {
              // remote more than local -> add Session GUI
              for (Long diffSessionId : diffSessionIds) {
                Session curSession = remoteSessionList.getSessionById(diffSessionId);
                createAndAddSessionGui(curSession, diffSessionId, user);
              }
            }
            // update local SessionList
            localSessionList = remoteSessionList;

          }
        }
        // at the end of every update checking loop, we update the ScrollPane
        Platform.runLater(() -> {
          allSessionScrollPane.setContent(SessionGuiManager.getInstance());
        });
      }
    });
    updateAddRemoveSessionThread.start();

  }
}

