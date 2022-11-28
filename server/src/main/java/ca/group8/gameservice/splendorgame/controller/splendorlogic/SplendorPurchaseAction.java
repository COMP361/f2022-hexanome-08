package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;

public class SplendorPurchaseAction extends CardAction {
  int goldTokenRequired;

  public SplendorPurchaseAction(PlayerInGame playerInGame,
                                GameInfo game,
                                Position position,
                                Card card, int goldTokenRequired) {
    super(playerInGame, game, position, card);
    this.goldTokenRequired= goldTokenRequired;
  }

   public int getGoldTokenRequired(){
      return goldTokenRequired;
    }


}
