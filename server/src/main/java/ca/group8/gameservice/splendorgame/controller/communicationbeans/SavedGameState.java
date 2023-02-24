package ca.group8.gameservice.splendorgame.controller.communicationbeans;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.ActionInterpreter;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;

/**
 * A class that is used to store info about all game specific data.
 */
public class SavedGameState {
  private final GameInfo gameInfo;
  private final PlayerStates playerStates;
  private final ActionInterpreter actionInterpreter;

  public SavedGameState(GameInfo gameInfo, PlayerStates playerStates,
                        ActionInterpreter actionInterpreter) {
    this.gameInfo = gameInfo;
    this.playerStates = playerStates;
    this.actionInterpreter = actionInterpreter;
  }
  public GameInfo getGameInfo() {
    return gameInfo;
  }

  public PlayerStates getPlayerStates() {
    return playerStates;
  }

  public ActionInterpreter getActionInterpreter() {
    return actionInterpreter;
  }

}
