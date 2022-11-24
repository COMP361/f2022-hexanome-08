package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.Action;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.Player;

public class SplendorTakeTokenAction extends Action{

  public SplendorTakeTokenAction(Player player, GameInfo game) {
    super(player, game);
  }
}
