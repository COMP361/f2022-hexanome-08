package project.view.splendor.communication;

import java.util.List;
import java.util.Map;


public class TableTop {

  private final Map<Extension, Board> gameBoards;
  private final Bank bank;

  public TableTop(Map<Extension, Board> gameBoards, Bank bank) {
    this.gameBoards = gameBoards;
    this.bank = bank;
  }

  public Map<Extension, Board> getGameBoards() {
    return gameBoards;
  }

  public Bank getBank() {
    return bank;
  }

}