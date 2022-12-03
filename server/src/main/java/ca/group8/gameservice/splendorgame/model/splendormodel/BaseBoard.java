package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the 12 face up cards in the base game.
 */
public class BaseBoard {

  private final Map<Integer, List<BaseCard>> baseDecks;
  private final Map<Integer, List<BaseCard>> baseCardsOnBoard;

  public BaseBoard(List<BaseCard> allBaseCards) {
    baseDecks = new HashMap<>();
    baseCardsOnBoard = new HashMap<>();
    generateDeckPerLevel(allBaseCards);
    drawCardsToBoard();
  }

  public Map<Integer, List<BaseCard>> getBaseDecks() {
    return baseDecks;
  }

  public Map<Integer, List<BaseCard>> getBaseCardsOnBoard() {
    return baseCardsOnBoard;
  }

  private void generateDeckPerLevel(List<BaseCard> allBaseCards) {
    List<BaseCard> levelOneDeck = new ArrayList<>();
    List<BaseCard> levelTwoDeck = new ArrayList<>();
    List<BaseCard> levelThreeDeck = new ArrayList<>();
    for (BaseCard card : allBaseCards) {
      if (card.getLevel() == 1) {
        levelOneDeck.add(card);
      } else if (card.getLevel() == 2) {
        levelTwoDeck.add(card);
      } else {
        levelThreeDeck.add(card);
      }
    }
    Collections.shuffle(levelOneDeck);
    Collections.shuffle(levelTwoDeck);
    Collections.shuffle(levelThreeDeck);
    baseDecks.put(1, levelOneDeck);
    baseDecks.put(2, levelTwoDeck);
    baseDecks.put(3, levelThreeDeck);
  }
  private void drawCardsToBoard() {
    for (int i = 1; i <= 3; i++) {
      List<BaseCard> levelCardsOnBoard = new ArrayList<>();
      // draw out 4 cards
      for (int j = 0; j < 4; j++) {
        levelCardsOnBoard.add(baseDecks.get(i).remove(j));
      }
      baseCardsOnBoard.put(i, levelCardsOnBoard);
    }
  }

  public void replaceCardOnBoard(Position cardPosition, DevelopmentCard newCard) {
    baseCardsOnBoard.get(cardPosition.getX()).set(cardPosition.getY(), (BaseCard) newCard);
  }

}
