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
   */
  public PlayerInGame getOnePlayerInGame(String name) {
    return playersInfo.get(name);
  }


  /**
   * Call this method to rename the player names if the ones who want to play now does not.
   * match with the ones who saved this game before.
   *
   * @param newNames the current player names who want to play this game
   */
  public void renamePlayers(List<String> newNames) {
    List<String> oldNames = new ArrayList<>(playersInfo.keySet());
    List<String> newNamesCopy = new ArrayList<>(newNames);
    List<String> assignedPlayers = new ArrayList<>();
    if (!newNames.equals(oldNames)) {
      Map<String, PlayerInGame> newPlayerMap = new HashMap<>();
      // for the old names, find their old data and assign it to them
      for (String oldName : oldNames) {
        // if any oldName match a new name, remove this old name from newNameCopy list
        if (newNames.contains(oldName)) {
          newPlayerMap.put(oldName, playersInfo.get(oldName));
          newNamesCopy.remove(oldName);
          assignedPlayers.add(oldName);
        }
      }
      // at this point, the new names copy should only contain the names
      // who have not participated in this game before [julia]
      // now iterate through playersInfo's key set, it's set so order is random
      // everytime
      int counter = 0;
      for (String otherPlayerName : playersInfo.keySet()) {
        if (!assignedPlayers.contains(otherPlayerName)) {
          PlayerInGame oldPlayerInGame = playersInfo.get(otherPlayerName);
          String newName = newNamesCopy.get(counter);
          // rename the player in game before putting in back
          oldPlayerInGame.setName(newName);
          // it has been renamed, even though the variable is oldPlayerInGame, it's new
          newPlayerMap.put(newName, oldPlayerInGame);
          counter += 1;
        }
        // finish assign all new players, end it
        if (counter >= newNames.size()) {
          break;
        }
      }

      // in the end, overwrite the previous map
      playersInfo = newPlayerMap;
    }
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
