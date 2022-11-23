
package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class Card {

    int prestigePoints;
    EnumMap<Colour,Integer> price;

    public Card(int paramPrestigePoints, EnumMap<Colour,Integer> paramPrice){


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
