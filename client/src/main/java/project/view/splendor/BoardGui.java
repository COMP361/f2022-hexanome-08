package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import java.util.Map;

/**
 * Interface of BoardGui.
 */
public interface BoardGui {

  // set up the board in terms of Gui pictures
  void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap);

  void clearContent();

}
