package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.Action;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.Player;

import java.util.EnumMap;

public class SplendorTakeTokenAction extends Action{
  private EnumMap<Colour,Integer> tokens;
  private boolean valid = false;

  public SplendorTakeTokenAction(Player player, GameInfo game) {
    super(player, game);
  }

  public EnumMap<Colour, Integer> getTokens() {
    return tokens;
  }

  public void setTokens(EnumMap<Colour,Integer> paramTokens){
    tokens = paramTokens;
    valid = true;
  }

  public boolean isValid(){
    return valid;
  }
}
