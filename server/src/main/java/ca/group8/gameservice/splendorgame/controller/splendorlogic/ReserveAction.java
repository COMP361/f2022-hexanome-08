package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.OrientBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.ReservedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.List;

/**
 * Action that allows a player to reserve a card.
 */
public class ReserveAction extends Action {

  private DevelopmentCard curCard;
  private Position cardPosition;


  @Override
  public Position getCardPosition() {
    assert cardPosition != null;
    return cardPosition;
  }

  @Override
  public DevelopmentCard getCurCard() {
    assert curCard != null;
    return curCard;
  }

  public void setCardPosition(Position cardPosition) {
    this.cardPosition = cardPosition;
  }

  public void setCurCard(DevelopmentCard curCard) {
    this.curCard = curCard;
  }

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
   * @param actionListGenerator
   * @param actionInterpreter
   */
  @Override
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
                      ActionGenerator actionListGenerator,
                      ActionInterpreter actionInterpreter) {
    // curCard is the card that we want to reserve
    List<CardEffect> cardEffects = curCard.getPurchaseEffects();
    // then it's an orient card
    if(cardEffects.size() > 0) {
      OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
      orientBoard.removeCard(cardPosition);
    } else {
      BaseBoard baseBoard = (BaseBoard) curTableTop.getBoard(Extension.BASE);
      baseBoard.removeCard(cardPosition);
    }

    // add to player's reserved hand
    ReservedHand reservedHand = playerInGame.getReservedHand();
    reservedHand.addDevelopmentCard(curCard);

    // add to player's wealth if one has less than 10 tokens in hand and bank has gold token
    Bank bank = curTableTop.getBank();
    int goldTokenLeft = bank.getAllTokens().get(Colour.GOLD);
    int tokensInHand = playerInGame.getTokenHand().getTokenTotalCount();
    if(goldTokenLeft > 0 && tokensInHand < 10) {
      bank.getAllTokens().put(Colour.GOLD, goldTokenLeft-1);
      int goldTokenInHand = playerInGame.getTokenHand().getAllTokens().get(Colour.GOLD);
      playerInGame.getTokenHand().getAllTokens().put(Colour.GOLD,goldTokenInHand+1);
    }
  }

  @Override
  public boolean checkIsCardAction() {
    return true;
  }
}
