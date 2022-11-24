package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.view.splendor.NobleListGui;

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


  public void initialize() {
    gameBoardAnchorPane = new AnchorPane();
    List<ImageView> testImages = new ArrayList<>();
    Image img = new Image("project/pictures/noble/noble1.png");
    ImageView imgv = new ImageView(img);
    testImages.add(imgv);
    Platform.runLater(() -> {
      NobleListGui allNobles = new NobleListGui(testImages);
      gameBoardAnchorPane.getChildren().add(allNobles);
    });
  }


}
