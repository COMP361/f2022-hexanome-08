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
import project.view.splendor.TokenBankGui;

/**
 * Game controller for game GUI.
 */
public class GameController {


  @FXML
  private AnchorPane gameBoardAnchorPane;
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
    for (int i = 1; i <= curPlayerNum; i++) {
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
    gameBoardAnchorPane.setMaxHeight(600);
    gameBoardAnchorPane.setMaxWidth(900);
    // initialize noble area
    initializeNobleList(5, 100, 100, 810, 50);

    //initialize token area

    Colour[] colours =  new Colour[] {
        Colour.RED, Colour.BLACK, Colour.WHITE, Colour.BLUE, Colour.GREEN,Colour.GOLD
    };
    // TODO: change based on number of players
    Map<Colour, Integer> bankMap = new HashMap<>();
    int playerCount = 3;
    int baseTokenCount = 0;
    if (playerCount == 4) {
      baseTokenCount = 7;
    } else if (playerCount == 3) {
      baseTokenCount = 5;
    } else if (playerCount == 2){
      baseTokenCount = 4;
    }
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

    //for (Text t :
    //    tokenBank.getColourTokenLeftMap().values()) {
    //  System.out.println(t.getText());
    //}
    //
    //for (Colour t :
    //    tokenBank.getColourTokenLeftMap().keySet()) {
    //  System.out.println(t);
    //}


  }


}
