package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

/**
 * This class represents the SuperClass of all Development Cards.
 */
public class DevelopmentCard extends Card {

  private int level;


  // TODO: Figure it a way for the Orient card that has no colour
  private Colour gemColour;
  private boolean isPaired;
  private String pairedCardId;
  private int gemNumber;

  /**
   * prestige points, price, name, level, colour, isPaired, pairID, gem number.
   */
  public DevelopmentCard(int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice,
                         String paramCardName, int level, Colour gemColour, boolean isPaired,
                         String pairedCardId, int gemNumber) {
    super(paramPrestigePoints, paramPrice, paramCardName);
    this.level = level;
    this.gemColour = gemColour;
    this.isPaired = isPaired;
    this.pairedCardId = pairedCardId;
    this.gemNumber = gemNumber;
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

  public String getPairedCardId() {
    return pairedCardId;
  }

  public void setPairedCardId(String paramPairedCardId) {
    pairedCardId = paramPairedCardId;
  }

  public int getGemNumber() {
    return gemNumber;
  }

  public void setPaired(boolean paramPaired) {
    isPaired = paramPaired;
  }

}
