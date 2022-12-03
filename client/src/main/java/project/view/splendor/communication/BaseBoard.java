package project.view.splendor.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseBoard {

  public Map<Integer, List<BaseCard>> getBaseDecks() {
    return baseDecks;
  }

  public Map<Integer, List<BaseCard>> getBaseCardsOnBoard() {
    return baseCardsOnBoard;
  }

  public void setBaseDecks(
      Map<Integer, List<BaseCard>> baseDecks) {
    this.baseDecks = baseDecks;
  }

  public void setBaseCardsOnBoard(
      Map<Integer, List<BaseCard>> baseCardsOnBoard) {
    this.baseCardsOnBoard = baseCardsOnBoard;
  }

  private Map<Integer, List<BaseCard>> baseDecks;
  private Map<Integer, List<BaseCard>> baseCardsOnBoard;

  public BaseBoard(Map<Integer, List<BaseCard>> baseDecks,
                   Map<Integer, List<BaseCard>> baseCardsOnBoard) {
    this.baseDecks = baseDecks;
    this.baseCardsOnBoard = baseCardsOnBoard;
  }
}