package project.view.splendor.playergui;

import ca.mcgill.comp361.splendormodel.model.Colour;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import project.controllers.guielementcontroller.ColourWealthGuiController;

/**
 * ColourWealthGui.
 */
public class ColourWealthGui extends Group {

  private final Colour colour;

  private final ColourWealthGuiController controller;

  /**
   * ColourWealthGui.
   *
   * @param colour colour
   */
  public ColourWealthGui(Colour colour) {
    this.colour = colour;
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/project/colour_wealth.fxml"));
    controller = new ColourWealthGuiController(colour);
    fxmlLoader.setController(controller);
    fxmlLoader.setRoot(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setGemCountText(int newGemCount) {

    controller.getGemCountText().setText("" + newGemCount);
  }

  public void setTokenCountText(int newTokenCount) {

    controller.getTokenCountText().setText("" + newTokenCount);
  }

  public Colour getColour() {
    return colour;
  }
}
