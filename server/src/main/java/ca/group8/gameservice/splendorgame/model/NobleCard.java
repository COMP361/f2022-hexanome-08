package ca.group8.gameservice.splendorgame.model;

import java.util.EnumMap;
import java.util.Optional;

public class NobleCard extends Card{

    public NobleCard(int parId, int parPrestige, EnumMap<Colour,Integer> parPrice){
        super( parId, parPrestige, parPrice);
    }

}
