package project.view.splendor.communication;

import java.util.EnumMap;
import project.view.splendor.Colour;

/**
 * Orient Card class.
 */
public class OrientCard extends DevelopmentCard {
  private final OrientType type;

  /**
   * Orient card constructor.
   *
   * @param paramPrestigePoints
   * @param paramPrice
   * @param cardName
   * @param level
   * @param gemColour
   * @param isPaired
   * @param pairedCardId
   * @param gemNumber
   * @param paramType
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
