
package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;
import org.graalvm.compiler.nodes.calc.IntegerDivRemNode.Op;

public class DevelopmentCard extends Card {
  private int cardId;
  private int prestigePoints;
  private EnumMap<Colour,Integer> price;
  private int level;
  private Optional<Colour> gemColor;
  private boolean isPaired = false;
  private int pairedCardId = -1;

  public DevelopmentCard(int paramPrestigePoints,
      EnumMap<Colour, Integer> paramPrice, int cardId, int prestigePoints,
      EnumMap<Colour, Integer> price, int level,
      Optional<Colour> gemColor, boolean isPaired, int pairedCardId) {
    super(paramPrestigePoints, paramPrice);

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
  }

  public int getCardId() {
    return cardId;
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

  public Optional<Colour> getGemColor() {
    return gemColor;
  }

  public boolean isPaired() {
    return false;
  }

  public int getPairedCardID() {
    return 0;
  }
}
