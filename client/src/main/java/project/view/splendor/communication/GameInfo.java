package project.view.splendor.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInfo {
  private String creator;
  private String currentPlayer; //represents which player's turn it is currently
  private final List<String> winners;
  private List<String> playerNames;
  private String firstPlayerName; //should be Player Name of first player.

  private boolean isFinished;
  private final TableTop tableTop;
  private final List<Extension> extensions;

  public GameInfo(String creator, String currentPlayer, List<String> winners, List<String> playerNames, String firstPlayerName, boolean isFinished, TableTop tableTop, List<Extension> extensions) {
    this.creator = creator;
    this.currentPlayer = currentPlayer;
    this.winners = winners;
    this.playerNames = playerNames;
    this.firstPlayerName = firstPlayerName;
    this.isFinished = isFinished;
    this.tableTop = tableTop;
    this.extensions = extensions;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(String currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public List<String> getWinners() {
    return winners;
  }

  public List<String> getPlayerNames() {
    return playerNames;
  }

  public void setPlayerNames(List<String> playerNames) {
    this.playerNames = playerNames;
  }

  public String getFirstPlayerName() {
    return firstPlayerName;
  }

  public void setFirstPlayerName(String firstPlayerName) {
    this.firstPlayerName = firstPlayerName;
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished(boolean finished) {
    isFinished = finished;
  }

  public TableTop getTableTop() {
    return tableTop;
  }

  public List<Extension> getExtensions() {
    return extensions;
  }


}
