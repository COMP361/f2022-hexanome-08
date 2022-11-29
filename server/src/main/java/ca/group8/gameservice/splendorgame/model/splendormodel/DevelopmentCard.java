package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;
//import org.graalvm.compiler.nodes.calc.IntegerDivRemNode.Op;

public class DevelopmentCard extends Card {

<<<<<<< HEAD
  private EnumMap<Colour,Integer> price;
=======
>>>>>>> 426b71b8fa406072bbe86730f93ee5c006d7d399
  private int level;
  private Optional<Colour> gemColour;
  private boolean isPaired;
  private String pairedCardId;
  private int gemNumber;



  public DevelopmentCard(int paramPrestigePoints,

      EnumMap<Colour, Integer> paramPrice, String paramCardName,
                         int level, Optional<Colour> gemColour, boolean isPaired, String pairedCardId, int gemNumber) {
    super(paramPrestigePoints, paramPrice, paramCardName);
    this.level = level;
    if(gemColour.isPresent()){
      this.gemColour = gemColour;
    }else{
      this.gemColour= Optional.empty();
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
  public Optional<Colour> getGemColour() { return gemColour; }

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
