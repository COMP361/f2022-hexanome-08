package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

/**
 * Nobles.
 */
public class NobleCard implements Card {
    private final int cardId;
    private final int prestigePoints;
    private final EnumMap<Colour, Integer> price;

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
