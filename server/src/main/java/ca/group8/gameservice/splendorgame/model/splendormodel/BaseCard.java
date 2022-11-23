package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class BaseCard extends DevelopmentCard {


    public BaseCard(int paramPrestigePoints,
        EnumMap<Colour, Integer> paramPrice, int cardId, int prestigePoints,
        EnumMap<Colour, Integer> price, int level,
        Optional<Colour> gemColor, boolean isPaired, int pairedCardId) {
        super(paramPrestigePoints, paramPrice, cardId, prestigePoints, price, level,
            gemColor, isPaired, pairedCardId);
    }
}
