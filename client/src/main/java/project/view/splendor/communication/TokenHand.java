package project.view.splendor.communication;

import java.util.EnumMap;


/**
 * Represents the Tokens that a Player has in their hand.
 */
public class TokenHand {

  private final int initialTokenAmount;
  private final EnumMap<Colour, Integer> allTokens;

  public TokenHand(int initialTokenAmount, EnumMap<Colour, Integer> allTokens) {
    this.initialTokenAmount = initialTokenAmount;
    this.allTokens = allTokens;
  }

  public int getInitialTokenAmount() {
    return initialTokenAmount;
  }

  public EnumMap<Colour, Integer> getAllTokens() {
    return allTokens;
  }

}


