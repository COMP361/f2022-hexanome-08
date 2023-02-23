package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class that holds info about city board.
 */

public class CityBoard extends Board {

  // keeps track of the ownership of any CityCard
  private final Map<String, CityCard> playerCities = new HashMap<>();
  // visible city cards on board
  private final CityCard[] allCityCards = new CityCard[3];

  public CityBoard(List<String> playerNames) {
    // initialize city to each player as null at the beginning
    playerNames.forEach(name -> playerCities.put(name, null));

    // can not test generateCityCards() because JSON parsing has random order issue
    List<CityCard> allCards = super.generateCityCards();
    // randomly get exactly 3 city cards on board (the rule)
    Collections.shuffle(allCards);
    List<CityCard> cityCardsInUse = allCards.subList(0, 3);
    for (int i = 0; i < allCityCards.length; i++) {
      allCityCards[i] = cityCardsInUse.get(i);
    }
  }


  /**
   * Assign the City Card to the player, remove it from the board.
   *
   * @param playerName player name who gets the card
   * @param card       the city card gives to the player
   */
  public void assignCityCard(String playerName, CityCard card) throws SplendorGameException{
    if (!playerCities.containsKey(playerName)) {
      throw new SplendorGameException("No such player in game!");
    }
    playerCities.put(playerName, card);
    for (int i = 0; i < allCityCards.length; i++) {
      if (allCityCards[i].equals(card)) {
        allCityCards[i] = null;
      }
    }
  }

  public CityCard[] getAllCityCards() {
    return allCityCards;
  }

  public Map<String, CityCard> getPlayerCities() {
    return playerCities;
  }

  /**
   * Do nothing, as CityBoard needs no update
   */
  @Override
  public void update() {

  }
}
