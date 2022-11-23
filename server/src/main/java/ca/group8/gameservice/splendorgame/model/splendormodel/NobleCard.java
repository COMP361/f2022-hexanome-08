
package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

public class NobleCard extends Card {
    private int cardId;
    private int prestigePoints;
    private EnumMap<Colour,Integer> price;


    public NobleCard(String paramCardId, int paramPrestigePoints,
        EnumMap<Colour, Integer> paramPrice) {
        super(paramCardId, paramPrestigePoints, paramPrice);
    }
}
}
