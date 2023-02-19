package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

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

  public DevelopmentCard getPairedCard() throws SplendorGameException {
    if (!isPaired) {
      throw new SplendorGameException("Card is not paired yet");
    }
    return pairedCard;
  }

  public int getGemNumber() {
    return gemNumber;
  }

  public void setPaired(boolean paramPaired) {
    isPaired = paramPaired;
  }

  public List<CardEffect> getPurchaseEffects() {
    return purchaseEffects;
  }

  public void setPairedCard(DevelopmentCard pairedCard) throws SplendorGameException {
    if(pairedCard == null) {
      throw new SplendorGameException("Error: pairedCard argument is null.");
    }
    this.pairedCard = pairedCard;
  }

  public void incrementGemNumber() {
    gemNumber += 1;
  }


}
