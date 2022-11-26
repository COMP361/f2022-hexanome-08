package project.view.splendor;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class VerticalPlayerInfoGui extends VBox implements PlayerInfoGui{

  private final PlayerPosition playerPosition;
  private final String playerName;

  public VerticalPlayerInfoGui(PlayerPosition playerPosition, String playerName) {
    this.playerPosition = playerPosition;
    this.playerName = playerName;
    // TODO: The fxml associated with this class, must be bind to controller = project.App
    FXMLLoader fxmlLoader;
    if (playerPosition.equals(PlayerPosition.LEFT)) {
      fxmlLoader = new FXMLLoader(getClass().getResource("/project/player_info_v_left.fxml"));
    } else if (playerPosition.equals(PlayerPosition.RIGHT)) {
      fxmlLoader = new FXMLLoader(getClass().getResource("/project/player_info_v_right.fxml"));
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

  public PlayerPosition getPlayerPosition() {
    return playerPosition;
  }

  public String getPlayerName(){
    return playerName;
  }

  @Override
  public void setup(double layoutX, double layoutY) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);




  }
}
