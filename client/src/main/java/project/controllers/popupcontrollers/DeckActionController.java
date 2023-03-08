package project.controllers.popupcontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import project.App;
import project.connection.GameRequestSender;

/**
 * Deck action controller class.
 */
public class DeckActionController implements Initializable {

  @FXML
  private Button reserveButton;

  private final long gameId;
  private final String actionId;

  private final Rectangle coverRectangle;

  public DeckActionController(long gameId, String actionId, Rectangle coverRectangle) {
    this.gameId = gameId;
    this.actionId = actionId;
    this.coverRectangle = coverRectangle;
  }


  private EventHandler<ActionEvent> createOnClickReserveHandler() {
    // TODO: Send the request in here to the game server, for now do nothing
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      String playerName = App.getUser().getUsername();
      String accessToken = App.getUser().getAccessToken();
      sender.sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionId);
      Stage curWindow = (Stage) reserveButton.getScene().getWindow();
      coverRectangle.setVisible(false);
      curWindow.close();
    };
  }

  //private EventHandler<ActionEvent> createOnClickBackHandler() {
  //  return event -> {
  //    Stage curWindow = (Stage) goBackButton.getScene().getWindow();
  //    curWindow.close();
  //  };
  //}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    //goBackButton.setOnAction(createOnClickBackHandler());
    reserveButton.setOnAction(createOnClickReserveHandler());
  }
}
