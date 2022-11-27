
package project.view.splendor.gameitems;

import java.util.EnumMap;
import project.view.splendor.Colour;

public class Card {

    int prestigePoints;
    EnumMap<Colour,Integer> price;

    String cardName;

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
