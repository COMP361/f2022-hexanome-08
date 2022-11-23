
package ca.group8.gameservice.splendorgame.model;

import java.util.EnumMap;

public class Card {
    int cardId;
    int prestigePoints;
    EnumMap<Colour,Integer> price;

    public Card(int paramCardId, int paramPrestigePoints, EnumMap<Colour,Integer> paramPrice){
        cardId=paramCardId;
        prestigePoints = paramPrestigePoints;
        price= paramPrice;
    }

}
