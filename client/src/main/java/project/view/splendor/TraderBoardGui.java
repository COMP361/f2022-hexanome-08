package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.Extension;
import ca.mcgill.comp361.splendormodel.model.Power;
import ca.mcgill.comp361.splendormodel.model.PowerEffect;
import ca.mcgill.comp361.splendormodel.model.TableTop;
import ca.mcgill.comp361.splendormodel.model.TraderBoard;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import project.App;

/**
 * Details the traderBoardGui.
 */
public class TraderBoardGui extends VBox implements BoardGui {


  private final long gameId;
  // the map that is used to look find the correct power GUI group, hardcoded
  private final Map<PowerEffect, Group> powerEffectGroupMap = new HashMap<>();

  private Map<String, Integer> nameToArmCodeMap;

  /**
   * Constructor.
   *
   * @param gameId gameId to associate to this traderBoard.
   * @param nameToArmCodeMap maps player names to arms/powers.
   */
  public TraderBoardGui(long gameId, Map<String, Integer> nameToArmCodeMap) {
    this.gameId = gameId;
    this.nameToArmCodeMap = nameToArmCodeMap;
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/project/trader_board.fxml"));

    fxmlLoader.setRoot(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void initialGuiActionSetup(TableTop tableTop, Map<String, Action> playerActionMap) {
    PowerEffect[] powerEffects = new PowerEffect[] {
        PowerEffect.EXTRA_TOKEN,
        PowerEffect.TWO_PLUS_ONE,
        PowerEffect.DOUBLE_GOLD,
        PowerEffect.FIVE_POINTS,
        PowerEffect.ARM_POINTS
    };
    // initialize the map reference, given a power effect, find the group that contains the power
    // easier to reference
    for (int i = 0; i < powerEffects.length; i++) {
      Group powerGroup = (Group) this.getChildren().get(i);
      powerEffectGroupMap.put(powerEffects[i], powerGroup);
    }

    TraderBoard traderBoard = (TraderBoard) tableTop.getBoard(Extension.TRADING_POST);
    Map<String, Map<PowerEffect, Power>> allPlayerPowers = traderBoard.getAllPlayerPowers();
    for (String name : nameToArmCodeMap.keySet()) {
      Map<PowerEffect, Power> playerPowerMap = allPlayerPowers.get(name);
      // add the offset the access the children imageview of group correctly
      int armImageViewIndex = nameToArmCodeMap.get(name) + 1;
      for (PowerEffect powerEffect : playerPowerMap.keySet()) {
        Power playerOnePower = playerPowerMap.get(powerEffect);
        Group powerGroup = powerEffectGroupMap.get(powerEffect);
        ImageView armImageView = (ImageView) powerGroup.getChildren().get(armImageViewIndex);
        Image armImage = new Image(App.getArmPath(nameToArmCodeMap.get(name)));
        if (playerOnePower.isUnlocked()) {
          // display the image view
          armImageView.setImage(armImage);
        }
        // otherwise, do nothing
      }
    }



  }

  @Override
  public void clearContent() {
    // clear all power image display in here
    // iterate through 2 -> 5 image views in each group in vbox, and set image of them to null
    for (int groupIndex = 0; groupIndex < 4; groupIndex++) {
      Group powerGroup = (Group) this.getChildren().get(groupIndex);
      for (int armImageViewIndex = 2; armImageViewIndex <= 5; armImageViewIndex++) {
        ImageView armImageView = (ImageView) powerGroup.getChildren().get(armImageViewIndex);
        // clean it
        armImageView.setImage(null);
      }
    }
  }

}
