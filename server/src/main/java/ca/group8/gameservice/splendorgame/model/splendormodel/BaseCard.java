package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class BaseCard extends DevelopmentCard {

    public BaseCard(String paramCardId, int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice,
                    int paramLevel, Optional<Colour> paramGemColour){
        super(paramCardId,paramPrestigePoints, paramPrice, paramLevel, paramGemColour);
    }



}
