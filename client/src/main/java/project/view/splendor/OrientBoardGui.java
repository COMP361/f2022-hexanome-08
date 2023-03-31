package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.PurchaseAction;
import ca.mcgill.comp361.splendormodel.actions.ReserveAction;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.Extension;
import ca.mcgill.comp361.splendormodel.model.OrientBoard;
import ca.mcgill.comp361.splendormodel.model.Position;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.App;
import project.config.GameBoardLayoutConfig;

/**
 * It displays the OrientBoardGui.
 */
public class OrientBoardGui implements BoardGui {

  private final AnchorPane playerBoardAnchorPane;
  private final long gameId;
  private final Map<Integer, OrientCardLevelGui> orientCardLevelGuiMap = new HashMap<>();
  private final VBox orientCardsBoard;

  /**
   * It displays the OrinetBoardGui.
   *
   * @param playerBoardAnchorPane playerBoardAnchorPane
   * @param gameId                gameID
   */
  public OrientBoardGui(AnchorPane playerBoardAnchorPane, long gameId) {
    this.gameId = gameId;
    this.playerBoardAnchorPane = playerBoardAnchorPane;
    this.orientCardsBoard = new VBox();
  }

  @Override
  public void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    OrientBoard orientBoard = (OrientBoard) tableTop.getBoard(Extension.ORIENT);
    // set up and add base card GUI, only purchase and reserve actions are
    // there in the action map at this point (or empty)
    Map<String, Action> reservePurchaseActions = playerActionMap.entrySet().stream()
        .filter(e -> e.getValue() instanceof ReserveAction
            ||
            e.getValue() instanceof PurchaseAction)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    Map<Position, List<ActionIdPair>> positionToActionMap =
        getPositionActions(reservePurchaseActions);

    // add from level 3 to level 1
    for (int i = 3; i >= 1; i--) {
      DevelopmentCard[] cardsOnBoard = orientBoard.getLevelCardsOnBoard(i);
      List<DevelopmentCard> deck = orientBoard.getDecks().get(i);
      OrientCardLevelGui orientBoardGui =
          new OrientCardLevelGui(i, cardsOnBoard, deck);
      orientBoardGui.setup();
      orientBoardGui.bindActionToCardAndDeck(positionToActionMap, gameId);
      orientCardLevelGuiMap.put(i, orientBoardGui);
      orientCardsBoard.getChildren().add(orientBoardGui);
    }
    // display it to main game GUI
    Platform.runLater(() -> {
      orientCardsBoard.setLayoutX(config.getOrientCardBoardLayoutX());
      orientCardsBoard.setLayoutY(config.getOrientCardBoardLayoutY());
      playerBoardAnchorPane.getChildren().add(orientCardsBoard);
    });
  }


  private Map<Position, List<ActionIdPair>> getPositionActions(
      Map<String, Action> reservePurchaseActions) {
    Map<Position, List<ActionIdPair>> positionToActionMap = new HashMap<>();
    // assign actions to positions (each position can have a list of action pair associated)
    for (String actionId : reservePurchaseActions.keySet()) {
      Action action = reservePurchaseActions.get(actionId);
      Position cardPosition;
      DevelopmentCard card;
      if (action instanceof PurchaseAction) {
        PurchaseAction purchaseAction = (PurchaseAction) action;
        cardPosition = purchaseAction.getCardPosition();
        card = purchaseAction.getCurCard();
      } else {
        ReserveAction reserveAction = (ReserveAction) action;
        cardPosition = reserveAction.getCardPosition();
        card = reserveAction.getCurCard();
      }
      // add the orient card actions
      if (!card.isBaseCard()) {
        List<ActionIdPair> actions;
        if (!positionToActionMap.containsKey(cardPosition)) {
          actions = new ArrayList<>();
        } else {
          actions = positionToActionMap.get(cardPosition);
        }
        actions.add(new ActionIdPair(actionId, action));
        positionToActionMap.put(cardPosition, actions);
      }

    }

    return positionToActionMap;
  }

  @Override
  public void clearContent() {
    orientCardsBoard.getChildren().clear();
  }


}
