package project;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;
import project.connection.LobbyServiceRequestSender;

/**
 * Lobby GUI controller.
 */
public class LogInController {

  @FXML
  private TextField userName;

  @FXML
  private PasswordField userPassword;

  @FXML
  private Label logInPageErrorMessage;

  @FXML
  private Button quitGameButton;

  /**
   * The logic of handling log in. The methods check if the user has input both username and user
   * password or not
   *
   * @throws IOException IOException if fxml file not found
   */
  @FXML
  protected void onLogInButtonClick() throws UnirestException {
    String userNameStr = userName.getText();
    String userPasswordStr = userPassword.getText();
    // retrieve the parsed JSONObject from the response
    LobbyServiceRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
    JSONObject logInResponseJson = lobbyRequestSender
        .sendLogInRequest(userNameStr, userPasswordStr);

    // extract fields from the object
    try {
      String accessToken = logInResponseJson.getString("access_token");
      lobbyRequestSender.setAccessToken(accessToken);
      JSONObject authorityResponseJson = lobbyRequestSender.sendAuthorityRequest(accessToken);
      String authority = authorityResponseJson.getString("authority");

      // if user is player, display admin_lobby_page
      if (authority.equals("ROLE_ADMIN")) {
        App.setRoot("admin_lobby_page");
        // TODO: how to visually display these session objects as JavaFX GUI?

      } else { // otherwise, player_lobby_page
        // App.setRoot("player_lobby_page");
        App.setRoot("LobbyService");
      }
      lobbyRequestSender.updateSessions();

    } catch (Exception e) {
      logInPageErrorMessage.setText("Please enter both valid username and password");
      userName.setText("");
      userPassword.setText("");
    }
  }

  @FXML
  protected void onQuitGameButtonClick() {
    Stage curStage = (Stage) quitGameButton.getScene().getWindow();
    curStage.close();
  }

}
