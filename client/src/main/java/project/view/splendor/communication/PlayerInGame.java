package project.view.splendor.communication;

import java.util.EnumMap;



public class PlayerInGame {
  private final String name;
  private int prestigePoints;
  private final EnumMap<Colour, Integer> wealth;
  private final TokenHand tokenHand;
  private final ReservedHand reservedHand;
  private final PurchasedHand purchasedHand;
  public PlayerInGame(String name, int prestigePoints, EnumMap<Colour, Integer> wealth, TokenHand tokenHand, ReservedHand reservedHand, PurchasedHand purchasedHand) {
    this.name = name;
    this.prestigePoints = prestigePoints;
    this.wealth = wealth;
    this.tokenHand = tokenHand;
    this.reservedHand = reservedHand;
    this.purchasedHand = purchasedHand;
  }

  public String getName() {
    return name;
  }

  public int getPrestigePoints() {
    return prestigePoints;
  }

  public void setPrestigePoints(int prestigePoints) {
    this.prestigePoints = prestigePoints;
  }

  public EnumMap<Colour, Integer> getWealth() {
    return wealth;
  }

  public TokenHand getTokenHand() {
    return tokenHand;
  }

  public ReservedHand getReservedHand() {
    return reservedHand;
  }

  public PurchasedHand getPurchasedHand() {
    return purchasedHand;
  }

}
