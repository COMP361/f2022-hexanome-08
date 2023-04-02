package project.controllers.guielementcontroller;

import ca.mcgill.comp361.splendormodel.model.Colour;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.App;

public class ColourWealthGuiController implements Initializable {

  @FXML
  private Text gemCountText;

  @FXML
  private Text tokenCountText;

  @FXML
  private Text separationText;

  @FXML
  private Rectangle backgroundRectangle;

  private final Colour colour;

  public ColourWealthGuiController(Colour colour) {
    this.colour = colour;
  }


  public Text getGemCountText() {
    return gemCountText;
  }

  public Text getTokenCountText() {
    return tokenCountText;
  }



  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    String colourString = App.getColourStringMap().get(colour);
    backgroundRectangle.setFill(Color.web(colourString));
    if (colour == Colour.BLACK) {
      gemCountText.setFill(Color.WHITE);
      separationText.setFill(Color.WHITE);
      tokenCountText.setFill(Color.WHITE);
    }
  }
}
