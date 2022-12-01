package project;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ReservedHandController implements Initializable {

  @FXML
  private HBox reservedDevCardsHbox;

  @FXML
  private HBox reservedNoblesHbox;

  private final List<ImageView> playerCards;
  private final List<ImageView> playerNobles;

  public ReservedHandController(List<ImageView> playerCards, List<ImageView> playerNobles) {
    this.playerCards = playerCards;
    this.playerNobles = playerNobles;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    System.out.println("Loading reserved cards");

    // TESTING USE ONLY!!!!
    for (int i = 0; i < 3; i++) {
      ImageView imgV = (ImageView) reservedDevCardsHbox.getChildren().get(i);
      imgV.setImage(playerCards.get(0).getImage());
    }

    Image img2 = new Image("project/pictures/noble/noble1.png");
    for (int i = 0; i < 5; i++) {
      ImageView imgV = (ImageView) reservedNoblesHbox.getChildren().get(i);
      imgV.setImage(playerNobles.get(0).getImage());
    }
  }
}
