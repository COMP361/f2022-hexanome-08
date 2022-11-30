package project.view.splendor;

import java.util.Map;
import javafx.scene.text.Text;

/**
 * TODO.
 */
public interface PlayerInfoGui {
  void setup(double layoutX, double layoutY);

  Map<Colour, Map<PlayerWealthInfo, Text>> getPlayerColourWealthMap(PlayerPosition playerPosition);

  Map<PlayerVisibleInfo, Text> getPlayerVisibleInfoMap(PlayerPosition playerPosition);

  void setHighlight(boolean highlightChoice);
}
