package project.view.lobby.communication;

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
   * save game.
   *
   * @param players    players
   * @param gamename   gamename
   * @param savegameid savegameid
   */
  public Savegame(String[] players, String gamename, String savegameid) {
    this.players = players;
    this.gamename = gamename;
    this.savegameid = savegameid;
  }

  /**
   * save the game.
   */
  public Savegame() {
  }

  /**
   * get Players.
   *
   * @return list of players
   */
  public String[] getPlayers() {
    return players;
  }

  /**
   * setPlayers.
   *
   * @param players players
   */
  public void setPlayers(String[] players) {
    this.players = players;
  }

  /**
   * getGamename.
   *
   * @return game name
   */
  public String getGamename() {
    return gamename;
  }

  /**
   * set Game name.
   *
   * @param gamename game name
   */
  public void setGamename(String gamename) {
    this.gamename = gamename;
  }

  /**
   * getSavegameid.
   *
   * @return string
   */
  public String getSavegameid() {
    return savegameid;
  }

  /**
   * setSavegameid.
   *
   * @param savegameid savegameid
   */
  public void setSavegameid(String savegameid) {
    this.savegameid = savegameid;
  }
}
