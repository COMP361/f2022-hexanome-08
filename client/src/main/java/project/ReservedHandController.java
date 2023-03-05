package project;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.NobleCard;
import ca.mcgill.comp361.splendormodel.model.ReservedHand;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Reserve hand controller class.
 */
public class ReservedHandController implements Initializable {

  @FXML
  private HBox reservedDevCardsHbox;

  @FXML
  private HBox reservedNoblesHbox;

  private final List<ImageView> playerCards = new ArrayList<>();
  private final List<ImageView> playerNobles = new ArrayList<>();

  private final Map<String, Action> playerActions;

  public ReservedHandController(ReservedHand reservedHand, Map<String, Action> playerActions) {
    List<NobleCard> reservedNobles = reservedHand.getNobleCards();
    List<DevelopmentCard> reservedCards = reservedHand.getDevelopmentCards();
    // initialize the list of image views from player's reserved hand
    for (NobleCard nobleCard : reservedNobles) {
      String noblePath = App.getNoblePath(nobleCard.getCardName());
      playerNobles.add(new ImageView(new Image(noblePath)));
    }

    for (DevelopmentCard card : reservedCards) {
      String cardPath;
      if (card.getPurchaseEffects() != null && card.getPurchaseEffects().size() > 0) {
        cardPath = App.getOrientCardPath(card.getCardName(), card.getLevel());
      } else {
        cardPath = App.getBaseCardPath(card.getCardName(), card.getLevel());
      }
      playerCards.add(new ImageView(new Image(cardPath)));
    }

    this.playerActions = playerActions;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // add the image view to the Hbox to display
    for (ImageView imageView : playerNobles) {
      reservedNoblesHbox.getChildren().add(imageView);
    }

    for (ImageView imageView : playerCards) {
      reservedDevCardsHbox.getChildren().add(imageView);
    }
  }
}
