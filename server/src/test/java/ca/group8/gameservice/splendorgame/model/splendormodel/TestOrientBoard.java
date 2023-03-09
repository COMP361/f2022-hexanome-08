package ca.group8.gameservice.splendorgame.model.splendormodel;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestOrientBoard {

  OrientBoard board = new OrientBoard();
  EnumMap<Colour,Integer> cardPrice = new EnumMap<>(Colour.class);
  int points = 5;
  int gemNumber = 1;
  String cardName = "card_name";
  Colour cardColour = Colour.ORIENT;
  List<DevelopmentCard> cardPool = new ArrayList<>();
  List<CardEffect> cardEffects = new ArrayList<>
      (Arrays.asList(CardEffect.FREE_CARD, CardEffect.SATCHEL));
  Map<Integer, List<DevelopmentCard>> testLevelDecks = new HashMap<>();
  Map<Integer, DevelopmentCard[]> testCardsOnBoard = new HashMap<>();

  /**
   * Use reflection to set the private field of the orient board for testing purpose
   * reset the board every time before each test
   */
  @BeforeEach
  void setUpBoard() throws NoSuchFieldException, IllegalAccessException {
    Field cardsOnBoard = OrientBoard.class.getDeclaredField("cardsOnBoard");
    cardsOnBoard.setAccessible(true);

    // 20 of level 3 cards, 30 of level 2 cards, 40 of level 1 cards
    for (int i = 1; i <= 3; i++) {
      List<DevelopmentCard> levelDeck = new ArrayList<>();
      for (int j = 0; j < 10 + i*10; j++) {
        levelDeck.add(new DevelopmentCard(points, cardPrice,
            cardName  + j, i, cardColour, gemNumber, cardEffects));
      }
      cardPool.addAll(levelDeck);
      testLevelDecks.put(i, levelDeck);
      // start with empty slots (2 empty slots for orient board
      DevelopmentCard[] levelCardsOnBoard = new DevelopmentCard[2];
      testCardsOnBoard.put(i, levelCardsOnBoard);
    }

    // set the field of board without public setter methods
    cardsOnBoard.set(board, testCardsOnBoard);
  }

  // handle decks set up using private method
  void setUpDeckFromPool()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // method name, the later arg is the argument type
    Method generateDeckPerLevel =
        OrientBoard.class.getDeclaredMethod("generateDeckPerLevel", List.class);
    generateDeckPerLevel.setAccessible(true);
    generateDeckPerLevel.invoke(board,cardPool);
  }

  @Test
  void createOrientBoard() {
    OrientBoard orientBoard = new OrientBoard();
    for (int i = 1; i <= 3; i++) {
      int cardsOnBoard = orientBoard.getCardsOnBoard().get(i).length;
      int levelDeckSize = orientBoard.getDecks().get(i).size();
      System.out.println(Arrays.toString(orientBoard.getCardsOnBoard().get(i)));
      System.out.println(orientBoard.getDecks().get(i));
      System.out.println("level: " + i + " with cards on board: " + cardsOnBoard + " with deck size: " + levelDeckSize);
    }
  }

  /**
   * Implicitly tested getDecks as well.
   */
  @Test
  void testGenerateDeckPerLevel()
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    setUpDeckFromPool();
    Map<Integer, List<DevelopmentCard>> curDeck = board.getDecks();
    for (int level = 1; level <= 3; level++) {
      assertEquals(testLevelDecks.get(level),curDeck.get(level));
    }
  }

  @Test
  void testPopLevelCardFromDeck()
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    setUpDeckFromPool();
    for (int level = 1; level <= 3; level++) {
      DevelopmentCard cardFromDeck = testLevelDecks.get(level).get(0);
      DevelopmentCard cardPoped = board.popLevelCardFromDeck(level);
      assertTrue(cardFromDeck.equals(cardPoped));
    }
  }

  @Test
  void testUpdate()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    setUpDeckFromPool();
    board.update();

    for (int level = 1; level <= 3; level++) {
      DevelopmentCard[] cardsOnBoard = testCardsOnBoard.get(level);
      List<DevelopmentCard> deck = testLevelDecks.get(level);

      for (int i = 0; i < cardsOnBoard.length; i++) {
        cardsOnBoard[i] = deck.remove(0);
      }

      assertEquals(cardsOnBoard, board.getCardsOnBoard().get(level));
    }
  }

  @Test
  void testRemoveCard()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
    setUpDeckFromPool();
    board.update();
    Position position = new Position(1, 1);
    DevelopmentCard testCard = testCardsOnBoard.get(position.getX())[position.getY()];
    DevelopmentCard curCard = board.removeCard(position);
    assertEquals(testCard, curCard);
    assertNull(board.getCardsOnBoard().get(position.getX())[position.getY()]);
  }

  @Test
  void testGetLevelCardsOnBoard()
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    setUpDeckFromPool();
    board.update();
    for (int i = 1; i <= 3; i++) {
      DevelopmentCard[] cardsOnBoard = testCardsOnBoard.get(i);
      List<DevelopmentCard> deck = testLevelDecks.get(i);
      for (int j = 0; j < cardsOnBoard.length; j++) {
        cardsOnBoard[j] = deck.remove(0);
      }
      assertEquals(cardsOnBoard, board.getLevelCardsOnBoard(i));
    }
  }


}
