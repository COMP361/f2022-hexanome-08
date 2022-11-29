package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;

public class ReserveAction extends CardAction {

  public ReserveAction(boolean isCardAction, Position position, Card card) {
    super(isCardAction, position, card);
  }

  @Override
  public void execute(GameInfo currentGameState, PlayerInGame playerState) {
    // TODO: Concrete implementation of ReserveAction
  }
}
