package project;

import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameController {

  @FXML
  SubScene inventoryScene;

  /**
   * Opening the development cards pop up once "My Cards" button is pressed.
   */

  public void openMyCards() {
    Stage newStage = new Stage();
    newStage.setTitle("My Development Cards");
    newStage.setScene(App.getHandCard());
    newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    newStage.show();
  }


}
