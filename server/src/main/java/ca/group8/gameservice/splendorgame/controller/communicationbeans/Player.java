package ca.group8.gameservice.splendorgame.controller.communicationbeans;

import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;

public class Player implements PlayerReadOnly {

  private String name;
  private String preferredColour;

  public Player(String name, String preferredColour) {
    this.name = name;
    this.preferredColour = preferredColour;
  }


  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPreferredColour() {
    return preferredColour;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPreferredColour(String preferredColour) {
    this.preferredColour = preferredColour;
  }

  @Override
  public boolean equals(Object otherPlayer) {
    if (otherPlayer == null) {
      return false;
    }
    if (otherPlayer.getClass() != Player.class) {
      return false;
    }

    // safe to downcast the object to player at this stage
    Player theOtherPlayer = (Player) otherPlayer;
    String curPlayerName = this.name.toLowerCase();
    String otherPlayerName = theOtherPlayer.name.toLowerCase();

    // if 2 players have same (case in-sensitive) names and pref colour, they are equal
    return curPlayerName.equals(otherPlayerName)
        && this.preferredColour.equals(theOtherPlayer.preferredColour);
  }

}
