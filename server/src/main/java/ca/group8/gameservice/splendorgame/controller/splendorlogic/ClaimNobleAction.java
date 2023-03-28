package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.ReservedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TraderBoard;
import java.util.HashMap;

/**
 * This class represents the Claim Noble action.
 */
public class ClaimNobleAction extends Action {

  private final NobleCard curCard;
  private final Position curPosition;

  /**
   * Constructor.
   *
   * @param nobleCard The noble DevelopmentCard (which can be claimed).
   * @param position  The position of the noble DevelopmentCard.
   */
  public ClaimNobleAction(NobleCard nobleCard, Position position) {
    assert nobleCard != null && position != null;
    super.type = this.getClass().getSimpleName();
    curCard = nobleCard;
    curPosition = position;
  }

  @Override
  void execute(TableTop curTableTop, PlayerInGame playerInGame,
               ActionGenerator actionGenerator,
               ActionInterpreter actionInterpreter) {

    //get the current BaseBoard
    BaseBoard baseBoard = (BaseBoard) curTableTop.getBoard(Extension.BASE);
    // NOTE: Based on the position of this noble card, we decided to remove it from
    // either reserved hand or base board
    if (curPosition.getX() < 0) {
      // X value = -1 indicate that this noble is in the reserved hand
      ReservedHand reservedHand = playerInGame.getReservedHand();
      reservedHand.removeNoble(curCard);
    } else {
      baseBoard.removeNoble(curCard); //remove the claimed noble from the baseboard
    }

    // regardless how we removed it, we put it in purchased hand
    playerInGame.getPurchasedHand().addNobleCard(curCard); //add Noble to player hand
    int prestigePointsEarned = curCard.getPrestigePoints();
    playerInGame.changePrestigePoints(prestigePointsEarned); //add prestige points to Player

    // reset map to next player
    actionGenerator.getPlayerActionMaps().put(playerInGame.getName(), new HashMap<>());

  }

  public NobleCard getCurCard() {
    assert curCard != null;
    return curCard;
  }

  public Position getCardPosition() {
    assert curPosition != null;
    return curPosition;
  }
}
