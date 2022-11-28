package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import java.util.EnumMap;

public class PlayerInGame implements PlayerReadOnly {

  private final String name;
  private TokenHand tokenHand;
  private PurchasedHand purchasedHand;
  private ReservedHand reservedHand;

  //need get reserved card number,
  private EnumMap<Colour, Integer> wealth;


  public PlayerInGame(String paramName){
    this.name = paramName;
    this.tokenHand = new TokenHand();
    this.purchasedHand = new PurchasedHand();
    this.reservedHand = new ReservedHand();
    this.wealth = new EnumMap<>(Colour.class);
  }


  public EnumMap<Colour, Integer> getTotalGems(){
    EnumMap<Colour, Integer> totalGems = new EnumMap<>(Colour.class);

    for(DevelopmentCard card: purchasedHand.getDevelopmentCards()){
      if(card.getGemColor().isPresent()){
        int oldValue = totalGems.get(card.getGemColor().get());
        totalGems.put(card.getGemColor().get(), oldValue+card.getGemNumber());
      }
    }
    return totalGems;
  }



  public EnumMap<Colour, Integer> getWealth(){
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
