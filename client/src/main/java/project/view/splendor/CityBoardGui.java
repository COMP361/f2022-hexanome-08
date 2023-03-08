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
import javafx.scene.shape.Rectangle;
import project.App;
import project.GameBoardLayoutConfig;

public class CityBoardGui implements BoardGui {

  private final AnchorPane playerBoardAnchorPane;

  private final long gameId;

  private final Rectangle coverRectangle;

  private final CitiesGui citiesGui;

  private final VBox cityCardBoard;

  public CityBoardGui(AnchorPane playerBoardAnchorPane, long gameId, Rectangle coverRectangle) {
    this.gameId = gameId;
    citiesGui = new CitiesGui(200, 100, 10);
    this.playerBoardAnchorPane = playerBoardAnchorPane;
    this.coverRectangle = coverRectangle;
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
