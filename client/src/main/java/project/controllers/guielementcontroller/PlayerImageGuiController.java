package project.controllers.guielementcontroller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.App;

public class PlayerImageGuiController implements Initializable {

  @FXML
  private Text maxPointsText;

  @FXML
  private Text currentPointsText;

  @FXML
  private Text reservedNobleCountText;

  @FXML
  private Text reservedCardsCountText;

  @FXML
  private Rectangle highlightRectangle;

  @FXML
  private ImageView armImageView;

  @FXML
  private ImageView playerImageView;

  private final String playerName;

  private final int armCode;

  public PlayerImageGuiController(String playerName, int armCode) {
    this.playerName = playerName;
    this.armCode = armCode;
  }

  public Text getCurrentPointsText() {
    return currentPointsText;
  }

  public Text getReservedNobleCountText() {
    return reservedNobleCountText;
  }

  public Text getReservedCardsCountText() {
    return reservedCardsCountText;
  }

  public Rectangle getHighlightRectangle() {
    return highlightRectangle;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    if (armCode > 0) {
      armImageView.setImage(new Image(App.getArmPath(armCode)));
    }
    playerImageView.setImage(App.getPlayerImage(playerName));
  }
}
