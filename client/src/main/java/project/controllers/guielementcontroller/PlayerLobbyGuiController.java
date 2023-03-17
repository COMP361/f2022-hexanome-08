package project.controllers.guielementcontroller;

import com.mashape.unirest.http.exceptions.UnirestException;
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
import project.controllers.popupcontrollers.LobbyWarnPopUpController;
import project.view.lobby.communication.Player;

public class PlayerLobbyGuiController implements Initializable {

  @FXML
  private Label textInfoLabel;

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

  private final Player player;

  public PlayerLobbyGuiController(Player player) {
    this.player = player;
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // set up text for player
    String name = player.getName();
    String role = player.getRole().toString();
    String labelContent = String.format("User name: %s \nUser role: %s\n", name, role);
    textInfoLabel.setText(labelContent);


    // set up the colour part and update button part
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
            name,
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


  }
}
