package project.view.splendor;

import java.util.Map;
import javafx.scene.text.Text;

public interface PlayerInfoGui {
  void setup(double layoutX, double layoutY);
  Map<Colour, Map<PlayerTokenInfo, Text>> getPlayerColourWealthMap(PlayerPosition playerPosition);
  Map<PlayerVisibleInfo, Text> getPlayerVisibleInfoMap(PlayerPosition playerPosition);
}
