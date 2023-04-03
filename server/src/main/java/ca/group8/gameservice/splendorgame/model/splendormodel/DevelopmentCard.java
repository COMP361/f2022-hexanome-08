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

    // constructing a new card price that considered gem discount already
    EnumMap<Colour, Integer> allGems = new EnumMap<>(curPlayerInfo.getTotalGems());
    EnumMap<Colour, Integer> cardPrice = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    cardPrice.remove(Colour.GOLD);
    for (Colour colour : cardPrice.keySet()) {
      int cardPriceVal = super.getPrice().get(colour);
      int gemCount = allGems.get(colour);
      int priceAfterDiscount = cardPriceVal - gemCount;
      if (priceAfterDiscount <= 0) {
        cardPrice.put(colour, 0);
      } else {
        cardPrice.put(colour, priceAfterDiscount);
      }
    }

    // diff price containing the tokens (only tokens) we need to pay off
    // for this particular player
    EnumMap<Colour, Integer> allTokens = new EnumMap<>(curPlayerInfo.getTokenHand().getAllTokens());
    Map<Colour, Integer> diffPrice = cardPrice.keySet().stream()
        .filter(colour -> colour != Colour.GOLD && colour != Colour.ORIENT)
        .collect(Collectors.toMap(
            key -> key,
            key -> allTokens.get(key) - cardPrice.get(key)
        ));
    int goldCardCount = (int) curPlayerInfo.getPurchasedHand().getDevelopmentCards()
        .stream().filter(c -> c.getGemColour() == Colour.GOLD).count();
    int goldTokenCount = allTokens.get(Colour.GOLD);
    //int goldTokenNeededToPay = 0;
    int goldTokensFromCard = 2 * goldCardCount;
    int totalGoldCount = goldTokensFromCard + goldTokenCount;
    EnumMap<Colour, Integer> tokensToPay = new EnumMap<>(cardPrice);

    int[] goldTokenArr = new int[totalGoldCount];
    if (hasDoubleGoldPower) {
      // this len = goldTokenCount array [2,2,2,2..] is used to consider the double gold
      // whenever in the diff price map we need a gold token to make up the price diff,
      // we take an entry out of the array, make it to zero and add it to the diff price map
      Arrays.fill(goldTokenArr, 2);
    } else {
      Arrays.fill(goldTokenArr, 1);
    }
    int i = 0;
    for (Colour colour : diffPrice.keySet()) {
      // excluding gold token in here since we only consider gold token card
      if (colour != Colour.GOLD) {
        while (diffPrice.get(colour) < 0 && i < goldTokenArr.length) {
          int newValue = diffPrice.get(colour) + goldTokenArr[i];
          i += 1;
          int oldValue = tokensToPay.get(colour);
          tokensToPay.put(colour,  oldValue - goldTokenArr[i-1]);
          diffPrice.put(colour, newValue);
        }
      }
    }

    //now i is the number of gold tokens use, need to find the best configuration
    int goldValueNeeded = i;
    int goldCardUsed = 0;
    int goldTokenUsed = 0;

    while (goldValueNeeded > 0 && (goldTokenCount>0 || goldCardCount>0)) {
      if (goldCardCount > 0 && goldValueNeeded > 2){
        goldValueNeeded -= 2;
        goldCardCount --;
        goldCardUsed ++;
      } else if (goldTokenCount>0) {
        goldTokenCount--;
        goldValueNeeded--;
        goldTokenUsed ++;
      } else {
        goldValueNeeded -= 2;
        goldCardCount--;
        goldCardUsed ++;
      }
    }

    //if gold token used is still >0 card cannot be bought
    if (goldValueNeeded>0){
      return null;
    }
    //if its less than zero than we have a spare gold token from gold card to assign
    if (goldValueNeeded < 0) {
      int newIndex = goldTokenArr[0];
      for (Colour colour : tokensToPay.keySet()) {
        int oldTokensToPay = tokensToPay.get(colour);
        if (oldTokensToPay > 0) {
          tokensToPay.put(colour, oldTokensToPay - newIndex);
        }
      }
    }

    //now just assign  gold tokens and card used and return
    tokensToPay.put(Colour.GOLD , goldTokenUsed);
    tokensToPay.put(Colour.ORIENT , goldCardUsed);
    return tokensToPay;





    ///endddd
//
//    if (goldTokenArr.length - i > 0) {
//      int newIndex = goldTokenArr.length - i;
//      for (Colour colour : tokensToPay.keySet()) {
//        int oldTokensToPay = tokensToPay.get(colour);
//        if (oldTokensToPay > 0) {
//          //tokensToPay.put(colour, oldTokensToPay - );
//
//        }
//      }
//    }
//
//
//    if (diffPrice.values().stream().anyMatch(v -> v < 0)) {
//      int goldTokensInHand = allTokens.get(Colour.GOLD);
//      if (goldTokensInHand > 0) {
//        goldTokenNeededToPay += computeGoldTokensNeeded(
//            goldTokensInHand,
//            hasDoubleGoldPower,
//            diffPrice);
//      }
//    }
//
//
//
//    //////// end of julias implementation
//
//
//    // prioritizing using gold card to pay off the difference
//    if (goldCardCount > 0) {
//      int[] goldTokenArr = new int[goldTokensFromCard];
//      if (hasDoubleGoldPower) {
//        // this len = goldTokenCount array [2,2,2,2..] is used to consider the double gold
//        // whenever in the diff price map we need a gold token to make up the price diff,
//        // we take an entry out of the array, make it to zero and add it to the diff price map
//        Arrays.fill(goldTokenArr, 2);
//      } else {
//        Arrays.fill(goldTokenArr, 1);
//      }
//      int i = 0;
//      for (Colour colour : diffPrice.keySet()) {
//        // excluding gold token in here since we only consider gold token card
//        if (colour != Colour.GOLD) {
//          while (diffPrice.get(colour) < 0 && i < goldTokenArr.length) {
//            int newValue = diffPrice.get(colour) + goldTokenArr[i];
//            i += 1;
//            int oldValue = tokensToPay.get(colour);
//            tokensToPay.put(colour,  oldValue - 1);
//            diffPrice.put(colour, newValue);
//          }
//        }
//      }
//
//      if (goldTokenArr.length - i > 0) {
//        int newIndex = goldTokenArr.length - i;
//        for (Colour colour : tokensToPay.keySet()) {
//          int oldTokensToPay = tokensToPay.get(colour);
//          if (oldTokensToPay > 0) {
//            //tokensToPay.put(colour, oldTokensToPay - );
//
//          }
//        }
//      }
//
//      //while (goldCardLeft > 0) {
//      //  for (Colour colour : diffPrice.keySet()) {
//      //    // excluding gold token in here since we only consider gold token card
//      //    if (colour != Colour.GOLD) {
//      //      while (diffPrice.get(colour) < 0) {
//      //        int newValue = diffPrice.get(colour) + goldTokenArr[i];
//      //        i += 1;
//      //        int oldValue = tokensToPay.get(colour);
//      //        tokensToPay.put(colour,  oldValue - 1);
//      //        if (i >= goldTokenArr.length) {
//      //          break;
//      //        }
//      //        diffPrice.put(colour, newValue);
//      //
//      //        // given i is a legal index value
//      //        // if i is odd -> we just used the first token of the card
//      //        //    -> it is possible to keep using another token of this colour if all other price
//      //        //      are non-negative
//      //        //
//      //        // if i is even -> we just used the second (last) token of the card
//      //        if (i % 2 == 1) {
//      //          if (diffPrice.values().stream().allMatch(v -> v >= 0)) {
//      //            continue;
//      //          }
//      //
//      //        }
//      //
//      //        //if (newValue == 0) {
//      //        //  // the new value becomes 0, it is only necessary to add more
//      //        //  // if we just used the first gold token of the card -> means this
//      //        //  // current i-value should be odd
//      //        //  // besides that, we must have all other values of diffPrice being non-negative
//      //        //  // otherwise we do not have the money to save for this particular colour
//      //        //  if (i % 2 == 1 && diffPrice.values().stream().noneMatch(v -> v < 0)) {
//      //        //    newValue = diffPrice.get(colour) + goldTokenArr[i];
//      //        //    i += 1;
//      //        //  }
//      //        //  // if any of the two condition failed above, we shall not consider saving
//      //        //  // token for this colour
//      //        //  diffPrice.put(colour, newValue);
//      //        //  goldCardLeft -= 1;
//      //        //  goldTokenNeededToPay += 2;
//      //        //}
//      //      }
//      //    }
//      //  }
//      //  if (i >= goldTokenArr.length) {
//      //    break;
//      //  }
//      //}
//
//
//      // after this computation, it is possible that we still need the help
//      // from gold tokens (still have some negative number), then we need to
//      // consider the
//      if (diffPrice.values().stream().anyMatch(v -> v < 0)) {
//        int goldTokensInHand = allTokens.get(Colour.GOLD);
//        if (goldTokensInHand > 0) {
//          goldTokenNeededToPay += computeGoldTokensNeeded(
//              goldTokensInHand,
//              hasDoubleGoldPower,
//              diffPrice);
//        }
//      }
//
//    } else {
//      // we do not have any gold card, then do a regular check with gold tokens in hand
//      int goldTokensInHand = allTokens.get(Colour.GOLD);
//      if (goldTokensInHand > 0) {
//        goldTokenNeededToPay += computeGoldTokensNeeded(
//            goldTokensInHand,
//            hasDoubleGoldPower,
//            diffPrice);
//      }
//    }
//
//    EnumMap<Colour, Integer> finalResult = SplendorDevHelper.getInstance().getRawTokenColoursMap();
//
//    // store gold tokens needed in here
//    allTokens = new EnumMap<>(curPlayerInfo.getTokenHand().getAllTokens());
//    allGems = new EnumMap<>(curPlayerInfo.getTotalGems());
//    finalResult.put(Colour.GOLD, goldTokenNeededToPay);
//    for (Colour colour : allTokens.keySet()) {
//      if (colour != Colour.GOLD) {
//        int tokensOfColourHas = allTokens.get(colour);
//        int gemsOfColourHas = allGems.get(colour);
//        int tokensOfColourLeft = diffPrice.get(colour);
//        if (tokensOfColourLeft < 0) {
//          finalResult.put(colour, tokensOfColourLeft);
//        } else {
//          if (gemsOfColourHas - cardPrice.get(colour) >= 0) {
//            // if we have enough gem, player does not need to pay anything for this colour
//            finalResult.put(colour, 0);
//          } else {
//            // otherwise, some tokens need to be considered
//            if (tokensOfColourLeft == 0) {
//              // we have just the right amount to pay off, thus all tokens will be used
//              finalResult.put(colour, tokensOfColourHas);
//            } else {
//              // tokensOfColourLeft > 0, which means that we have some tokens left
//              finalResult.put(colour, tokensOfColourHas - tokensOfColourLeft);
//            }
//
//          }
//        }
//      }
//    }
//
//    return finalResult;

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
