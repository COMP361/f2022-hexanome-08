package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.HashMap;
import java.util.Map;

public class AvailableCards {
  private final String cardName;
  private final int level;
  private final int prestigePoint;
  private final Map<Color,Integer> price;
  private final boolean isOrient;
  private final boolean needsBurn;

  public AvailableCards(String cardName, int level, int prestigePoint,
                        boolean isOrient, boolean needsBurn) {
    this.cardName = cardName;
    this.level = level;
    this.prestigePoint = prestigePoint;
    this.price = new HashMap<>();
    this.isOrient = isOrient;
    this.needsBurn = needsBurn;
  }


  public String getCardName() {
    return cardName;
  }

  public int getLevel() {
    return level;
  }

  public int getPrestigePoint() {
    return prestigePoint;
  }

  public Map<Color, Integer> getPrice() {
    return price;
  }

  public boolean isOrient() {
    return isOrient;
  }

  public boolean isNeedsBurn() {
    return needsBurn;
  }




}
