package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.PurchaseAction;
import ca.mcgill.comp361.splendormodel.actions.ReserveAction;
import ca.mcgill.comp361.splendormodel.actions.ReturnTokenAction;
import ca.mcgill.comp361.splendormodel.actions.TakeTokenAction;
import ca.mcgill.comp361.splendormodel.model.BaseBoard;
import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.Extension;
import ca.mcgill.comp361.splendormodel.model.NobleCard;
import ca.mcgill.comp361.splendormodel.model.Position;
import ca.mcgill.comp361.splendormodel.model.SplendorDevHelper;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import project.App;
import project.GameBoardLayoutConfig;

/**
 * Display the baseboardGUI.
 */
public class BaseBoardGui implements BoardGui {

  private final NobleBoardGui nobleBoardGui;
  private final TokenBankGui tokenBankGui;
  private final Map<Integer, BaseCardLevelGui> baseCardLevelGuiMap = new HashMap<>();
  private final VBox baseCardBoard;
  private final AnchorPane playerBoardAnchorPane;

  private final long gameId;
  private final Rectangle coverRectangle;

  /**
   * What the baseBoardGUI has.
   *
   * @param playerBoardAnchorPane playerBoardAnchorPane
   * @param gameId gameId
   * @param coverRectangle coverRectangle
   */
  public BaseBoardGui(AnchorPane playerBoardAnchorPane, long gameId, Rectangle coverRectangle) {
    this.gameId = gameId;
    nobleBoardGui = new NobleBoardGui(100, 100, 5);
    tokenBankGui = new TokenBankGui(gameId);
    this.playerBoardAnchorPane = playerBoardAnchorPane;
    this.baseCardBoard = new VBox();
    this.coverRectangle = coverRectangle;
  }


  @Override
  public void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    BaseBoard baseBoard = (BaseBoard) tableTop.getBoard(Extension.BASE);
    // set up and add noble GUI. since the nobles are not clickable in set up, no actions!
    List<NobleCard> nobleCards = baseBoard.getNobles();
    Platform.runLater(() -> {
      nobleBoardGui.setup(nobleCards, config.getNobleLayoutX(), config.getNobleLayoutY(), true);
      playerBoardAnchorPane.getChildren().add(nobleBoardGui);
    });

    // set up and add bank GUI. need to consider the action map for all take token actions
    // since it's set up, not update, no return token action can be here
    Map<String, TakeTokenAction> takeTokenActionMap = new HashMap<>();
    Map<String, ReturnTokenAction> returnTokenActionMap = new HashMap<>();

    for (String actionId : playerActionMap.keySet()) {
      Action action = playerActionMap.get(actionId);
      if (action instanceof TakeTokenAction) {
        TakeTokenAction takeTokenAction = (TakeTokenAction) action;
        //System.out.println(takeTokenAction.getTokens());
        takeTokenActionMap.put(actionId, takeTokenAction);
      }
      if (action instanceof ReturnTokenAction) {
        ReturnTokenAction returnTokenAction = (ReturnTokenAction) action;
        //System.out.println(takeTokenAction.getTokens());
        returnTokenActionMap.put(actionId, returnTokenAction);
      }
    }
    EnumMap<Colour, Integer> bankBalance = tableTop.getBank().getAllTokens();
    if (returnTokenActionMap.isEmpty()) {
      Platform.runLater(() -> {
        tokenBankGui.setup(takeTokenActionMap,
            bankBalance,
            config.getTokenBankLayoutX(),
            config.getTokenBankLayoutY(),
            true);
        playerBoardAnchorPane.getChildren().add(tokenBankGui);
      });
    } else { //means there are only return token actions
      Platform.runLater(() -> {
        tokenBankGui.setupReturnToken(returnTokenActionMap,
            bankBalance,  //this is an empty bank map
            config.getTokenBankLayoutX(),
            config.getTokenBankLayoutY(),
            true);
        playerBoardAnchorPane.getChildren().add(tokenBankGui);
      });
    }


    // set up and add base card GUI, only purchase and reserve actions are
    // there in the action map at this point (or empty)
    Map<String, Action> reservePurchaseActions = playerActionMap.entrySet()
        .stream().filter(e -> e.getValue() instanceof ReserveAction
                    ||
            e.getValue() instanceof PurchaseAction)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    Map<Position, List<ActionIdPair>> positionToActionMap =
        getPositionActions(reservePurchaseActions);
    // add from level 3 to level 1
    for (int i = 3; i >= 1; i--) {
      DevelopmentCard[] cardsOnBoard = baseBoard.getLevelCardsOnBoard(i);
      List<DevelopmentCard> deck = baseBoard.getDecks().get(i);
      BaseCardLevelGui baseCardLevelGui =
          new BaseCardLevelGui(i, cardsOnBoard, deck, coverRectangle);
      baseCardLevelGui.setup();
      baseCardLevelGui.bindActionToCardAndDeck(positionToActionMap, gameId);
      baseCardLevelGuiMap.put(i, baseCardLevelGui);
      baseCardBoard.getChildren().add(baseCardLevelGui);
    }
    // display it to main game GUI
    Platform.runLater(() -> {
      baseCardBoard.setLayoutX(config.getBaseCardBoardLayoutX());
      baseCardBoard.setLayoutY(config.getBaseCardBoardLayoutY());
      playerBoardAnchorPane.getChildren().add(baseCardBoard);
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
      // only take the positions of the card with no effect
      if (card.getPurchaseEffects().size() == 0
          && (card.getGemNumber() == 1
          || (card.isPaired() && card.getGemNumber() == 2))) {
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
    for (Position position : positionToActionMap.keySet()) {
      System.out.print("level + index: " + position.getX() + " " + position.getY());
      System.out.println(" " + positionToActionMap.get(position).size());
    }
    return positionToActionMap;
  }


  @Override
  public void clearContent() {
    ObservableList<Node> currentChildren = playerBoardAnchorPane.getChildren();
    currentChildren.remove(nobleBoardGui);
    currentChildren.remove(tokenBankGui);
    baseCardBoard.getChildren().clear();
  }
}
