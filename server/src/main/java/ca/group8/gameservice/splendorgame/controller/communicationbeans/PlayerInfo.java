package ca.group8.gameservice.splendorgame.controller.communicationbeans;

/**
 * Player info outside of game.
 */
public class PlayerInfo {

  String name;
  String preferredColour;

  /**
   * Constructor.
   *
   * @param name of player
   * @param preferredColour chosen colour
   */
  public PlayerInfo(String name, String preferredColour) {
    this.name = name;
    this.preferredColour = preferredColour;
  }

  /**
   * Default constructor.
   */
  public PlayerInfo() {
  }

  /**
   * Gets player's name.
   *
   * @return String of name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the player's name.
   *
   * @param name of player
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets preferred colour.
   *
   * @return String of code of colour
   */
  public String getPreferredColour() {
    return preferredColour;
  }

  /**
   * Sets preferred colour.
   *
   * @param preferredColour String of code of colour
   */
  public void setPreferredColour(String preferredColour) {
    this.preferredColour = preferredColour;
  }
}