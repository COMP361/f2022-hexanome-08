package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.Action;
import ca.group8.gameservice.splendorgame.model.Game;
import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.Player;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;

public class CardAction extends Action {
  private Position position;
  private Card card;

  public CardAction(Player player, GameInfo game, Position position, Card card) {
    super(player, game);
    this.position = position;
    this.card = card;
  }

  public Position getPosition() {
    return position;
  }

  public Card getCard() {
    return card;
  }
}
