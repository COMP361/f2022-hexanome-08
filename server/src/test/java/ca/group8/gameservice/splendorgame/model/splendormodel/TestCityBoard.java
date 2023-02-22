package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

public class TestCityBoard {
  List<String> playerNames = new ArrayList<>(Arrays.asList("Bob", "Tom"));
  CityCard[] testCityCards = new CityCard[3];
  CityBoard cityBoard = new CityBoard(playerNames);

  @BeforeEach
  void setUpBoard() {
    for (int i = 0; i < 3; i++) {
      testCityCards[i] =
          new CityCard(13,new EnumMap<>(Colour.class), "city1", 3);
    }
  }

}
