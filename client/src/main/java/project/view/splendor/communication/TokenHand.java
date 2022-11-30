package project.view.splendor.communication;

import java.util.EnumMap;
import project.view.splendor.Colour;

/**
 * Represents the Tokens that a Player has in their hand.
 */
public class TokenHand {

  public TokenHand(EnumMap<Colour, Integer> allTokens, int initialAmount) {
    this.allTokens = allTokens;
    this.initialAmount = initialAmount;
  }

  public EnumMap<Colour, Integer> getAllTokens() {
    return allTokens;
  }

  public int getInitialAmount() {
    return initialAmount;
  }

  public void setAllTokens(
      EnumMap<Colour, Integer> allTokens) {
    this.allTokens = allTokens;
  }

  public void setInitialAmount(int initialAmount) {
    this.initialAmount = initialAmount;
  }

  private EnumMap<Colour, Integer> allTokens;
  private int initialAmount;


}


