package ca.group8.gameservice.splendorgame.controller.communicationbeans;


/**
 * Communication bean class needed for game server to talk to LS. (parsing / unparsing JSON)
 */
public class GameServerParameters {

  private String name;

  private String displayName;

  private String location;

  private int maxSessionPlayers;
  private int minSessionPlayers;

  private String webSupport;

  /**
   * Constructor of GameServerParameters.
   *
   * @param name game service name
   * @param displayName game display name
   * @param location game host location
   * @param maxSessionPlayers maxSessionPlayers
   * @param minSessionPlayers minSessionPlayers
   * @param webSupport bool for webSupport or not
   */
  public GameServerParameters(String name, String displayName, String location,
                              int maxSessionPlayers, int minSessionPlayers,
                              String webSupport) {
    this.name = name;
    this.displayName = displayName;
    this.location = location;
    this.maxSessionPlayers = maxSessionPlayers;
    this.minSessionPlayers = minSessionPlayers;
    this.webSupport = webSupport;
  }

  /**
   * get name.
   *
   * @return String name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name game service name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the displayName.
   *
   * @return String displayName
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Set displayName.
   *
   * @param displayName String new name
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Get the location.
   *
   * @return String location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Set the location.
   *
   * @param location String new location
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Get maxSessionPlayers.
   *
   * @return int maxSessionPlayers
   */
  public int getMaxSessionPlayers() {
    return maxSessionPlayers;
  }

  /**
   * Set max number of players.
   *
   * @param maxSessionPlayers number of players
   */
  public void setMaxSessionPlayers(int maxSessionPlayers) {
    this.maxSessionPlayers = maxSessionPlayers;
  }

  /**
   * Get minSessionPlayers.
   *
   * @return int minSessionPlayers
   */
  public int getMinSessionPlayers() {
    return minSessionPlayers;
  }

  /**
   * Set max number of players.
   *
   * @param minSessionPlayers number of players
   */
  public void setMinSessionPlayers(int minSessionPlayers) {
    this.minSessionPlayers = minSessionPlayers;
  }

  /**
   * Get webSupport.
   *
   * @return String webSupport
   */
  public String getWebSupport() {
    return webSupport;
  }

  /**
   * Sets the webSupport.
   *
   * @param webSupport string of web support
   */
  public void setWebSupport(String webSupport) {
    this.webSupport = webSupport;
  }


}
