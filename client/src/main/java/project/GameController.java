package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    TokenBankGui tokenBank = new TokenBankGui();
    Platform.runLater(() -> {
      gameBoardAnchorPane.getChildren().add(tokenBank);
      tokenBank.setLayoutX(40);
      tokenBank.setLayoutY(5);
    });

    for (Text t :
        tokenBank.getColourTokenNumMap().values()) {
      System.out.println(t.getText());
    }

    for (Colour t :
        tokenBank.getColourTokenNumMap().keySet()) {
      System.out.println(t);
    }


  }


}
