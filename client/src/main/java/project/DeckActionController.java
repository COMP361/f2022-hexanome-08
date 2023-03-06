package project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import project.connection.GameRequestSender;

/**
 * Deck action controller class.
 */
public class DeckActionController implements Initializable {

  @FXML
  private Button reserveButton;

  @FXML
  private Button goBackButton;


  private final long gameId;
  private final String actionId;

  public DeckActionController(long gameId, String actionId) {
    this.gameId = gameId;
    this.actionId = actionId;
  }


  private EventHandler<ActionEvent> createOnClickReserveHandler() {
    // TODO: Send the request in here to the game server, for now do nothing
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      String playerName = App.getUser().getUsername();
      String accessToken = App.getUser().getAccessToken();
      sender.sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionId);
      System.out.println("Reserved From the Deck");
      Stage curWindow = (Stage) goBackButton.getScene().getWindow();
      curWindow.close();
    };
  }

  private EventHandler<ActionEvent> createOnClickBackHandler() {
    return event -> {
      Stage curWindow = (Stage) goBackButton.getScene().getWindow();
      curWindow.close();
    };
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    goBackButton.setOnAction(createOnClickBackHandler());
    reserveButton.setOnAction(createOnClickReserveHandler());
  }
}
