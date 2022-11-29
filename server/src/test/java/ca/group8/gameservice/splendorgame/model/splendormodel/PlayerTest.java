package ca.group8.gameservice.splendorgame.model.splendormodel;



import ca.group8.gameservice.splendorgame.controller.communicationbeans.Player;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Handler;
import org.junit.Assert;
import org.junit.Test;

public class PlayerTest {

  PlayerInGame player = new PlayerInGame("Julia");
  PlayerInGame player2 = new PlayerInGame("Julia");

  @Test
  void testIsEmpty(){
    assert(player.isEmpty());
  }

  @Test
  void testGetters(){
    EnumMap<Colour,Integer> wealth = new EnumMap<Colour, Integer>(Colour.class);
    wealth.put(Colour.BLUE, 4);
    wealth.put(Colour.BLACK, 2);
    player.getTokenHand().addToken(wealth);
    DevelopmentCard card = new DevelopmentCard(3, wealth,"card", 2, Optional.of(Colour.BLUE),false, "32", 2);
    player.getPurchasedHand().addDevelopmentCard(card);
    EnumMap<Colour,Integer> gems = new EnumMap<Colour, Integer>(Colour.class);
    gems.put(Colour.BLUE,1);
    Assert.assertEquals(player.getTotalGems(),gems);
    wealth.put(Colour.BLUE,5);
    Assert.assertEquals(player.getWealth(),wealth);
  }

  @Test
  void testEquals(){
    assert(player.equals(player2));
  }


}
