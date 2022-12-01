package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

/**
 * This class represents the bank.
 */
public class Bank {

  private final EnumMap<Colour, Integer> allTokens;
  private final int initialValue; //this is the value all gems (excl. gold) are initialized too

  /**
   * Relies on Game Info to know how many players are in the game
   * Initalization values:
   * 5 gold tokens always
   * 4 players -- 7 of all gems (excl. gold)
   * 3 players -- 5 of all gems (excl. gold)
   * 2 players -- 4 of all gems (excl. gold)
   */
  //TODO: Do we want to have this param?? Or should we implement this logic in GameState
  //TODO: and pass an integer value to Bank representing the initial gem values.
  public Bank(int numPlayers) {
    this.allTokens = new EnumMap<>(Colour.class);
    initialValue = (numPlayers * 2) - 1;
    for (Colour colour : Colour.values()) {
      if (colour == Colour.GOLD) {
        allTokens.put(colour, 5);
      } else {
        allTokens.put(colour, initialValue);
      }
    }
  }

  /**
   * Adds a map of tokens to the bank.
   */
  public void addToken(EnumMap<Colour, Integer> paramTokens) {
    //verify that this number of gems can be added (meaning new sum will not exceed initial value)
    for (Colour colour : Colour.values()) {
      assert (allTokens.get(colour) + paramTokens.get(colour)) <= initialValue;
    }
    //add Tokens
    for (Colour colour : Colour.values()) {
      int newVal = allTokens.get(colour) + paramTokens.get(colour);
      allTokens.replace(colour, newVal);
    }
  }


  /**
   * Removes a Map of Tokens from the Bank.
   */

  public void removeToken(EnumMap<Colour, Integer> paramTokens) {
    //verify that this number of gems can be removed (meaning new sum will not be less than 0)
    for (Colour colour : Colour.values()) {
      assert (allTokens.get(colour) - paramTokens.get(colour)) >= 0;
    }
    //remove Tokens
    for (Colour colour : Colour.values()) {
      int newVal = allTokens.get(colour) - paramTokens.get(colour);
      allTokens.replace(colour, newVal);
    }
  }

  public EnumMap<Colour, Integer> getAllTokens() {
    return allTokens;
  }

  public int getInitialValue() {
    return initialValue;
  }
}


