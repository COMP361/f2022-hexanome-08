package project.view.splendor;

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Creates the GUI for the Noble Board.
 */
public class NobleBoardGui extends VBox implements NumOfPlayerDependentGui {

  private final double nobleImageWidth;
  private final double nobleImageHeight;
  private final double nobleImageSpace;

  /**
   * Constructor for the Noble Board.
   *
   * @param nobleImageWidth provides the width of the image.
   * @param nobleImageHeight provides the height of the image.
   * @param nobleImageSpace TODO.
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

  @Override
  public void setup(int numOfPlayers, double layoutX, double layoutY) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);
    setSpacing(nobleImageSpace);

    // GUI class dependent things to setup
    List<ImageView> testImages = new ArrayList<>();
    for (int i = 1; i <= numOfPlayers + 1; i++) {
      // TODO: Should be randomly selected, do it later
      Image img = new Image(String.format("project/pictures/noble/noble%d.png", i));
      ImageView imgv = new ImageView(img);
      imgv.setFitWidth(nobleImageWidth);
      imgv.setFitHeight(nobleImageHeight);
      testImages.add(imgv);
    }
    for (ImageView img : testImages) {
      // no function assigned to ImageView in here!! (player can not click on Nobles)
      getChildren().add(img);
    }

  }
}
