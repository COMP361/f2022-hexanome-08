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

  public TokenHand getTokenHand() {
    return tokenHand;
  }

  public PurchasedHand getPurchasedHand() {
    return purchasedHand;
  }

  public ReservedHand getReservedHand() {
    return reservedHand;
  }


  public EnumMap<Colour, Integer> getWealth(){
    for(DevelopmentCard card: purchasedHand.getDevelopmentCards()){
      int oldValue = wealth.get(card.g)
      wealth.put()
    }


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
