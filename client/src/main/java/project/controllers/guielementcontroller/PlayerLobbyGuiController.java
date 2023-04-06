package project.controllers.guielementcontroller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import project.App;
import project.config.GameBoardLayoutConfig;
import project.view.lobby.communication.Player;

/**
 * PlayerLobbyGuiController.
 */
public class PlayerLobbyGuiController implements Initializable {

  private final Player player;
  private final GameBoardLayoutConfig config = App.getGuiLayouts();
  @FXML
  private Label textInfoLabel;
  // the FXML fields shared with Setting Page Controller
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

  /**
   * PlayerLobbyGuiController.
   *
   * @param player player
   */
  public PlayerLobbyGuiController(Player player) {
    this.player = player;
  }

  /**
   * getColourStringFromColourPicker.
   *
   * @return string
   */
  public String getColourStringFromColourPicker() {
    // Get the selected color from the ColorPicker
    Color chosenColor = colorPicker.getValue();
    // Convert the color to a 16-byte encoded string
    return App.colorToColourString(chosenColor);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // set up text for player
    String name = player.getName();
    String role = player.getRole().toString();
    String labelContent = String.format("User name: %s \nUser role: %s\n", name, role);
    textInfoLabel.setText(labelContent);

    // set up the colour part and update button part
    App.bindColourUpdateAction(player, colorPicker, false, colorUpdateButton, config);

    // bind the actions to password update
    App.bindPasswordUpdateAction(player, passwordField, passwordUpdateButton, config);

    // bind the actions to delete user (flag indicating staying at admin page)
    App.bindDeleteUserAction(player, deletePlayerButton, false, config);

    String warn = "THE DELETION CAN NOT BE REVERSED!!!";
    App.bindToolTip(warn, 15, deletePlayerButton, 20);
  }
}
