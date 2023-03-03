package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import project.view.InvalidDataException;

import java.util.Map;

public interface BoardGui {

  // set up the board in terms of Gui pictures
  void guiSetup(TableTop tableTop);

  // assign actions to the gui
  void assignActionsToBoard(Map<String, Action> actionMap) throws InvalidDataException;

}
