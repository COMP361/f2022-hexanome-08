package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Players.
 */
public class PlayerInGame {


  private final String name;
  private int prestigePoints;
  private final EnumMap<Colour, Integer> wealth;
  private final TokenHand tokenHand;
  private final ReservedHand reservedHand;
  private final PurchasedHand purchasedHand;


  /**
   * Constructor.
   */
  public PlayerInGame(String name) {
    this.name = name;
    this.tokenHand = new TokenHand(0); // Initialize to 0 (empty)
    this.purchasedHand = new PurchasedHand();
    this.reservedHand = new ReservedHand();
    this.wealth = new EnumMap<>(Colour.class);
    this.prestigePoints = 0;
  }

  /**
   * This method calculates what tokens the player must use to buy the card. It then removes the
   * required tokens from the player's tokenHand.
   *
   * @param goldTokenRequired The number of gold tokens required to complete this purchase.
   * @param card              The card the player is buying.
   * @return A list of the tokens used up to buy a card (the ones removed from tokenHand).
   */
  public EnumMap<Colour, Integer> payTokensToBuy(int goldTokenRequired, DevelopmentCard card) {
    EnumMap<Colour, Integer> tokensPaid = new EnumMap(Colour.class);

    //TODO: Implement this functionality!
    return tokensPaid;
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

  public String getName() {
    return name;
  }

  public int getPrestigePoints() {
    return prestigePoints;
  }

  public void addPrestigePoints(int amount) {
    prestigePoints += amount;
  }

  public void removePrestigePoints(int amount) {
    prestigePoints -= amount;
  }

  /**
   * Check whether the player associated with this instance of player.
   * info satisfy the condition of being a winner or not
   *
   * @return whether this player is winner or not
   */
  public boolean isWinner() {
    // TODO: implement detail logic


    return false;
  }

  /**
   * Returns map of total gems a player has.
   */
  public EnumMap<Colour, Integer> getTotalGems() {
    EnumMap<Colour, Integer> totalGems = new EnumMap<>(Colour.class);
    // initialize first
    for (Colour c : Colour.values()) {
      totalGems.put(c, 0);
    }
    if (purchasedHand.getDevelopmentCards().size() > 0) {
      for (DevelopmentCard card : purchasedHand.getDevelopmentCards()) {
        Colour colour = card.getGemColour();
        if (!totalGems.containsKey(colour)) {
          // if gem map does not contain the colour, put the value in map
          totalGems.put(colour, card.getGemNumber());
        } else {
          // if it contains it, increment on the old value
          int oldValue = totalGems.get(colour);
          totalGems.put(colour, oldValue + card.getGemNumber());
        }
      }
    }
    return totalGems;
  }

  /**
   * Gems plus tokens.
   */
  public EnumMap<Colour, Integer> getWealth() {
    Logger logger = LoggerFactory.getLogger(PlayerInGame.class);
    EnumMap<Colour, Integer> gems = getTotalGems();
    logger.info("All tokens in token hand: " + tokenHand.getAllTokens());
    logger.info("All gems as a enum map: " + gems);

    for (Colour colour : Colour.values()) {
      wealth.put(colour, tokenHand.getAllTokens().get(colour) + gems.get(colour));
    }
    return wealth;
  }

  @Override
  public boolean equals(Object o) {
    PlayerInGame player = (PlayerInGame) o;
    return (this.getName().equals((player).getName()));
  }

}
