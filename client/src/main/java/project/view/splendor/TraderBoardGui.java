package project.view.splendor;
import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.PurchaseAction;
import ca.mcgill.comp361.splendormodel.actions.ReserveAction;
import ca.mcgill.comp361.splendormodel.actions.TakeTokenAction;
import ca.mcgill.comp361.splendormodel.model.*;

import java.util.*;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import project.App;
import project.GameBoardLayoutConfig;
import project.view.InvalidDataException;

public class TraderBoardGui implements BoardGui{


  private final long gameId;
  private final AnchorPane playerBoardAnchorPane;

  private final VBox powerBoardGui;

  public TraderBoardGui(AnchorPane playerBoardAnchorPane, long gameId) {
    this.playerBoardAnchorPane = playerBoardAnchorPane;
    this.gameId = gameId;
    this.powerBoardGui = new PowerBoardGui(200,500);
  }

  @Override
  public void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    TraderBoard traderBoard = (TraderBoard) tableTop.getBoard(Extension.TRADING_POST);



  }

  @Override
  public void clearContent() {

  }

}
