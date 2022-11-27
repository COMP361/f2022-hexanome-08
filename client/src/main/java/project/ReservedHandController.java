package project;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ReservedHandController {

  @FXML
  private HBox reservedDevCardsHbox;

  @FXML
  private HBox reservedNoblesHbox;


  public void initialize() {
    // TODO: send the GET request under player/inventory here, not with the button
    //  not long polling!!!
    System.out.println("Loading reserved cards");
    Image img = new Image(String.format("project/pictures/level2/b1.png"));

    // TESTING USE ONLY!!!!
    for (int i = 0; i < 3; i++) {
      ImageView imgV = (ImageView) reservedDevCardsHbox.getChildren().get(i);
      imgV.setImage(img);
    }

    Image img2 = new Image(String.format("project/pictures/noble/noble1.png"));
    for (int i = 0; i < 5; i++) {
      ImageView imgV = (ImageView) reservedNoblesHbox.getChildren().get(i);
      imgV.setImage(img2);
    }

  }
}
