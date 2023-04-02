package project.view.splendor.boardgui;

import ca.mcgill.comp361.splendormodel.model.CityCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import project.App;

/**
 * It shows the CitiesGui display.
 */
public class CitiesGui extends VBox {

  private final double cityWidth;
  private final double cityHeight;
  private final double citySpace;

  /**
   * Constructor for the city board.
   *
   * @param cityWidth  provide the width of the city image
   * @param cityHeight provide the height of the city image
   */
  public CitiesGui(double cityWidth, double cityHeight, double citySpace) {
    this.cityWidth = cityWidth;
    this.cityHeight = cityHeight;
    this.citySpace = citySpace;
  }

  public double getCityWidth() {
    return cityWidth;
  }

  public double getCityHeight() {
    return cityHeight;
  }

  public double getCitySpace() {
    return citySpace;
  }


  /**
   * Set up the CityBoard GUI object.
   *
   * @param allCities a list of city cards
   * @param layoutX   layout X
   * @param layoutY   layout Y
   */
  public void setup(CityCard[] allCities, double layoutX, double layoutY) {
    this.getChildren().clear();
    setLayoutX(layoutX);
    setLayoutY(layoutY);
    setSpacing(citySpace);
    for (CityCard city : allCities) {
      if (city != null) {
        Image img = new Image(App.getCityPath(city));
        ImageView imgv = new ImageView(img);
        imgv.setFitWidth(cityWidth);
        imgv.setFitHeight(cityHeight);
        this.getChildren().add(imgv);
      }
    }
  }
}
