package project.view.splendor;

import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.SplendorDevHelper;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.App;

/**
 * TODO.
 */
public class HorizontalPlayerInfoGui extends HBox implements PlayerInfoGui {
  private final PlayerPosition playerPosition;
  private final String playerName;
  private final int initialTokenNum;

  private final int armCode;

  /**
   * TODO.
   *
   * @param playerPosition  TODO.
   * @param playerName      Player name as a string.
   * @param initialTokenNum TODO.
   */
  public HorizontalPlayerInfoGui(PlayerPosition playerPosition, String playerName,
                                 int initialTokenNum, int armCode) {
    this.playerPosition = playerPosition;
    this.playerName = playerName;
    this.initialTokenNum = initialTokenNum;
    this.armCode = armCode;
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

  public String getPlayerName() {
    return playerName;
  }

  /**
   * Display the wealth map of the player.
   *
   * @param playerPosition playerPosition
   * @return return a map
   */
  public Map<Colour, Map<PlayerWealthInfo, Text>> getPlayerColourWealthMap(
      PlayerPosition playerPosition) {
    Map<Colour, Map<PlayerWealthInfo, Text>> result = new HashMap<>();
    Colour[] colours = App.getAllColours();
    if (playerPosition.equals(PlayerPosition.TOP)) {
      for (int i = 0; i < colours.length; i++) {
        Map<PlayerWealthInfo, Text> info = new HashMap<>();
        Group curGroup = (Group) this.getChildren().get(i);
        info.put(PlayerWealthInfo.GEM, (Text) curGroup.getChildren().get(1));
        info.put(PlayerWealthInfo.TOKEN, (Text) curGroup.getChildren().get(3));
        result.put(colours[i], info);
      }
    } else if (playerPosition.equals(PlayerPosition.BOTTOM)) {
      for (int i = 1; i < 7; i++) {
        Map<PlayerWealthInfo, Text> info = new HashMap<>();
        VBox currBox = (VBox) this.getChildren().get(i);
        Group curGroup = (Group) currBox.getChildren().get(1);
        info.put(PlayerWealthInfo.GEM, (Text) curGroup.getChildren().get(1));
        info.put(PlayerWealthInfo.TOKEN, (Text) curGroup.getChildren().get(3));
        result.put(colours[i - 1], info);
      }
    }

    return result;
  }

  /**
   * get the player info.
   *
   * @param playerPosition playerPosition
   * @return return the map
   */
  public Map<PlayerVisibleInfo, Text> getPlayerVisibleInfoMap(PlayerPosition playerPosition) {
    Map<PlayerVisibleInfo, Text> resultMap = new HashMap<>();
    if (playerPosition.equals(PlayerPosition.TOP)) {
      Group group = (Group) this.getChildren().get(6);
      resultMap.put(PlayerVisibleInfo.POINT, (Text) group.getChildren().get(7));
      resultMap.put(PlayerVisibleInfo.RESERVED_CARDS, (Text) group.getChildren().get(9));
      resultMap.put(PlayerVisibleInfo.RESERVED_NOBLES, (Text) group.getChildren().get(8));

    } else if (playerPosition.equals(PlayerPosition.BOTTOM)) {
      Group group = (Group) this.getChildren().get(0);
      resultMap.put(PlayerVisibleInfo.POINT, (Text) group.getChildren().get(7));
      resultMap.put(PlayerVisibleInfo.RESERVED_CARDS, (Text) group.getChildren().get(9));
      resultMap.put(PlayerVisibleInfo.RESERVED_NOBLES, (Text) group.getChildren().get(8));
    }
    return resultMap;
  }

  @Override
  public void setHighlight(boolean highlightChoice) {
    if (highlightChoice && playerPosition.equals(PlayerPosition.BOTTOM)) {
      Group groupPlayer = (Group) this.getChildren().get(0);
      Rectangle highlight = (Rectangle) groupPlayer.getChildren().get(0);
      highlight.setFill(Color.GREEN);
    } else if (highlightChoice && playerPosition.equals(PlayerPosition.TOP)) {
      Group groupPlayer = (Group) this.getChildren().get(6);
      Rectangle highlight = (Rectangle) groupPlayer.getChildren().get(0);
      highlight.setFill(Color.GREEN);
    } else if (!highlightChoice && playerPosition.equals(PlayerPosition.BOTTOM)) {
      Group groupPlayer = (Group) this.getChildren().get(0);
      Rectangle highlight = (Rectangle) groupPlayer.getChildren().get(0);
      highlight.setFill(Color.WHITE);
    } else if (!highlightChoice && playerPosition.equals(PlayerPosition.TOP)) {
      Group groupPlayer = (Group) this.getChildren().get(6);
      Rectangle highlight = (Rectangle) groupPlayer.getChildren().get(0);
      highlight.setFill(Color.WHITE);
    }
  }

  @Override
  public void setNewPrestigePoints(int newPoints) {
    Map<PlayerVisibleInfo, Text> visibleInfoTextMap = getPlayerVisibleInfoMap(playerPosition);
    visibleInfoTextMap.get(PlayerVisibleInfo.POINT).setText(Integer.toString(newPoints));
  }

  @Override
  public void setNewTokenInHand(EnumMap<Colour, Integer> newTokens) {
    Map<Colour, Map<PlayerWealthInfo, Text>> wealthInfo = getPlayerColourWealthMap(playerPosition);
    for (Colour colour : wealthInfo.keySet()) {
      Map<PlayerWealthInfo, Text> info = wealthInfo.get(colour);
      info.get(PlayerWealthInfo.TOKEN).setText(Integer.toString(newTokens.get(colour)));
    }
  }

  @Override
  public void setGemsInHand(EnumMap<Colour, Integer> gemsInHand) {
    // the map to GUI display
    Map<Colour, Map<PlayerWealthInfo, Text>> wealthInfo = getPlayerColourWealthMap(playerPosition);
    Set<Colour> regularColours = SplendorDevHelper.getInstance().getRawTokenColoursMap().keySet();
    for (Colour colour : regularColours) {
      Map<PlayerWealthInfo, Text> perColourWealth = wealthInfo.get(colour);
      perColourWealth
          .get(PlayerWealthInfo.GEM)
          .setText(Integer.toString(gemsInHand.get(colour)));
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

  private void setupPlayerImage() {
    int childrenCount = this.getChildren().size();
    if (playerPosition.equals(PlayerPosition.TOP)) {
      Group imageGroup = (Group) this.getChildren().get(childrenCount - 1);
      ImageView playerImageView = (ImageView) imageGroup.getChildren().get(1);
      playerImageView.setImage(App.getPlayerImage(playerName));
    } else if (playerPosition.equals(PlayerPosition.BOTTOM)) {
      Group imageGroup = (Group) this.getChildren().get(0);
      ImageView playerImageView = (ImageView) imageGroup.getChildren().get(1);
      playerImageView.setImage(App.getPlayerImage(playerName));
    }
  }

  private void setupArmImage(int armCode) {
    // only update the image if armCode > 0 -> we are playing trader extension
    if (armCode > 0) {
      int childrenCount = this.getChildren().size();
      Group imageGroup = null;
      if (playerPosition.equals(PlayerPosition.TOP)) {
        imageGroup = (Group) this.getChildren().get(childrenCount - 1);
      }

      if (playerPosition.equals(PlayerPosition.BOTTOM)) {
        imageGroup = (Group) this.getChildren().get(0);
      }
      String armPath = App.getArmPath(armCode);
      Image armImage = new Image(armPath);
      ImageView armImageView = (ImageView) imageGroup.getChildren().get(10);
      armImageView.setImage(armImage);
    }
  }

  @Override
  public void setup(double layoutX, double layoutY) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);
    giveInitialStartTokens();
    setupPlayerImage();
    setupArmImage(armCode);
  }
}