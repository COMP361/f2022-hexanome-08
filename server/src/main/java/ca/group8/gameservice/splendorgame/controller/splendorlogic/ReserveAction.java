package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.OrientBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.ReservedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.HashMap;
import java.util.List;

/**
 * Action that allows a player to reserve a card.
 */
public class ReserveAction extends Action {

  private final DevelopmentCard curCard;
  private final Position cardPosition;

  public ReserveAction(Position position, DevelopmentCard curCard) {
    super.type = this.getClass().getSimpleName();
    this.cardPosition = position;
    this.curCard = curCard;
  }

  /**
   * Execution of reserve action.
   *
   * @param curTableTop
   * @param playerInGame
   * @param actionGenerator
   * @param actionInterpreter
   */
  @Override
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
                      ActionGenerator actionGenerator,
                      ActionInterpreter actionInterpreter) {
    // curCard is the card that we want to reserve
    List<CardEffect> cardEffects = curCard.getPurchaseEffects();
    // then it's an orient card
    if (cardEffects.size() > 0) {
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
    int tokensInHand = playerInGame.getTokenHand().getTokenTotalCount();
    if (goldTokenLeft > 0 && tokensInHand < 10) {
      bank.getAllTokens().put(Colour.GOLD, goldTokenLeft - 1);
      int goldTokenInHand = playerInGame.getTokenHand().getAllTokens().get(Colour.GOLD);
      playerInGame.getTokenHand().getAllTokens().put(Colour.GOLD, goldTokenInHand + 1);
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

