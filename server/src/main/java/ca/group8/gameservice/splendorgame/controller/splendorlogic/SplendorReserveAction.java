package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.Game;
import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.Player;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;

public class SplendorReserveAction extends CardAction{

  public SplendorReserveAction(Player player,
                               GameInfo game,
                               Position position,
                               Card card) {
    super(player, game, position, card);
  }

}
