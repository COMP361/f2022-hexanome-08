package project.view.splendor.communication;

import project.view.splendor.communication.CardEffect;
import project.view.splendor.communication.Colour;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class DevelopmentCard extends Card {
  private final int level;
  private int gemNumber;
  private boolean isPaired;
  private final Colour gemColour;
  private DevelopmentCard pairedCard;

  private final List<CardEffect> purchaseEffects;

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

  public int getGemNumber() {
    return gemNumber;
  }

  public void setGemNumber(int gemNumber) {
    this.gemNumber = gemNumber;
  }

  public boolean isPaired() {
    return isPaired;
  }

  public void setPaired(boolean paired) {
    isPaired = paired;
  }

  public Colour getGemColour() {
    return gemColour;
  }

  public DevelopmentCard getPairedCard() {
    return pairedCard;
  }

  public void setPairedCard(DevelopmentCard pairedCard) {
    this.pairedCard = pairedCard;
  }

  public List<CardEffect> getPurchaseEffects() {
    return purchaseEffects;
  }

}