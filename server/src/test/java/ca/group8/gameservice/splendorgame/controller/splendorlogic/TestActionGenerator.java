package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import static org.junit.Assert.*;

public class TestActionGenerator {

    SplendorActionListGenerator generator;
    GameInfo g1;
    Long gameID = Long.valueOf(123);

    @BeforeEach
    void setUp() {
        generator = new SplendorActionListGenerator();
        ArrayList<String> names = new ArrayList<String>();
        names.add("Dave");
        names.add("Bob");
        try {
            g1 = new GameInfo(names);
        } catch (Exception e) {
            System.out.println("TestActionGenerator can't make GameInfo");
        }
    }

    @Test
    void firstTurnActions(){

    }

    EnumMap<Colour,Integer> makeMap(int red, int blue,
                                     int green, int white, int black, int gold){
        EnumMap<Colour,Integer> price = new EnumMap<>(Colour.class);
        price.replace(Colour.RED,red);
        price.replace(Colour.BLUE,blue);
        price.replace(Colour.GREEN,green);
        price.replace(Colour.WHITE,white);
        price.replace(Colour.BLACK,black);
        price.replace(Colour.GOLD,gold);
        return price;
    }

}
