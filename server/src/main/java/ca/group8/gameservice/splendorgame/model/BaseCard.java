package main.java.ca.group8.gameservice.splendorgame.model;

import java.util.EnumMap;
import java.util.Optional;

public class BaseCard implements DevelopmentCard{
    private final int cardId;
    private final int prestigePoints;
    private final EnumMap<Colour,Integer> price;
    private final int level;
    private final boolean isPaired = false;
    private final int pairedCardId = -1;
    private Optional<Colour> gemColor;

    public BaseCard(int parId, int parPrestige, EnumMap<Colour,Integer> parPrice,
                    int parLevel, Optional<Colour> parGem){
        cardId = parId;
        prestigePoints = parPrestige;
        price = parPrice;
        level = parLevel;
        if(parGem.isPresent()){
            gemColor = parGem;
        }
    }



    public int getCardId() {
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
