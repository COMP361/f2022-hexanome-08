package project.view.splendor.communication;

import java.util.EnumMap;

/**
 * This class represents the bank.
 */
public class Bank {
  private final EnumMap<Colour, Integer> allTokens;
  private final int initialValue; //this is the value all gems (excl. gold) are initialized to

  public EnumMap<Colour, Integer> getAllTokens() {
    return allTokens;
  }

  public int getInitialValue() {
    return initialValue;
  }

  public Bank(EnumMap<Colour, Integer> allTokens, int initialValue) {
    this.allTokens = allTokens;
    this.initialValue = initialValue;
  }
}


