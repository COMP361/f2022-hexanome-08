package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import java.util.EnumMap;

public class PlayerInGame implements PlayerReadOnly {

  private final String name;
  private TokenHand tokenHand = new TokenHand();
  private PurchasedHand purchasedHand = new PurchasedHand();
  private ReservedHand reservedHand = new ReservedHand();

  //need get reserved card number,
  private EnumMap<Colour, Integer> wealth = new EnumMap<>(Colour.class);


  public PlayerInGame(String paramName){
    name = paramName;
  }


  public EnumMap<Colour, Integer> getTotalGems(){
    EnumMap<Colour, Integer> totalGems = new EnumMap<>(Colour.class);

    for(DevelopmentCard card: purchasedHand.getDevelopmentCards()){
      int oldValue = totalGems.get(card.getGemColor());
      totalGems.put(card.getGemColor().get(), oldValue+card.getGemNumber());
    }
    return totalGems;
  }



  public EnumMap<Colour, Integer> getWealth(){
    EnumMap<Colour, Integer> wealth = new EnumMap<>(Colour.class);
    EnumMap<Colour, Integer> gems = getTotalGems();

    for(Colour colour : Colour.values()) {
      wealth.put(colour, tokenHand.getAllTokens().get(colour)+gems.get(colour));
    }
    return wealth;
  }

  public TokenHand getTokenHand() {
    return tokenHand;
  }

  public PurchasedHand getPurchasedHand() {
    return purchasedHand;
  }

  public ReservedHand getReservedHand() {
    return reservedHand;
  }




  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPreferredColour() {
    return getPreferredColour();
  }
}
