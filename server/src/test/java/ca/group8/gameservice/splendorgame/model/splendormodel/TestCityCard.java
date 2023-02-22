package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestCityCard {

  EnumMap<Colour, Integer> price = new EnumMap<Colour,Integer>(Colour.class){{
    put(Colour.BLUE, 3);
    put(Colour.RED, 0);
    put(Colour.BLACK, 0);
    put(Colour.GREEN, 0);
    put(Colour.WHITE, 0);
  }};
  CityCard card = new CityCard(15, price, "c1", 1);
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
        "name", 1, Colour.BLACK, 1, new ArrayList<>()));

    Field developmentCards = PurchasedHand.class.getDeclaredField("developmentCards");
    developmentCards.setAccessible(true);

    Field purchasedHand = PlayerInGame.class.getDeclaredField("purchasedHand");
    purchasedHand.setAccessible(true);
    PurchasedHand testPurchaseHand = new PurchasedHand();
    developmentCards.set(testPurchaseHand, allCardsInHand);
    player.addPrestigePoints(15);
    purchasedHand.set(player, testPurchaseHand);

  }

  void setInvalidWealth_No_Points() throws NoSuchFieldException, IllegalAccessException {
    List<DevelopmentCard> allCardsInHand = new ArrayList<>();
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLACK, 1, new ArrayList<>()));

    Field developmentCards = PurchasedHand.class.getDeclaredField("developmentCards");
    developmentCards.setAccessible(true);

    Field purchasedHand = PlayerInGame.class.getDeclaredField("purchasedHand");
    purchasedHand.setAccessible(true);
    PurchasedHand testPurchaseHand = new PurchasedHand();
    developmentCards.set(testPurchaseHand, allCardsInHand);
    player.addPrestigePoints(10);
    purchasedHand.set(player, testPurchaseHand);
  }

  void setInvalidWealth_No_Cards() throws NoSuchFieldException, IllegalAccessException {
    List<DevelopmentCard> allCardsInHand = new ArrayList<>();
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));
    allCardsInHand.add(new DevelopmentCard(5, new EnumMap<>(Colour.class),
        "name", 1, Colour.BLUE, 1, new ArrayList<>()));

    Field developmentCards = PurchasedHand.class.getDeclaredField("developmentCards");
    developmentCards.setAccessible(true);

    Field purchasedHand = PlayerInGame.class.getDeclaredField("purchasedHand");
    purchasedHand.setAccessible(true);
    PurchasedHand testPurchaseHand = new PurchasedHand();
    developmentCards.set(testPurchaseHand, allCardsInHand);
    player.addPrestigePoints(15);
    purchasedHand.set(player, testPurchaseHand);
  }

  @Test
  void testGetAnyColourCount() {
    assertEquals(1,card.getAnyColourCount());
  }

  @Test
  void testCanUnlock_True() throws NoSuchFieldException, IllegalAccessException {
    setValidWealth();
    assertTrue(card.canUnlock(player));
  }

  @Test
  void testCanUnlock_False_NoPoints() throws NoSuchFieldException, IllegalAccessException {
    setInvalidWealth_No_Points();
    assertFalse(card.canUnlock(player));
  }
  @Test
  void testCanUnlock_False_NoCards() throws NoSuchFieldException, IllegalAccessException {
    setInvalidWealth_No_Cards();
    assertFalse(card.canUnlock(player));
  }

}
