package ca.group8.gameservice.splendorgame.controller.communicationbeans;

import java.util.LinkedList;

/**
 * The class needed to encode/decode to JSON String as the information to communicate with LS.
 */
public class LauncherInfo {

  // The name of the game that registered in LS
  String gameServer;

  LinkedList<PlayerInfo> players;

  String creator;

  // Optional id of a saved game to load, it will be a request param from client request
  String savegame;

  /**
   * Default constructor.
   */
  public LauncherInfo() {
  }

  /**
   * By not providing the save game id, we create a new game.
   *
   * @param gameServer game server Id
   * @param players list of players
   * @param creator creator
   */
  public LauncherInfo(String gameServer, LinkedList<PlayerInfo> players, String creator) {
    this.gameServer = gameServer;
    this.players = players;
    this.creator = creator;
    savegame = "";
  }

  /**
   * By providing the save game id, we create a game based a previous saved game states.
   *
   * @param gameServer game server
   * @param players players
   * @param creator creator
   * @param savegame saved game
   */
  public LauncherInfo(String gameServer, LinkedList<PlayerInfo> players,
                      String creator, String savegame) {
    this.gameServer = gameServer;
    this.players = players;
    this.creator = creator;
    this.savegame = savegame;
  }

  /**
   * Gets game server.
   *
   * @return String game server
   */
  public String getGameServer() {
    return gameServer;
  }

  /**
   * Sets the game server.
   *
   * @param gameServer game server
   */
  public void setGameServer(String gameServer) {
    this.gameServer = gameServer;
  }

  /**
   * Gets players.
   *
   * @return List of players
   */
  public LinkedList<PlayerInfo> getPlayers() {
    return players;
  }

  /**
   * Sets the players for the game.
   *
   * @param players List of players
   */
  public void setPlayers(LinkedList<PlayerInfo> players) {
    this.players = players;
  }

  /**
   * Gets the creator's .
   *
   * @return String of creator
   */
  public String getCreator() {
    return creator;
  }

  /**
   * Sets the creator.
   *
   * @param creator creator
   */
  public void setCreator(String creator) {
    this.creator = creator;
  }

  /**
   * Get saved games.
   *
   * @return String of saved game
   */
  public String getSavegame() {
    return savegame;
  }

  /**
   * Sets the savegame.
   *
   * @param savegame String of savegame
   */
  public void setSavegame(String savegame) {
    this.savegame = savegame;
  }

}
