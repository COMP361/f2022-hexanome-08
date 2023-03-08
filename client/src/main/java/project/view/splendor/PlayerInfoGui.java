package project.view.splendor;


import ca.mcgill.comp361.splendormodel.model.Colour;
import java.util.EnumMap;

/**
 * TODO.
 */
public interface PlayerInfoGui {
  void setup(double layoutX, double layoutY);

  void setHighlight(boolean highlightChoice);

  void setNewPrestigePoints(int newPoints);

  void setNewTokenInHand(EnumMap<Colour, Integer> newTokens);

  void setGemsInHand(EnumMap<Colour, Integer> gemsInHand);
}
