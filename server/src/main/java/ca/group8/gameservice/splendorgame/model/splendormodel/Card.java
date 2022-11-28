
package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class Card {

    private int prestigePoints;
    private EnumMap<Colour,Integer> price;
    private String cardName;

    public Card(int paramPrestigePoints, EnumMap<Colour,Integer> paramPrice, String paramCardName){
        prestigePoints = paramPrestigePoints;
        price= paramPrice;
        cardName=paramCardName;
    }

    public int getPrestigePoints() {

        return prestigePoints;
    }

    public EnumMap<Colour, Integer> getPrice() {
        return price;
    }

    public String getCardName() {
        return cardName;
    }

}
