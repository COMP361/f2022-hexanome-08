package project.view.splendor.gameitems;

import java.util.EnumMap;
import java.util.Optional;
import project.view.splendor.Colour;

/**
 * Represents a Base Card.
 */
public class BaseCard extends DevelopmentCard {

  /**
   * Construct a new base card.
   *
   * @param paramPrestigePoints number of prestige points the card has.
   * @param paramPrice price of the card.
   * @param cardName Card name.
   * @param level Card level.
   * @param gemColor gemColour associated with the card.
   * @param isPaired boolean whether card has been paired.
   * @param pairedCardId ID of card that this has been paired to.
   * @param gemNumber Number of gems associated with card.
   */
  public BaseCard(int paramPrestigePoints,
                  EnumMap<Colour, Integer> paramPrice, String cardName, int level,
                  Optional<Colour> gemColor, boolean isPaired, int pairedCardId, int gemNumber) {
    super(paramPrestigePoints, paramPrice, cardName, level,
        gemColor, isPaired, pairedCardId, gemNumber);

  }

}
