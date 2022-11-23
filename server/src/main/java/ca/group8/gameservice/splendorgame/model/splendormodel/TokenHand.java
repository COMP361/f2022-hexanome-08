package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

/**
 * Represents the Tokens that a Player has in their hand.
 */
public class TokenHand {

  private EnumMap<Colour, Integer> allTokens;

  /**
   * Relies on Game Info to know how many players are in the game.
   * Initialize all token values to zero.
   */
  protected TokenHand() {
    for (Colour colour : Colour.values()) {
      allTokens.put(colour, 0);
    }
  }

  public EnumMap<Colour, Integer> getAllTokens() {
    return allTokens;
  }

  /**
   * Adds a certain amount (quantity) of a certain GemColour colour from the TokenHand.
   *
   * @param colour = Gem Colour.
   * @param quantity = quantity to add.
   *
   */
  //TODO: QUESTIN: Is verifying that you have 10 tokens or less in the controller? yes
  public void addToken(Colour colour, int quantity) {
    //add Tokens
    allTokens.put(colour, (allTokens.get(colour) + quantity));
  }

  //TODO: Is it going to be called Gems or tokens? Cause in concept model we called it Gems

  /**
   * Removes a certain amount (quantity) of a certain GemColour colour from the TokenHand.
   *
   * @param colour = Gem Colour.
   * @param quantity = quantity to remove.
   *
   */
  public void removeToken(Colour colour, int quantity) {
    //verify that this number of tokens can be removed (meaning new sum >=0)
    assert (allTokens.get(colour) - quantity) >= 0;
    //remove Tokens
    allTokens.put(colour, (allTokens.get(colour) - quantity));
  }
}


