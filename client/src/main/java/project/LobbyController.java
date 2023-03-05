package project;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import org.apache.commons.codec.digest.DigestUtils;
import project.connection.LobbyRequestSender;
import project.view.lobby.communication.GameParameters;
import project.view.lobby.communication.Savegame;
import project.view.lobby.communication.Session;
import project.view.lobby.SessionGui;
import project.view.lobby.SessionGuiManager;
import project.view.lobby.communication.SessionList;
import project.view.lobby.communication.User;

/**
 * login GUI controller.
 */
public class LobbyController implements Initializable {

  @FXML
  private ChoiceBox<String> gameChoices;

  @FXML
  private ScrollPane allSessionScrollPane;

  private final Map<String, Savegame> allSaveGamesMap = new HashMap<>();

  @FXML
  protected void onCreateSessionButtonClick() throws UnirestException {
    LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    User curUser = App.getUser();
    Map<String, String> gameNameMapping = lobbyRequestSender.getGameNameMapping();

    // Get the current game display name in the choice box
    String displayGameName = gameChoices.getValue();
    String gameName = gameNameMapping.get(displayGameName);
    String saveGameId = "";
    // display name can be a game service name or a saved id, do that check
    if (allSaveGamesMap.containsKey(displayGameName)) {
      // then we need to get the service name and save game id from here
      Savegame savegame = allSaveGamesMap.get(displayGameName);
      gameName = savegame.getGamename();
      saveGameId = savegame.getSavegameid();
    }

    String accessToken = curUser.getAccessToken();
    String creator = curUser.getUsername();
    lobbyRequestSender.sendCreateSessionRequest(creator, accessToken, gameName, saveGameId);
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
      LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
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
    SessionGuiManager.addSessionUpdateThread(updateSessionInfoThread);
  }

  private Set<Long> findDifferentSessionIds(Set<Long> setA, Set<Long> setB) {
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


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    SessionGuiManager.resetManager();
    // Get all available games and pre-set the ChoiceBox
    // mainly about display on session info
    LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    List<GameParameters> gameParameters = lobbyRequestSender.sendAllGamesRequest();
    List<String> gameDisplayNames = new ArrayList<>();

    // map from display name to actual game name
    for (GameParameters g : gameParameters) {
      gameDisplayNames.add(g.getDisplayName());
      lobbyRequestSender.addGameNameMapping(g.getDisplayName(), g.getName());

      // use every game name to get the list of all save game ids
      String token = App.getUser().getAccessToken();
      Savegame[] savegames = lobbyRequestSender.getAllSavedGames(token, g.getName());
      if (savegames.length > 0) {
        // we have more than 1 saved game ids for this current service game
        for (Savegame savegame : savegames) {
          allSaveGamesMap.put(savegame.getSavegameid(), savegame);
          gameDisplayNames.add(savegame.getSavegameid());
        }
      }
    }


    // Available games to choose from ChoiceBox (Note, this includes save game ids)
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
    SessionGuiManager.addSessionUpdateThread(updateAddRemoveSessionThread);

  }
}

