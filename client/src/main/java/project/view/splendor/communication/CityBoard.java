package project.view.splendor.communication;

import java.util.HashMap;
import java.util.Map;

public class CityBoard extends Board {

    // keeps track of the ownership of any CityCard
    private Map<String, CityCard> playerCities = new HashMap<>();
    // visible city cards on board
    private final CityCard[] allCityCards = new CityCard[3];
    public CityBoard(String type, Map<String, CityCard> playerCities) {
        super(type);
        this.playerCities = playerCities;
    }

    public Map<String, CityCard> getPlayerCities() {
        return playerCities;
    }

    public void setPlayerCities(Map<String, CityCard> playerCities) {
        this.playerCities = playerCities;
    }

    public CityCard[] getAllCityCards() {
        return allCityCards;
    }
}
