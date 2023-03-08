package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import java.util.EnumMap;

/**
 * Represents the Tokens that a Player has in their hand.
 */
public class TokenHand {

  private final int initialTokenAmount;
  private final EnumMap<Colour, Integer> allTokens;


  /**
   * Relies on Game Info to know how many players are in the game.
   * Initialize all token values to zero.
   */
  public TokenHand(int initialTokenAmount) {
    allTokens = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    allTokens.replaceAll((colour, value) -> initialTokenAmount);
    this.initialTokenAmount = initialTokenAmount;
  }

  /**
   * Adds a certain amount (quantity) of a certain GemColour colour to the TokenHand.
   *
   * @param paramTokens red,white,black,green and blue (note gold is not added in here)
   */
  public void addToken(EnumMap<Colour, Integer> paramTokens) {
    //add Tokens
    for (Colour colour : paramTokens.keySet()) {
      int newVal = allTokens.get(colour) + paramTokens.get(colour);
      allTokens.replace(colour, newVal);
    }
  }

  /**
   * Removes a Map of Tokens from the tokenHand.
   */
  public void removeToken(EnumMap<Colour, Integer> paramTokens) {
    //verify that this number of gems can be removed (meaning new sum will not be less than 0)
    //Must be done before the next loop to ensure it passes for all colours
    for (Colour colour : paramTokens.keySet()) {
      assert (allTokens.get(colour) - paramTokens.get(colour)) >= 0;
    }
    //remove Tokens
    for (Colour colour : paramTokens.keySet()) {
      int newVal = allTokens.get(colour) - paramTokens.get(colour);
      allTokens.replace(colour, newVal);
    }
  }

  public int getGoldTokenNumber() {
    return allTokens.get(Colour.GOLD);
  }

  public EnumMap<Colour, Integer> getAllTokens() {
    return allTokens;
  }

  public int getTokenTotalCount() {
    int sum = 0;
    for (Colour c : allTokens.keySet()) {
      sum += allTokens.get(c);
    }
    return sum;
  }
}


