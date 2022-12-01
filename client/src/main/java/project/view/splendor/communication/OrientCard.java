package project.view.splendor.communication;

import java.util.EnumMap;
import project.view.splendor.Colour;

public class OrientCard extends DevelopmentCard {
  private final OrientType type;

  public OrientCard(int paramPrestigePoints,
                    EnumMap<Colour, Integer> paramPrice, String cardName, int level,
                    Colour gemColour, boolean isPaired, String pairedCardId, int gemNumber,
                    OrientType paramType) {
    super(paramPrestigePoints, paramPrice, cardName, level,
        gemColour, isPaired, pairedCardId, gemNumber);
    type = paramType;
  }

  public OrientType getType() {
    return type;
  }
}
