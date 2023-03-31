package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.OrientBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.ReservedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action that allows a player to reserve a card.
 */
public class ReserveAction extends Action {

  private final DevelopmentCard curCard;
  private final Position cardPosition;

  /**
   * ReserveAction.
   *
   * @param position position
   * @param curCard curCard
   */
  public ReserveAction(Position position, DevelopmentCard curCard) {
    super.type = this.getClass().getSimpleName();
    this.cardPosition = position;
    this.curCard = curCard;
  }

  /**
   * Execution of reserve action.
   *
   * @param curTableTop curTableTop
   * @param playerInGame playerInGame
   * @param actionGenerator actionGenerator
   * @param actionInterpreter actionInterpreter
   */
  @Override
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
                      ActionGenerator actionGenerator,
                      ActionInterpreter actionInterpreter) {
    // curCard is the card that we want to reserve
    // then it's an orient card
    if (!curCard.isBaseCard()) {
      OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
      orientBoard.removeCard(cardPosition);
      orientBoard.update();
    } else {
      BaseBoard baseBoard = (BaseBoard) curTableTop.getBoard(Extension.BASE);
      baseBoard.removeCard(cardPosition);
      baseBoard.update();
    }

    // add to player's reserved hand
    ReservedHand reservedHand = playerInGame.getReservedHand();
    reservedHand.addDevelopmentCard(curCard);

    // add to player's wealth if one has less than 10 tokens in hand and bank has gold token
    Bank bank = curTableTop.getBank();
    int goldTokenLeft = bank.getAllTokens().get(Colour.GOLD);
    Logger logger = LoggerFactory.getLogger(ReserveAction.class);
    logger.info("Gold tokens left in bank when reserving" + goldTokenLeft);
    int tokensInHand = playerInGame.getTokenHand().getTokenTotalCount();
    logger.info("Tokens in player hand when reserving" + tokensInHand);
    if (goldTokenLeft > 0) {
      bank.getAllTokens().put(Colour.GOLD, goldTokenLeft - 1);
      int goldTokenInHand = playerInGame.getTokenHand().getAllTokens().get(Colour.GOLD);
      playerInGame.getTokenHand().getAllTokens().put(Colour.GOLD, goldTokenInHand + 1);
    }
    tokensInHand = playerInGame.getTokenHand().getTokenTotalCount();
    if (tokensInHand > 10) {
      actionGenerator.updateReturnTokenActions(tokensInHand - 10, playerInGame);
      return;
    }
    // since we do not possibly generate more actions, we now know it's end of the turn
    // set action map to {}
    actionGenerator.getPlayerActionMaps().put(playerInGame.getName(), new HashMap<>());
  }

  public Position getCardPosition() {
    assert cardPosition != null;
    return cardPosition;
  }

  public DevelopmentCard getCurCard() {
    assert curCard != null;
    return curCard;
  }
}

