package project.view.splendor;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.App;
import project.view.splendor.communication.DevelopmentCard;

/**
 * A class to visually represent a player in the game (vertical layout of player info).
 */
public class VerticalPlayerInfoGui extends VBox implements PlayerInfoGui {

  private final PlayerPosition playerPosition;
  private final String playerName;

  private final int initialTokenNum;

  /**
   * Construct new visual display of player.
   *
   * @param playerPosition  the position the player will be on the client's display.
   * @param playerName      represents the player's name as a String.
   * @param initialTokenNum TODO.
   */
  public VerticalPlayerInfoGui(PlayerPosition playerPosition, String playerName,
                               int initialTokenNum) {
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

  public String getPlayerName() {
    return playerName;
  }

  public int getInitialTokenNum() {
    return initialTokenNum;
  }

  public Map<Colour, Map<PlayerWealthInfo, Text>> getPlayerColourWealthMap(
      PlayerPosition playerPosition) {
    Map<Colour, Map<PlayerWealthInfo, Text>> resultMap = new HashMap<>();
    Colour[] colours = App.getBaseColours();
    ObservableList<Node> allChildren = this.getChildren();
    if (playerPosition.equals(PlayerPosition.LEFT)) {
      for (int i = 0; i < colours.length; i++) {
        Map<PlayerWealthInfo, Text> mapInMap = new HashMap<>();
        Group curGroup = (Group) allChildren.get(i + 1);
        mapInMap.put(PlayerWealthInfo.GEM, (Text) curGroup.getChildren().get(1));
        mapInMap.put(PlayerWealthInfo.TOKEN, (Text) curGroup.getChildren().get(3));
        resultMap.put(colours[i], mapInMap);
      }
      Group curGroup = (Group) allChildren.get(6);
      Map<PlayerWealthInfo, Text> goldMap = new HashMap<>();
      goldMap.put(PlayerWealthInfo.TOKEN, (Text) curGroup.getChildren().get(1));
      resultMap.put(Colour.GOLD, goldMap);
    } else if (playerPosition.equals(PlayerPosition.RIGHT)) {
      for (int i = 0; i < colours.length; i++) {
        Map<PlayerWealthInfo, Text> mapInMap = new HashMap<>();
        HBox curBox = (HBox) allChildren.get(i);
        Group curGroup = (Group) curBox.getChildren().get(1);
        mapInMap.put(PlayerWealthInfo.GEM, (Text) curGroup.getChildren().get(1));
        mapInMap.put(PlayerWealthInfo.TOKEN, (Text) curGroup.getChildren().get(3));
        resultMap.put(colours[i], mapInMap);
      }
      HBox curBox = (HBox) allChildren.get(5);
      Map<PlayerWealthInfo, Text> goldMap = new HashMap<>();
      Group curGroup = (Group) curBox.getChildren().get(1);
      goldMap.put(PlayerWealthInfo.TOKEN, (Text) curGroup.getChildren().get(1));
      resultMap.put(Colour.GOLD, goldMap);
    }
    return resultMap;
  }

  public Map<PlayerVisibleInfo, Text> getPlayerVisibleInfoMap(PlayerPosition playerPosition) {
    Map<PlayerVisibleInfo, Text> resultMap = new HashMap<>();
    ObservableList<Node> allChildren = this.getChildren();
    if (playerPosition.equals(PlayerPosition.LEFT)) {
      Group group = (Group) allChildren.get(0);
      resultMap.put(PlayerVisibleInfo.POINT, (Text) group.getChildren().get(5));
      resultMap.put(PlayerVisibleInfo.RESERVED_CARDS, (Text) group.getChildren().get(7));
      resultMap.put(PlayerVisibleInfo.RESERVED_NOBLES, (Text) group.getChildren().get(8));

    } else if (playerPosition.equals(PlayerPosition.RIGHT)) {
      Group group = (Group) allChildren.get(6);

      resultMap.put(PlayerVisibleInfo.POINT, (Text) group.getChildren().get(6));
      resultMap.put(PlayerVisibleInfo.RESERVED_CARDS, (Text) group.getChildren().get(8));
      resultMap.put(PlayerVisibleInfo.RESERVED_NOBLES, (Text) group.getChildren().get(7));
    }
    return resultMap;
  }

  @Override
  public void setNewPrestigePoints(int newPoints) {
    Map<PlayerVisibleInfo, Text> visibleInfoTextMap =
        this.getPlayerVisibleInfoMap(this.playerPosition);
    visibleInfoTextMap.get(PlayerVisibleInfo.POINT).setText(Integer.toString(newPoints));
  }

  @Override
  public void setNewTokenInHand(EnumMap<Colour, Integer> newTokens) {
    Map<Colour, Map<PlayerWealthInfo, Text>> wealthInfo =
        this.getPlayerColourWealthMap(this.playerPosition);
    for (Colour colour : Colour.values()) {
      Map<PlayerWealthInfo, Text> info = wealthInfo.get(colour);
      info.get(PlayerWealthInfo.TOKEN).setText(Integer.toString(newTokens.get(colour)));
    }
  }

  @Override
  public void setGemsInHand(List<DevelopmentCard> allDevCardsInHand) {
    EnumMap<Colour, Integer> totalGems = new EnumMap<>(Colour.class);
    for (Colour c : Colour.values()) {
      totalGems.put(c, 0);
    }
    for (DevelopmentCard card : allDevCardsInHand) {
      Colour colour = card.getGemColour();
      int oldValue = totalGems.get(colour);
      totalGems.put(colour, oldValue + card.getGemNumber());
    }
    Map<Colour, Map<PlayerWealthInfo, Text>> wealthInfo =
        this.getPlayerColourWealthMap(this.playerPosition);
    for (Colour colour : Colour.values()) {
      wealthInfo.get(colour).get(PlayerWealthInfo.GEM)
          .setText(Integer.toString(totalGems.get(colour)));
    }
  }

  @Override
  public void setHighlight(boolean highlightChoice) {
    if (highlightChoice && playerPosition.equals(PlayerPosition.RIGHT)) {
      Group groupPlayer = (Group) this.getChildren().get(6);
      Rectangle highlight = (Rectangle) groupPlayer.getChildren().get(0);
      highlight.setFill(Color.GREEN);
    } else if (highlightChoice && playerPosition.equals(PlayerPosition.LEFT)) {
      Group groupPlayer = (Group) this.getChildren().get(0);
      Rectangle highlight = (Rectangle) groupPlayer.getChildren().get(0);
      highlight.setFill(Color.GREEN);
    } else if (!highlightChoice && playerPosition.equals(PlayerPosition.RIGHT)) {
      Group groupPlayer = (Group) this.getChildren().get(6);
      Rectangle highlight = (Rectangle) groupPlayer.getChildren().get(0);
      highlight.setFill(Color.WHITE);
    } else if (!highlightChoice && playerPosition.equals(PlayerPosition.LEFT)) {
      Group groupPlayer = (Group) this.getChildren().get(0);
      Rectangle highlight = (Rectangle) groupPlayer.getChildren().get(0);
      highlight.setFill(Color.WHITE);
    }
  }


  private void giveInitialStartTokens() {
    Map<Colour, Map<PlayerWealthInfo, Text>> allTokenColourMap =
        getPlayerColourWealthMap(playerPosition);
    Colour[] allColours = App.getAllColours();
    for (Colour c : allColours) {
      Map<PlayerWealthInfo, Text> oneColourMap = allTokenColourMap.get(c);
      Text tokenText = oneColourMap.get(PlayerWealthInfo.TOKEN);
      tokenText.setText(initialTokenNum + "");
    }
  }

  @Override
  public void setup(double layoutX, double layoutY) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);
    giveInitialStartTokens();


  }
}
