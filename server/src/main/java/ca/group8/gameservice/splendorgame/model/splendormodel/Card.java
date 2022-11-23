
package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

public class Card {
    private int prestigePoints;
    private EnumMap<Colour, Integer> price;

    public Card(int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice){
        prestigePoints = paramPrestigePoints;
        price= paramPrice;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }

    public EnumMap<Colour, Integer> getPrice() {
        return price;
    }
}
