package project.view.splendor;
import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.view.InvalidDataException;

import java.util.Map;

public class TraderBoardGui implements BoardGui {


  private final long gameId;
  private final AnchorPane playerBoardAnchorPane;

  public TraderBoardGui(AnchorPane playerBoardAnchorPane, long gameId) {
    this.playerBoardAnchorPane = playerBoardAnchorPane;
    this.gameId = gameId;
  }

  @Override
  public void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap) {

  }

  @Override
  public void clearContent() {

  }

}
