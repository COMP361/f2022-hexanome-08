package project.view.splendor;

import java.util.Map;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.Action;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import javafx.scene.layout.VBox;
import project.view.InvalidDataException;

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
