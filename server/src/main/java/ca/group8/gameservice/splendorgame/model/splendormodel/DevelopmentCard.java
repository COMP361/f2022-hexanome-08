package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
   * canBeBought.
   *
   * @param hasDoubleGoldPower hasDoubleGoldPower
   */
  public EnumMap<Colour, Integer> canBeBought(boolean hasDoubleGoldPower,
                                              PlayerInGame curPlayerInfo) {

    Logger logger = LoggerFactory.getLogger(DevelopmentCard.class);
    EnumMap<Colour, Integer> wealth = new EnumMap<>(curPlayerInfo.getWealth());
    EnumMap<Colour, Integer> wealthWithoutGoldCard = new EnumMap<>(curPlayerInfo.getWealth());
    // discount (dev cards)
    EnumMap<Colour, Integer> allGems = new EnumMap<>(curPlayerInfo.getTotalGems());
    // (tokens)
    EnumMap<Colour, Integer> allTokens = new EnumMap<>(curPlayerInfo.getTokenHand().getAllTokens());
    int goldCardCount = (int) curPlayerInfo.getPurchasedHand().getDevelopmentCards()
        .stream().filter(c -> c.getGemColour() == Colour.GOLD).count();

    EnumMap<Colour, Integer> cardPrice = super.getPrice();
    // leave only gold token count in wealth
    int goldTokensFromCard = 2 * goldCardCount;
    wealthWithoutGoldCard.put(Colour.GOLD, wealth.get(Colour.GOLD) - goldTokensFromCard);
    int goldTokensInHand = wealthWithoutGoldCard.get(Colour.GOLD);
    Map<Colour, Integer> diffPrice;
    int goldTokenNeededToPay = 0;

    // prioritizing card to use
    if (goldCardCount > 0) {
      int[] goldTokenArr = new int[goldTokensFromCard];
      if (hasDoubleGoldPower) {
        // this len = goldTokenCount array [2,2,2,2..] is used to consider the double gold
        // whenever in the diff price map we need a gold token to make up the price diff,
        // we take an entry out of the array, make it to zero and add it to the diff price map
        Arrays.fill(goldTokenArr, 2);
      } else {
        Arrays.fill(goldTokenArr, 1);
      }
      int i = 0;
      diffPrice = wealthWithoutGoldCard.keySet().stream()
          .filter(colour -> !colour.equals(Colour.GOLD) && !colour.equals(Colour.ORIENT))
          .collect(Collectors.toMap(
              key -> key,
              key -> wealthWithoutGoldCard.get(key) - cardPrice.get(key)
          ));


      int goldCardLeft = goldCardCount;
      while (goldCardLeft > 0) {
        for (Colour colour : diffPrice.keySet()) {
          // excluding gold token in here since we only consider gold token card
          if (colour != Colour.GOLD) {
            while (diffPrice.get(colour) < 0) {
              if (i >= goldTokenArr.length) {
                break;
              }
              int newValue = diffPrice.get(colour) + goldTokenArr[i];
              i += 1;
              diffPrice.put(colour, newValue);
              if (newValue == 0 && i % 2 == 1) {
                // when the current colour reaches 0
                if (diffPrice.values().stream().noneMatch(v -> v < 0)) {
                  newValue = diffPrice.get(colour) + goldTokenArr[i];
                  i += 1;
                }
                diffPrice.put(colour, newValue);
                goldCardLeft -= 1;
                goldTokenNeededToPay += 2;
              }
            }
          }
        }
        if (i >= goldTokenArr.length) {
          break;
        }
      }


      // after this computation, it is possible that we still need the help
      // from gold tokens (still have some negative number), then we need to
      // consider the
      if (diffPrice.values().stream().anyMatch(v -> v < 0)) {
        if (goldTokensInHand > 0) {
          goldTokenNeededToPay += computeGoldTokensNeeded(
              goldTokensInHand,
              hasDoubleGoldPower,
              diffPrice);
        }
      }

    } else {
      // we do not have any gold card, then do a regular check with gold tokens in hand
      diffPrice = wealthWithoutGoldCard.keySet().stream()
          .filter(colour -> !colour.equals(Colour.GOLD) && !colour.equals(Colour.ORIENT))
          .collect(Collectors.toMap(
              key -> key,
              key -> wealthWithoutGoldCard.get(key) - cardPrice.get(key)
          ));
      if (goldTokensInHand > 0) {
        goldTokenNeededToPay += computeGoldTokensNeeded(
            goldTokensInHand,
            hasDoubleGoldPower,
            diffPrice);
      }
    }

    EnumMap<Colour, Integer> finalResult = SplendorDevHelper.getInstance().getRawTokenColoursMap();

    // store gold tokens needed in here
    finalResult.put(Colour.GOLD, goldTokenNeededToPay);
    for (Colour colour : allTokens.keySet()) {
      if (colour != Colour.GOLD) {
        int tokensOfColourHas = allTokens.get(colour);
        int gemsOfColourHas = allGems.get(colour);
        int tokensOfColourLeft = diffPrice.get(colour);
        if (tokensOfColourLeft < 0) {
         finalResult.put(colour, tokensOfColourLeft);
        } else {
          if (gemsOfColourHas - cardPrice.get(colour) >= 0) {
            // if we have enough gem, player does not need to pay anything for this colour
            finalResult.put(colour, 0);
          } else {
            // otherwise, some tokens need to be considered
            if (tokensOfColourLeft == 0) {
              // we have just the right amount to pay off, thus all tokens will be used
              finalResult.put(colour, tokensOfColourHas);
            } else {
              // tokensOfColourLeft > 0, which means that we have some tokens left
              finalResult.put(colour, tokensOfColourHas - tokensOfColourLeft);
            }

          }
        }
      }
    }

    return finalResult;

  }

  private int computeGoldTokensNeeded(int goldTokensInHand,
                                                boolean hasDoubleGoldPower,
                                                Map<Colour, Integer> diffPrice) {
    // compute the price considering gold tokens
    int goldTokenNeededToPay = 0;
    int[] goldTokenArr = new int[goldTokensInHand];
    if (hasDoubleGoldPower) {
      Arrays.fill(goldTokenArr, 2);
    } else {
      Arrays.fill(goldTokenArr, 1);
    }
    int i = 0;
    while (i < goldTokenArr.length && diffPrice.values().stream().anyMatch(v -> v < 0)) {
      for (Colour colour : diffPrice.keySet()) {
        if (diffPrice.get(colour) < 0) {
          int curLeftOver = diffPrice.get(colour);
          diffPrice.put(colour, curLeftOver + goldTokenArr[i]);
          // increment the gold token amt needed, move to next gold token spot
          goldTokenArr[i] = 0;
          i += 1;
          goldTokenNeededToPay += 1;
          if (i == goldTokenArr.length) {
            break;
          }
        }
      }
    }
    return goldTokenNeededToPay;
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

    return super.equals(other)
            && this.level == other.level
            && this.gemNumber == other.gemNumber
            && this.isPaired == other.isPaired
            && this.gemColour.equals(other.gemColour)
            && this.purchaseEffects.equals(other.purchaseEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), level, gemNumber, isPaired, gemColour, purchaseEffects);
  }

}
