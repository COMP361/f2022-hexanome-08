package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.TakeTokenAction;
import ca.mcgill.comp361.splendormodel.model.Bank;
import ca.mcgill.comp361.splendormodel.model.BaseBoard;
import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.Extension;
import ca.mcgill.comp361.splendormodel.model.NobleCard;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.App;
import project.GameBoardLayoutConfig;
import project.view.InvalidDataException;

public class BaseBoardGui implements BoardGui {

  private final NobleBoardGui nobleBoardGui;
  private final TokenBankGui tokenBankGui;
  //private final Map<Integer, BaseCardLevelGui> baseCardLevelGuiMap;
  //private final VBox basedCardsContainer;
  private final AnchorPane playerBoardAnchorPane;
  private final Map<String,Action> playerActionMap;

  private final long gameId;

  public BaseBoardGui(AnchorPane playerBoardAnchorPane,
                      Map<String,Action> playerActionMap, long gameId) {
      this.gameId = gameId;
      nobleBoardGui = new NobleBoardGui(100, 100, 5);
      tokenBankGui = new TokenBankGui(gameId);
      this.playerBoardAnchorPane = playerBoardAnchorPane;
      this.playerActionMap = playerActionMap;
  }


  @Override
  public void guiSetup(TableTop tableTop) {
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
    for (String actionId : playerActionMap.keySet()) {
      Action action = playerActionMap.get(actionId);
      if (action instanceof TakeTokenAction) {
        takeTokenActionMap.put(actionId, (TakeTokenAction) action);
      }
    }
    EnumMap<Colour, Integer> bankBalance = tableTop.getBank().getAllTokens();
    Platform.runLater(() -> {
      tokenBankGui.setup(takeTokenActionMap,
          bankBalance,
          config.getTokenBankLayoutX(),
          config.getTokenBankLayoutY(),
          true);
      playerBoardAnchorPane.getChildren().add(tokenBankGui);
    });



  }

  @Override
  public void assignActionsToBoard(Map<String, Action> actionMap) throws InvalidDataException {
    // if the type of the input board is not BaseBoard, throw an InvalidDataException
    //if(board.getType().equals("BaseBoard"))
  }
}
