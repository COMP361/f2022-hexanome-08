package project.controllers.stagecontrollers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import project.App;
import project.connection.LobbyRequestSender;
import project.view.lobby.PlayerLobbyGui;
import project.view.lobby.communication.Player;

public class AdminPageController extends AbstractLobbyController {

  private List<Player> allRegisteredPlayers = new ArrayList<>();

  // app user related fields
  @FXML
  private ScrollPane allPlayersScrollPane;

  @FXML
  private VBox allPlayersVbox;

  @FXML
  private Button settingButton;

  @FXML
  private Button lobbyPageButton;

  // new user related fields
  @FXML
  private ChoiceBox<String> rolesChoiceBox;

  @FXML
  private ColorPicker newUserColourPicker;


  @FXML
  private TextField userName;

  @FXML
  private PasswordField userPassword;

  @FXML
  private Button addUserButton;


  private void pageSpecificActionBind() {
    settingButton.setOnAction(event -> {
      App.loadNewSceneToPrimaryStage("setting_page.fxml", new SettingPageController());
    });
    // jump back to lobby page
    lobbyPageButton.setOnAction(event -> {
      App.loadNewSceneToPrimaryStage("lobby_page.fxml", new LobbyController());
    });
  }
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    super.initialize(url,resourceBundle);
    // for admin page, bind action to setting and lobby button
    pageSpecificActionBind();

    LobbyRequestSender sender = App.getLobbyServiceRequestSender();
    // set up the user gui in the scroll pane region
    String adminToken = App.getUser().getAccessToken();
    allRegisteredPlayers = Arrays.asList(sender.getPlayers(adminToken));
    for (Player player : allRegisteredPlayers) {
      allPlayersVbox.getChildren().add(new PlayerLobbyGui(player));
    }
    allPlayersScrollPane.setContent(allPlayersVbox);



    // TODO: Finish other features on admin zone


  }
}
