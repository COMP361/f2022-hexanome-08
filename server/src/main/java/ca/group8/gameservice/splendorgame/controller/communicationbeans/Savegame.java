package ca.group8.gameservice.splendorgame.controller.communicationbeans;

/**
 * Simple bean to wrap up the information passed from client to BGP
 * upon registration of a new savegame.
 *
 * @author Maximilian Schiedermeier, August 2020
 */
public class Savegame {

  // Encodes the players in specific order, starting with the game creator.
  String[] players;

  // Name of the gameserver, as registered at the BGP;
  String gamename;

  // Unique identifier, that will be sent to the gameserver as
  // additional session start information, if this savegame was loaded by a session.
  String savegameid;

  /**
   * Constructor.
   *
   * @param players in game
   * @param gamename of game
   * @param savegameid of game
   */
  public Savegame(String[] players, String gamename, String savegameid) {
    this.players = players;
    this.gamename = gamename;
    this.savegameid = savegameid;
  }

  /**
   * Default constructor.
   */
  public Savegame() {
  }

  /**
   * Gets all the players.
   *
   * @return array of players
   */
  public String[] getPlayers() {
    return players;
  }

  /**
   * Sets list of players.
   *
   * @param players array of players
   */
  public void setPlayers(String[] players) {
    this.players = players;
  }

  /**
   * Gets game name.
   *
   * @return String of game name
   */
  public String getGamename() {
    return gamename;
  }

  /**
   * Set game name.
   *
   * @param gamename String of game name
   */
  public void setGamename(String gamename) {
    this.gamename = gamename;
  }

  /**
   * Get saved game's id.
   *
   * @return String of game id
   */
  public String getSavegameid() {
    return savegameid;
  }

  /**
   * Sets saved game id.
   *
   * @param savegameid new game id
   */
  public void setSavegameid(String savegameid) {
    this.savegameid = savegameid;
  }
}
