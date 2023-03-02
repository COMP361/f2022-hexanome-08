package project.view.splendor;

import java.util.List;

import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

  /**
   * Set up the NobleBoardGui with a list of NobleCard.
   *
   * @param allNobles a list of noble cards
   * @param layoutX layout x
   * @param layoutY layout y
   * @param firstSetup whether it's first setup or not
   */
  public void setup(List<NobleCard> allNobles, double layoutX, double layoutY, boolean firstSetup) {

    // GUI class dependent things to setup
    if (firstSetup) {
      // set the layout of the GUI
      setLayoutX(layoutX);
      setLayoutY(layoutY);
      setSpacing(nobleImageSpace);
    } else {
      this.getChildren().clear();
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
