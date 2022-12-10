package project.view.splendor.communication;

import java.util.ArrayList;
import java.util.List;

public class GameInfo { // TODO add gametype


  private String currentPlayer; //represents which player's turn it is currently
  private List<String> winners;
  private ArrayList<String> playerNames;
  private String firstPlayer; //should be Player Name of first player.

  private TableTop tableTop;


  public GameInfo(String currentPlayer, List<String> winners, ArrayList<String> playerNames,
                  String firstPlayer, TableTop tableTop) {
    this.currentPlayer = currentPlayer;
    this.winners = winners;
    this.playerNames = playerNames;
    this.firstPlayer = firstPlayer;
    this.tableTop = tableTop;
  }

  public String getCurrentPlayer() {
    return currentPlayer;
  }

  public List<String> getWinners() {
    return winners;
  }

  public ArrayList<String> getPlayerNames() {
    return playerNames;
  }

  public String getFirstPlayer() {
    return firstPlayer;
  }

  public TableTop getTableTop() {
    return tableTop;
  }

  public void setCurrentPlayer(String currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public void setWinners(List<String> winners) {
    this.winners = winners;
  }

  public void setPlayerNames(ArrayList<String> playerNames) {
    this.playerNames = playerNames;
  }

  public void setFirstPlayer(String firstPlayer) {
    this.firstPlayer = firstPlayer;
  }

  public void setTableTop(TableTop tableTop) {
    this.tableTop = tableTop;
  }


}
