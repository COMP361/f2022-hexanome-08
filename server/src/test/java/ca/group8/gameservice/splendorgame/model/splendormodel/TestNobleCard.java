package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestNobleCard {
  EnumMap<Colour, Integer> price = new EnumMap<Colour,Integer>(Colour.class){{
    put(Colour.BLUE, 4);
    put(Colour.RED, 0);
    put(Colour.BLACK, 4);
    put(Colour.GREEN, 0);
    put(Colour.WHITE, 0);
  }};
  NobleCard card = new NobleCard(15, price, "c1");
  PlayerInGame player = new PlayerInGame("Bob");

  void setValidWealth() throws NoSuchFieldException, IllegalAccessException {
    List<DevelopmentCard> allCardsInHand = new ArrayList<>();
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLACK, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLACK, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLACK, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLACK, 1, new ArrayList<>()));

    Field developmentCards = PurchasedHand.class.getDeclaredField("developmentCards");
    developmentCards.setAccessible(true);
    Field purchasedHand = PlayerInGame.class.getDeclaredField("purchasedHand");
    purchasedHand.setAccessible(true);
    PurchasedHand testPurchaseHand = new PurchasedHand();
    developmentCards.set(testPurchaseHand, allCardsInHand);
    purchasedHand.set(player, testPurchaseHand);
  }

  void setInvalidWealth() throws NoSuchFieldException, IllegalAccessException {
    List<DevelopmentCard> allCardsInHand = new ArrayList<>();
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLACK, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLACK, 1, new ArrayList<>()));
    Field developmentCards = PurchasedHand.class.getDeclaredField("developmentCards");
    developmentCards.setAccessible(true);
    Field purchasedHand = PlayerInGame.class.getDeclaredField("purchasedHand");
    purchasedHand.setAccessible(true);
    PurchasedHand testPurchaseHand = new PurchasedHand();
    developmentCards.set(testPurchaseHand, allCardsInHand);
    purchasedHand.set(player, testPurchaseHand);
  }

  @Test
  void testCanVisit_True() throws NoSuchFieldException, IllegalAccessException {
    setValidWealth();
    assertTrue(card.canVisit(player));

  }

  @Test
  void testCanVisit_False() throws NoSuchFieldException, IllegalAccessException {
    setInvalidWealth();
    assertFalse(card.canVisit(player));
  }
}
