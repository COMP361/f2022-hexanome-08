package project;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import project.connection.LobbyServiceRequestSender;
import project.view.lobby.Game;

/**
 * login GUI controller.
 */
public class LobbyController {

  @FXML
  private ChoiceBox<String> gameChoices;

  @FXML
  private MenuItem logOutFromLobbyMenuItem;


  @FXML
  protected void onCreateSessionButtonClick() throws UnirestException {
    // TODO: How to add a Session on GUI (with the buttons, everything)
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    Map<String, String> gameNameMapping = lobbyRequestSender.getGameNameMapping();
    String curGameDisplayName = gameChoices.getValue();
    String gameName = gameNameMapping.get(curGameDisplayName);
    String accessToken = lobbyRequestSender.getAccessToken();
    assert accessToken != null && !accessToken.equals("");
    // TODO: Figure out where to get the saveGameName (maybe input by user?)
    lobbyRequestSender.sendCreateSessionRequest(accessToken, gameName, "");
  }


  @FXML
  protected void onLogOutFromLobbyMenu() throws IOException {
    App.setRoot("start_page");
  }

  /**
   * Initializing info for local lobby.
   *
   * @throws UnirestException in case unirest failed to send a request
   */
  public void initialize() throws UnirestException {
    // Get all available games
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    List<Game> games = lobbyRequestSender.sendAllGamesRequest();
    List<String> gameDisplayNames = new ArrayList<>();

    for (Game g : games) {
      gameDisplayNames.add(g.getDisplayName());
      lobbyRequestSender.addGameNameMapping(g.getDisplayName(), g.getName());
    }

    ObservableList<String> gameOptionsList =
        FXCollections.observableArrayList(gameDisplayNames);
    gameChoices.setItems(gameOptionsList);

  }
}
