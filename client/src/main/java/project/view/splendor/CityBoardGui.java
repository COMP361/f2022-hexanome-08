package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.CityBoard;
import ca.mcgill.comp361.splendormodel.model.CityCard;
import ca.mcgill.comp361.splendormodel.model.Extension;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.App;
import project.config.GameBoardLayoutConfig;

/**
 * it displays the city board GUI.
 */
public class CityBoardGui implements BoardGui {

  private final AnchorPane playerBoardAnchorPane;

  private final long gameId;
  private final CitiesGui citiesGui;

  private final VBox cityCardBoard;

  /**
   * it shows how the City Board GUI functions.
   *
   * @param playerBoardAnchorPane playerBoardAnchorPane
   * @param gameId                gameId
   */
  public CityBoardGui(AnchorPane playerBoardAnchorPane, long gameId) {
    this.gameId = gameId;
    GameBoardLayoutConfig config = App.getGuiLayouts();
    citiesGui = new CitiesGui(config.getCityWidth(), config.getCityHeight(), config.getCitySpace());
    this.playerBoardAnchorPane = playerBoardAnchorPane;
    this.cityCardBoard = new VBox();
  }

  @Override
  public void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    CityBoard cityBoard = (CityBoard) tableTop.getBoard(Extension.CITY);
    CityCard[] cityCards = cityBoard.getAllCityCards();
    citiesGui.setup(cityCards, config.getPacBoardLayoutX(), config.getPacBoardLayoutY());
    cityCardBoard.getChildren().add(citiesGui);
    Platform.runLater(() -> {
      cityCardBoard.setLayoutX(config.getPacBoardLayoutX());
      cityCardBoard.setLayoutY(config.getPacBoardLayoutY());
      playerBoardAnchorPane.getChildren().add(cityCardBoard);
    });

  }


  @Override
  public void clearContent() {
    cityCardBoard.getChildren().clear();
  }

}
