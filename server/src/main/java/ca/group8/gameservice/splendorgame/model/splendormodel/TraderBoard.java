package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraderBoard extends Board {
  private final Map<String, Map<PowerEffect, Power>> allPlayerPowers = new HashMap<>();

  public TraderBoard(List<String> playerNames) {
    setup(playerNames);
  }


  /**
   * Get all powers of all players
   * @return a map from String to Map<PowerEffect,Power>
   */
  public Map<String, Map<PowerEffect, Power>> getAllPlayerPowers() {
    return allPlayerPowers;
  }

  /**
   * Get one specific power instance of a given player
   * @param playerName player name that we want to get the power from
   * @param effect the effect of the specific power
   * @return the specific power
   */
  public Power getPlayerOnePower(String playerName, PowerEffect effect) {
    assert allPlayerPowers.containsKey(playerName) &&
        allPlayerPowers.get(playerName).containsKey(effect);

    return allPlayerPowers.get(playerName).get(effect);
  }

  @Override
  public void update(Card aCard, int index) {
    // TODO:
  }

  @Override
  public void setup(List<String> playerNames) {
    for (String playerName : playerNames) {
      Map<PowerEffect, Power> curPlayerPowers = new HashMap<>();
      for (PowerEffect pe : PowerEffect.values()) {
        switch (pe) {
          case ARM_POINTS -> curPlayerPowers.put(PowerEffect.ARM_POINTS, new ArmPointsPower());
          case DOUBLE_GOLD -> curPlayerPowers.put(PowerEffect.DOUBLE_GOLD, new DoubleGoldPower());
          case EXTRA_TOKEN -> curPlayerPowers.put(PowerEffect.EXTRA_TOKEN, new ExtraTokenPower());
          case FIVE_POINTS -> curPlayerPowers.put(PowerEffect.FIVE_POINTS, new FivePointsPower());
          case TWO_PLUS_ONE -> curPlayerPowers.put(PowerEffect.TWO_PLUS_ONE, new TwoPlusOnePower());
        }
      }
      allPlayerPowers.put(playerName, curPlayerPowers);
    }
  }
}
