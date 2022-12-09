package ca.group8.gameservice.splendorgame.model.splendormodel;


import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.ArrayList;
import java.util.List;

public class GameInfo implements BroadcastContent { // TODO add gametype


  private String currentPlayer; //represents which player's turn it is currently
  private final List<String> winners;
  private final ArrayList<String> playerNames;
  private final String firstPlayer; //should be Player Name of first player.

  private final TableTop tableTop;

  /**
   * Makes a game State.
   *
   * @param playerNames NOTE: In this implementation, activePlayers is an arrayList
   *                    meaning you cannot get(Player) based on giving the player
   *                    name that is in the list.(can only index list)
   */
  public GameInfo(ArrayList<String> playerNames) {
    // Shuffle the list of playerNames before assigning it to the field
    this.playerNames = playerNames;
    this.winners = new ArrayList<>();
    String randomFirstPlayer = playerNames.get(0);
    firstPlayer = randomFirstPlayer;
    currentPlayer = randomFirstPlayer;
    // TODO: 2
    tableTop = new TableTop(playerNames.size());

  }


  //TODO Figure out if this should be public/private/... based on what needs to call this method
  //protected void setWinner(String player) {
  //  winner = Optional.of(player);
  //}
  public void addWinner(String potentialWinner) {
    winners.add(potentialWinner);
  }

  public void checkWinner() {
    //TODO: Implement this operation (will be based on TableTop implementation)
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

  public String getFirstPlayer() {
    return firstPlayer;
  }


  public ArrayList<String> getPlayerNames() {
    return playerNames;
  }

  public TableTop getTableTop() {
    return tableTop;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
