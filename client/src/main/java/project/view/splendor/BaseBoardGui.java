package project.view.splendor;

import java.util.Map;
import javafx.scene.layout.VBox;
import project.view.InvalidDataException;
import project.view.splendor.communication.Action;
import project.view.splendor.communication.BaseBoard;
import project.view.splendor.communication.Board;
import project.view.splendor.communication.TableTop;

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
