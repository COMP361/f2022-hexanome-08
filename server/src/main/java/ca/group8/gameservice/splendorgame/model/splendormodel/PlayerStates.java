package ca.group8.gameservice.splendorgame.model.splendormodel;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStates implements BroadcastContent {
  private final Map<String, PlayerInGame> playersInfo;

  public PlayerStates(List<String> playerNames) {
    this.playersInfo = new HashMap<>();
    for (String name : playerNames) {
      playersInfo.put(name, new PlayerInGame(name));
    }
  }

  public Map<String, PlayerInGame> getPlayersInfo() {
    return playersInfo;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
