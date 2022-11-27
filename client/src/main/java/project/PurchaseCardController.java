package project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import project.view.splendor.gameitems.DevelopmentCard;

public class PurchaseCardController implements Initializable {
  private final DevelopmentCard cardPurchased;

  @FXML
  private Button purchaseButton;

  @FXML
  private Button reserveButton;

  @FXML
  private Button goBackButton;

  public PurchaseCardController(DevelopmentCard cardPurchased) {
    this.cardPurchased = cardPurchased;
  }

  private EventHandler<ActionEvent> createOnClickPurchaseHandler() {
    // TODO: Send the request in here to the game server, for now do nothing
    return event -> {
      System.out.println("Sent request to server to purchase " + cardPurchased.getCardName());

      Stage curWindow = (Stage) goBackButton.getScene().getWindow();
      curWindow.close();
    };
  }

  private EventHandler<ActionEvent> createOnClickReserveHandler() {
    // TODO: Send the request in here to the game server, for now do nothing
    return event -> {
      System.out.println("Sent request to server to purchase " + cardPurchased.getCardName());

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
    purchaseButton.setOnAction(createOnClickPurchaseHandler());
    reserveButton.setOnAction(createOnClickReserveHandler());
    goBackButton.setOnAction(createOnClickBackHandler());
  }
}
