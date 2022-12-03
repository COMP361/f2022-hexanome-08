package project;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import project.connection.SplendorServiceRequestSender;

public class CardActionController implements Initializable {
  private final String[] actionHash;

  private final long gameId;

  @FXML
  private Button purchaseButton;

  @FXML
  private Button reserveButton;

  @FXML
  private Button goBackButton;

  public CardActionController(long gameId, String[] actionHash) {
    this.gameId = gameId;
    this.actionHash = actionHash;
  }

  private EventHandler<ActionEvent> createOnClickPurchaseHandler() {
    // TODO: Send the request in here to the game server, for now do nothing
    return event -> {
      Button purchaseButton = (Button) event.getSource();
      if (actionHash[0] == null) {
        // TODO: Index 0 is the hash for purchase hash
        // No action is assigned, this button gets greyed out
        purchaseButton.setDisable(true);
      } else {
        // it's clickable, we can send some requests here
        SplendorServiceRequestSender gameRequestSender = App.getGameRequestSender();
        String playerName = App.getUser().getUsername();
        String accessToken = App.getUser().getAccessToken();
        // sends a POST request that tells the server which action we chose
        try {
          gameRequestSender.
              sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionHash[0]);
        } catch (UnirestException e) {
          throw new RuntimeException(e);
        }
        Stage curWindow = (Stage) purchaseButton.getScene().getWindow();
        curWindow.close();
      }

    };
  }

  private EventHandler<ActionEvent> createOnClickReserveHandler() {
    // TODO: Send the request in here to the game server, for now do nothing
    return event -> {
      if (actionHash[1] == null) {
        // TODO: Index 1 is the hash for reserve hash
        // No action is assigned, this button gets greyed out
        Button purchaseButton = (Button) event.getSource();
        purchaseButton.setDisable(true);
      } else {
        // it's clickable, we can send some requests here
        SplendorServiceRequestSender gameRequestSender = App.getGameRequestSender();
        String playerName = App.getUser().getUsername();
        String accessToken = App.getUser().getAccessToken();
        // sends a POST request that tells the server which action we chose
        try {
          gameRequestSender.
              sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionHash[1]);
        } catch (UnirestException e) {
          throw new RuntimeException(e);
        }
        Stage curWindow = (Stage) goBackButton.getScene().getWindow();
        curWindow.close();
      }
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
    purchaseButton.setOnAction(createOnClickPurchaseHandler());
    reserveButton.setOnAction(createOnClickReserveHandler());
    goBackButton.setOnAction(createOnClickBackHandler());
  }
}
