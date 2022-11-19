package project.view.lobby;

import java.util.List;

/**
 * Sessions needed to do GUI representation.
 */
public class Session {
  private String creator;
  private String savegameid;
  private GameParameters gameParameters;
  private List<String> players;
  private PlayerLocations playerLocations;
  private boolean launched;

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getCreator() {
    return creator;
  }

  public void setSavegameid(String savegameid) {
    this.savegameid = savegameid;
  }

  public String getSavegameid() {
    return savegameid;
  }

  public void setGameParameters(GameParameters gameParameters) {
    this.gameParameters = gameParameters;
  }

  public GameParameters getGameParameters() {
    return gameParameters;
  }

  public void setPlayers(List<String> players) {
    this.players = players;
  }

  public List<String> getPlayers() {
    return players;
  }

  public void setPlayerLocations(PlayerLocations playerLocations) {
    this.playerLocations = playerLocations;
  }

  public PlayerLocations getPlayerLocations() {
    return playerLocations;
  }

  public void setLaunched(boolean launched) {
    this.launched = launched;
  }

  public boolean getLaunched() {
    return launched;
  }
}
