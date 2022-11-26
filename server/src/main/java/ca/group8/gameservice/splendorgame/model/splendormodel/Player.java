package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import java.util.EnumMap;

public class Player implements PlayerReadOnly {

  private final String name;
  private TokenHand tokenHand;
  private PurchasedHand purchasedHand;
  private ReservedHand reservedHand;

  //need get reserved card number,
  private EnumMap<Colour, Integer> wealth = new EnumMap<>(Colour.class);


  public Player(String paramName, TokenHand paramTokenHand,
      PurchasedHand purchasedHand,
      ReservedHand reservedHand) {
    tokenHand=paramTokenHand;
    name = paramName;
    this.purchasedHand = purchasedHand;
    this.reservedHand = reservedHand;
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
    return null;
  }

  @Override
  public String getPreferredColour() {
    return null;
  }
}
