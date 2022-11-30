package project.view.splendor.communication;

import java.util.EnumMap;
import project.view.splendor.Colour;


public class PlayerInGame {


  public PlayerInGame(String name, EnumMap<Colour, Integer> wealth, int prestigePoints,
                      TokenHand tokenHand, PurchasedHand purchasedHand, ReservedHand reservedHand) {
    this.name = name;
    this.wealth = wealth;
    this.prestigePoints = prestigePoints;
    this.tokenHand = tokenHand;
    this.purchasedHand = purchasedHand;
    this.reservedHand = reservedHand;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setWealth(
      EnumMap<Colour, Integer> wealth) {
    this.wealth = wealth;
  }

  public void setPrestigePoints(int prestigePoints) {
    this.prestigePoints = prestigePoints;
  }

  public void setTokenHand(TokenHand tokenHand) {
    this.tokenHand = tokenHand;
  }

  public void setPurchasedHand(PurchasedHand purchasedHand) {
    this.purchasedHand = purchasedHand;
  }

  public void setReservedHand(ReservedHand reservedHand) {
    this.reservedHand = reservedHand;
  }

  private String name;
  private EnumMap<Colour, Integer> wealth;
  private int prestigePoints;
  private TokenHand tokenHand;
  private PurchasedHand purchasedHand;

  // need get reserved card number
  private ReservedHand reservedHand;

  public String getName() {
    return name;
  }

  public EnumMap<Colour, Integer> getWealth() {
    return wealth;
  }

  public int getPrestigePoints() {
    return prestigePoints;
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
}
