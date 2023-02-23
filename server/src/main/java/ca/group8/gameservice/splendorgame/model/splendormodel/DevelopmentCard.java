package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the SuperClass of all Development Cards.
 */
public class DevelopmentCard extends Card {

  private final int level;
  private int gemNumber;
  private boolean isPaired;
  private final Colour gemColour;
  private DevelopmentCard pairedCard;

  private final List<CardEffect> purchaseEffects;

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

  public int getGemNumber() {
    return gemNumber;
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
   *
   * @return whether this dev card is base card or not
   */
  public boolean isBaseCard() {
    return purchaseEffects.isEmpty();
  }

  /**
   * If a card has either Gold or Orient colour, then it's not a regular gem colour.
   *
   * @return whether a card has regular gem colour or not
   */
  public boolean hasRegularGemColour() {
    return !(gemColour.equals(Colour.GOLD) || gemColour.equals(Colour.ORIENT));
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if(!(obj instanceof DevelopmentCard)) {
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
