package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;

/**
 * This class represents the Claim Noble action.
 */
public class ClaimNobleAction extends Action {

  private DevelopmentCard curCard;
  private Position curPosition;

  /**
   * Constructor.
   *
   * @param DevelopmentCard     The noble DevelopmentCard (which can be claimed).
   * @param position The position of the noble DevelopmentCard.
   */
  public ClaimNobleAction(DevelopmentCard DevelopmentCard, Position position) {
    assert DevelopmentCard != null && curPosition != null;
    super.type = this.getClass().getSimpleName();
    curCard = DevelopmentCard;
    curPosition = position;
  }

  @Override
  void execute(TableTop curTableTop, PlayerInGame playerInGame,
               ActionGenerator actionListGenerator,
               ActionInterpreter actionInterpreter) {

  }

  @Override
  DevelopmentCard getCurCard() {
    assert curCard != null;
    return curCard;
  }

  @Override
  Position getCardPosition() {
    assert curPosition != null;
    return curPosition;
  }
}
