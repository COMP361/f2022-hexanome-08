package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.view.splendor.Colour;
import project.view.splendor.HorizontalPlayerInfoGui;
import project.view.splendor.PlayerPosition;
import project.view.splendor.TokenBankGui;
import project.view.splendor.VerticalPlayerInfoGui;

/**
 * Game controller for game GUI.
 */
public class GameController {


  @FXML
  private AnchorPane gameBoardAnchorPane;
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


  private void initializeNobleList(int curPlayerNum,
                                   double imgWidth, double imgHeight,
                                   double nobleListLayoutX, double nobleListLayoutY) {
    // TODO: Need to bind action on these Noble (ImageView) in the future so that clicking
    //  on them will send a REST request and a pop up
    List<ImageView> testImages = new ArrayList<>();
    for (int i = 1; i <= curPlayerNum+1; i++) {
      Image img = new Image(String.format("project/pictures/noble/noble%d.png",i));
      ImageView imgv = new ImageView(img);
      imgv.setFitWidth(imgWidth);
      imgv.setFitHeight(imgHeight);
      testImages.add(imgv);
    }

    Platform.runLater(() -> {
      VBox nobleList = new VBox();
      for (ImageView img : testImages) {
        nobleList.getChildren().add(img);
      }
      gameBoardAnchorPane.getChildren().add(nobleList);
      nobleList.setLayoutX(nobleListLayoutX);
      nobleList.setLayoutY(nobleListLayoutY);
    });
  }

  public void initialize() {
    // TODO: change based on number of players, get the info from server later
    int curPlayerNum = 2;
    int baseTokenCount = 0;
    if (curPlayerNum == 4) {
      baseTokenCount = 7;
    } else if (curPlayerNum == 3) {
      baseTokenCount = 5;
    } else if (curPlayerNum == 2){
      baseTokenCount = 4;
    }
    // initialize noble area
    initializeNobleList(curPlayerNum, 100, 100, 810, 50);

    //initialize token area
    Colour[] colours =  new Colour[] {
        Colour.RED, Colour.BLACK, Colour.WHITE, Colour.BLUE, Colour.GREEN,Colour.GOLD
    };
    Map<Colour, Integer> bankMap = new HashMap<>();
    for (Colour c : colours) {
      if(c.equals(Colour.GOLD)) {
        bankMap.put(c, 5);
      } else {
        bankMap.put(c,baseTokenCount);
      }
    }
    TokenBankGui tokenBank = new TokenBankGui();
    Platform.runLater(() -> {
      tokenBank.setup(bankMap,40,5);
      gameBoardAnchorPane.getChildren().add(tokenBank);
    });

    //initialize player area
    List<VerticalPlayerInfoGui> verticalPlayers = new ArrayList<>();
    List<HorizontalPlayerInfoGui> horizontalPlayers = new ArrayList<>();

    if (curPlayerNum >= 2) {
      HorizontalPlayerInfoGui curPlayer = new HorizontalPlayerInfoGui(PlayerPosition.BOTTOM);
      VerticalPlayerInfoGui leftPlayer = new VerticalPlayerInfoGui(PlayerPosition.LEFT);
      horizontalPlayers.add(curPlayer);
      verticalPlayers.add(leftPlayer);
      if (curPlayerNum >= 3) {
        HorizontalPlayerInfoGui topPlayer = new HorizontalPlayerInfoGui(PlayerPosition.TOP);
        horizontalPlayers.add(topPlayer);
        if (curPlayerNum == 4) {
          VerticalPlayerInfoGui rightPlayer = new VerticalPlayerInfoGui(PlayerPosition.RIGHT);
          verticalPlayers.add(rightPlayer);
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


  }


}
