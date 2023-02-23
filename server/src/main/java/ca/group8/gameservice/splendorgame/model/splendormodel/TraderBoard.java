package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that holds info about TraderBoard.
 */
public class TraderBoard extends Board {
  private final Map<String, Map<PowerEffect, Power>> allPlayerPowers = new HashMap<>();

  public TraderBoard(List<String> playerNames) {
    super.type = this.getClass().getSimpleName();
    for (String playerName : playerNames) {
      Map<PowerEffect, Power> curPlayerPowers = new HashMap<>();
      for (PowerEffect pe : PowerEffect.values()) {
        switch (pe) {
          case ARM_POINTS:
            curPlayerPowers.put(PowerEffect.ARM_POINTS, new ArmPointsPower());
            break;
          case DOUBLE_GOLD:
            curPlayerPowers.put(PowerEffect.DOUBLE_GOLD, new DoubleGoldPower());
            break;
          case EXTRA_TOKEN:
            curPlayerPowers.put(PowerEffect.EXTRA_TOKEN, new ExtraTokenPower());
            break;
          case FIVE_POINTS:
            curPlayerPowers.put(PowerEffect.FIVE_POINTS, new FivePointsPower());
            break;
          case TWO_PLUS_ONE:
            curPlayerPowers.put(PowerEffect.TWO_PLUS_ONE, new TwoPlusOnePower());
            break;
          default: break;
        }
      }
      allPlayerPowers.put(playerName, curPlayerPowers);
    }
  }


  /**
   * Get all powers of all players.
   *
   * @return a map from String to Map< PowerEffect,Power >
   */
  public Map<String, Map<PowerEffect, Power>> getAllPlayerPowers() {
    return allPlayerPowers;
  }

  /**
   * Get one specific power instance of a given player.
   *
   * @param playerName player name that we want to get the power from
   * @param effect     the effect of the specific power
   * @return the specific power
   */
  public Power getPlayerOnePower(String playerName, PowerEffect effect)
  throws SplendorGameException {
    if (!allPlayerPowers.containsKey(playerName)) {
      throw new SplendorGameException("Player not in this game!");
    }
    return allPlayerPowers.get(playerName).get(effect);
  }

  /**
   * Nothing needs to be done. The unlock/lock states of power is associated with power.
   * objects, not the board.
   */
  @Override
  public void update() {

  }
}
