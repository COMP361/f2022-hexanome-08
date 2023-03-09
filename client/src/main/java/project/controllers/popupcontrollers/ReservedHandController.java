package project.controllers.popupcontrollers;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.PurchaseAction;
import ca.mcgill.comp361.splendormodel.actions.ReserveAction;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.NobleCard;
import ca.mcgill.comp361.splendormodel.model.Position;
import ca.mcgill.comp361.splendormodel.model.ReservedHand;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import project.App;
import project.view.splendor.ActionIdPair;

/**
 * Reserve hand controller class.
 */
public class ReservedHandController implements Initializable {

  private final List<ImageView> playerCards = new ArrayList<>();
  private final List<ImageView> playerNobles = new ArrayList<>();
  private final Map<String, Action> playerActions;
  private final Rectangle coverRectangle;
  @FXML
  private HBox reservedDevCardsHbox;
  @FXML
  private HBox reservedNoblesHbox;

  /**
   * Controller for the ReservedHand.
   *
   * @param reservedHand reservedHand
   * @param playerActions playerActions
   * @param coverRectangle coverRectangle
   */
  public ReservedHandController(ReservedHand reservedHand, Map<String, Action> playerActions,
                                Rectangle coverRectangle, long gameId) {
    Map<String, Action> purchaseActions = playerActions.entrySet()
        .stream().filter(e -> e.getValue() instanceof PurchaseAction)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    Map<Position, List<ActionIdPair>> positionToActionMap =
        getPositionActionsInReservedHand(purchaseActions);


    this.coverRectangle = coverRectangle;
    List<NobleCard> reservedNobles = reservedHand.getNobleCards();
    List<DevelopmentCard> reservedCards = reservedHand.getDevelopmentCards();
    // initialize the list of image views from player's reserved hand
    for (NobleCard nobleCard : reservedNobles) {
      String noblePath = App.getNoblePath(nobleCard.getCardName());
      ImageView nobleImageView = new ImageView(new Image(noblePath));
      nobleImageView.setFitHeight(100);
      nobleImageView.setFitWidth(100);
      playerNobles.add(nobleImageView);
    }

    for (int i = 0; i<reservedCards.size(); i++) {
      String cardPath;
      DevelopmentCard card = reservedCards.get(i);
      if (!card.isBaseCard()) {
        cardPath = App.getOrientCardPath(card.getCardName(), card.getLevel());
      } else {
        cardPath = App.getBaseCardPath(card.getCardName(), card.getLevel());
      }
      ImageView cardImageView = new ImageView(new Image(cardPath));

      List<ActionIdPair> actions = positionToActionMap.get(new Position(0,i));
      if(actions!=null){
        cardImageView.setOnMouseClicked(createClickOnCardHandler(gameId,actions));//TODO);
      }
      cardImageView.setFitWidth(80);
      cardImageView.setFitHeight(100);
      playerCards.add(cardImageView);
    }

    this.playerActions = playerActions;
  }

  private Map<Position, List<ActionIdPair>> getPositionActionsInReservedHand(
      Map<String, Action> purchaseActions) {
    Map<Position, List<ActionIdPair>> positionToActionMap = new HashMap<>();
    // assign actions to positions (each position can have a list of action pair associated)
    for (String actionId : purchaseActions.keySet()) {
      Action action = purchaseActions.get(actionId);
      Position cardPosition;
      DevelopmentCard card;
      PurchaseAction purchaseAction = (PurchaseAction) action;
      cardPosition = purchaseAction.getCardPosition();
      int level = cardPosition.getX();

      if(level==0){
        List<ActionIdPair> actions = new ArrayList<>();
        actions.add(new ActionIdPair(actionId, action));
        positionToActionMap.put(cardPosition, actions);
      }
    }
    return positionToActionMap;
  }


  private EventHandler<MouseEvent> createClickOnCardHandler(long gameId,
      List<ActionIdPair> allActions) {
    return event -> {
      try {
        App.loadPopUpWithController("card_action.fxml",
            new CardActionController(gameId, allActions, coverRectangle),
            coverRectangle, 360, 170);
        Button button = (Button) event.getSource();
        Stage window = (Stage) button.getScene().getWindow();
        window.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // add the image view to the Hbox to display
    for (ImageView imageView : playerNobles) {
      reservedNoblesHbox.getChildren().add(imageView);
      reservedNoblesHbox.setSpacing(5);
    }

    for (ImageView imageView : playerCards) {
      reservedDevCardsHbox.getChildren().add(imageView);
      reservedDevCardsHbox.setSpacing(5);
    }
  }
}
