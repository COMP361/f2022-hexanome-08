package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

/**
 * This class represents development cards that are not orient.
 */
public class BaseCard extends DevelopmentCard {

  /**
   * Constructor.
   */
  public BaseCard(int paramPrestigePoints,
                  EnumMap<Colour, Integer> paramPrice, String cardName, int level,
                  Colour gemColour, boolean isPaired, String pairedCardId, int gemNumber) {
    super(paramPrestigePoints, paramPrice, cardName, level,
        gemColour, isPaired, pairedCardId, gemNumber);

  }

}
