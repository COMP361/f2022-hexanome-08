package project.view.splendor;


import javafx.scene.layout.VBox;

public class PowerBoardGui extends VBox {

    private final double PowerBoardWidth;
    private final double PowerBoardHeight;

    /**
     * Construct Power board
     * @param powerBoardWidth provide width of the board
     * @param powerBoardHeight provide height of the board
     */
    public PowerBoardGui(double powerBoardWidth, double powerBoardHeight) {
        PowerBoardWidth = powerBoardWidth;
        PowerBoardHeight = powerBoardHeight;
    }

    public double getPowerBoardWidth() {
        return PowerBoardWidth;
    }

    public double getPowerBoardHeight() {
        return PowerBoardHeight;
    }

    public void setup() {

    }
}
