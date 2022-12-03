package project.view.splendor.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStates {
  public Map<String, PlayerInGame> getPlayersInfo() {
    return playersInfo;
  }

  public void setPlayersInfo(
      Map<String, PlayerInGame> playersInfo) {
    this.playersInfo = playersInfo;
  }

  public PlayerStates(Map<String, PlayerInGame> playersInfo) {
    this.playersInfo = playersInfo;
  }

  private Map<String, PlayerInGame> playersInfo;

}