package project.view.splendor;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import project.view.splendor.communication.NobleCard;

/**
 * Creates the GUI for the Noble Board.
 */
public class NobleBoardGui extends VBox {

  private final double nobleImageWidth;
  private final double nobleImageHeight;
  private final double nobleImageSpace;

  /**
   * Constructor for the Noble Board.
   *
   * @param nobleImageWidth  provides the width of the image.
   * @param nobleImageHeight provides the height of the image.
   * @param nobleImageSpace  TODO.
   */
  public NobleBoardGui(double nobleImageWidth, double nobleImageHeight, double nobleImageSpace) {
    this.nobleImageHeight = nobleImageHeight;
    this.nobleImageWidth = nobleImageWidth;
    this.nobleImageSpace = nobleImageSpace;
  }

  public double getNobleImageWidth() {
    return nobleImageWidth;
  }

  public double getNobleImageHeight() {
    return nobleImageHeight;
  }

  public double getNobleImageSpace() {
    return nobleImageSpace;
  }

  public void setup(List<NobleCard> allNobles, double layoutX, double layoutY, boolean firstSetup) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);
    setSpacing(nobleImageSpace);
    // GUI class dependent things to setup
    if (!firstSetup) {
      this.getChildren().removeAll();
    }
    for (NobleCard noble : allNobles) {
      String nobleName = noble.getCardName();
      Image img = new Image(String.format("project/pictures/noble/%s.png", nobleName));
      ImageView imgv = new ImageView(img);
      imgv.setFitWidth(nobleImageWidth);
      imgv.setFitHeight(nobleImageHeight);
      this.getChildren().add(imgv);
    }

  }
}
