package project.view.splendor;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import org.w3c.dom.Node;

public class HorizontalPlayerInfoGui extends HBox{

  public HorizontalPlayerInfoGui(PlayerPosition playerPosition) {
    // TODO: The fxml associated with this class, must be bind to controller = project.App
    FXMLLoader fxmlLoader;
    if (playerPosition.equals(PlayerPosition.TOP)) {
      fxmlLoader = new FXMLLoader(getClass().getResource("/project/player_info_h_top.fxml"));
    } else if (playerPosition.equals(PlayerPosition.BOTTOM)) {
      // TODO: Needs to assign extra info for the bottom player (current player)
      fxmlLoader = new FXMLLoader(getClass().getResource("/project/player_info_h_bottom.fxml"));
    } else {
      // will throw exception if not LEFT OR RIGHT
      fxmlLoader = new FXMLLoader();
    }
    fxmlLoader.setRoot(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

