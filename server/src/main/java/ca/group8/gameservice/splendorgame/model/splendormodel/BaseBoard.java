package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class stores the cards/decks with level 1 to 3 on base board and the nobles.
 */
public class BaseBoard extends Board {

  private final Map<Integer, List<DevelopmentCard>> decks = new HashMap<>();
  private final Map<Integer, DevelopmentCard[]> cardsOnBoard = new HashMap<>();
  private final List<NobleCard> nobles;

  /**
   * BaseBoard.
   *
   * @param playerNames playerNames
   */
  public BaseBoard(List<String> playerNames) {
    super.type = this.getClass().getSimpleName();
    // set up decks and cards on board
    int playerCount = playerNames.size();

    // set up nobles on board
    List<NobleCard> allNobles = super.generateNobleCards();
    Collections.shuffle(allNobles);
    nobles = allNobles.subList(0, playerCount + 1); // noble count = player count + 1

    // get all cards info from json file
    List<DevelopmentCard> baseDevCards
        = super.generateDevelopmentCards("cardinfo_basecard");

    // initialize decks and slots to put card on board
    generateDeckPerLevel(baseDevCards);
    for (int i = 1; i <= 3; i++) {
      cardsOnBoard.put(i, new DevelopmentCard[4]);
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
   * getter for nobles field.
   *
   * @return the list of noble cards
   */
  public List<NobleCard> getNobles() {
    return nobles;
  }

  /**
   * Find the noble in the list and remove it.
   *
   * @param noble the noble that we want to remove (has been unlocked or reserved)
   */
  public void removeNoble(NobleCard noble) {
    nobles.remove(noble);
  }

  /**
   * Return the noble when player does not meet the requirement to unlock one.
   *
   * @param noble the noble to be added back
   */
  public void addNoble(NobleCard noble) {
    nobles.add(noble);
  }

  /**
   * Quickly set the card at that position to null, as a representation of removing it.
   *
   * @param cardPosition x and y: x stands for the level and y stands for the index
   * @return development card that is removed.
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
   * getDecks.
   *
   * @return a map containing all decks
   */

  public Map<Integer, List<DevelopmentCard>> getDecks() {
    return decks;
  }

  /**
   * getCardsOnBoard.
   *
   * @return a map containing cards on board
   */

  public Map<Integer, DevelopmentCard[]> getCardsOnBoard() {
    return cardsOnBoard;
  }


  /**
   * Generate decks for each level of base dev card.
   *
   * @param allCards all dev cards (with no CardEffect) parsed from json
   */
  private void generateDeckPerLevel(List<DevelopmentCard> allCards) {

    for (int i = 1; i <= 3; i++) {
      int curLevel = i;
      List<DevelopmentCard> levelDeck = allCards.stream()
          .filter(c -> c.getLevel() == curLevel)
          .filter(c -> c.isBaseCard())
          .collect(Collectors.toList());
      // TODO: Commented out shuffle for JUnit testing
      Collections.shuffle(levelDeck);
      decks.put(curLevel, levelDeck);
    }

  }

  /**
   * Refill any null position on the board (3 of len = 4 DevelopmentCard array).
   * must make sure once the card is purchased, make the corresponding slot (index) to null
   */
  @Override
  public void update() {
    for (int i = 1; i <= 3; i++) {
      DevelopmentCard[] curLevelCardsOnBoard = getLevelCardsOnBoard(i);
      for (int j = 0; j < 4; j++) {
        if (curLevelCardsOnBoard[j] == null) {
          // might pop a dummy card
          curLevelCardsOnBoard[j] = popLevelCardFromDeck(i);
        }
      }
    }
  }

}
