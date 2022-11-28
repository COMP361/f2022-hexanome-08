package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class BaseCard extends DevelopmentCard {


    public BaseCard(int paramPrestigePoints,
        EnumMap<Colour, Integer> paramPrice, String cardName, int level,
        Optional<Colour> gemColor, boolean isPaired, String pairedCardId, int gemNumber) {
        super(paramPrestigePoints, paramPrice, cardName, level,
            gemColor, isPaired, pairedCardId, gemNumber);

    }

}
