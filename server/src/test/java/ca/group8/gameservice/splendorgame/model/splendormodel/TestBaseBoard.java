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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestBaseBoard {

  List<String> playerNames = Arrays.asList("Jen", "Ben");
  BaseBoard board = new BaseBoard(playerNames);
  EnumMap<Colour,Integer> cardPrice = new EnumMap<>(Colour.class);
  List<NobleCard> testNobles = new ArrayList<>();
  int points = 5;
  int gemNumber = 1;
  String cardName = "card_name";
  String nobleName = "noble_name";
  Colour cardColour = Colour.BLUE;
  List<DevelopmentCard> cardPool = new ArrayList<>();
  Map<Integer, List<DevelopmentCard>> testLevelDecks = new HashMap<>();
  Map<Integer, DevelopmentCard[]> testCardsOnBoard = new HashMap<>();

  /**
   * Use reflection to set the private field of the base board for testing purpose
   * reset the board every time before each test
   */
  @BeforeEach
  void setUpBoard() throws NoSuchFieldException, IllegalAccessException {
    Field cardsOnBoard = BaseBoard.class.getDeclaredField("cardsOnBoard");
    Field nobles = BaseBoard.class.getDeclaredField("nobles");
    cardsOnBoard.setAccessible(true);
    nobles.setAccessible(true);

    // generate number of nobles based on number of players
    for (int i = 0; i < playerNames.size()+1; i++) {
      testNobles.add(new NobleCard(points, cardPrice, nobleName + "_" + i));
    }


    // 20 of level 3 cards, 30 of level 2 cards, 40 of level 1 cards
    for (int i = 1; i <= 3; i++) {
      List<DevelopmentCard> levelDeck = new ArrayList<>();
      for (int j = 0; j < 10 + i*10; j++) {
        levelDeck.add(new DevelopmentCard(points, cardPrice,
            cardName  + j, i, cardColour, gemNumber, new ArrayList<>()));
      }
      cardPool.addAll(levelDeck);
      testLevelDecks.put(i, levelDeck);
      // start with empty slots
      DevelopmentCard[] levelCardsOnBoard = new DevelopmentCard[4];
      testCardsOnBoard.put(i, levelCardsOnBoard);
    }

    // set the field of board without public setter methods
    cardsOnBoard.set(board, testCardsOnBoard);
    nobles.set(board, testNobles);

  }

  // handle decks set up using private method
  void setUpDeckFromPool()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // method name, the later arg is the argument type
    Method generateDeckPerLevel =
        BaseBoard.class.getDeclaredMethod("generateDeckPerLevel", List.class);
    generateDeckPerLevel.setAccessible(true);
    generateDeckPerLevel.invoke(board,cardPool);
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
      assertEquals(testLevelDecks.get(level).get(0),board.popLevelCardFromDeck(level));
    }
  }

  // handle cards on board set up with private method and reflection
  void setUpCardsOnBoard()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    setUpDeckFromPool();
    Method refillCardBoard = BaseBoard.class.getDeclaredMethod("refillCardBoard");
    refillCardBoard.setAccessible(true);
    refillCardBoard.invoke(board);
  }

  @Test
  void testRefillCardBoard()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    setUpDeckFromPool();
    setUpCardsOnBoard();

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
    setUpCardsOnBoard();
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
    setUpCardsOnBoard();
    for (int i = 1; i <= 3; i++) {
      DevelopmentCard[] cardsOnBoard = testCardsOnBoard.get(i);
      List<DevelopmentCard> deck = testLevelDecks.get(i);
      for (int j = 0; j < cardsOnBoard.length; j++) {
        cardsOnBoard[j] = deck.remove(0);
      }
      assertEquals(cardsOnBoard, board.getLevelCardsOnBoard(i));
    }
  }


  @Test
  void testGetNobles() {
    assertEquals(testNobles, board.getNobles());
  }

  @Test
  void testRemoveNoble() {
    NobleCard nobleToRemove = testNobles.get(1);
    testNobles.remove(nobleToRemove);
    board.removeNoble(nobleToRemove);
    assertEquals(testNobles, board.getNobles());
  }

  @Test
  void testAddNoble() {
    NobleCard newNoble = new NobleCard(points, cardPrice, nobleName + "_" + "99");
    testNobles.add(newNoble);
    board.addNoble(newNoble);
    assertEquals(testNobles, board.getNobles());
  }

}
