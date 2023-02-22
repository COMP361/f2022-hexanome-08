package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores the cards/decks for orient extension.
 */
public class OrientBoard extends Board {

  private final Map<Integer, List<DevelopmentCard>> decks = new HashMap<>();
  private final Map<Integer, DevelopmentCard[]> cardsOnBoard = new HashMap<>();

  public OrientBoard() {
    // get all cards info from json file
    List<DevelopmentCard> orientDevCards
        = super.generateDevelopmentCards("cardinfo_orientcard");

    // initialize decks and slots to put card on board
    generateDeckPerLevel(orientDevCards);
    for (int i = 1; i <= 3; i++) {
      cardsOnBoard.put(i, new DevelopmentCard[2]); // 2 cards on board
    }
    // fill the board
    refillCardBoard();
  }

  /**
   * Used for reserve from the Deck.
   *
   * @param cardLevel level of the card
   * @return a card taken from cardLevel deck
   */
  public DevelopmentCard popLevelCardFromDeck(int cardLevel) {
    return decks.get(cardLevel).remove(0);
  }

  /**
   * Used to get the DevCard[] for a certain level.
   *
   * @param cardLevel level of the card
   * @return an array of dev cards on board
   */
  public DevelopmentCard[] getLevelCardsOnBoard(int cardLevel) {
    return cardsOnBoard.get(cardLevel);
  }

  /**
   * Refill any null position on the board (3 of len = 2 DevelopmentCard array).
   * must make sure once the card is purchased, make the corresponding slot (index) -> null
   */
  private void refillCardBoard() {
    for (int i = 1; i <= 3; i++) {
      DevelopmentCard[] curLevelCardsOnBoard = getLevelCardsOnBoard(i);
      for (int j = 0; j < 2; j++) {
        if (curLevelCardsOnBoard[j] == null) {
          curLevelCardsOnBoard[j] = popLevelCardFromDeck(i);
        }
      }
    }
  }

  /**
   * Quickly set the card at that position to null, as a representation of removing it.
   *
   * @param cardPosition x and y: x stands for the level and y stands for the index
   */
  public DevelopmentCard removeCard(Position cardPosition) {
    int level = cardPosition.getX();
    int cardIndex = cardPosition.getY();
    DevelopmentCard resultCard = cardsOnBoard.get(level)[cardIndex];
    cardsOnBoard.get(level)[cardIndex] = null;
    return resultCard;
  }

  /**
   * Generate decks for each level of orient card.
   *
   * @param allBaseCards all dev cards (with no CardEffect) parsed from json
   */
  private void generateDeckPerLevel(List<DevelopmentCard> allBaseCards) {
    List<DevelopmentCard> levelOneDeck = new ArrayList<>();
    List<DevelopmentCard> levelTwoDeck = new ArrayList<>();
    List<DevelopmentCard> levelThreeDeck = new ArrayList<>();
    for (DevelopmentCard card : allBaseCards) {
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
    decks.put(1, levelOneDeck);
    decks.put(2, levelTwoDeck);
    decks.put(3, levelThreeDeck);
  }

  @Override
  public void update(Card card, int index) {

  }
}
