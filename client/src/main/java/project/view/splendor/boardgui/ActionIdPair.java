package project.view.splendor.boardgui;

import ca.mcgill.comp361.splendormodel.actions.Action;

/**
 * Function of ActionIdPair.
 */
public class ActionIdPair {
  private final String actionId;
  private final Action action;

  public ActionIdPair(String actionId, Action action) {
    this.actionId = actionId;
    this.action = action;
  }

  public String getActionId() {
    return actionId;
  }

  public Action getAction() {
    return action;
  }
}
