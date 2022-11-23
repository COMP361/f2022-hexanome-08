package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.Game;
import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;

public class SplendorReserveAction extends CardAction{

  public SplendorReserveAction(PlayerReadOnly player,
      Game game,
      Position postion,
      Card card) {
    super(player, game, postion, card);
  }

}
