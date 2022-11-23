package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class BaseCard extends DevelopmentCard {

    public BaseCard(String paramCardId, int paramPrestigePoints, EnumMap<Colour,Integer> paramPrice,
                    int paramLevel, Optional<Colour> paramGemColour){
        super(paramCardId,paramPrestigePoints, paramPrice, paramLevel, paramGemColour);
    }



    public String getCardId() {
        return cardId;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }

    public EnumMap<Colour, Integer> getPrice() {
        return price;
    }

    public int getLevel() {
        return level;
    }

    public Optional<Colour> getGemColor() {
        return gemColor;
    }

    public boolean isPaired() {
        return false;
    }

    public int getPairedCardID() {
        return 0;
    }

}
