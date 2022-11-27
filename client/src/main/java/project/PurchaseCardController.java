package project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import project.view.splendor.gameitems.DevelopmentCard;

public class PurchaseCardController implements Initializable {
  private final DevelopmentCard cardPurchased;

  public PurchaseCardController(DevelopmentCard cardPurchased) {
    this.cardPurchased = cardPurchased;
  }
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    System.out.println(cardPurchased.getCardName());
  }
}
