package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCityBoard {
  List<String> playerNames = new ArrayList<>(Arrays.asList("Bob", "Tom"));
  CityCard[] testCityCards = new CityCard[3];
  CityBoard cityBoard = new CityBoard(playerNames);

  @BeforeEach
  void setUpBoard() throws NoSuchFieldException, IllegalAccessException {
    CityCard[] cityCards = new CityCard[3];
    for (int i = 0; i < 3; i++) {
      CityCard card =  new CityCard(13,
          new EnumMap<>(Colour.class), "city"+i, 3);
      testCityCards[i] = card;
      cityCards[i] = card;

    }

    Field allCityCards = CityBoard.class.getDeclaredField("allCityCards");
    allCityCards.setAccessible(true);
    allCityCards.set(cityBoard, cityCards);
  }

  /**
   * Implicitly tested getAllCityCards and getPlayerCities
   */
  @Test
  void testAssignCityCard_Success() {
    CityCard cardToAssign = testCityCards[1];
    cityBoard.assignCityCard("Bob", cardToAssign);
    assertNull(cityBoard.getAllCityCards()[1]);
    assertEquals(cardToAssign, cityBoard.getPlayerCities().get("Bob"));
  }

  @Test
  void testRenamePlayers() {
    List<String> newPlayerNames = new ArrayList<>(Arrays.asList("ruoyu", "julia"));
    cityBoard.renamePlayers(newPlayerNames);
    assertEquals(new HashSet<>(newPlayerNames), cityBoard.getPlayerCities().keySet());

  }


}
