package project.view.splendor.boardgui;

import ca.mcgill.comp361.splendormodel.actions.Action;

/**
 * Function of ActionIdPair.
 */
public class ActionIdPair {
  private final String actionId;
  private final Action action;

  /**
   * ActionIdPair.
   *
   * @param actionId actionId
   * @param action   action
   */
  public ActionIdPair(String actionId, Action action) {
    this.actionId = actionId;
    this.action = action;
  }

  /**
   * getActionId.
   *
   * @return id
   */
  public String getActionId() {
    return actionId;
  }

  /**
   * get action.
   *
   * @return action
   */
  public Action getAction() {
    return action;
  }
}
