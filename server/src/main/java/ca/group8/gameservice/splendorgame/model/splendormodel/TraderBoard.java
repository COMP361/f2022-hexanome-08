package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that holds info about TraderBoard.
 */
public class TraderBoard extends Board {
  private Map<String, Map<PowerEffect, Power>> allPlayerPowers = new HashMap<>();

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
          default:
            break;
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


  public int getUnlockedPowerCount(String playerName) {
    Map<PowerEffect, Power> playerPowers = allPlayerPowers.get(playerName);
    int count = 0;
    for (Power power : playerPowers.values()) {
      if (power.isUnlocked()) {
        count += 1;
      }
    }
    return count;
  }

  /**
   * Get one specific power instance of a given player.
   *
   * @param playerName player name that we want to get the power from
   * @param effect     the effect of the specific power
   * @return the specific power
   */
  public Power getPlayerOnePower(String playerName, PowerEffect effect) {
    //if (!allPlayerPowers.containsKey(playerName)) {
    //  throw new SplendorGameException("Player not in this game!");
    //}
    // whether player is in game or not will be verified in controller
    return allPlayerPowers.get(playerName).get(effect);
  }

  /**
   * Nothing needs to be done. The unlock/lock states of power is associated with power.
   * objects, not the board.
   */
  @Override
  public void update() {

  }

  /**
   * Call this method to rename the player names if the ones who want to play now does not.
   * match with the ones who saved this game before.
   *
   * @param playerNames the current player names who want to play this game
   */
  @Override
  public void renamePlayers(List<String> playerNames) {
    List<String> curNames = new ArrayList<>(allPlayerPowers.keySet());
    // only update if names are different
    if (!playerNames.equals(curNames)) {
      int nameIndex = 0;
      Map<String, Map<PowerEffect, Power>> newPowerMap = new HashMap<>();
      for (String curName : allPlayerPowers.keySet()) {
        Map<PowerEffect, Power> curPowerMap = allPlayerPowers.get(curName);
        String newName = playerNames.get(nameIndex);
        nameIndex += 1;
        newPowerMap.put(newName, curPowerMap);
      }
      allPlayerPowers = newPowerMap;
    }
  }
}
