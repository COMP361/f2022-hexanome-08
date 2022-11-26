package project.view.splendor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import project.App;


public class VerticalPlayerInfoGui extends VBox implements PlayerInfoGui{

  private final PlayerPosition playerPosition;
  private final String playerName;

  private final int initialTokenNum;

  public VerticalPlayerInfoGui(PlayerPosition playerPosition, String playerName, int initialTokenNum) {
    this.playerPosition = playerPosition;
    this.playerName = playerName;
    this.initialTokenNum = initialTokenNum;
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
  public int getInitialTokenNum() {
    return initialTokenNum;
  }

  public Map<Colour,Map<PlayerTokenInfo, Text>> getPlayerColourWealthMap(PlayerPosition playerPosition){
    Map<Colour, Map<PlayerTokenInfo, Text>> resultMap = new HashMap<>();
    Colour[] colours = App.getBaseColours();
    ObservableList<Node> allChildren = this.getChildren();
    if (playerPosition.equals(PlayerPosition.LEFT)){
      for (int i = 0; i < colours.length; i++){
        Map<PlayerTokenInfo, Text> mapInMap = new HashMap<>();
        Group curGroup = (Group) allChildren.get(i+1);
        mapInMap.put(PlayerTokenInfo.GEM,(Text) curGroup.getChildren().get(2));
        mapInMap.put(PlayerTokenInfo.TOKEN,(Text) curGroup.getChildren().get(3));
        resultMap.put(colours[i], mapInMap);
      }
      Group curGroup = (Group) allChildren.get(6);
      Map<PlayerTokenInfo, Text> goldMap= new HashMap<>();
      goldMap.put(PlayerTokenInfo.TOKEN, (Text) curGroup.getChildren().get(1));
      resultMap.put(Colour.GOLD, goldMap);
    } else if (playerPosition.equals(PlayerPosition.RIGHT)) {
      for (int i = 0; i < colours.length; i++){
        Map<PlayerTokenInfo, Text> mapInMap = new HashMap<>();
        HBox curBox = (HBox) allChildren.get(i);
        Group curGroup = (Group) curBox.getChildren().get(1);
        mapInMap.put(PlayerTokenInfo.GEM, (Text) curGroup.getChildren().get(1));
        mapInMap.put(PlayerTokenInfo.TOKEN,(Text) curGroup.getChildren().get(3));
        resultMap.put(colours[i], mapInMap);
      }
      HBox curBox = (HBox) allChildren.get(5);
      Map<PlayerTokenInfo, Text> goldMap= new HashMap<>();
      Group curGroup = (Group) curBox.getChildren().get(1);
      goldMap.put(PlayerTokenInfo.TOKEN, (Text) curGroup.getChildren().get(1));
      resultMap.put(Colour.GOLD, goldMap);
    }
    return resultMap;
  }

  public Map<PlayerVisibleInfo, Text> getPlayerVisibleInfoMap(PlayerPosition playerPosition){
    Map<PlayerVisibleInfo, Text> resultMap = new HashMap<>();
    ObservableList<Node> allChildren = this.getChildren();
    if (playerPosition.equals(PlayerPosition.LEFT)){
      Group group = (Group) allChildren.get(0);
      resultMap.put(PlayerVisibleInfo.POINT, (Text) group.getChildren().get(5));
      resultMap.put(PlayerVisibleInfo.RESERVED_CARDS, (Text) group.getChildren().get(7));
      resultMap.put(PlayerVisibleInfo.RESERVED_NOBLES, (Text) group.getChildren().get(8));

    } else if (playerPosition.equals(PlayerPosition.RIGHT)){
      Group group = (Group) allChildren.get(6);

      resultMap.put(PlayerVisibleInfo.POINT, (Text) group.getChildren().get(6));
      resultMap.put(PlayerVisibleInfo.RESERVED_CARDS, (Text) group.getChildren().get(8));
      resultMap.put(PlayerVisibleInfo.RESERVED_NOBLES, (Text) group.getChildren().get(7));
    }
    return resultMap;
  }


  private void giveInitialStartTokens() {
    Map<Colour, Map<PlayerTokenInfo, Text>> allTokenColourMap = getPlayerColourWealthMap(playerPosition);
    Colour[] baseColours = App.getBaseColours();
    for (Colour c : baseColours) {
      Map<PlayerTokenInfo, Text> oneColourMap = allTokenColourMap.get(c);
      Text tokenText = oneColourMap.get(PlayerTokenInfo.TOKEN);
      tokenText.setText(initialTokenNum+"");
    }
    Text goldTokenText = allTokenColourMap.get(Colour.GOLD).get(PlayerTokenInfo.TOKEN);
    goldTokenText.setText(initialTokenNum+"");
  }

  @Override
  public void setup(double layoutX, double layoutY) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);
    giveInitialStartTokens();



  }
}
