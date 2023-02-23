package ca.group8.gameservice.splendorgame.controller.communicationbeans;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.ActionInterpreter;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;

public class SavedGameState {
  private final long gameId;
  private final GameInfo gameInfo;
  private final PlayerStates playerStates;
  private final ActionInterpreter actionInterpreter;

  public SavedGameState(long gameId, GameInfo gameInfo, PlayerStates playerStates,
                        ActionInterpreter actionInterpreter) {
    this.gameId = gameId;
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

  public long getGameId() {
    return gameId;
  }
}
