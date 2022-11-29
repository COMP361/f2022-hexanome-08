package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.EnumMap;

public class PlayerInGame implements BroadcastContent {

  private final String name;
  private TokenHand tokenHand;
  private PurchasedHand purchasedHand;
  private ReservedHand reservedHand;

  //need get reserved card number,
  private EnumMap<Colour, Integer> wealth;


  public PlayerInGame(String paramName){
    this.name = paramName;
    this.tokenHand = new TokenHand(3); // TODO: HARDCODED 3 FOR M5 ONLY
    this.purchasedHand = new PurchasedHand();
    this.reservedHand = new ReservedHand();
    this.wealth = new EnumMap<>(Colour.class);
  }


  public EnumMap<Colour, Integer> getTotalGems(){
    EnumMap<Colour, Integer> totalGems = new EnumMap<>(Colour.class);

    for(DevelopmentCard card: purchasedHand.getDevelopmentCards()){
      if(card.getGemColour().isPresent()){
        int oldValue = totalGems.get(card.getGemColour().get());
        totalGems.put(card.getGemColour().get(), oldValue+card.getGemNumber());
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

  public String getName() {
    return name;
  }

  @Override
  public boolean isEmpty() {
    return this.name != null && !tokenHand.getAllTokens().isEmpty()
        && !purchasedHand.getDevelopmentCards().isEmpty()
        && !reservedHand.getDevelopmentCards().isEmpty();
  }
}
