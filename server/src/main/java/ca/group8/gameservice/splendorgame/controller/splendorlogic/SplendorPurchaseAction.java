package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.Action;
import ca.group8.gameservice.splendorgame.model.Game;
import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;

public class SplendorPurchaseAction extends CardAction {

  public SplendorPurchaseAction(PlayerReadOnly player,
      Game game,
      Position postion,
      Card card) {
    super(player, game, postion, card);
  }
}
