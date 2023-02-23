package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;

/**
 * Action that allows a player to reserve a card.
 */
public class ReserveAction extends Action {

  private Card curCard;
  private Position cardPosition;


  @Override
  public Position getCardPosition() {
    assert cardPosition != null;
    return cardPosition;
  }

  @Override
  public Card getCurCard() {
    assert curCard != null;
    return curCard;
  }

  public void setCardPosition(Position cardPosition) {
    this.cardPosition = cardPosition;
  }

  public void setCurCard(Card curCard) {
    this.curCard = curCard;
  }

  public ReserveAction(Position position, Card curCard) {
    super.type = this.getClass().getSimpleName();
    this.cardPosition = position;
    this.curCard = curCard;
  }

  @Override
  //TODO
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
                      ActionGenerator actionListGenerator,
                      ActionInterpreter actionInterpreter) {

    //ReservedHand reservedHand = player.getReservedHand();
    //Card reserveCard = this.card;
    //
    ////add card to reserved hand (based on whether it is a development card or noble)
    //if (reserveCard instanceof NobleCard) {
    //  NobleCard card = (NobleCard) reserveCard;
    //  reservedHand.addNobleCard(card);
    //  Position position = currentGameState.getTableTop().getNobleBoard().getCardPosition(card);
    //  currentGameState.getTableTop().getNobleBoard().remove(position);
    //
    //} else {
    //  DevelopmentCard card = (DevelopmentCard) reserveCard;
    //  reservedHand.addDevelopmentCard(card);
    //  int level = card.getLevel();
    //  Card newCard = currentGameState.getTableTop().getDecks().get(level).pop();
    //  Position curCardPosition = this.position;
    //  currentGameState.getTableTop().getBaseBoard().takeAndReplaceCard(newCard, curCardPosition);
    //}

  }

  @Override
  public boolean checkIsCardAction() {
    return true;
  }
}
