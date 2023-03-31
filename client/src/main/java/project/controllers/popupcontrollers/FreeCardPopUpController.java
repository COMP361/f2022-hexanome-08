package project.controllers.popupcontrollers;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.CardExtraAction;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.Position;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import project.App;

/**
 * Control free card pop up.
 */
public class FreeCardPopUpController extends ActionSelectionSender implements Initializable {
  private final Map<String, Action> playerActionMap;
  @FXML
  private HBox freeCardsHbox;
  @FXML
  private Text title;

  public FreeCardPopUpController(long gameId, Map<String, Action> playerActionMap) {
    super(gameId);
    this.playerActionMap = playerActionMap;
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    //TODO: The server has taken care of preventing generating FREE_CARD
    // action generation if it's dummy card
    int cardsAvailableToFree = playerActionMap.size();
    ImageView[] sortedImageViews = new ImageView[cardsAvailableToFree];
    for (String actionId : playerActionMap.keySet()) {
      Action action = playerActionMap.get(actionId);
      CardExtraAction cardExtraAction = (CardExtraAction) action;
      DevelopmentCard card = (DevelopmentCard) cardExtraAction.getCurCard();
      Position cardPosition = cardExtraAction.getCardPosition();
      String cardImagePath;
      int imageViewInsertIndex;
      if (card.isBaseCard()) {
        cardImagePath = App.getBaseCardPath(card.getCardName(), card.getLevel());
        imageViewInsertIndex = cardPosition.getY();
      } else {
        cardImagePath = App.getOrientCardPath(card.getCardName(), card.getLevel());
        if (cardsAvailableToFree == 5 && cardPosition.getY() == 1) {
          imageViewInsertIndex = cardPosition.getY() + 3;
        } else {
          imageViewInsertIndex = cardPosition.getY() + 4;
        }
      }


      ImageView cardImageView = new ImageView(new Image(cardImagePath));
      cardImageView.setFitHeight(100);
      cardImageView.setFitWidth(80);
      sortedImageViews[imageViewInsertIndex] = cardImageView;
      cardImageView.setOnMouseClicked(createOnActionSelectionClick(actionId));
    }
    // add all sorted, function assigned image views to the HBox
    freeCardsHbox.getChildren().addAll(sortedImageViews);
    title.setText("Choose one development card to take for free :)");
  }

}
