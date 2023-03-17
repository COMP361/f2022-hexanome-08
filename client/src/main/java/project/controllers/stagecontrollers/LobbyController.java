package project.controllers.stagecontrollers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import org.apache.commons.codec.digest.DigestUtils;
import project.App;
import project.connection.LobbyRequestSender;
import project.view.lobby.SessionGui;
import project.view.lobby.SessionGuiManager;
import project.view.lobby.communication.GameParameters;
import project.view.lobby.communication.Savegame;
import project.view.lobby.communication.Session;
import project.view.lobby.communication.SessionList;
import project.view.lobby.communication.User;

/**
 * login GUI controller.
 */
public class LobbyController extends AbstractLobbyController {

  private final Map<String, Savegame> allSaveGamesMap = new HashMap<>();
  @FXML
  private ChoiceBox<String> gameChoices;
  @FXML
  private ScrollPane allSessionScrollPane;
  @FXML
  private Button createSessionButton;

  @FXML
  private Button adminZoneButton;

  @FXML
  private Button settingButton;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    super.initialize(url,resourceBundle);
    // for lobby page, bind action to setting and admin button
    pageSpecificActionBind();

    // everytime we assign a new lobby controller, clean
    // the previous sessions
    SessionGuiManager.getInstance().clearSessionsRecorded();

    // assign function to the "Create" button
    createSessionButton.setOnAction(createClickOnCreateButton());

    // get all possible game names (including saved games) from LS
    setUpAvailableGameNames();

    // Set up the thread to keep updating sessions
    createUpdateGuiThread().start();
  }

  private EventHandler<ActionEvent> createClickOnCreateButton() {
    return event -> {
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
      try {
        lobbyRequestSender.sendCreateSessionRequest(creator, accessToken, gameName, saveGameId);
      } catch (UnirestException e) {
        throw new RuntimeException(e);
      }
    };
  }



  private void createAndAddSessionGui(Session curSession, Long sessionId, User user,
                                      Thread lobbyUpdateThread) {
    // create the new GUI
    SessionGui curSessionGui = new SessionGui(curSession, sessionId, user, lobbyUpdateThread);
    // set up the session gui
    curSessionGui.setup();
    SessionGuiManager.getInstance().addSessionGui(curSessionGui);

  }


  private Thread createUpdateGuiThread() {
    return new Thread(() -> {

      LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;

      try {
        while (!Thread.currentThread().isInterrupted()) {
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
              e.printStackTrace();
              throw new RuntimeException(e);
            }
            responseCode = longPullResponse.getStatus();
          }

          if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Interrupted Lobby Thread");
          }

          if (responseCode == 200) {
            // long pulling ends in success, we obtain the list of new sessions
            hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
            // try doing things without first check
            SessionList sessionList =
                new Gson().fromJson(longPullResponse.getBody(), SessionList.class);
            Map<Long, Session> sessionMap = sessionList.getSessions();
            // clear all old sessions and add the new ones
            SessionGuiManager.getInstance().clearSessionsRecorded();
            for (long sessionId : sessionMap.keySet()) {
              Session curSession = sessionMap.get(sessionId);
              createAndAddSessionGui(curSession, sessionId, App.getUser(), Thread.currentThread());
            }
          }
          // update the content in scroll pane
          Platform.runLater(() -> {
            allSessionScrollPane.setContent(SessionGuiManager.getInstance());
          });
        }
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }

    });
  }


  private void setUpAvailableGameNames() {
    // Get all available games and pre-set the ChoiceBox
    // mainly about display on session info
    LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    List<GameParameters> gameParameters = lobbyRequestSender.sendAllGamesRequest();
    List<String> gameDisplayNames = new ArrayList<>();
    List<String> saveGameIds = new ArrayList<>();

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
          // adding all saved game ids to a separate list
          saveGameIds.add(savegame.getSavegameid());
        }
      }
    }
    gameDisplayNames.addAll(saveGameIds);
    // Available games to choose from ChoiceBox (Note, this includes save game ids)
    ObservableList<String> gameOptionsList = FXCollections.observableArrayList(gameDisplayNames);
    gameChoices.setItems(gameOptionsList);
  }

  private void pageSpecificActionBind() {
    settingButton.setOnAction(event -> {
      App.loadNewSceneToPrimaryStage("setting_page.fxml", new SettingPageController());
    });

    adminZoneButton.setVisible(false);
    // potentially enable admin zone button functionality
    String role = App.getUser().getAuthority();
    // only set up for admin role
    if (role.equals("ROLE_ADMIN")) {
      adminZoneButton.setVisible(true);
      adminZoneButton.setOnAction(event -> {
        App.loadNewSceneToPrimaryStage("admin_zone.fxml",new AdminPageController());
      });
    }
  }



}

