package project.view.splendor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javafx.scene.text.Text;
import project.view.splendor.communication.DevelopmentCard;

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
