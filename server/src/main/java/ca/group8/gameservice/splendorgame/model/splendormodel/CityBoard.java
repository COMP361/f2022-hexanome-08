package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityBoard extends Board {

  // keeps track of the ownership of any CityCard
  private final Map<String, CityCard> playerCities = new HashMap<>();
  // visible city cards on board
  private final List<CityCard> allCityCards = new ArrayList<>();
  public CityBoard(List<String> playerNames) {
    setup(playerNames);
  }

  @Override
  public void update(Card aCard, int index) {

  }

  @Override
  public void setup(List<String> playerNames) {
    int cardCount = playerNames.size();
  }

}
