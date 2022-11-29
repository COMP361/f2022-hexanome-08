package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;

/**
 * Generic Action abstract class for blackboard architecture. Implement your own actions, representing kinds of actions
 * performed by players in a blackboard architecture. Actions are generated by a custom ActionGenerator and performed by
 * a custom ActionInterpreter.
 */

public abstract class Action {

  // Useful when the client receives the map of Actions, to identify whether action
  // is CardAction or not
  private final boolean isCardAction;

  public Action(boolean isCardAction) {
    this.isCardAction = isCardAction;
  }
  public boolean checkIsCardAction() {
    return isCardAction;
  }

  // abstract method that leave to subclass to implement
  // the reason why it returns void is that we only provide the
  // VALID ACTIONS MAP to player when generated them, so there is no need to check
  // whether the Action player chose is valid or not. They must be
  public abstract void execute(GameInfo currentGameState, PlayerInGame playerState);
}
