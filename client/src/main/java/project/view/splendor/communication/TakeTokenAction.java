package project.view.splendor.communication;

import java.util.EnumMap;
import project.view.splendor.Colour;

public class TakeTokenAction extends Action {

  private EnumMap<Colour, Integer> tokens;
  public TakeTokenAction(boolean isCardAction, EnumMap<Colour, Integer> tokens) {
    super(isCardAction);
    this.tokens = tokens;
  }

  public EnumMap<Colour, Integer> getTokens() {
    return tokens;
  }

  public void setTokens(
      EnumMap<Colour, Integer> tokens) {
    this.tokens = tokens;
  }
  //
  //@Override
  //public boolean checkIsCardAction() {
  //  return false;
  //}
}
