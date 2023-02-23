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

  private String currentPlayer; //represents which player's turn it is currently
  private final List<String> winners;
  private final List<String> playerNames;
  private final String firstPlayerName; //should be Player Name of first player.

  private final TableTop tableTop;
  private final List<Extension> extensions;

  private final Map<String, Map<String, Action>> playerActionMaps = new HashMap<>();


  /**
   * Constructor for a game state instance. Stores all info related to game except the detail.
   * information of each player
   *
   * @param extensions extensions that are used in the game.
   * @param playerNames players who are playing the game
   */
  public GameInfo(List<Extension> extensions, List<String> playerNames) {
    // TODO: OPTIONALLY Shuffle the list of playerNames before assigning it to the field
    // Collections.shuffle(playerNames);
    this.playerNames = playerNames;
    this.winners = new ArrayList<>();
    firstPlayerName = playerNames.get(0);
    currentPlayer = playerNames.get(0);
    this.extensions = Collections.unmodifiableList(extensions);
    tableTop = new TableTop(playerNames, extensions);

  }

  /**
   * Update (overwrite) the given player's action map with a new map.
   *
   * @param playerName   player name that we want to modify action map on
   * @param newActionMap the new action map
   */
  public void updatePlayerActionMap(String playerName, Map<String, Action> newActionMap) {
    playerActionMaps.put(playerName, newActionMap);
  }


  public void addWinner(String potentialWinner) {
    winners.add(potentialWinner);
  }


  public int getNumOfPlayers() {
    return playerNames.size();
  }


  public boolean isFinished() {
    return winners.size() > 0;
  }

  /**
   * Gets the player who's currently making a move.
   *
   * @return Current player object (as a Player).
   */
  public String getCurrentPlayer() {
    return currentPlayer;
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

  @Override
  public boolean isEmpty() {
    return false;
  }
}
