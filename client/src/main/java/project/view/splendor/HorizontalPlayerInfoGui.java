package project.view.splendor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import project.App;

public class HorizontalPlayerInfoGui extends HBox implements PlayerInfoGui{
  private final PlayerPosition playerPosition;
  private final String playerName;

  public HorizontalPlayerInfoGui(PlayerPosition playerPosition, String playerName) {
    this.playerPosition = playerPosition;
    this.playerName = playerName;
    // TODO: The fxml associated with this class, must be bind to controller = project.App
    FXMLLoader fxmlLoader;
    if (playerPosition.equals(PlayerPosition.TOP)) {
      fxmlLoader = new FXMLLoader(getClass().getResource("/project/player_info_h_top.fxml"));
    } else if (playerPosition.equals(PlayerPosition.BOTTOM)) {
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



  public PlayerPosition getPlayerPosition() {
    return playerPosition;
  }

  public String getPlayerName(){
    return playerName;
  }


  public Map<Colour, Map<PlayerVisibleInfo, Text>> getTopPlayerTokenHandInfo() {
    Map<Colour, Map<PlayerVisibleInfo, Text>> result = new HashMap<>();
    Colour[] colours = App.getAllColours();
    for (int i = 0; i< 5; i++) {
      Map<PlayerVisibleInfo, Text> info = new HashMap<>();
      Group curGroup  = (Group) this.getChildren().get(i);
      info.put(PlayerVisibleInfo.GEM, (Text) curGroup.getChildren().get(1));
      info.put(PlayerVisibleInfo.TOKEN, (Text) curGroup.getChildren().get(3));
      result.put(colours[i],info);
    }
    Map<PlayerVisibleInfo,Text> info = new HashMap<>();
    Group curGroup = (Group) this.getChildren().get(6);
    info.put(PlayerVisibleInfo.TOKEN,(Text) curGroup.getChildren().get(1));
    result.put(Colour.GOLD,info);
    return result;

  }

  @Override
  public void setup(double layoutX, double layoutY) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);

  }
}

