package project.view.splendor.communication;

import java.util.EnumMap;
import project.view.splendor.Colour;

/**
 * This class represents the bank.
 */
public class Bank {

  public void setAllTokens(
      EnumMap<Colour, Integer> allTokens) {
    this.allTokens = allTokens;
  }

  public void setInitialValue(int initialValue) {
    this.initialValue = initialValue;
  }

  private EnumMap<Colour, Integer> allTokens;
  private int initialValue; //this is the value all gems (excl. gold) are initialized too

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


