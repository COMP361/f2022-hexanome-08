package project.view.lobby;

/**
 * GameParameters object for the reply message of /api/gameservices.
 */
public class Game {
  private String location;
  private int maxSessionPlayers;
  private int minSessionPlayers;
  private String name;
  private String displayName;
  private String webSupport;

  public void setLocation(String location) {
    this.location = location;
  }

  public String getLocation() {
    return location;
  }

  public void setMaxSessionPlayers(int maxSessionPlayers) {
    this.maxSessionPlayers = maxSessionPlayers;
  }

  public int getMaxSessionPlayers() {
    return maxSessionPlayers;
  }

  public void setMinSessionPlayers(int minSessionPlayers) {
    this.minSessionPlayers = minSessionPlayers;
  }

  public int getMinSessionPlayers() {
    return minSessionPlayers;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setWebSupport(String webSupport) {
    this.webSupport = webSupport;
  }

  public String getWebSupport() {
    return webSupport;
  }
}