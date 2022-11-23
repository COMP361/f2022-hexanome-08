package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.Action;
import ca.group8.gameservice.splendorgame.model.Game;
import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;

public class CardAction extends Action {
  private Position postion;
  private Card card;

  public CardAction(PlayerReadOnly player, Game game, Position postion, Card card) {
    super(player, game);
    this.postion = postion;
    this.card = card;
  }

  public Position getPostion() {
    return postion;
  }

  public Card getCard() {
    return card;
  }
}
