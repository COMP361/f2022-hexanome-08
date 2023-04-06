package project.view.lobby.communication;

/**
 * GameParameters object for the reply message of /api/sessions.
 */
public class GameParameters {
  private int maxSessionPlayers;
  private int minSessionPlayers;
  private String displayName;
  private String name;
  private String location;
  private String webSupport;

  /**
   * getMaxSessionPlayers.
   *
   * @return int
   */
  public int getMaxSessionPlayers() {
    return maxSessionPlayers;
  }

  /**
   * setMaxSessionPlayers.
   *
   * @param maxSessionPlayers maxSessionPlayers
   */
  public void setMaxSessionPlayers(int maxSessionPlayers) {
    this.maxSessionPlayers = maxSessionPlayers;
  }

  /**
   * getMinSessionPlayers.
   *
   * @return int
   */
  public int getMinSessionPlayers() {
    return minSessionPlayers;
  }

  /**
   * setMinSessionPlayers.
   *
   * @param minSessionPlayers minSessionPlayers
   */
  public void setMinSessionPlayers(int minSessionPlayers) {
    this.minSessionPlayers = minSessionPlayers;
  }

  /**
   * get Display Name.
   *
   * @return name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * set Display Name.
   *
   * @param displayName Name
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * get Name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * setName.
   *
   * @param name name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * get Location.
   *
   * @return location
   */
  public String getLocation() {
    return location;
  }

  /**
   * setLocation.
   *
   * @param location location
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * get Web Support.
   *
   * @return support
   */
  public String getWebSupport() {
    return webSupport;
  }

  /**
   * setWebSupport.
   *
   * @param webSupport webSupport
   */
  public void setWebSupport(String webSupport) {
    this.webSupport = webSupport;
  }
}
