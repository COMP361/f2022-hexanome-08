package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;

public class SplendorReserveAction extends CardAction{

  public SplendorReserveAction(PlayerInGame playerInGame,
                               GameInfo game,
                               Position position,
                               Card card) {
    super(playerInGame, game, position, card);
  }

}
