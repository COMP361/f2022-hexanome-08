package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.ReservedHand;

/**
 * Action that allows a player to reserve a card.
 */
public class ReserveAction implements Action {

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
  public Card getCard() {
    return card;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  private Position position;
  private Card card;

  public ReserveAction(Position position, Card card) {
    this.position = position;
    this.card = card;
  }

  @Override
  public void execute(GameInfo currentGameState, PlayerInGame player) {

    ReservedHand reservedHand = player.getReservedHand();
    Card reserveCard = this.card;

    //add card to reserved hand (based on whether it is a development card or noble)
    if (reserveCard instanceof NobleCard) {
      NobleCard card = (NobleCard) reserveCard;
      reservedHand.addNobleCard(card);
      Position position = currentGameState.getTableTop().getNobleBoard().getCardPosition(card);
      currentGameState.getTableTop().getNobleBoard().remove(position);

    } else {
      DevelopmentCard card = (DevelopmentCard) reserveCard;
      reservedHand.addDevelopmentCard(card);
      int level = card.getLevel();
      Card newCard = currentGameState.getTableTop().getDecks().get(level).pop();
      Position curCardPosition = this.position;
      currentGameState.getTableTop().getBaseBoard().takeAndReplaceCard(newCard, curCardPosition);
    }

  }

  @Override
  public boolean checkIsCardAction() {
    return true;
  }
}
