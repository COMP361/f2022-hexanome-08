package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class stores the cards/decks for orient extension.
 */
public class OrientBoard extends Board {

  private final Map<Integer, List<DevelopmentCard>> decks = new HashMap<>();
  private final Map<Integer, DevelopmentCard[]> cardsOnBoard = new HashMap<>();

  /**
   * OrientBoard.
   */
  public OrientBoard() {
    super.type = this.getClass().getSimpleName();
    // get all cards info from json file
    List<DevelopmentCard> orientDevCards
        = super.generateDevelopmentCards("cardinfo_orientcard");

    // initialize decks and slots to put card on board
    generateDeckPerLevel(orientDevCards);
    for (int i = 1; i <= 3; i++) {
      cardsOnBoard.put(i, new DevelopmentCard[2]); // 2 cards on board
    }
    // fill the board
    update();
  }

  /**
   * Used for reserve from the Deck.
   *
   * @param cardLevel level of the card
   * @return a card taken from cardLevel deck
   */
  public DevelopmentCard popLevelCardFromDeck(int cardLevel) {
    if (decks.get(cardLevel).isEmpty()) {
      //TODO: everything about this dummy card is wrong!
      return new DevelopmentCard(-1,
          SplendorDevHelper.getInstance().getRawTokenColoursMap(),
          "dummy_card",
          -1,
          Colour.ORIENT,
          -1,
          new ArrayList<>());
    } else {
      return decks.get(cardLevel).remove(0);
    }
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
   * Quickly set the card at that position to null, as a representation of removing it.
   *
   * @param cardPosition x and y: x stands for the level and y stands for the index
   * @return card from given position
   */
  public DevelopmentCard removeCard(Position cardPosition) {
    int level = cardPosition.getX();
    int cardIndex = cardPosition.getY();
    DevelopmentCard resultCard;
    if (cardIndex == -1) {
      // when the player reserve from the deck
      resultCard = decks.get(level).remove(0);
    } else {
      resultCard = cardsOnBoard.get(level)[cardIndex];
      cardsOnBoard.get(level)[cardIndex] = null;
    }
    return resultCard;
  }

  /**
   * Generate decks for each level of orient card.
   *
   * @param allCards all dev cards (with CardEffect) parsed from json
   */
  private void generateDeckPerLevel(List<DevelopmentCard> allCards) {
    for (int i = 1; i <= 3; i++) {
      int curLevel = i;
      List<DevelopmentCard> levelDeck = allCards.stream()
          .filter(c -> c.getLevel() == curLevel)
          .filter(c -> !c.isBaseCard())
          .collect(Collectors.toList());
      // TODO: Commented out shuffle for JUnit testing
      Collections.shuffle(levelDeck);
      decks.put(curLevel, levelDeck);
    }
  }

  /**
   * Gets decks.
   *
   * @return Map of all decks
   */
  public Map<Integer, List<DevelopmentCard>> getDecks() {
    return decks;
  }

  /**
   * Gets cards on board.
   *
   * @return Map of cards on board
   */
  public Map<Integer, DevelopmentCard[]> getCardsOnBoard() {
    return cardsOnBoard;
  }


  /**
   * Refill any null position on the board (3 of len = 2 DevelopmentCard array).
   * must make sure once the card is purchased, make the corresponding slot (index) to null
   */
  @Override
  public void update() {
    for (int i = 1; i <= 3; i++) {
      DevelopmentCard[] curLevelCardsOnBoard = getLevelCardsOnBoard(i);
      for (int j = 0; j < 2; j++) {
        if (curLevelCardsOnBoard[j] == null) {
          curLevelCardsOnBoard[j] = popLevelCardFromDeck(i);
        }
      }
    }
  }
}
