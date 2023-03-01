package project.view.splendor.communication;

import java.util.EnumMap;

public class TakeTokenAction extends Action {
  private EnumMap<Colour, Integer> tokensTaken;

  public TakeTokenAction(String type, EnumMap<Colour, Integer> tokensTaken) {
    this.tokensTaken = tokensTaken;
  }

  public EnumMap<Colour, Integer> getTokensTaken() {
    return tokensTaken;
  }

  public void setTokensTaken(EnumMap<Colour, Integer> tokensTaken) {
    this.tokensTaken = tokensTaken;
  }
}