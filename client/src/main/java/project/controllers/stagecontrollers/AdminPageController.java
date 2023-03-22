package project.controllers.stagecontrollers;

import ca.mcgill.comp361.splendormodel.model.Colour;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import project.App;
import project.GameBoardLayoutConfig;
import project.connection.LobbyRequestSender;
import project.controllers.popupcontrollers.LobbyWarnPopUpController;
import project.view.lobby.PlayerLobbyGui;
import project.view.lobby.communication.Player;
import project.view.lobby.communication.Role;

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
    GameBoardLayoutConfig config = App.getGuiLayouts();
    super.initialize(url, resourceBundle);
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

    //add choices for rolesChoiceBox (player, service, admin)
    rolesChoiceBox.getItems().add("Player");
    rolesChoiceBox.getItems().add("Admin");
    rolesChoiceBox.getItems().add("Service");

    addUserButton.setOnAction(event -> {
      String msg;
      String title;
      String username = userName.getText();
      String password = userPassword.getText();
      String pref_colour = newUserColourPicker.getValue().toString();
      //pref_colour = pref_colour.substring(2,8);
      Role role = Role.valueOf("ROLE_" + rolesChoiceBox.getValue().toUpperCase(Locale.ROOT));
      Player new_player = new Player(username, pref_colour, password,role);
      //passing in name of player who we are adding

      try {
        App.getLobbyServiceRequestSender().putOneNewPlayer(App.getUser().getAccessToken()
            ,username,new_player);
        //refresh the page
        App.loadNewSceneToPrimaryStage("admin_zone.fxml", new AdminPageController());
        title = "Add New Player Confirmation";
        msg = "Player was added to the Lobby Service database!";
        App.loadPopUpWithController("lobby_warn.fxml",
            new LobbyWarnPopUpController(msg, title),
                config.getSmallPopUpWidth(),
                config.getSmallPopUpHeight());
      } catch (UnirestException e) {
        title = "Add New Player Error";
        msg = "Player could not be added to the Lobby Service database";
        App.loadPopUpWithController("lobby_warn.fxml",
            new LobbyWarnPopUpController(msg, title),
                config.getSmallPopUpWidth(),
                config.getSmallPopUpHeight());

      }
    });


    // TODO: Finish other features on admin zone


  }
}
