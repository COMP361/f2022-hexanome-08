package project.view.splendor.communication;

import java.util.EnumMap;

public class DevelopmentCard extends Card {

  private int level;


  // TODO: Figure it a way for the Orient card that has no colour
  private Colour gemColour;
  private boolean isPaired;
  private String pairedCardId;
  private int gemNumber;



  public DevelopmentCard(int paramPrestigePoints,
      EnumMap<Colour, Integer> paramPrice, String paramCardName,
                         int level, Colour gemColour, boolean isPaired, String pairedCardId, int gemNumber) {
    super(paramPrestigePoints, paramPrice, paramCardName);
    this.level = level;
    this.gemColour = gemColour;
    this.isPaired = isPaired;
    this.pairedCardId = pairedCardId;
    this.gemNumber=gemNumber;
  }

  public int getLevel() {
    return level;
  }

  /**
   *
   * @return Optional type of Colour.
   */
  //public Optional<Colour> getGemColour() { return gemColour; }

  public Colour getGemColour() { return gemColour; }
  public Boolean isPaired() {
    return isPaired;
  }

  public String getPairedCardId() {
    return pairedCardId;
  }

  public int getGemNumber() { return gemNumber; }

  public void setPaired(boolean paramPaired) {
    isPaired = paramPaired;
  }

  public void setPairedCardId(String paramPairedCardId) {
    pairedCardId = paramPairedCardId;
  }

}
