package project.view.splendor;

import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;

import java.util.EnumMap;
import java.util.List;

/**
 * TODO.
 */
public interface PlayerInfoGui {
  void setup(double layoutX, double layoutY);

  void setHighlight(boolean highlightChoice);

  void setNewPrestigePoints(int newPoints);

  void setNewTokenInHand(EnumMap<Colour, Integer> newTokens);

  void setGemsInHand(List<DevelopmentCard> allDevCardsInHand);
}
