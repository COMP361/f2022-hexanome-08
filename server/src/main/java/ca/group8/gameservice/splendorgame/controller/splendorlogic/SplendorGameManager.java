package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Manages instances of Splendor games.
 */
@Component
public class SplendorGameManager {
  private final Map<Long, GameInfo> activeGames;

  private final Map<Long, PlayerStates> activePlayers;

  public SplendorGameManager() {
    this.activePlayers = new HashMap<>();
    this.activeGames = new HashMap<>();
  }


  public GameInfo getGameById(long gameId) throws ModelAccessException {
    if (!isExistentGameId(gameId)) {
      throw new ModelAccessException("No registered game with that ID");
    }
    return activeGames.get(gameId);
  }


  public boolean isExistentGameId(long gameId) {
    return activeGames.containsKey(gameId);
  }

  public void addGame(long gameId, GameInfo newGameInfo) throws ModelAccessException {
    activeGames.put(gameId, newGameInfo);
  }

  public void addPlayersToGame(long gameId, PlayerStates newPlayerStates) {
    activePlayers.put(gameId, newPlayerStates);
  }

  public void removePlayerStates(long gameId) {
    assert activePlayers.containsKey(gameId);
    activePlayers.remove(gameId);
  }

  public void removeGame(long gameId) {
    activeGames.remove(gameId);
  }


  public PlayerStates getPlayerStatesById(long gameId) {
    assert activePlayers.containsKey(gameId);
    return activePlayers.get(gameId);
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
