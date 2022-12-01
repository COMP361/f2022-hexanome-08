package ca.group8.gameservice.splendorgame.model.splendormodel;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.EnumMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Players.
 */
public class PlayerInGame implements BroadcastContent {


  private final String name;
  private EnumMap<Colour, Integer> wealth;
  private int prestigePoints;
  private TokenHand tokenHand;
  private PurchasedHand purchasedHand;

  // need get reserved card number
  private ReservedHand reservedHand;


  /**
   * Constructor.
   */
  public PlayerInGame(String name) {
    this.name = name;
    this.tokenHand = new TokenHand(3); // TODO: HARDCODED 3 FOR M5 ONLY
    this.purchasedHand = new PurchasedHand();
    this.reservedHand = new ReservedHand();
    this.wealth = new EnumMap<>(Colour.class);
    this.prestigePoints = 0;
  }

  /**
   * If all hands are empty.
   */
  @Override
  public boolean isEmpty() {
    return tokenHand.getAllTokens().isEmpty()
        && purchasedHand.getDevelopmentCards().isEmpty()
        && reservedHand.getDevelopmentCards().isEmpty();
  }

  /**
   * Returns map of total gems a layer has.
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

  public void setPrestigePoints(int newPrestigePoints) {
    prestigePoints = newPrestigePoints;
  }

  @Override
  public boolean equals(Object o) {
    PlayerInGame player = (PlayerInGame) o;
    return (this.getName().equals((player).getName()));
  }

}
