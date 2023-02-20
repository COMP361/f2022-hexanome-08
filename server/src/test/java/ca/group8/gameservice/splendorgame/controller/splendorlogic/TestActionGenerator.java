package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestActionGenerator {

    ActionGenerator generator;
    GameInfo g1;
    PlayerStates playersInfo;
    Long gameID = Long.valueOf(123);
    EnumMap<Colour,Integer> price1;
    EnumMap<Colour,Integer> price2;

/*
    @BeforeEach
    void setUp() {
        generator = new ActionGenerator();
        ArrayList<String> names = new ArrayList<String>();
        names.add("Dave");
        names.add("Bob");
        try {
            g1 = new GameInfo(names);
            playersInfo = new PlayerStates(names);
        } catch (Exception e) {
            System.out.println("TestActionGenerator can't make GameInfo");
        }
        price1 = new EnumMap<>(Colour.class);
        price2 = new EnumMap<>(Colour.class);
        int counter = 1;
        for (Colour colour : Colour.values()) {
            price1.put(colour, 3);
            price2.put(colour, counter);
            counter++;
        }
    }

    /*
    @Test
    void firstTurnActions(){
        PlayerInGame daveInfo = playersInfo.getPlayersInfo().get("Dave");
        generator.generateActions(gameID,g1,daveInfo);
        Map<String, Action> listOfActions = generator.lookUpActions(gameID,"Dave");
        assertEquals(listOfActions.size(),13);
    }

     */




}
