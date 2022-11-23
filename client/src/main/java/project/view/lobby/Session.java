package project.view.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Sessions needed to do GUI representation.
 */
public class Session {

  private final GameParameters gameParameters;
  private final String creator;
  private boolean launched;
  private final ArrayList<String> players;

  // Optional fields
  private String savegameid;
  private Map<String, String> playerLocations;

  /**
   * Session constructor.
   *
   * @param creator creator name
   * @param gameParameters game parameters
   */
  public Session(String creator, GameParameters gameParameters) {
    this.creator = creator;
    this.gameParameters = gameParameters;
    players = new ArrayList<>();
    players.add(creator);
    launched = false;
    playerLocations = new HashMap<>();
  }

  public GameParameters getGameParameters() {
    return gameParameters;
  }

  public String getCreator() {
    return creator;
  }

  public boolean isLaunched() {
    return launched;
  }

  public ArrayList<String> getPlayers() {
    return players;
  }

  public String getSavegameid() {
    return savegameid;
  }

  public Map<String, String> getPlayerLocations() {
    return playerLocations;
  }


}
