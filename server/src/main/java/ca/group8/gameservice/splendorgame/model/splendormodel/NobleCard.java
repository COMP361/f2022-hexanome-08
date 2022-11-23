package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;


public class NobleCard extends Card {
    private int cardId;
    private int prestigePoints;
    private EnumMap<Colour,Integer> price;

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
