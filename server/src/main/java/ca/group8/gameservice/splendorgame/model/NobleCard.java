package main.java.ca.group8.gameservice.splendorgame.model;

import java.util.EnumMap;
import java.util.Optional;

/**
 * Nobles.
 */
public class NobleCard implements Card {
    private int cardId;
    private int prestigePoints;
    private EnumMap<Colour, Integer> price;

    /**
     * Constructor.
     */
    public NobleCard(int parId, int parPrestige, EnumMap<Colour,Integer> parPrice){
        cardId = parId;
        prestigePoints = parPrestige;
        price = parPrice;
    }

    public int getCardId() {
        return cardId;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }

    public EnumMap<Colour, Integer> getPrice() {
        return price;
    }



}
