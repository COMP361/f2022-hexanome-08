package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the bank.
 */
public class Bank {

  private final EnumMap<Colour, Integer> allTokens;
  private final int initialValue; //this is the value all gems (excl. gold) are initialized to

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
    this.allTokens = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    if (numPlayers == 2) {
      initialValue = 4;
    } else {
      initialValue = (numPlayers * 2) - 1;
    }

    for (Colour colour : Colour.values()) {
      if (colour != Colour.ORIENT) {
        if (colour == Colour.GOLD) {
          allTokens.put(colour, 5);
        } else {
          allTokens.put(colour, initialValue);
        }
      }
    }
  }

  /**
   * Return the tokens that was paid by player.
   *
   * @param paramTokens token map as the price
   */
  public void returnToken(EnumMap<Colour, Integer> paramTokens) {
    ////verify that this number of gems can be added (meaning new sum will not exceed initial value)
    //for (Colour colour : SplendorDevHelper.getInstance().getRawTokenColoursMap().keySet()) {
    //  assert (allTokens.get(colour) + paramTokens.get(colour)) <= initialValue;
    //}
    //add Tokens
    Logger logger = LoggerFactory.getLogger(Bank.class);
    logger.warn("bank balance: " + allTokens);
    logger.warn("tokens return: " + paramTokens);
    for (Colour colour : SplendorDevHelper.getInstance().getRawTokenColoursMap().keySet()) {
      int newVal = allTokens.get(colour) + paramTokens.get(colour);
      allTokens.replace(colour, newVal);
    }
  }

  /**
   * Modify the game state to reflect that the player has taken some tokens out of bank.
   *
   * @param paramTokens the tokens map that player wants to take
   */
  public void takeToken(EnumMap<Colour, Integer> paramTokens) {
    //verify that this number of gems can be removed (meaning new sum will not be less than 0)
    //remove Tokens
    for (Colour colour : Colour.values()) {
      if (colour != Colour.ORIENT && !colour.equals(Colour.GOLD)) {
        int newVal = allTokens.get(colour) - paramTokens.get(colour);
        assert newVal >= 0;
        allTokens.replace(colour, newVal);
      }
    }
  }


  public EnumMap<Colour, Integer> getAllTokens() {
    return allTokens;
  }

  /**
   * @return a number indicating how many non-gold tokens are left
   */
  public int getRegularTokenCount() {

    return allTokens.entrySet()
        .stream()
        .filter(entry -> !entry.getKey().equals(Colour.GOLD) &&
            !entry.getKey().equals(Colour.ORIENT))
        .mapToInt(Map.Entry::getValue)
        .sum();
  }

  public int getInitialValue() {
    return initialValue;
  }
}


