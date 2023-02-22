package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
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
  private List<CityCard> allCityCards = new ArrayList<>();

  public CityBoard(List<String> playerNames) {
    // initialize city to each player as null initially
    playerNames.stream().map(name -> playerCities.put(name, null));

    List<CityCard> allCards = super.generateCityCards();

    // randomly get 3 city cards on board (the rule)
    Collections.shuffle(allCards);
    allCityCards = allCards.subList(0, 3);
  }


  /**
   * Assign the City Card to the player, remove it from the board.
   *
   * @param playerName player name who gets the card
   * @param card       the city card gives to the player
   */
  public void assignCityCard(String playerName, CityCard card) {
    playerCities.put(playerName, card);
    allCityCards.remove(card);
  }

  /**
   * Get the city card of a player, return null if player does not have city card yet.
   *
   * @param playerName player name who we want to check city card on
   * @return possibly the city card of the player, null if one doesn't have city card
   */
  public CityCard getPlayerCityCard(String playerName) {
    if (playerCities.containsKey(playerName) && playerCities.get(playerName) != null) {
      return playerCities.get(playerName);
    } else {
      return null;
    }
  }

  @Override
  public void update(Card card, int index) {
    // TODO:
  }


}
