package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * class that holds info about city board.
 */

public class CityBoard extends Board {

  // visible city cards on board
  private final CityCard[] allCityCards = new CityCard[3];
  // keeps track of the ownership of any CityCard
  private Map<String, CityCard> playerCities = new HashMap<>();

  /**
   * CityBoard.
   *
   * @param playerNames playerNames
   */
  public CityBoard(List<String> playerNames) {
    super.type = this.getClass().getSimpleName();
    // initialize city to each player as null at the beginning
    playerNames.forEach(name -> playerCities.put(name, null));

    // randomly got 3 city card names in this format: "city[1-7]_[1-2]" for 3 times
    String[] cityNames = new String[3];
    for (int i = 0; i < cityNames.length; i++) {
      // randomly got one number from 1 -> 7
      int prefix = new Random().nextInt(7) + 1;
      // randomly got one number from 1 -> 2
      int suffix = new Random().nextInt(1) + 1;
      cityNames[i] = String.format("city%s_%s", prefix, suffix);
    }

    // can not test generateCityCards() because JSON parsing has random order issue
    List<CityCard> allCards = super.generateCityCards();
    // now we have the 3 random names, we can use them to get the cards
    // out of all city cards
    for (int i = 0; i < allCityCards.length; i++) {
      for (CityCard cityCard : allCards) {
        if (cityCard.getCardName().equals(cityNames[i])) {
          // store the card with the matching random name
          // then break it immediately
          allCityCards[i] = cityCard;
          break;
        }
      }
    }
  }


  /**
   * Assign the City Card to the player, remove it from the board.
   *
   * @param playerName player name who gets the card
   * @param card       the city card gives to the player
   */
  public void assignCityCard(String playerName, CityCard card) {
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
   * Do nothing, as CityBoard needs no update.
   */
  @Override
  public void update() {

  }

  /**
   * Call this method to rename the player names if the ones who want to play now does not.
   * match with the ones who saved this game before.
   *
   * @param newNames the current player names who want to play this game
   */
  @Override
  public void renamePlayers(List<String> newNames) {
    List<String> oldNames = new ArrayList<>(playerCities.keySet());
    List<String> newNamesCopy = new ArrayList<>(newNames);
    List<String> assignedPlayers = new ArrayList<>();
    Map<String, CityCard> newCityMap = new HashMap<>();
    // for the old names, find their old data and assign it to them
    for (String oldName : oldNames) {
      // if any oldName match a new name, remove this old name from newNameCopy list
      if (newNames.contains(oldName)) {
        newCityMap.put(oldName, playerCities.get(oldName));
        newNamesCopy.remove(oldName);
        assignedPlayers.add(oldName);
      }
    }

    int counter = 0;
    for (String otherPlayerName : playerCities.keySet()) {
      if (!assignedPlayers.contains(otherPlayerName)) {
        CityCard oldCityCard = playerCities.get(otherPlayerName);
        String newName = newNamesCopy.get(counter);
        newCityMap.put(newName, oldCityCard);
        counter += 1;
      }
      // finish assign all new players, end it
      if (counter >= newNames.size()) {
        break;
      }
    }
    playerCities = newCityMap;

  }
}
