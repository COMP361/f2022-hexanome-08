package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;

public class ActionIdPair {
  private final String actionId;
  private final Action action;

  public String getActionId() {
    return actionId;
  }

  public Action getAction() {
    return action;
  }

  public ActionIdPair(String actionId, Action action) {
    this.actionId = actionId;
    this.action = action;
  }
}
