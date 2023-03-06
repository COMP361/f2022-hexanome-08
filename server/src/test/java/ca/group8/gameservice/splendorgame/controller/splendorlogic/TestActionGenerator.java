package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TraderBoard;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestActionGenerator {


    PlayerInGame playerInGame;
    List<Extension> extensions = Arrays.asList(Extension.BASE, Extension.ORIENT,Extension.TRADING_POST);
    List<String> players = Arrays.asList("Bob", "Tim");
    ActionGenerator actionGenerator;
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank(2);
        Map<String, Map<String, Action>> actionMap = new HashMap<>();
        actionMap.put("Bob", new HashMap<>());
        actionMap.put("Tim", new HashMap<>());
        actionGenerator = new ActionGenerator(actionMap, new TableTop(players, extensions));
        playerInGame = new PlayerInGame("Bob");
    }

    @Test
    void testGenerateTakeTokenActions_15Actions_withPower_Off()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method generateTakeTokenActions = ActionGenerator
            .class.getDeclaredMethod("generateTakeTokenActions", Bank.class, PlayerInGame.class);
        generateTakeTokenActions.setAccessible(true);
        List<Action> results = (List<Action>) generateTakeTokenActions.invoke(actionGenerator, bank, playerInGame);
        assertEquals(15, results.size());
        for (Action action : results) {
            assertTrue(action instanceof TakeTokenAction);
        }
    }

    //@Test
    //void testGenerateTakeTokenActions_35Actions_withPowerOn()
    //    throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    //    TableTop tableTop = actionGenerator.getTableTop();
    //    TraderBoard traderBoard = (TraderBoard)tableTop.getBoard(Extension.TRADING_POST);
    //    Power power = traderBoard.getPlayerOnePower("Bob", PowerEffect.TWO_PLUS_ONE);
    //    power.unlock();
    //
    //    Method generateTakeTokenActions = ActionGenerator
    //        .class.getDeclaredMethod("generateTakeTokenActions", Bank.class, PlayerInGame.class);
    //    generateTakeTokenActions.setAccessible(true);
    //    List<Action> results = (List<Action>) generateTakeTokenActions.invoke(actionGenerator, bank, playerInGame);
    //    assertEquals(35, results.size());
    //    for (Action action : results) {
    //        assertTrue(action instanceof TakeTokenAction);
    //    }
    //}

/*
    @BeforeEach
    void setUp() {
        generator = new ActionGenerator();
        ArrayList<String> names = new ArrayList<String>();
        names.add("Dave");
        names.add("Bob");
      for (Colour colour : Colour.values()) {
            price1.put(colour, 3);
            try {
            g1 = new GameInfo(names);
            playersInfo = new PlayerStates(names);
        } catch (Exception e) {
            System.out.println("TestActionGenerator can't make GameInfo");
        }
        price1 = new EnumMap<>(Colour.class);
        price2 = new EnumMap<>(Colour.class);
        int counter = 1;
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
