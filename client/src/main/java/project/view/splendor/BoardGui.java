package project.view.splendor;



import ca.group8.gameservice.splendorgame.controller.splendorlogic.Action;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import project.view.InvalidDataException;

import java.util.Map;

public interface BoardGui {

  // set up the board in terms of Gui pictures
  void guiSetup(TableTop tableTop);

  // assign actions to the gui
  void assignActionsToBoard(Map<String, Action> actionMap) throws InvalidDataException;

}
