package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;
//import org.graalvm.compiler.nodes.calc.IntegerDivRemNode.Op;

public class DevelopmentCard extends Card {
  private int prestigePoints;
  private EnumMap<Colour,Integer> price;
  private int level;
  private Optional<Colour> gemColor;
  private boolean isPaired = false;
  private int pairedCardId = -1;
  private int gemNumber;



  public DevelopmentCard(int paramPrestigePoints,
      EnumMap<Colour, Integer> paramPrice, String cardName, int prestigePoints,
      EnumMap<Colour, Integer> price, int level,
      Optional<Colour> gemColor, boolean isPaired, int pairedCardId, int gemNumber) {
    super(paramPrestigePoints, paramPrice, cardName);

    this.prestigePoints = prestigePoints;
    this.price = price;
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


  public int getPrestigePoints() {
    return prestigePoints;
  }

  public EnumMap<Colour, Integer> getPrice() {
    return price;

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



}
