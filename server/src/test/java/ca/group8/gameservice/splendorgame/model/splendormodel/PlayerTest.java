package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Handler;
import org.junit.Assert;
import org.junit.Test;

public class PlayerTest {

  PlayerInGame player = new PlayerInGame("Julia");
  PlayerInGame player2 = new PlayerInGame("Julia");

//  @Test
//  void testIsEmpty(){
//    assert(player.isEmpty());
//  }

  @Test
  public void testGetters(){
    EnumMap<Colour,Integer> wealth = new EnumMap<Colour, Integer>(Colour.class);
    for(Colour colour:Colour.values()){
      wealth.put(colour,3);
    }
    wealth.put(Colour.BLUE, 4);
    wealth.put(Colour.BLACK, 2);
    player.getTokenHand().addToken(wealth);
    DevelopmentCard card = new DevelopmentCard(3, wealth,"card", 2, Colour.BLUE,false, "32", 2);
    player.getPurchasedHand().addDevelopmentCard(card);
    EnumMap<Colour,Integer> gems = new EnumMap<Colour, Integer>(Colour.class);
    for(Colour colour:Colour.values()){
      gems.put(colour,0);
    }
    gems.put(Colour.BLUE,2);
    Assert.assertEquals(player.getTotalGems(),gems);
    wealth.put(Colour.BLUE,5);
    //Assert.assertEquals(player.getWealth(),wealth);
  }

  @Test
  public void testEquals(){
    assert(player.equals(player2));
  }


}
