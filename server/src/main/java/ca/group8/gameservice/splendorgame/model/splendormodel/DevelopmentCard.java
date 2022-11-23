
package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class DevelopmentCard extends Card {

  public int level;
  public Optional<Colour> gemColor;
  public boolean isPaired = false;
  public int pairedCardId = -1;

  public DevelopmentCard(String paramCardId, int paramPrestigePoints,
      EnumMap<Colour, Integer> paramPrice, int paramLevel, Optional<Colour> paramGemColour) {

    super(paramCardId, paramPrestigePoints, paramPrice);
    level = paramLevel;
    if(paramGemColour.isPresent()){
      gemColor = paramGemColour;
    }
  }
}
