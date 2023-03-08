package ca.group8.gameservice.splendorgame.controller.communicationbeans;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.ActionInterpreter;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import java.util.List;

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


  /**
   * Call this method to rename the player names if the ones who want to play now does not.
   * match with the ones who saved this game before.
   *
   * @param playerNames the current player names who want to play this game
   */
  public void renamePlayers(List<String> playerNames, String creator) {
    // since we relinked the references, the name changing happen in
    // the gameInfo and player states will happen synchronously in interpreter
    //actionInterpreter.relinkReferences(gameInfo, playerStates);
    gameInfo.renamePlayers(playerNames, creator);
    playerStates.renamePlayers(playerNames);
  }

}
