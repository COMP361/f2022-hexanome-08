package project.view.lobby.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Sessions needed to do GUI representation.
 */
public class Session {

  private final GameParameters gameParameters;
  private final String creator;
  private final boolean launched;
  private final ArrayList<String> players;
  private final Map<String, String> playerLocations;
  // Optional fields
  private String savegameid;

  /**
   * Session constructor.
   *
   * @param creator        creator name
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

  /**
   * getGameParameters.
   *
   * @return GameParameters
   */
  public GameParameters getGameParameters() {
    return gameParameters;
  }

  /**
   * getCreator.
   *
   * @return creater
   */
  public String getCreator() {
    return creator;
  }

  /**
   *  isLaunched.
   *
   * @return boolean
   */
  public boolean isLaunched() {
    return launched;
  }

  /**
   * getPlayers.
   *
   * @return players
   */
  public ArrayList<String> getPlayers() {
    return players;
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
   * getPlayerLocations.
   *
   * @return the map
   */
  public Map<String, String> getPlayerLocations() {
    return playerLocations;
  }


}
