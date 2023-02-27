package project.view.splendor;

import java.util.Map;
import project.view.InvalidDataException;
import project.view.splendor.communication.Action;
import project.view.splendor.communication.TableTop;

public interface BoardGui {

  // set up the board in terms of Gui pictures
  void guiSetup(TableTop tableTop);

  // assign actions to the gui
  void assignActionsToBoard(Map<String, Action> actionMap) throws InvalidDataException;

}
