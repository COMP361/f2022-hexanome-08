package ca.group8.gameservice.splendorgame.controller.communicationbeans;

/**
 * Player info outside of game.
 */
public class PlayerInfo {

  String name;
  String preferredColour;

  public PlayerInfo(String name, String preferredColour) {
    this.name = name;
    this.preferredColour = preferredColour;
  }

  public PlayerInfo() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPreferredColour() {
    return preferredColour;
  }

  public void setPreferredColour(String preferredColour) {
    this.preferredColour = preferredColour;
  }
}