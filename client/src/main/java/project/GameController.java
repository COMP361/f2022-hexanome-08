package project;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.view.splendor.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Game controller for game GUI.
 */
public class GameController {


  @FXML
  private AnchorPane playerBoardAnchorPane;
  /**
   * Opening the development cards pop up once "My Cards" button is pressed.
   */

  @FXML
  protected void onExitGameClick() throws IOException {
    App.setRoot("admin_lobby_page");
  }

  @FXML
  protected void openMyCards() {
    Stage newStage = new Stage();
    newStage.setTitle("My Development Cards");
    newStage.setScene(App.getHandCard());
    newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    newStage.show();
  }


  private List<String> sortPlayerNames(String curPlayerName, List<String> allPlayerNames) {
    while(!allPlayerNames.get(0).equals(curPlayerName)) {
      String tmpPlayerName = allPlayerNames.remove(0);
      allPlayerNames.add(tmpPlayerName);
    }
    return new ArrayList<>(allPlayerNames);
  }
  private Map<PlayerPosition, String> setPlayerToPosition(String curPlayerName, List<String> allPlayerNames) {
    Map<PlayerPosition, String> resultMap = new HashMap<>();
    List<String> orderedNames = sortPlayerNames(curPlayerName, allPlayerNames);
    for (int i = 0; i < orderedNames.size(); i++) {
      resultMap.put(PlayerPosition.values()[i], orderedNames.get(i));
    }
    return resultMap;
  }


  public void initialize() {

    GameBoardLayoutConfig config = App.getGuiLayouts();
    // TODO: change based on number of players, get the info from server later
    String firstPlayer = "A"; // needs to highlight the boarder of this player
    String curPlayer = "D";
    String[] allPlayerNames = new String[] {"C","A","D","B"};
    List<String> playerNames = new ArrayList<>(Arrays.asList(allPlayerNames));
    int curPlayerNum = playerNames.size();
    Map<PlayerPosition, String> sortedPositionPlayerNameMap =
        setPlayerToPosition(curPlayer, playerNames);

    // initialize noble area
    NobleBoardGui nobleBoard = new NobleBoardGui(100,100,5);
    Platform.runLater(() -> {
      nobleBoard.setup(curPlayerNum,config.getNobleLayoutX(), config.getNobleLayoutY());
      playerBoardAnchorPane.getChildren().add(nobleBoard);
    });

    // initialize token area
    TokenBankGui tokenBank = new TokenBankGui();
    Platform.runLater(() -> {
      tokenBank.setup(curPlayerNum,config.getTokenBankLayoutX(),config.getTokenBankLayoutY());
      playerBoardAnchorPane.getChildren().add(tokenBank);
    });

    // initialize player area
    Map<String, PlayerInfoGui> playerNameGuiMap = new HashMap<>();
    List<VerticalPlayerInfoGui> verticalPlayers = new ArrayList<>();
    List<HorizontalPlayerInfoGui> horizontalPlayers = new ArrayList<>();
    if (curPlayerNum >= 2) {
      String btmPlayerName = sortedPositionPlayerNameMap.get(PlayerPosition.BOTTOM);
      String leftPlayerName = sortedPositionPlayerNameMap.get(PlayerPosition.LEFT);
      HorizontalPlayerInfoGui btmPlayerGui =
          new HorizontalPlayerInfoGui(PlayerPosition.BOTTOM, btmPlayerName, 3);
      VerticalPlayerInfoGui leftPlayerGui =
          new VerticalPlayerInfoGui(PlayerPosition.LEFT, leftPlayerName, 3);
      btmPlayerGui.setup(config.getBtmPlayerLayoutX(),config.getBtmPlayerLayoutY());
      leftPlayerGui.setup(config.getLeftPlayerLayoutX(),config.getLeftPlayerLayoutY());
      horizontalPlayers.add(btmPlayerGui);
      verticalPlayers.add(leftPlayerGui);
      playerNameGuiMap.put(btmPlayerName, btmPlayerGui);
      playerNameGuiMap.put(leftPlayerName, leftPlayerGui);
      if (curPlayerNum >= 3) {
        String topPlayerName = sortedPositionPlayerNameMap.get(PlayerPosition.TOP);
        HorizontalPlayerInfoGui topPlayerGui =
            new HorizontalPlayerInfoGui(PlayerPosition.TOP, topPlayerName, 3);
        topPlayerGui.setup(config.getTopPlayerLayoutX(),config.getTopPlayerLayoutY());
        horizontalPlayers.add(topPlayerGui);
        playerNameGuiMap.put(topPlayerName, topPlayerGui);
        if (curPlayerNum == 4) {
          String rightPlayerName = sortedPositionPlayerNameMap.get(PlayerPosition.RIGHT);
          VerticalPlayerInfoGui rightPlayerGui =
              new VerticalPlayerInfoGui(PlayerPosition.RIGHT, rightPlayerName, 3);
          rightPlayerGui.setup(config.getRightPlayerLayoutX(),config.getRightPlayerLayoutY());
          verticalPlayers.add(rightPlayerGui);
          playerNameGuiMap.put(rightPlayerName, rightPlayerGui);
        }
      }
    }

    Platform.runLater(() -> {
      for (VerticalPlayerInfoGui vPlayer : verticalPlayers) {
        playerBoardAnchorPane.getChildren().add(vPlayer);
      }

      for (HorizontalPlayerInfoGui hPlayer : horizontalPlayers) {
        playerBoardAnchorPane.getChildren().add(hPlayer);
      }
    });


    // check how to highlight which player?
    String currentTurnPlayerName = "D";
    for (String name : playerNameGuiMap.keySet()) {
      if(name.equals(currentTurnPlayerName)) {
        playerNameGuiMap.get(name).setHighlight(true);
      }
    }
  }

}
