package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;

/**
 * This class represents the Claim Noble action.
 */
public class ClaimNobleAction extends Action {

  private NobleCard curCard;
  private Position curPosition;

  /**
   * Constructor.
   *
   * @param nobleCard The noble DevelopmentCard (which can be claimed).
   * @param position  The position of the noble DevelopmentCard.
   */
  public ClaimNobleAction(NobleCard nobleCard, Position position) {
    assert nobleCard != null && curPosition != null;
    super.type = this.getClass().getSimpleName();
    curCard = nobleCard;
    curPosition = position;
  }

  @Override
  void execute(TableTop curTableTop, PlayerInGame playerInGame,
               ActionGenerator actionListGenerator,
               ActionInterpreter actionInterpreter) {

  }

  @Override
  public NobleCard getCurCard() {
    assert curCard != null;
    return curCard;
  }

  @Override
  public Position getCardPosition() {
    assert curPosition != null;
    return curPosition;
  }
}
