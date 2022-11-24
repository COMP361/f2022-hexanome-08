package project;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Game controller for game GUI.
 */
public class GameController {


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

  }


}
