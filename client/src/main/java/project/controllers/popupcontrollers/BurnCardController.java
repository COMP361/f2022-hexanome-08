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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.App;

/**
 * Control free card pop up.
 */
public class BurnCardController extends ActionSelectionSender implements Initializable {
  private final Map<String, Action> playerActionMap;
  @FXML
  private HBox freeCardsHbox;
  @FXML
  private Text title;
  public BurnCardController(long gameId, Map<String, Action> playerActionMap) {
    super(gameId);
    this.playerActionMap = playerActionMap;
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    int cardsAvailableToBurn = playerActionMap.size();
    HBox[] cardSatchelPairHbox = new HBox[cardsAvailableToBurn];
    int counter = 0;
    for (String actionId : playerActionMap.keySet()) {
      Action action = playerActionMap.get(actionId);
      CardExtraAction cardExtraAction = (CardExtraAction) action;
      DevelopmentCard card = (DevelopmentCard) cardExtraAction.getCurCard();
      String cardImagePath;
      int imageViewInsertIndex;
      if (card.isBaseCard()) {
        cardImagePath = App.getBaseCardPath(card.getCardName(), card.getLevel());
      } else {
        cardImagePath = App.getOrientCardPath(card.getCardName(), card.getLevel());
      }
      imageViewInsertIndex = counter;
      ImageView cardImageView = new ImageView(new Image(cardImagePath));
      cardImageView.setFitHeight(100);
      cardImageView.setFitWidth(80);
      Rectangle satchelMark = new Rectangle();
      if (card.isPaired()) {
        satchelMark.setFill(Color.BLUE);
      } else {
        satchelMark.setFill(Color.WHITESMOKE);
      }
      HBox curPairedCard = new HBox(cardImageView, satchelMark);
      cardSatchelPairHbox[imageViewInsertIndex] = curPairedCard;
      curPairedCard.setOnMouseClicked(createOnActionSelectionClick(actionId));
      counter++;
    }
    // add all sorted, function assigned image views to the HBox
    freeCardsHbox.getChildren().addAll(cardSatchelPairHbox);
    title.setText("Choose one card to burn");
  }

}
