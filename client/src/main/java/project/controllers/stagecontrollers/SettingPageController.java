package project.controllers.stagecontrollers;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import project.App;
import project.controllers.popupcontrollers.LobbyWarnPopUpController;
import project.view.lobby.communication.Player;

public class SettingPageController extends AbstractLobbyController {

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

  @FXML
  private Button adminZoneButton;

  @FXML
  private Button lobbyPageButton;



  private void pageSpecificActionBind() {
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
    Player player = App.getLobbyServiceRequestSender()
        .getOnePlayer(App.getUser().getAccessToken(), App.getUser().getUsername());

    Color color = Color.web(player.getPreferredColour());
    colorPicker.setValue(color);
    colorUpdateButton.setOnAction(event -> {
      // Get the selected color from the ColorPicker
      Color chosenColor = colorPicker.getValue();

      // Convert the color to a 16-byte encoded string
      String colorString = String.format("%02X%02X%02X%02X",
          (int) (chosenColor.getRed() * 255),
          (int) (chosenColor.getGreen() * 255),
          (int) (chosenColor.getBlue() * 255),
          (int) (chosenColor.getOpacity() * 255));
      try {
        App.getLobbyServiceRequestSender().updateOnePlayerColour(
            App.getUser().getAccessToken(),
            App.getUser().getUsername(),
            colorString
        );
      } catch (UnirestException e) {
        // somehow failed to update the colour
        e.printStackTrace();
        String error = "Could not update user's new colour choice!\nPlease try again";
        App.loadPopUpWithController("lobby_warn.fxml",
            new LobbyWarnPopUpController(error),
            360,
            170);
      }
    });

    // TODO: Finish the other update action binding (can copy paste from PlayerLobbyGui controller)
  }
}
