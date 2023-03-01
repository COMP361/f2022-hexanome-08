package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
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
    this.wealth = SplendorDevHelper.getInstance().getRawTokenColoursMap(); // wealth including gold
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
    EnumMap<Colour, Integer> tokensPaid = card.getPrice();

    //if gold tokens are required, add this value to tokensPaid map

    //remove "discount" provided by gems from purchasedhand
    EnumMap<Colour, Integer> totalGems = this.getTotalGems();
    int goldTokensRemaining = goldTokenRequired;
    for (Colour c : totalGems.keySet()) {
      //calculate price after discount
      int priceAfterDiscount = tokensPaid.get(c) - totalGems.get(c);
      //if price is negative, meaning you have more gems than required, just set tokens paid to 0.
      if (priceAfterDiscount < 0) {
        tokensPaid.put(c, 0);
      } else if (priceAfterDiscount >= goldTokensRemaining && goldTokensRemaining > 0) {
        //deals with case where you use up all remaining gold tokens available
        tokensPaid.put(c, priceAfterDiscount - goldTokensRemaining);
        goldTokensRemaining = 0;
      } else if (priceAfterDiscount < goldTokensRemaining && goldTokensRemaining > 0) {
        //deals with case where you pay price and have extra gold tokens remaining
        goldTokensRemaining -= priceAfterDiscount;
        tokensPaid.put(c, 0);
      } else {
        tokensPaid.put(c, priceAfterDiscount);
      }
    }

    //check to see if any gold tokens were used
    //if the total Gold wealth of player == number of gold tokens in their hand,
    //this means that the Player ONLY used gold tokens for this purchase (not gold gem cards)
    if (this.getWealth().get(Colour.GOLD) <= tokenHand.getGoldTokenNumber()) {
      tokensPaid.put(Colour.GOLD, goldTokenRequired);
    }

    /*TODO: Need to deal with whether gold tokens are returned to the bank or not
    Based on this implementation, how will we know if "goldTokenRequired"
    parameter is burning a card with goldTokens on it, or if we are using gold tokens
    that must be returned to the bank.
    We need to figure out how to identify if its a gold gem card being burned.
    Once this is decided, TODO add the gold tokens returned to bank to tokenHand variable.
    */

    //remove tokens (after discount_ for this payment from playerHand
    tokenHand.removeToken(tokensPaid);

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
   * Returns map of total gems (out of all dev cards) a player has.
   * Guarantee to return a map with only RED, BLUE, WHITE, BLACK AND GREEN
   */
  public EnumMap<Colour, Integer> getTotalGems() {
    EnumMap<Colour, Integer> totalGems = SplendorDevHelper.getInstance().getRawGemColoursMap();
    if (purchasedHand.getDevelopmentCards().size() > 0) {
      for (DevelopmentCard card : purchasedHand.getDevelopmentCards()) {
        // Only count the card with regular gem colours
        if (card.hasRegularGemColour()) {
          Colour colour = card.getGemColour();
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
    //logger.info("All tokens in token hand: " + tokenHand.getAllTokens());
    //logger.info("All gems as a enum map: " + gems);

    for (Colour colour : SplendorDevHelper.getInstance().getRawTokenColoursMap().keySet()) {
      if (colour.equals(Colour.GOLD)) {
        wealth.put(colour, tokenHand.getAllTokens().get(colour));
      } else {
        wealth.put(colour, tokenHand.getAllTokens().get(colour) + gems.get(colour));
      }
    }
    return wealth;
  }


}
