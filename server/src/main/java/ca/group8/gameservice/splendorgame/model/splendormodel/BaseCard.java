package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class BaseCard extends DevelopmentCard {

    public BaseCard(int paramPrestigePoints, EnumMap<Colour,Integer> paramPrice,
                    int paramLevel, Optional<Colour> paramGemColour, int paramGemNumber){
        super(paramPrestigePoints, paramPrice, paramLevel, paramGemColour, paramGemNumber);
    }

}
