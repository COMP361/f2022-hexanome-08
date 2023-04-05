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
import javafx.scene.paint.Color;
import org.apache.commons.codec.digest.DigestUtils;
import project.App;
import project.connection.LobbyRequestSender;
import project.controllers.popupcontrollers.LobbyWarnPopUpController;
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

  private EventHandler<ActionEvent> createClickOnCreateButton() {
    return event -> {
      LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
      User curUser = App.getUser();
      Map<String, String> gameNameMapping = lobbyRequestSender.getGameNameMapping();

      // Get the current game display name in the choice box
      String displayGameName = gameChoices.getValue();
      // if there is no registered game, we throw a popup to player
      if (displayGameName == null) {
        App.loadPopUpWithController("lobby_warn.fxml",
            new LobbyWarnPopUpController(
                "Please choose a option of game first!"
                    + "\nIf there is no game, ask the admin in person\nto create one!",
                "NO GAME CHOSEN"),
            360,
            170);
        return;
      }

      // split the "display name: save game id" or "display name"
      // to an array of [display name, save game id]
      // or [display name]
      String[] gameNameParts = displayGameName.split(":\\s*");
      String gameName = gameNameMapping.get(gameNameParts[0]);
      String saveGameId = "";
      if (gameNameParts.length == 2) {
        saveGameId = gameNameParts[1];
        Savegame savegame = allSaveGamesMap.get(saveGameId);
        gameName = savegame.getGamename();
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


  private Thread createSessionGuiUpdateThread() {
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
            // before continuing to next iteration or exit the loop, if interrupted, finish
            if (Thread.currentThread().isInterrupted()) {
              throw new InterruptedException("Lobby Thread: " + Thread.currentThread().getName()
                  + " terminated");
            }
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
              // create the new GUI
              SessionGui curSessionGui =
                  new SessionGui(curSession, sessionId, App.getUser(), Thread.currentThread());
              // set up the session gui
              curSessionGui.setup();
              SessionGuiManager.getInstance().addSessionGui(curSessionGui);
            }
          }
          // change the order of session GUI based on username
          // and update the color if it's launched
          SessionGuiManager.getInstance().setupSessionGuiOrder();

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
          saveGameIds.add(g.getDisplayName() + ": " + savegame.getSavegameid());
        }
      }
    }
    gameDisplayNames.addAll(saveGameIds);
    // Available games to choose from ChoiceBox (Note, this includes save game ids)
    ObservableList<String> gameOptionsList = FXCollections.observableArrayList(gameDisplayNames);
    gameChoices.setItems(gameOptionsList);
    // default, base game (if there is any game registered)
    if (!gameOptionsList.isEmpty()) {
      gameChoices.setValue(gameOptionsList.get(0));
    }
  }

  private void pageSpecificActionBind() {
    settingButton.setOnAction(event -> {
      // before leaving the lobby page, make sure to stop the update thread
      super.getSessionUpdateThread().interrupt();
      App.loadNewSceneToPrimaryStage("setting_page.fxml", new SettingPageController());
    });

    adminZoneButton.setVisible(false);
    // potentially enable admin zone button functionality
    String role = App.getUser().getAuthority();
    // only set up for admin role
    if (role.equals("ROLE_ADMIN")) {
      adminZoneButton.setVisible(true);
      adminZoneButton.setStyle("-fx-background-color: #e50916");
      adminZoneButton.setTextFill(Color.WHITE);
      adminZoneButton.setOnAction(event -> {
        // before leaving the lobby page, make sure to stop the update thread
        super.getSessionUpdateThread().interrupt();
        App.loadNewSceneToPrimaryStage("admin_zone.fxml", new AdminPageController());
      });
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    super.initialize(url, resourceBundle);
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
    initializeSessionUpdateThread(createSessionGuiUpdateThread());
    super.getSessionUpdateThread().setDaemon(true);
    super.getSessionUpdateThread().start();
  }


}

