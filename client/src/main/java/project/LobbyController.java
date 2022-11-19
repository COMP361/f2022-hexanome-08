package project;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import project.view.lobby.Game;
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

    EventHandler<ActionEvent> deleteSessionHandler = event -> {
      // TODO: add the request of deleting session here (some weird exceptions exist)

      for (Node n : sessionVbox.getChildren()) {
        Pane p = (Pane) n;
        String paneSessionId = p.getAccessibleText();
        if (paneSessionId != null && paneSessionId.equals(sessionId)) {
          try {
            lobbyRequestSender.sendDeleteSessionRequest(accessToken, sessionId);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }
          sessionVbox.getChildren().remove(n);
        }
      }

    };

    EventHandler<ActionEvent> launchSessionHandler = event -> {
      // TODO: add the request of launching session here
      System.out.println("Launch Session");
    };

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
    // Get all available games
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    User user = App.getUser();
    List<Game> games = lobbyRequestSender.sendAllGamesRequest();
    List<String> gameDisplayNames = new ArrayList<>();

    for (Game g : games) {
      gameDisplayNames.add(g.getDisplayName());
      lobbyRequestSender.addGameNameMapping(g.getDisplayName(), g.getName());
    }

    ObservableList<String> gameOptionsList =
        FXCollections.observableArrayList(gameDisplayNames);
    gameChoices.setItems(gameOptionsList);

    Map<String, Session> sessionIdMap = App.getLobbyServiceRequestSender().getSessionIdMap();
    Set<String> sessionIds = sessionIdMap.keySet();

    for (String sessionId : sessionIds) {
      String creator = sessionIdMap.get(sessionId).getCreator();
      String gameDisplayName = sessionIdMap.get(sessionId).getGameParameters().getDisplayName();
      int maxSessionPlayers =
          sessionIdMap.get(sessionId).getGameParameters().getMaxSessionPlayers();

      Label sessionInfo = new Label(
          gameDisplayName + " max player: " + maxSessionPlayers + " creator: " + creator);
      String accessToken = user.getAccessToken();
      Pane newPane = generateSessionPane(accessToken, sessionId, sessionInfo);
      sessionVbox.getChildren().add(newPane);
    }
  }


}
