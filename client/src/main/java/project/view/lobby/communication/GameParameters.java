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

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getWebSupport() {
    return webSupport;
  }

  public void setWebSupport(String webSupport) {
    this.webSupport = webSupport;
  }
}
