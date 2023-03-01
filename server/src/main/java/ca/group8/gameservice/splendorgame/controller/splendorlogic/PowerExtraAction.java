package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TraderBoard;
import java.util.Map;

/**
 * This class represents the Extra Actions associated with a specific PowerEffect.
 */
public class PowerExtraAction extends Action {

  private final PowerEffect powerEffect;

  public PowerExtraAction(Card curCard, PowerEffect powerEffect) {
    assert curCard != null && powerEffect != null;
    super.type = this.getClass().getSimpleName();
    this.powerEffect = powerEffect;
  }

  public PowerEffect getPowerEffect() {
    return powerEffect;
  }

  @Override
    // TODO
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
               ActionGenerator actionListGenerator,
               ActionInterpreter actionInterpreter) {

    switch (powerEffect) {
      //TODO: no default case... added for checkstyle purposes
      default:
      case FIVE_POINTS:
        playerInGame.addPrestigePoints(5);
        break;
      case ARM_POINTS:
        int pointsToAdd = 0;
        TraderBoard traderBoard = (TraderBoard) curTableTop.getBoard(Extension.TRADING_POST);
        Map<PowerEffect, Power> playerPowers =
            traderBoard.getAllPlayerPowers().get(playerInGame.getName());
        //Loop through all of this Player's powers. If power is unlocked, increment count by 1.
        for (PowerEffect power : PowerEffect.values()) {
          if (playerPowers.get(power).isUnlocked()) {
            pointsToAdd += 1;
          }
        }
        playerInGame.addPrestigePoints(pointsToAdd);
        //TODO: Add cases for creating cascading actions for other 3 powers.
    }

  }

  @Override
  Card getCurCard() throws NullPointerException {
    throw new NullPointerException("There is no card associated with this action.");
  }

  @Override
    //TODO: Should this have a curCard position associated with it??
  Position getCardPosition() throws NullPointerException {
    throw new NullPointerException("There is no card position associated with this action.");
  }
}
