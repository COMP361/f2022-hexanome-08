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
public class ReserveAction extends CardAction {

  public ReserveAction(boolean isCardAction, Position position, Card card) {
    super(isCardAction, position, card);
  }

  @Override
  public void execute(GameInfo currentGameState, PlayerInGame player) {

    ReservedHand reservedHand = player.getReservedHand();
    Card reserveCard = this.getCard();

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
      Position curCardPosition = super.getPosition();
      currentGameState.getTableTop().getBaseBoard().takeAndReplaceCard(newCard, curCardPosition);
    }

  }
}
