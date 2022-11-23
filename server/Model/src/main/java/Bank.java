import java.util.EnumMap;

/**
 * This class represents the bank.
 */
public class Bank {

  private EnumMap<GemColour, Integer> allGems;
  private final int initialValue; //this is the value all gems (excl. gold) are initialized too

  /**
   * Relies on Game Info to know how many players are in the game
   * Initalization values:
   * 5 gold tokens always
   * 4 players -- 7 of all gems (excl. gold)
   * 3 players -- 5 of all gems (excl. gold)
   * 2 players -- 4 of all gems (excl. gold)
   *
   */
  //TODO: Do we want to have this param?? Or should we implement this logic in GameState
  //TODO: and pass an integer value to Bank representing the initial gem values.
  protected Bank(GameInfo gameInfo) {
    initialValue = (GameInfo.getNumOfPlayers() * 2) - 1;
    for (GemColour colour : GemColour.values()) {
      if (colour == GemColour.GOLD) {
        allGems.put(colour, 5);
      } else {
        allGems.put(colour, initialValue);
      }
    }
  }

  /**
   * Adds a certain amount (quantity) of a certain GemColour colour to the Bank.
   *
   * @param colour = Gem Colour.
   * @param quantity = quantity to add.
   *
   */
  //TODO: Is it going to be called Gems or tokens? Cause in concept model we called it Gems
  public void addGem(GemColour colour, int quantity) {
    //verify that this number of gems can be added (meaning new sum will not exceed initial value)
    assert (allGems.get(colour) + quantity) <= initialValue;
    //add Gems
    allGems.put(colour, (allGems.get(colour) + quantity));
  }

  /**
   * Removes a certain amount (quantity) of a certain GemColour colour from the Bank.
   *
   * @param colour = Gem Colour.
   * @param quantity = quantity to remove.
   *
   */
  //TODO: Is it going to be called Gems or tokens? Cause in concept model we called it Gems
  public void removeGem(GemColour colour, int quantity) {
    //verify that this number of gems can be removed (meaning new sum >=0)
    assert (allGems.get(colour) - quantity) >= 0;
    //remove Gems
    allGems.put(colour, (allGems.get(colour) - quantity));
  }
}

