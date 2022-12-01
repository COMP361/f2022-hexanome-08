package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

/**
 * Expansion Cards, have a type.
 */
public class OrientCard extends DevelopmentCard {
  private final OrientType type;

  /**
   * Constructor.
   */
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
