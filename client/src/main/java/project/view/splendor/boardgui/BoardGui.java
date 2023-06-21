package project.view.splendor.boardgui;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import java.util.Map;

/**
 * Interface of BoardGui.
 */
public interface BoardGui {

  // set up the board in terms of Gui pictures

  /**
   * set up the board in terms of Gui pictures.
   *
   * @param tableTop        tableTop
   * @param playerActionMap playerActionMap
   */
  void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap);

  /**
   * clearContent.
   */
  void clearContent();

}
