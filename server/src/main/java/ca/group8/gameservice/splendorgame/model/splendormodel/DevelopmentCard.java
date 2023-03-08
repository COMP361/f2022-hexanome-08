package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class represents the SuperClass of all Development Cards.
 */
public class DevelopmentCard extends Card {

  private final int level;
  private final Colour gemColour;
  private final List<CardEffect> purchaseEffects;
  private int gemNumber;
  private boolean isPaired;
  private DevelopmentCard pairedCard;

  /**
   * prestige points, price, name, level, colour, isPaired, pairID, gem number.
   */
  public DevelopmentCard(int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice,
                         String paramCardName, int level, Colour gemColour, int gemNumber,
                         List<CardEffect> purchaseEffects) {
    super(paramPrestigePoints, paramPrice, paramCardName);
    super.type = this.getClass().getSimpleName();
    this.isPaired = false;
    this.pairedCard = null;
    this.level = level;
    this.gemColour = gemColour;
    this.gemNumber = gemNumber;
    this.purchaseEffects = Collections.unmodifiableList(purchaseEffects);
  }

  public int getLevel() {
    return level;
  }

  public Colour getGemColour() {
    return gemColour;
  }

  public Boolean isPaired() {
    return isPaired;
  }

  /**
   * Get the dev card instance that is paired to this card.
   *
   * @return dev card instance that is paired to this card
   * @throws SplendorGameException exception during game running
   */
  public DevelopmentCard getPairedCard() throws SplendorGameException {
    if (!isPaired || pairedCard == null) {
      throw new SplendorGameException("Card is not paired yet");
    }
    return pairedCard;
  }

  public void setPairedCard(DevelopmentCard card) {
    pairedCard = card;
  }

  /**
   * Return the gem number of this dev card.
   *
   * @return if it's paired, return 2, otherwise 1
   */
  public int getGemNumber() {
    return gemNumber;
  }

  public void setIsPaired(boolean value) {
    isPaired = value;
  }

  /**
   * get the purchase effects of this card, if empty, then it's base card, otherwise.
   * it's an orient card
   *
   * @return list of purchaseEffects, can be empty
   */
  public List<CardEffect> getPurchaseEffects() {
    return purchaseEffects;
  }

  /**
   * pair a dev card with purchaseEffect containing SATCHEL to this card.
   *
   * @param pairedCard the card with purchaseEffect containing SATCHEL
   * @post gemNumber increment by 1, isPaired set to true, pairedCard gets assigned
   */
  public void pairCard(DevelopmentCard pairedCard) {
    if (pairedCard != null && pairedCard.purchaseEffects.contains(CardEffect.SATCHEL)) {
      isPaired = true;
      this.pairedCard = pairedCard;
      gemNumber += 1;
    }
  }

  /**
   * If a development card has no purchase effect, then it's a base card, otherwise it's orient
   * A card is a base card if it meets the following conditions: It has no purchase effects AND...
   * It has only 1 gem associated with it
   * OR it is a normal card, but its paired (so isPaired is true, plus gemNumber is 2).
   *
   * @return whether this dev card is base card or not
   */
  public boolean isBaseCard() {
    return purchaseEffects.isEmpty() && (gemNumber == 1 || (isPaired && gemNumber == 2));
  }

  /**
   * If a card has either Gold or Orient colour, then it's not a regular gem colour.
   *
   * @return whether a card has regular gem colour or not
   */
  public boolean hasRegularGemColour() {
    return !(gemColour.equals(Colour.GOLD) || gemColour.equals(Colour.ORIENT));
  }


  /**
   * @param hasDoubleGoldPower
   * @param wealth
   * @return -1 if can not afford, 0 or >0 as a number of gold token needed
   */
  public int canBeBought(boolean hasDoubleGoldPower, EnumMap<Colour, Integer> wealth) {
    EnumMap<Colour, Integer> cardPrice = super.getPrice();

    // such Colour -> Integer map only contain the difference between regular token
    // colours, not the gold colour
    // diffPrices -> {BLUE:0, RED:-1, ..} is the result of wealth - price, we do not
    // consider gold in here
    Map<Colour, Integer> diffPrice = wealth.keySet().stream()
        .filter(colour -> !colour.equals(Colour.GOLD) && !colour.equals(Colour.ORIENT))
        .collect(Collectors.toMap(
            key -> key,
            key -> wealth.get(key) - cardPrice.get(key)
        ));


    int goldTokenCount = wealth.get(Colour.GOLD);
    boolean hasGoldToken = goldTokenCount > 0;
    int goldTokenNeeded = 0;
    // do something to diff price map in here
    if (hasGoldToken) {
      // only consider count the gold token differently if the player has the power and one has
      // some gold tokens, otherwise there is no point considering it
      int[] goldTokenArr = new int[goldTokenCount];
      if (hasDoubleGoldPower) {
        // this len = goldTokenCount array [2,2,2,2..] is used to consider the double gold
        // whenever in the diff price map we need a gold token to make up the price diff,
        // we take an entry out of the array, make it to zero and add it to the diff price map
        Arrays.fill(goldTokenArr, 2);
      } else {
        Arrays.fill(goldTokenArr, 1);
      }
      // either we have a
      int i = 0;
      while (i < goldTokenArr.length && diffPrice.values().stream().anyMatch(v -> v < 0)) {
        for (Colour colour : diffPrice.keySet()) {
          if (diffPrice.get(colour) < 0) {
            int curLeftOver = diffPrice.get(colour);
            diffPrice.put(colour, curLeftOver + goldTokenArr[i]);
            // increment the gold token amt needed, move to next gold token spot
            goldTokenArr[i] = 0;
            i += 1;
            goldTokenNeeded += 1;
            if (i == goldTokenArr.length) {
              break;
            }

          }
        }
      }
    }

    // has no gold token, return the result regularly. if the diff map stays all non-negative,
    // then the player can afford this without using any gold token
    if (diffPrice.values().stream().allMatch(count -> count >= 0)) {
      return goldTokenNeeded;
    } else {
      // otherwise, no gold token and can not afford, return -1
      return -1;
    }
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof DevelopmentCard)) {
      return false;
    }

    DevelopmentCard other = (DevelopmentCard) obj;

    return super.equals(other) &&
        this.level == other.level &&
        this.gemNumber == other.gemNumber &&
        this.isPaired == other.isPaired &&
        this.gemColour.equals(other.gemColour) &&
        this.purchaseEffects.equals(other.purchaseEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), level, gemNumber, isPaired, gemColour, purchaseEffects);
  }

}
