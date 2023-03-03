package project.view.splendor;

import java.util.Map;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.Action;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import javafx.scene.layout.VBox;
import project.view.InvalidDataException;

public class BaseBoardGui implements BoardGui {

  private NobleBoardGui nobleBoardGui;
  private TokenBankGui tokenBankGui;
  private Map<Integer, BaseCardLevelGui> baseCardLevelGuiMap;
  private VBox basedCardsContainer;
  //private final BaseBoard baseBoard;
  //private final Extension extension = Extension.BASE;



  @Override
  public void guiSetup(TableTop tableTop) {
    //nobleBoardGui.setup();
    //tokenBankGui.setup();
    //for (int i = 1; i <= 3; i++) {
    //  baseCardLevelGuiMap.get(i).setup();
    //}
  }

  @Override
  public void assignActionsToBoard(Map<String, Action> actionMap) throws InvalidDataException {
    // if the type of the input board is not BaseBoard, throw an InvalidDataException
    //if(board.getType().equals("BaseBoard"))
  }
}
