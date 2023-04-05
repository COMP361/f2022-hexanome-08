package project.view.splendor.playergui;


import ca.mcgill.comp361.splendormodel.model.Colour;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import project.App;

/**
 * TODO.
 */
public class PlayerInfoGui {
  private final PlayerPosition playerPosition;
  private final String playerName;
  private final int armCode;
  private final Pane container;
  private final Map<Colour, ColourWealthGui> colourWealthGuiMap = new HashMap<>();

  private final PlayerImageGui playerImageGui;

  private final long gameId;

  /**
   * Construct new visual display of player.
   *
   * @param playerPosition the position the player will be on the client's display.
   * @param playerName     represents the player's name as a String.
   */
  public PlayerInfoGui(long gameId, PlayerPosition playerPosition, String playerName, int armCode) {
    this.gameId = gameId;
    this.playerPosition = playerPosition;
    this.playerName = playerName;
    this.armCode = armCode;
    this.playerImageGui = new PlayerImageGui(gameId, playerName, armCode);
    if (playerPosition == PlayerPosition.LEFT || playerPosition == PlayerPosition.RIGHT) {
      container = new VBox();
    } else {
      container = new HBox();
    }
  }

  public PlayerPosition getPlayerPosition() {
    return playerPosition;
  }

  public String getPlayerName() {
    return playerName;
  }

  public Pane getContainer() {
    return container;
  }

  public void setNewPrestigePoints(int newPoints) {
    playerImageGui.setCurrentPointsText(newPoints);
  }

  /**
   * restore the token in hand for the player.
   *
   * @param newTokens newTokens
   */
  public void setNewTokenInHand(EnumMap<Colour, Integer> newTokens) {
    for (Colour colour : newTokens.keySet()) {
      ColourWealthGui colourWealthGui = colourWealthGuiMap.get(colour);
      int newTokenCount = newTokens.get(colour);
      colourWealthGui.setTokenCountText(newTokenCount);
    }
  }

  /**
   * setGemsInHand.
   *
   * @param gemsInHand gemsInHand
   */
  public void setGemsInHand(EnumMap<Colour, Integer> gemsInHand) {
    for (Colour colour : gemsInHand.keySet()) {
      ColourWealthGui colourWealthGui = colourWealthGuiMap.get(colour);
      int newGemCount = gemsInHand.get(colour);
      colourWealthGui.setGemCountText(newGemCount);
    }
  }

  public void setHighlight(boolean highlightChoice) {
    playerImageGui.setHighlightRectangle(highlightChoice);
  }

  public void setReservedNobleCount(int reservedNobleCount) {
    playerImageGui.setReservedNobleCountText(reservedNobleCount);
  }

  public void setReservedCardCount(int reservedCardCount) {
    playerImageGui.setReservedCardsCountText(reservedCardCount);
  }

  /**
   * setup.
   *
   * @param layoutX layoutX
   * @param layoutY layoutY
   */
  public void setup(double layoutX, double layoutY) {
    // set the layout of the GUI
    container.setLayoutX(layoutX);
    container.setLayoutY(layoutY);

    // set up the colour wealth guis
    Colour[] allColours = App.getAllColours();
    for (Colour allColour : allColours) {
      ColourWealthGui colourWealthGui = new ColourWealthGui(allColour);
      colourWealthGuiMap.put(allColour, colourWealthGui);
      container.getChildren().add(colourWealthGui);
    }

    // based on position, decide whether to add the player image at last or first
    if (playerPosition == PlayerPosition.LEFT || playerPosition == PlayerPosition.BOTTOM) {
      container.getChildren().add(0, playerImageGui);
    } else {
      container.getChildren().add(playerImageGui);
    }
  }
}
