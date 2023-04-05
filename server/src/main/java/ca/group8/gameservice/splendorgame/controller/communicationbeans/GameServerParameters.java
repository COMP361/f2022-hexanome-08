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


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public int getMaxSessionPlayers() {
    return maxSessionPlayers;
  }

  public void setMaxSessionPlayers(int maxSessionPlayers) {
    this.maxSessionPlayers = maxSessionPlayers;
  }

  public int getMinSessionPlayers() {
    return minSessionPlayers;
  }

  public void setMinSessionPlayers(int minSessionPlayers) {
    this.minSessionPlayers = minSessionPlayers;
  }

  public String getWebSupport() {
    return webSupport;
  }

  public void setWebSupport(String webSupport) {
    this.webSupport = webSupport;
  }


}
