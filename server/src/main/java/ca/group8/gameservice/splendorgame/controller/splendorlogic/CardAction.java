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

  /*
  1. Same class
  2. same player
  3. same game info
  4. same card
   */

  // Overriding equals() to compare two Complex objects
  @Override
  public boolean equals(Object o) {

    // If the object is compared with itself then return true
    if (o == this) {
      return true;
    }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
    if (!(this.getClass()==o.getClass())) {
      return false;
    }

    // typecast o to CardAction so that we can compare data members
    CardAction c = (CardAction) o;

    // Compare the Player, Game, and Card fields
    return this.getPlayer()==(c.getPlayer())
            && this.getGame().equals(c.getGame())
            && this.getCard().equals(c.getCard());
  }
}
