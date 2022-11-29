package project.view.splendor.gameitems;

import java.util.EnumMap;
import java.util.Optional;
import project.view.splendor.Colour;

public class BaseCard extends DevelopmentCard {


    public BaseCard(int paramPrestigePoints,
                    EnumMap<Colour, Integer> paramPrice, String cardName, int level,
                    Optional<Colour> gemColor, boolean isPaired, int pairedCardId, int gemNumber) {
        super(paramPrestigePoints, paramPrice, cardName, level,
            gemColor, isPaired, pairedCardId, gemNumber);

    }

}
