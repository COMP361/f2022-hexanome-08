package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import java.util.EnumMap;

public class Player implements PlayerReadOnly {

  private final String name;
  private TokenHand tokenHand;
  //needs purchased hand and reserved hand

  public Player(String paramName, TokenHand paramTokenHand) {
    tokenHand=paramTokenHand;
    name = paramName;
  }

  public EnumMap<Colour, Integer> getWealth(){

  }



  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getPreferredColour() {
    return null;
  }
}
