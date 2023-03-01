package ca.group8.gameservice.splendorgame.model.splendormodel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPlayerInGame {
  PlayerInGame p1 = new PlayerInGame("Ben");
  PlayerInGame p2 = new PlayerInGame("Mary");
  EnumMap<Colour,Integer> price_1 = SplendorDevHelper.getInstance().getRawGemColoursMap();
  EnumMap<Colour,Integer> price_2 = SplendorDevHelper.getInstance().getRawGemColoursMap();
  EnumMap<Colour,Integer> price_3 = SplendorDevHelper.getInstance().getRawGemColoursMap();
  List<CardEffect> purchaseEffects = new ArrayList<>();
  DevelopmentCard c1 = new DevelopmentCard(1,price_1,"card1",1,Colour.RED,1,purchaseEffects);
  DevelopmentCard c2 = new DevelopmentCard(2,price_2,"card2",1,Colour.BLUE,2,purchaseEffects);
  DevelopmentCard c3 = new DevelopmentCard(3,price_3,"card3",1,Colour.BLACK,1,purchaseEffects);


  @BeforeEach
  void setup() {
    EnumMap<Colour,Integer> tokens = SplendorDevHelper.getInstance().getRawGemColoursMap();
    for (Colour c : tokens.keySet()) {
      tokens.put(c,0);
      price_1.put(c,0);
      price_2.put(c,0);
      price_3.put(c,0);
    }
    tokens.put(Colour.GREEN, 1);
    tokens.put(Colour.BLACK, 1);
    tokens.put(Colour.GOLD, 1);
    p1.getTokenHand().addToken(tokens);

    price_1.put(Colour.RED,1);
    price_1.put(Colour.BLUE,1);
    price_2.put(Colour.GREEN,2);
    p1.getPurchasedHand().addDevelopmentCard(c1);
    p1.getPurchasedHand().addDevelopmentCard(c2);
    p1.getPurchasedHand().addDevelopmentCard(c3);
  }


  @Test
  void TestAddPrestigePoints() {
    assertEquals(0, p1.getPrestigePoints());
    p1.addPrestigePoints(3);
    assertEquals(3, p1.getPrestigePoints());
  }

  @Test
  void TestRemovePrestigePoints() {
    assertEquals(0, p1.getPrestigePoints());
    p1.addPrestigePoints(5);
    assertEquals(5, p1.getPrestigePoints());
    p1.removePrestigePoints(2);
    assertEquals(3, p1.getPrestigePoints());
  }

  @Test
  void TestGetTotalGems() {
    EnumMap<Colour, Integer> totalGems = p1.getTotalGems();
    for (Colour c : totalGems.keySet()) {
      int gems = totalGems.get(c);
      if (c==Colour.RED) {
        assertEquals(1, gems);
      } else if (c==Colour.BLUE) {
        assertEquals(2, gems);
      } else if (c==Colour.BLACK) {
        assertEquals(1, gems);
      }
    }
  }

  @Test
  void TestGetWealth() {
    EnumMap<Colour, Integer> wealth = p1.getWealth();
    for (Colour c : wealth.keySet()) {
      int gems = wealth.get(c);
      if (c==Colour.RED) {
        assertEquals(1,gems);
      } else if (c==Colour.BLUE) {
        assertEquals(2, gems);
      } else if (c==Colour.BLACK) {
        assertEquals(2, gems);
      } else if (c==Colour.GREEN) {
        assertEquals(1, gems);
      }
    }
  }

  @Test
  void TestEquals() {
    assertFalse(p1.equals(p2));
    assertTrue(p1.equals(p1));
  }

  @Test
  void TestPayTokensToBuy_ReturnValue() {
    //check initial values
    int red_gems = p1.getWealth().get(Colour.RED);
    int blue_gems = p1.getWealth().get(Colour.BLUE);
    assertEquals(1,red_gems);
    assertEquals(2,blue_gems);

    //pay tokens
    EnumMap<Colour, Integer> tokensPaid = p1.payTokensToBuy(0,c1);

    for (Colour c : tokensPaid.keySet()) {
      assertEquals(c1.getPrice().get(c),tokensPaid.get(c));
    }

  }

  @Test
  void TestPayTokensToBuy_TokenHand() {
    //check initial values
    int red_gems = p1.getWealth().get(Colour.RED);
    int blue_gems = p1.getWealth().get(Colour.BLUE);
    assertEquals(1,red_gems);
    assertEquals(2,blue_gems);

    //pay tokens
    EnumMap<Colour, Integer> tokensPaid = p1.payTokensToBuy(0,c1);

    //Since discount covers entire purchase, the player should still have no red Gems in TokenHand.
    //Checking to ensure discount is applied and values aren't taken from TokenHand.
    EnumMap<Colour, Integer> p1_TokenHand = p1.getTokenHand().getAllTokens();
    int result_red = p1_TokenHand.get(Colour.RED);
    int result_blue = p1_TokenHand.get(Colour.BLUE);
    assertEquals(0, result_red);
    assertEquals(0, result_blue);
  }

  @Test
  void TestPayTokensToBuy_Gold() {
    //check initial values
    int red_gems = p1.getWealth().get(Colour.RED);
    int blue_gems = p1.getWealth().get(Colour.BLUE);
    int black_gems = p1.getWealth().get(Colour.BLACK);
    int black_tokens = p1.getTokenHand().getAllTokens().get(Colour.BLACK);
    int green_tokens = p1.getTokenHand().getAllTokens().get(Colour.GREEN);
    int gold_tokens = p1.getTokenHand().getAllTokens().get(Colour.GOLD);
    //1 green, 2 black, 2 blue, 1 red
    assertEquals(1, red_gems);
    assertEquals(2,blue_gems);
    assertEquals(2, black_gems);
    assertEquals(1, black_tokens);
    assertEquals(1,green_tokens);
    assertEquals(1,gold_tokens);

    //pay tokens
    EnumMap<Colour, Integer> tokensPaid = p1.payTokensToBuy(1,c2);

    //Here there is no discount, and 1 gold token should be used.
    //Checking to ensure discount is applied and values aren't taken from TokenHand.
    EnumMap<Colour, Integer> p1_TokenHand = p1.getTokenHand().getAllTokens();
    int result_red = p1_TokenHand.get(Colour.RED);
    int result_blue = p1_TokenHand.get(Colour.BLUE);
    int result_black_gems = p1.getWealth().get(Colour.BLACK);
    int result_black_tokens = p1_TokenHand.get(Colour.BLACK);
    int result_green = p1_TokenHand.get(Colour.GREEN);
    int result_gold = p1_TokenHand.get(Colour.GOLD);

    assertEquals(0, result_red);
    assertEquals(0, result_blue);
    assertEquals(2, result_black_gems);
    assertEquals(1, result_black_tokens);
    assertEquals(0,result_green);
    assertEquals(0,result_gold);
  }

}
