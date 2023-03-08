package project.view.splendor;


import javafx.scene.layout.VBox;

/**
 * Details the PowerBoardGUI.
 */
public class PowerBoardGui extends VBox {

  private final double powerBoardWidth;
  private final double powerBoardHeight;

  /**
   * Construct Power board.
   *
   * @param powerBoardWidth  provide width of the board
   * @param powerBoardHeight provide height of the board
   */
  public PowerBoardGui(double powerBoardWidth, double powerBoardHeight) {
    this.powerBoardWidth = powerBoardWidth;
    this.powerBoardHeight = powerBoardHeight;
  }

  public double getPowerBoardWidth() {
    return powerBoardWidth;
  }

  public double getPowerBoardHeight() {
    return powerBoardHeight;
  }

  public void setup() {

  }
}
