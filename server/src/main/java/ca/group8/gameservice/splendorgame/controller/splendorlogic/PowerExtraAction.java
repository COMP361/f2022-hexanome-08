package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;

/**
 * This class represents the Extra Actions associated with a specific PowerEffect.
 */
public class PowerExtraAction extends Action {

  private final PowerEffect powerEffect;

  public PowerExtraAction(Card curCard, PowerEffect powerEffect) {
    assert curCard != null && powerEffect != null;
    this.powerEffect = powerEffect;
  }

  public PowerEffect getPowerEffect() {
    return powerEffect;
  }

  @Override
  //TODO
  void execute(TableTop curTableTop, PlayerInGame playerInGame,
               SplendorActionListGenerator actionListGenerator,
               SplendorActionInterpreter actionInterpreter) {

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
