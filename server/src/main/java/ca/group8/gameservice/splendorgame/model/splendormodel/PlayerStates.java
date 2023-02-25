package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.SplendorLogicException;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PlayerStates class that store a Map of player names to their game states in a specific game.
 */
public class PlayerStates implements BroadcastContent {
  private Map<String, PlayerInGame> playersInfo;

  /**
   * Constructor of player states.
   *
   * @param playerNames a list of player names in String
   */
  public PlayerStates(List<String> playerNames) {
    this.playersInfo = new HashMap<>();
    for (String name : playerNames) {
      playersInfo.put(name, new PlayerInGame(name));
    }
  }

  public Map<String, PlayerInGame> getPlayersInfo() {
    return playersInfo;
  }

  /**
   * This method gets you one playerInGame object from the playersInfo map.
   *
   * @param name The name associated with a PlayerInGame object
   * @return the PlayerInGame object associated with the name parameter
   * @throws SplendorLogicException if you request a player who is not in the playersInfo map..
   */
  public PlayerInGame getOnePlayerInGame(String name) throws SplendorLogicException {
    //throw an exception if name (method argument) is not a player in playersInfo.
    if (!playersInfo.containsKey(name)) {
      throw new SplendorLogicException("Error: Player you are requesting is not "
          + "in the map of Players in this game");
    }

    //get playerInGame object from playersInfo list
    return playersInfo.get(name);
  }


  /**
   * Call this method to rename the player names if the ones who want to play now does not.
   * match with the ones who saved this game before.
   *
   * @param playerNames the current player names who want to play this game
   */
  public void renamePlayers(List<String> playerNames) {
    List<String> curNames = new ArrayList<>(playersInfo.keySet());
    // only update if names are different
    if(!playerNames.equals(curNames)) {
      int nameIndex = 0;
      Map<String, PlayerInGame> newPlayerMap = new HashMap<>();
      for (String curName : playersInfo.keySet()) {
        PlayerInGame curPlayer = playersInfo.get(curName);
        String newName = playerNames.get(nameIndex);
        nameIndex += 1;
        newPlayerMap.put(newName, curPlayer);
      }
      playersInfo = newPlayerMap;
    }
  }
  @Override
  public boolean isEmpty() {
    return false;
  }
}
