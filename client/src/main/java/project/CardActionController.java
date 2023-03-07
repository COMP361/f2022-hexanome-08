package project;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.PurchaseAction;
import ca.mcgill.comp361.splendormodel.actions.ReserveAction;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import project.connection.GameRequestSender;
import project.view.splendor.ActionIdPair;

/**
 * Card action controller class.
 */
public class CardActionController implements Initializable {
  private final List<ActionIdPair> allActionsPair;

  private final long gameId;

  @FXML
  private Button purchaseButton;

  @FXML
  private Button reserveButton;

  //@FXML
  //private Button goBackButton;

  public CardActionController(long gameId, List<ActionIdPair> allActionsPair) {
    this.gameId = gameId;
    this.allActionsPair = allActionsPair;
  }

  //private EventHandler<ActionEvent> createOnClickPurchaseHandler(String actionId) {
  //  // TODO: Send the request in here to the game server, for now do nothing
  //  return event -> {
  //    Button purchaseButton = (Button) event.getSource();
  //    // it's clickable, we can send some requests here
  //    GameRequestSender gameRequestSender = App.getGameRequestSender();
  //    String playerName = App.getUser().getUsername();
  //    String accessToken = App.getUser().getAccessToken();
  //    // sends a POST request that tells the server which action we chose
  //    gameRequestSender.sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionId);
  //    Stage curWindow = (Stage) purchaseButton.getScene().getWindow();
  //    curWindow.close();
  //
  //  };
  //}
  //
  //private EventHandler<ActionEvent> createOnClickReserveHandler(String actionId) {
  //  // TODO: Send the request in here to the game server, for now do nothing
  //  return event -> {
  //    // it's clickable, we can send some requests here
  //    Button reserveButton = (Button) event.getSource();
  //    GameRequestSender gameRequestSender = App.getGameRequestSender();
  //    String playerName = App.getUser().getUsername();
  //    String accessToken = App.getUser().getAccessToken();
  //    // sends a POST request that tells the server which action we chose
  //    gameRequestSender.sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionId);
  //    Stage curWindow = (Stage) reserveButton.getScene().getWindow();
  //    curWindow.close();
  //  };
  //}

  // click on either purchase or reserve do the same thing, send the request
  private EventHandler<ActionEvent> createOnClickButtonHandler(String actionId) {
    // TODO: Send the request in here to the game server, for now do nothing
    return event -> {
      // it's clickable, we can send some requests here

      GameRequestSender gameRequestSender = App.getGameRequestSender();
      String playerName = App.getUser().getUsername();
      String accessToken = App.getUser().getAccessToken();
      // sends a POST request that tells the server which action we chose
      gameRequestSender.sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionId);
      Button button = (Button) event.getSource();
      Stage curWindow = (Stage) button.getScene().getWindow();
      curWindow.close();
    };
  }


  //
  //private EventHandler<ActionEvent> createOnClickBackHandler() {
  //  return event -> {
  //    Stage curWindow = (Stage) goBackButton.getScene().getWindow();
  //    curWindow.close();
  //  };
  //}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // by default, these buttons are greyed out
    purchaseButton.setDisable(true);
    reserveButton.setDisable(true);

    // set them clickable if we have any action
    for (ActionIdPair actionIdPair : allActionsPair) {
      Action action = actionIdPair.getAction();
      if (action instanceof PurchaseAction) {
        purchaseButton.setDisable(false);
        purchaseButton.setOnAction(createOnClickButtonHandler(actionIdPair.getActionId()));
      }
      if (action instanceof ReserveAction) {
        reserveButton.setDisable(false);
        reserveButton.setOnAction(createOnClickButtonHandler(actionIdPair.getActionId()));
      }
    }

    //goBackButton.setOnAction(createOnClickBackHandler());
  }
}
