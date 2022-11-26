package project;

import java.io.IOException;
import java.util.ArrayList;
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


  public void initialize() {
    // TODO: change based on number of players, get the info from server later
    int curPlayerNum = 2;

    // initialize noble area
    NobleBoardGui nobleBoard = new NobleBoardGui(100,100,5);
    Platform.runLater(() -> {
      nobleBoard.setup(curPlayerNum,935, 280);
      playerBoardAnchorPane.getChildren().add(nobleBoard);
    });

    // initialize token area
    TokenBankGui tokenBank = new TokenBankGui();
    Platform.runLater(() -> {
      tokenBank.setup(curPlayerNum,190,170);
      playerBoardAnchorPane.getChildren().add(tokenBank);
    });

    // initialize player area
    List<VerticalPlayerInfoGui> verticalPlayers = new ArrayList<>();
//    List<HorizontalPlayerInfoGui> horizontalPlayers = new ArrayList<>();
    if (curPlayerNum >= 2) {
//      HorizontalPlayerInfoGui curPlayer = new HorizontalPlayerInfoGui(PlayerPosition.BOTTOM);
      VerticalPlayerInfoGui leftPlayer = new VerticalPlayerInfoGui(PlayerPosition.LEFT, "p1", 3);
//      curPlayer.setup(0,0);
      leftPlayer.setup(0,142.5);

//      horizontalPlayers.add(curPlayer);
      verticalPlayers.add(leftPlayer);
//      if (curPlayerNum >= 3) {
//        HorizontalPlayerInfoGui topPlayer = new HorizontalPlayerInfoGui(PlayerPosition.TOP);
//        horizontalPlayers.add(topPlayer);
//        if (curPlayerNum == 4) {
//          VerticalPlayerInfoGui rightPlayer = new VerticalPlayerInfoGui(PlayerPosition.RIGHT);
//          verticalPlayers.add(rightPlayer);
//        }
//      }
    }

    Platform.runLater(() -> {
      for (VerticalPlayerInfoGui vPlayer : verticalPlayers) {
        playerBoardAnchorPane.getChildren().add(vPlayer);
      }

//      for (HorizontalPlayerInfoGui hPlayer : horizontalPlayers) {
//        playerBoardAnchorPane.getChildren().add(hPlayer);
//      }
    });


  }


}
