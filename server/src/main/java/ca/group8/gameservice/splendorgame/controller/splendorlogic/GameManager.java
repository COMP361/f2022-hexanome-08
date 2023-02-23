package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Manages instances of Splendor games.
 */
@Component
public class GameManager {

  private final Map<Long, PlayerStates> activePlayers;
  private final Map<Long, GameInfo> activeGames;
  private final Map<Long, ActionInterpreter> gameActionInterpreters;

  /**
   * Construct a new GameManager instance (initialize all maps to empty HashMaps).
   */
  public GameManager() {
    this.activePlayers = new HashMap<>();
    this.activeGames = new HashMap<>();
    this.gameActionInterpreters = new HashMap<>();
  }

  /**
   * get one instance of GameInfo object that contains the public game details on game board.
   *
   * @param gameId game id
   * @return one instance of GameInfo object
   * @throws ModelAccessException if model access went wrong
   */
  public GameInfo getGameById(long gameId) throws ModelAccessException {
    if (!isExistentGameId(gameId)) {
      throw new ModelAccessException("No registered game with that ID");
    }
    return activeGames.get(gameId);
  }

  public boolean isExistentGameId(long gameId) {
    return activeGames.containsKey(gameId);
  }

  /**
   * Add a new game instance to the list of games.
   *
   * @param gameId      ID of the new game instance.
   * @param newGameInfo Actual GameInfo instance.
   */
  public void addGame(long gameId, GameInfo newGameInfo) {
    assert newGameInfo != null;
    assert !activeGames.containsKey(gameId); //ensure gameId isn't already in list.

    activeGames.put(gameId, newGameInfo);
  }

  public PlayerStates getPlayerStatesById(long gameId) {
    assert activePlayers.containsKey(gameId);
    return activePlayers.get(gameId);
  }

  public void addGamePlayerStates(long gameId, PlayerStates newPlayerStates) {
    assert !activePlayers.containsKey(newPlayerStates);
    activePlayers.put(gameId, newPlayerStates);
  }

  public ActionInterpreter getGameActionInterpreter(long gameId) {
    assert gameActionInterpreters.containsKey(gameId);
    return gameActionInterpreters.get(gameId);
  }

  public void addGameActionInterpreter(long gameId, ActionInterpreter actionInterpreter) {
    assert activeGames.containsKey(gameId) && actionInterpreter != null;
    gameActionInterpreters.put(gameId, actionInterpreter);
  }

  /**
   * Remove all data related to a specific game.
   *
   * @param gameId game to be removed.
   */
  public void removeGameRelatedData(long gameId) {
    assert activeGames.containsKey(gameId);

    removePlayerStates(gameId);
    removeGame(gameId);
    removeActionInterpreter(gameId);

  }

  public void removePlayerStates(long gameId) {
    assert activePlayers.containsKey(gameId);
    activePlayers.remove(gameId);
  }

  public void removeGame(long gameId) {
    assert activeGames.containsKey(gameId);
    activeGames.remove(gameId);
  }

  public void removeActionInterpreter(long gameId) {
    assert gameActionInterpreters.containsKey(gameId);
    gameActionInterpreters.remove(gameId);
  }


  public Map<Long, GameInfo> getActiveGames() {
    return activeGames;
  }

  /**
   * Check whether current player is in game or not.
   */
  public boolean isPlayerInGame(long gameId, String playerName) {
    return activePlayers.get(gameId).getPlayersInfo().containsKey(playerName);
  }

  public Map<Long, PlayerStates> getActivePlayers() {
    return activePlayers;
  }


}
