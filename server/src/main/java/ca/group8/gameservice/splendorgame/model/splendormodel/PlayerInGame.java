package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import java.util.EnumMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Players.
 */
public class PlayerInGame {


  private final EnumMap<Colour, Integer> wealth;
  private final TokenHand tokenHand;
  private final ReservedHand reservedHand;
  private final PurchasedHand purchasedHand;
  private String name;
  private int prestigePoints;


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
   * @param goldCardsRequired The number of gold tokens required to complete this purchase
   * @param paidTokens        tokens paid to buy the card
   */
  public void payTokensToBuy(int goldCardsRequired, EnumMap<Colour, Integer> paidTokens) {

    // remove tokens (after discount_ for this payment from playerHand
    tokenHand.removeToken(paidTokens);

    List<DevelopmentCard> allDevCards = purchasedHand.getDevelopmentCards();
    while (goldCardsRequired > 0) {
      for (DevelopmentCard card : allDevCards) {
        if (card.getGemColour().equals(Colour.GOLD)) {
          goldCardsRequired -= 1;
          // remove the gold card that's used to pay
          purchasedHand.removeDevelopmentCard(card);
        }
      }
    }
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

  public void setName(String name) {
    this.name = name;
  }

  public int getPrestigePoints() {
    return prestigePoints;
  }

  /**
   * Amount can be negative, as to minus.
   *
   * @param amount amount
   */
  public void changePrestigePoints(int amount) {
    prestigePoints += amount;
  }

  public void removePrestigePoints(int amount) {
    prestigePoints -= amount;
  }

  /**
   * Returns map of total gems (out of all dev cards) a player has.
   * Guarantee to return a map with only RED, BLUE, WHITE, BLACK AND GREEN
   */
  public EnumMap<Colour, Integer> getTotalGems() {
    EnumMap<Colour, Integer> totalGems = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    if (purchasedHand.getDevelopmentCards().size() > 0) {
      for (DevelopmentCard card : purchasedHand.getDevelopmentCards()) {
        // Only count the card with regular gem colours
        //if (card.hasRegularGemColour()) {
        //}
        Colour colour = card.getGemColour();
        if (!colour.equals(Colour.ORIENT)){
          Logger logger  = LoggerFactory.getLogger(PlayerInGame.class);
          logger.warn("Colour " + colour);
          logger.warn("TotalGems: " + totalGems);
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
