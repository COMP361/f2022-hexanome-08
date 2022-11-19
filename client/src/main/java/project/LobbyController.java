package project;

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
    String gameDisplayName = gameChoices.getValue();
    String gameName = gameNameMapping.get(gameDisplayName);
    String accessToken = curUser.getAccessToken();
    String userName = curUser.getUsername();
    JSONObject gameDetailsJson = lobbyRequestSender.getGameDetailsRequest(gameName);

    int maxSessionPlayers = gameDetailsJson.getInt("maxSessionPlayers");

    // TODO: Do we need these 2 info ???
    int minSessionPlayers = gameDetailsJson.getInt("minSessionPlayers");
    String gameLocation = gameDetailsJson.getString("location");
    //

    Label sessionInfo = new Label(
        gameDisplayName + " max player: " + maxSessionPlayers + " creator: " + userName);

    // TODO: Figure out where to get the saveGameName (maybe input by user?)
    String sessionId =
        lobbyRequestSender.sendCreateSessionRequest(userName, accessToken, gameName, "");
    lobbyRequestSender.updateSessionMapping();

    Pane p = generateSessionPane(accessToken, sessionId, sessionInfo);
    sessionVbox.getChildren().add(p);
  }


  @FXML
  protected void onLogOutFromLobbyMenu() throws IOException {
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

  private Pane generateSessionPane(String accessToken,
                                   String sessionId,
                                   Label sessionInfoContent) {
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    User user = App.getUser();

    EventHandler<ActionEvent> deleteSessionHandler = event -> {
      for (Node n : sessionVbox.getChildren()) {
        String paneSessionId = n.getAccessibleText();
        if (paneSessionId != null && paneSessionId.equals(sessionId)) {
          /* TODO: If the onAction method involves GUI changes, defer this change by using
              Platform.runLater(() -> { methods_you_want_to_call }) */
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
      System.out.println("Launch Session");
    };
    // give different buttons depending on username and creator name
    //    String sessionCreatorName =
    //    lobbyRequestSender.getSessionIdMap().get(sessionId).getCreator();
    //    String curUserName = user.getUsername();
    //    if (curUserName.equals(sessionCreatorName)) {
    //      Button deleteButton = new Button("Delete");
    //      Button launchButton = new Button("Launch");
    //      deleteButton.setOnAction(deleteSessionHandler);
    //      launchButton.setOnAction(launchSessionHandler);
    //      HBox hb = new HBox(sessionInfoContent, deleteButton, launchButton);
    //    } else {
    //      Button joinButton = new Button("Join");
    //      HBox hb = new HBox(sessionInfoContent, joinButton);
    //    }


    Button deleteButton = new Button("Delete");
    Button launchButton = new Button("Launch");
    deleteButton.setOnAction(deleteSessionHandler);
    launchButton.setOnAction(launchSessionHandler);
    HBox hb = new HBox(sessionInfoContent, deleteButton, launchButton);
    Pane p = new Pane(hb);
    p.setAccessibleText(sessionId);
    return p;
  }


  /**
   * Initializing info for local lobby.
   *
   * @throws UnirestException in case unirest failed to send a request
   */
  public void initialize() throws UnirestException {
    // Get all available games and pre-set the ChoiceBox
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    User user = App.getUser();
    List<GameParameters> gameParameters = lobbyRequestSender.sendAllGamesRequest();
    List<String> gameDisplayNames = new ArrayList<>();

    for (GameParameters g : gameParameters) {
      gameDisplayNames.add(g.getDisplayName());
      lobbyRequestSender.addGameNameMapping(g.getDisplayName(), g.getName());
    }

    ObservableList<String> gameOptionsList =
        FXCollections.observableArrayList(gameDisplayNames);
    gameChoices.setItems(gameOptionsList);

    // Busy waiting with backend javaFX thread to get lobby main page update
    // Similar thing with splendor game board
    Thread lobbyMainPageUpdateThread = new Thread(() -> {
      while (true) {
        // Start busy waiting by trying to update with the latest session info
        try {
          lobbyRequestSender.updateSessionMapping();
        } catch (UnirestException e) {
          throw new RuntimeException(e);
        }
        Map<String, Session> sessionIdMap = lobbyRequestSender.getSessionIdMap();
        Set<String> remoteSessionIds = sessionIdMap.keySet();
        Set<String> localSessionIds = new HashSet<>();
        for (Node n : sessionVbox.getChildren()) {
          String curLocalSessionId = n.getAccessibleText();
          localSessionIds.add(curLocalSessionId);
        }

        Set<String> diffSessionIds;
        if (localSessionIds.size() > remoteSessionIds.size()) {
          // use the set with more objects to removeAll the one with less objects
          // more local than remote, need to remove GUI
          localSessionIds.removeAll(remoteSessionIds);
          diffSessionIds = new HashSet<>(localSessionIds);
          for (Node n : sessionVbox.getChildren()) {
            String curSessionId = n.getAccessibleText();
            if (diffSessionIds.contains(curSessionId)) {
              Platform.runLater(() -> {
                sessionVbox.getChildren().remove(n);
                try {
                  lobbyRequestSender.updateSessionMapping();
                }catch (UnirestException e) {
                  throw new RuntimeException(e);
                }
              });
            }
          }
        }
        else if (localSessionIds.size() < remoteSessionIds.size()) {
          // more remote than local, need to add GUI
          remoteSessionIds.removeAll(localSessionIds);
          diffSessionIds = new HashSet<>(remoteSessionIds);
          for (String diffSessionId : diffSessionIds) {
            Session missingSession = sessionIdMap.get(diffSessionId);
            String creator = missingSession.getCreator();
            String gameDisplayName = missingSession.getGameParameters().getDisplayName();
            int maxSessionPlayers = missingSession.getGameParameters().getMaxSessionPlayers();
            Label sessionInfo = new Label(
                gameDisplayName + " max player: " + maxSessionPlayers + " creator: " + creator);
            if (user != null) {
              String accessToken = user.getAccessToken();
              Pane newPane = generateSessionPane(accessToken, diffSessionId, sessionInfo);

              if (sessionVbox.getChildren().size() < sessionIdMap.size()) {
                Platform.runLater(() -> {
                  sessionVbox.getChildren().add(newPane);
                  try {
                    lobbyRequestSender.updateSessionMapping();
                  } catch (UnirestException e) {
                    throw new RuntimeException(e);
                  }
                });
              }
            }
          }
        }

        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    });
    lobbyMainPageUpdateThread.start();
  }


}
