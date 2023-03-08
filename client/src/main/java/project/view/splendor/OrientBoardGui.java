package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.PurchaseAction;
import ca.mcgill.comp361.splendormodel.actions.ReserveAction;
import ca.mcgill.comp361.splendormodel.actions.TakeTokenAction;
import ca.mcgill.comp361.splendormodel.model.*;

import java.util.*;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import project.App;
import project.GameBoardLayoutConfig;
import project.view.InvalidDataException;

public class OrientBoardGui implements BoardGui {

  private Map<Integer, OrientCardLevelGui> orientCardLevelGuiMap = new HashMap<>();
  private VBox orientCardsBoard;
  private final AnchorPane playerBoardAnchorPane;

  private final Rectangle coverRectangle;
  private final long gameId;

  public OrientBoardGui(AnchorPane playerBoardAnchorPane, long gameId, Rectangle coverRectangle) {
    this.gameId = gameId;
    this.playerBoardAnchorPane = playerBoardAnchorPane;
    this.orientCardsBoard = new VBox();
    this.coverRectangle = coverRectangle;
  }

  @Override
  public void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    OrientBoard orientBoard = (OrientBoard) tableTop.getBoard(Extension.ORIENT);
    // set up and add base card GUI, only purchase and reserve actions are
    // there in the action map at this point (or empty)
    Map<String, Action> reservePurchaseActions = playerActionMap.entrySet().stream()
        .filter(e -> e.getValue() instanceof ReserveAction ||
            e.getValue() instanceof PurchaseAction)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    //System.out.println("Before reorganizing: ");
    //for (Action action : reservePurchaseActions.values()) {
    //  if(action instanceof PurchaseAction) {
    //    Position position = ((PurchaseAction) action).getCardPosition();
    //    System.out.println("Purchase action at: " + position.getX() + " " + position.getY());
    //  } else {
    //    Position position = ((ReserveAction) action).getCardPosition();
    //    System.out.println("Reserve action at: " + position.getX() + " " + position.getY());
    //  }
    //
    //}
    Map<Position, List<ActionIdPair>> positionToActionMap =
            getPositionActions(reservePurchaseActions);
    //
    //System.out.println("After reorganizing: ");
    //for (Position position : )


    // add from level 3 to level 1
    for (int i = 3; i >=1; i--) {
      DevelopmentCard[] cardsOnBoard = orientBoard.getLevelCardsOnBoard(i);
      List<DevelopmentCard> deck = orientBoard.getDecks().get(i);
      OrientCardLevelGui orientBoardGui  = new OrientCardLevelGui(i, cardsOnBoard, deck, coverRectangle);
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

  //// return only the purchase actions and reserve actions that are bind to orient card
  //private Map<Position, List<ActionIdPair>> cleanupActionMap(Map<String, Action> playerActionMap) {
  //  Map<String, Action> reservePurchaseActions = playerActionMap.entrySet().stream()
  //      .filter(e -> e.getValue() instanceof ReserveAction ||
  //          e.getValue() instanceof PurchaseAction)
  //      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  //
  //  Map<Position, List<ActionIdPair>> result = new HashMap<>();
  //  // we have filtered to make sure we only have Purchase or Reserve left
  //  for (String actionId : reservePurchaseActions.keySet()) {
  //    Position cardPosition;
  //    Action action = reservePurchaseActions.get(actionId);
  //    DevelopmentCard curCard;
  //    if (action instanceof PurchaseAction) {
  //      cardPosition = ((PurchaseAction) action).getCardPosition();
  //      curCard = ((PurchaseAction) action).getCurCard();
  //    } else {
  //      cardPosition = ((ReserveAction) action).getCardPosition();
  //      curCard = ((ReserveAction) action).getCurCard();
  //    }
  //    // only add the actions with orient cards
  //    if (!curCard.isBaseCard()) {
  //      if (result.containsKey(cardPosition)) {
  //        List<ActionIdPair> cardActions = new ArrayList<>();
  //        ActionIdPair firstPair = new ActionIdPair(actionId, action);
  //        result.put(cardPosition, cardActions);
  //      } else {
  //        List<ActionIdPair> cardActions = result.get(cardPosition);
  //
  //        cardActions.add()
  //      }
  //    }
  //
  //
  //  }
  //
  //  return result;
  //
  //}


  private Map<Position, List<ActionIdPair>> getPositionActions (
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
      }
      else {
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
