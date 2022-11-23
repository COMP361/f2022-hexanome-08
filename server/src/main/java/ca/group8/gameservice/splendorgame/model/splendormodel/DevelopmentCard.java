package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class DevelopmentCard extends Card {

  private int level;
  private Optional<Colour> gemColor;
  private int gemNumber;
  private boolean isPaired = false;
  private int pairedCardId = -1;

  public DevelopmentCard(String paramCardId, int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice,
                         int paramLevel, Optional<Colour> paramGemColour, int gemNumber) {

    super(paramPrestigePoints, paramPrice);
    level = paramLevel;
    gemNumber = paramGemNumber;
    if(paramGemColour.isPresent()){
      gemColor = paramGemColour;
    }
    this.gemNumber=gemNumber;
  }

  public int getLevel() {
    return level;
  }

  /**
   *
   * @return Optional type of Colour.
   */
  public Optional<Colour> getGemColor() {
    return gemColor;
  }

  public Boolean isPaired() {
    return isPaired;
  }

  public int getPairedCardID() {
    return 0;
  }


  public int getGemNumber() {
    return gemNumber;
  }

  public int getLevel() {
    return level;
  }

  public Optional<Colour> getGemColor() {
    return gemColor;
  }

  public int getGemNumber() {
    return gemNumber;
  }

  public boolean isPaired() {
    return isPaired;
  }

  public int getPairedCardId() {
    return pairedCardId;
  }
}
