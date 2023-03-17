package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
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
    baseBoard.removeNoble(curCard); //remove the claimed noble from the baseboard
    playerInGame.getPurchasedHand().addNobleCard(curCard); //add Noble to player hand
    int prestigePointsEarned = curCard.getPrestigePoints();
    playerInGame.changePrestigePoints(prestigePointsEarned); //add prestige points to Player

    // reset map to next player
    actionGenerator.getPlayerActionMaps().put(playerInGame.getName(), new HashMap<>());

    //TraderBoard traderBoard = (TraderBoard) curTableTop.getBoard(Extension.TRADING_POST);
    //Power power = traderBoard.getPlayerOnePower(playerInGame.getName(), PowerEffect.FIVE_POINTS);
    //
    ////if Power was previously locked, but has now been unlocked (validityCheck = true)
    //if (!power.isUnlocked() && power.validityCheck(playerInGame)) {
    //  power.unlock(); //set status so power is now unlocked
    //  playerInGame.changePrestigePoints(5); //add power (5 prestige points) to player
    //}


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
