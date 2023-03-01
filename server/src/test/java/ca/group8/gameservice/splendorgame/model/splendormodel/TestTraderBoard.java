package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTraderBoard {

  List<String> playerNames = new ArrayList<>(Arrays.asList("Bob", "Tom", "Marry"));
  TraderBoard traderBoard = new TraderBoard(playerNames);


  @Test
  void testGetAllPlayerPowers() {
    Map<String, Map<PowerEffect, Power>> allPlayerPowers = new HashMap<>();
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
    assertEquals(allPlayerPowers, traderBoard.getAllPlayerPowers());
  }

  @Test
  void testGetPlayerOnePower_True() throws NoSuchFieldException, IllegalAccessException {
    Field allPlayerPowers = TraderBoard.class.getDeclaredField("allPlayerPowers");
    allPlayerPowers.setAccessible(true);
    Map<String,Map<PowerEffect, Power>> testPowers
        = (Map<String,Map<PowerEffect, Power>> ) allPlayerPowers.get(traderBoard);
    Power power = traderBoard.getPlayerOnePower("Bob", PowerEffect.ARM_POINTS);
    Power testPower = testPowers.get("Bob").get(PowerEffect.ARM_POINTS);
    assertEquals(testPower, power);
  }

}
