package project.view.splendor.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseBoard {
  private final Map<Integer, List<DevelopmentCard>> decks = new HashMap<>();
  private final Map<Integer, DevelopmentCard[]> cardsOnBoard = new HashMap<>();
  private final List<NobleCard> nobles;
  public BaseBoard(List<NobleCard> nobles) {
    this.nobles = nobles;
  }

  public Map<Integer, List<DevelopmentCard>> getDecks() {
    return decks;
  }

  public Map<Integer, DevelopmentCard[]> getCardsOnBoard() {
    return cardsOnBoard;
  }

  public List<NobleCard> getNobles() {
    return nobles;
  }
}