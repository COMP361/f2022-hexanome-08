package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;
//import org.graalvm.compiler.nodes.calc.IntegerDivRemNode.Op;

public class DevelopmentCard extends Card {

  private int level;
  private Optional<Colour> gemColor;
  private boolean isPaired;
  private String pairedCardId;
  private int gemNumber;



  public DevelopmentCard(int paramPrestigePoints,
      EnumMap<Colour, Integer> paramPrice, String paramCardName,
                         int level, Optional<Colour> gemColor, boolean isPaired, String pairedCardId, int gemNumber) {
    super(paramPrestigePoints, paramPrice, paramCardName);
    this.level = level;
    if(gemColor.isPresent()){
      this.gemColor = gemColor;
    }else{
      this.gemColor= Optional.empty();
    }
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
  public Optional<Colour> getGemColor() { return gemColor; }

  public Boolean isPaired() {
    return isPaired;
  }

  public int getPairedCardID() {
    return 0;
  }

  public int getGemNumber() { return gemNumber; }

  public void setPaired(boolean paramPaired) {
    isPaired = paramPaired;
  }

  public void setPairedCardId(String paramPairedCardId) {
    pairedCardId = paramPairedCardId;
  }
}
