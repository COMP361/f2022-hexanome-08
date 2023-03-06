package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import java.util.Map;

import javafx.scene.layout.VBox;
import project.view.InvalidDataException;

public class OrientBoardGui implements BoardGui{

  private Map<Integer, OrientCardLevelGui> orientCardLevelGuiMap;
  private VBox orientCardsContainer;
  //private final OrientBoard orientBoard;
  //private final Extension extension = Extension.ORIENT;
  @Override
  public void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap) {

  }

  @Override
  public void clearContent() {

  }

}
