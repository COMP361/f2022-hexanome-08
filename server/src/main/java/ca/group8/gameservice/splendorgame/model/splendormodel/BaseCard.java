package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class BaseCard implements DevelopmentCard{
    private int cardId;
    private int prestigePoints;
    private EnumMap<Colour,Integer> price;
    private int level;
    private Optional<Colour> gemColor;
    private boolean isPaired = false;
    private int pairedCardId = -1;

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
