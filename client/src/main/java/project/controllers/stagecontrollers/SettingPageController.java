package project.controllers.stagecontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.PasswordField;
import project.App;
import project.config.GameBoardLayoutConfig;
import project.view.lobby.communication.Player;

/**
 * SettingPageController.
 */
public class SettingPageController extends AbstractLobbyController {
  private final GameBoardLayoutConfig config = App.getGuiLayouts();
  @FXML
  private Button adminZoneButton;
  @FXML
  private Button lobbyPageButton;
  // the FXML fields shared with Player Lobby Gui Controller
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button passwordUpdateButton;
  @FXML
  private ColorPicker colorPicker;
  @FXML
  private Button colorUpdateButton;
  @FXML
  private Button deletePlayerButton;

  private void pageSpecificActionBind() {
    adminZoneButton.setVisible(false);
    // potentially enable admin zone button functionality
    String role = App.getUser().getAuthority();
    // only set up for admin role
    if (role.equals("ROLE_ADMIN")) {
      adminZoneButton.setVisible(true);
      adminZoneButton.setOnAction(event -> {
        App.loadNewSceneToPrimaryStage("admin_zone.fxml", new AdminPageController());
      });
    }

    // jump back to lobby page
    lobbyPageButton.setOnAction(event -> {
      App.loadNewSceneToPrimaryStage("lobby_page.fxml", new LobbyController());
    });
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    super.initialize(url, resourceBundle);

    // for admin page, bind action to admin and lobby button (admin potentially greyed out)
    pageSpecificActionBind();

    // set up the colour part and update button part
    Player player = App.getLobbyServiceRequestSender().getOnePlayer(
        App.getUser().getAccessToken(),
        App.getUser().getUsername());

    // set up the colour part and update button part
    App.bindColourUpdateAction(player, colorPicker, true, colorUpdateButton, config);

    // bind the actions to password update
    App.bindPasswordUpdateAction(player, passwordField, passwordUpdateButton, config);

    // bind the actions to delete user (flag indicating staying at admin page)
    App.bindDeleteUserAction(player, deletePlayerButton, true, config);

    String warn = "THE DELETION CAN NOT BE REVERSED!!!";
    App.bindToolTip(warn, 15, deletePlayerButton, 20);

    //deletePlayerButton.setDisable(true);
    //if (!App.getUser().getAuthority().equals("ROLE_ADMIN")) {
    //  deletePlayerButton.setDisable(false);
    //  App.bindDeleteUserAction(player, deletePlayerButton, true, config);
    //
    //  String warn = "THE DELETION CAN NOT BE REVERSED!!!";
    //  App.bindToolTip(warn, 15, deletePlayerButton, 20);
    //}
  }
}
