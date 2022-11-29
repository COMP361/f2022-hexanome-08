package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;

import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import java.util.EnumMap;

public class SplendorTakeTokenAction extends Action{
  private EnumMap<Colour,Integer> tokens;

  public SplendorTakeTokenAction(boolean isCardAction, EnumMap<Colour,Integer> tokens) {
    super(isCardAction);
    this.tokens = tokens;
  }

  public EnumMap<Colour, Integer> getTokens() {
    return tokens;
  }

  @Override
  public void execute(GameInfo currentGameState, PlayerInGame playerState) {
    // TODO: Concrete implementation of how TakeTokenAction is executed
  }
}
