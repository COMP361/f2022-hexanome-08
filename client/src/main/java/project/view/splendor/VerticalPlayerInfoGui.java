package project.view.splendor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import project.App;

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

  public Map<Colour,Map<PlayerVisibleInfo, Text>> getLeftPlayerTokenHandInfo(){
    Map<Colour, Map<PlayerVisibleInfo, Text>> resultMap = new HashMap<>();
    Colour[] colours = App.getBaseColours();
    ObservableList<Node> allChildren = this.getChildren();
    for (int i = 0; i < colours.length; i++){
      Map<PlayerVisibleInfo, Text> mapInMap = new HashMap<>();
      Group curGroup = (Group) allChildren.get(i+1);
      mapInMap.put(PlayerVisibleInfo.GEM,(Text) curGroup.getChildren().get(2));
      mapInMap.put(PlayerVisibleInfo.TOKEN,(Text) curGroup.getChildren().get(3));
      resultMap.put(colours[i], mapInMap);
    }
    Group curGroup = (Group) allChildren.get(6);
    Map<PlayerVisibleInfo, Text> goldMap= new HashMap<>();
    goldMap.put(PlayerVisibleInfo.TOKEN, (Text) curGroup.getChildren().get(1));
    resultMap.put(Colour.GOLD, goldMap);
    return resultMap;
  }

  public Map<PlayerVisibleInfo, Text> getLeftPlayerInfo(){
    Map<PlayerVisibleInfo, Text> resultMap = new HashMap<>();
    ObservableList<Node> allChildren = this.getChildren();
    Group group = (Group) allChildren.get(0);

    resultMap.put(PlayerVisibleInfo.POINT, (Text) group.getChildren().get(5));
    resultMap.put(PlayerVisibleInfo.RESERVED_CARDS, (Text) group.getChildren().get(7));
    resultMap.put(PlayerVisibleInfo.RESERVED_NOBLES, (Text) group.getChildren().get(8));
    return resultMap;
  }


  @Override
  public void setup(double layoutX, double layoutY) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);




  }
}
