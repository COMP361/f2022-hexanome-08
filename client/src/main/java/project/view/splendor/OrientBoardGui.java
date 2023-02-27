package project.view.splendor;

import java.util.Map;
import javafx.scene.layout.VBox;
import project.view.InvalidDataException;
import project.view.splendor.communication.Action;
import project.view.splendor.communication.TableTop;

public class OrientBoardGui implements BoardGui{

  private Map<Integer, OrientCardLevelGui> orientCardLevelGuiMap;
  private VBox orientCardsContainer;
  //private final OrientBoard orientBoard;
  //private final Extension extension = Extension.ORIENT;
  @Override
  public void guiSetup(TableTop tableTop) {

  }

  @Override
  public void assignActionsToBoard(Map<String, Action> actionMap) throws InvalidDataException {

  }
}
