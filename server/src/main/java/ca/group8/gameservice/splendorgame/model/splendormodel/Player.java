package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import java.util.EnumMap;

public class Player implements PlayerReadOnly {

  private final String name;
  private TokenHand tokenHand;
  private PurchasedHand purchasedHand;
  private ReservedHand reservedHand;
  //needs purchased hand and reserved hand

  public Player(String paramName, TokenHand paramTokenHand) {
    tokenHand=paramTokenHand;
    name = paramName;
  }

  public EnumMap<Colour, Integer> getTotalGems(){
    EnumMap<Colour, Integer> totalGems = new EnumMap<>(Colour.class);

    for(DevelopmentCard card: purchasedHand.getDevelopmentCards()){
      int oldValue = totalGems.get(card.getGemColor());
      totalGems.put(card.getGemColor().get(), oldValue+card.getGemNumber());
    }
  }



  public EnumMap<Colour, Integer> getWealth(){
    EnumMap<Colour, Integer> wealth = new EnumMap<>(Colour.class);
    EnumMap<Colour, Integer> gems = getTotalGems();

    for(Colour colour : Colour.values()) {
      wealth.put(colour, tokenHand.getAllTokens().get(colour)+gems.get(colour);
    }
    return wealth;


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
