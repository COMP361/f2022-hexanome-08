package ca.group8.gameservice.splendorgame.model.splendormodel;


import ca.group8.gameservice.splendorgame.controller.splendorlogic.Action;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class that contains basic game information on the board, supports long polling.
 */
public class GameInfo implements BroadcastContent {

  private String creator;
  private String currentPlayer; //represents which player's turn it is currently
  private List<String> winners;
  private List<String> playerNames;
  private String firstPlayerName; //should be Player Name of first player.

  private final TableTop tableTop;
  private final List<Extension> extensions;

  private boolean isFinished;
  private Map<String, Map<String, Action>> playerActionMaps = new HashMap<>();


  /**
   * Constructor for a game state instance. Stores all info related to game except the detail.
   * information of each player
   *
   * @param extensions extensions that are used in the game.
   * @param playerNames players who are playing the game
   */
  public GameInfo(List<Extension> extensions, List<String> playerNames, String creator) {
    this.playerNames = playerNames;
    this.winners = new ArrayList<>();
    firstPlayerName = playerNames.get(0);
    currentPlayer = playerNames.get(0);
    this.extensions = Collections.unmodifiableList(extensions);
    tableTop = new TableTop(playerNames, extensions);
    this.creator = creator;
    this.isFinished = false;

  }


  /**
   * Gets the player who's currently making a move.
   *
   * @return Current player object (as a Player).
   */
  public String getCurrentPlayer() {
    return currentPlayer;
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished() {
    isFinished = true;
  }

  /**
   * Set the turn to next player.
   */
  public void setNextPlayer() {
    int index = playerNames.indexOf(currentPlayer);
    //if last player in list return to first player
    currentPlayer = playerNames.get((index + 1) % playerNames.size());
  }

  public List<String> getWinners() {
    return new ArrayList<>(winners);
  }

  public String getFirstPlayerName() {
    return firstPlayerName;
  }


  public List<String> getPlayerNames() {
    return playerNames;
  }

  public TableTop getTableTop() {
    return tableTop;
  }

  public List<Extension> getExtensions() {
    return extensions;
  }

  public Map<String, Map<String, Action>> getPlayerActionMaps() {
    return playerActionMaps;
  }
  public String getCreator() {
    return creator;
  }

  public void setWinners(List<String> winners) {
    this.winners = winners;
  }

  /**
   * Call this method to rename the player names if the ones who want to play now does not.
   * match with the ones who saved this game before.
   *
   * @param playerNames the current player names who want to play this game
   */
  public void renamePlayers(List<String> playerNames, String creator) {
    if(!playerNames.equals(this.playerNames)){
      Collections.shuffle(playerNames);
      this.playerNames = playerNames;
      this.creator = creator;
      this.firstPlayerName = playerNames.get(0);
      // rename all boards if necessary (base and orient do not need updates)
      for (Extension extension : tableTop.getGameBoards().keySet()) {
        tableTop.getBoard(extension).renamePlayers(playerNames);
      }
      // rename action map names
      int nameIndex = 0;
      Map<String, Map<String, Action>> newActionMap = new HashMap<>();
      for (String curName : playerActionMaps.keySet()) {
        Map<String, Action> curActionMap = playerActionMaps.get(curName);
        String newName = playerNames.get(nameIndex);
        nameIndex += 1;
        newActionMap.put(newName, curActionMap);
      }
      playerActionMaps = newActionMap;
    }
  }
  @Override
  public boolean isEmpty() {
    return false;
  }
}
