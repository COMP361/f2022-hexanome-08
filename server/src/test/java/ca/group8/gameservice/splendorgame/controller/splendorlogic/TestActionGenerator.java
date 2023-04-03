package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.CityCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.OrientBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TraderBoard;
import ca.group8.gameservice.splendorgame.testutils.CardFactory;
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
    List<Extension> extensions = Arrays.asList(Extension.BASE, Extension.ORIENT,Extension.TRADING_POST, Extension.CITY);
    List<String> players = Arrays.asList("ruoyu", "Bob");
    ActionGenerator actionGenerator;
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank(2);
        Map<String, Map<String, Action>> actionMap = new HashMap<>();
        actionMap.put("ruoyu", new HashMap<>());
        actionMap.put("Bob", new HashMap<>());
        actionGenerator = new ActionGenerator(actionMap, new TableTop(players, extensions));
        playerInGame = new PlayerInGame("Bob");
    }

    @Test
    void testUpdateBonusTokenPowerActions() {
        actionGenerator.updateBonusTokenPowerActions(playerInGame);
        Map<String, Action> playerActionMap = actionGenerator.getPlayerActionMaps().get(playerInGame.getName());
        assertEquals(5, playerActionMap.size());
    }


    @Test
    void testUpdateClaimNobleActions() {
        List<Position> positions = Arrays.asList(
            new Position(0, 1),
            new Position(0, 2));

        List<NobleCard> nobleCards = Arrays.asList(
          new NobleCard(5,new EnumMap<>(Colour.class), "n1"),
            new NobleCard(3,new EnumMap<>(Colour.class), "n2")
        );
        String name = playerInGame.getName();
        actionGenerator.updateClaimNobleActions(positions, nobleCards, playerInGame);
        assertEquals(2, actionGenerator.getPlayerActionMaps().get(name).size());
    }


    @Test
    void testUpdateReturnTokenActions_extraToken_1() {
        int extraTokenCount = 1;
        EnumMap<Colour, Integer> tokensTaken = SplendorDevHelper.getInstance()
            .getRawTokenColoursMap();
        for (Colour c : tokensTaken.keySet()) {
            if (!c.equals(Colour.GOLD)) {
                tokensTaken.put(c, 1);
            }
        }
        // remove the tokens from the bank
        actionGenerator.getTableTop().getBank().takeToken(tokensTaken);

        // give valid amount of tokens to player
        playerInGame.getTokenHand().addToken(tokensTaken);

        // update the actions
        actionGenerator.updateReturnTokenActions(extraTokenCount, playerInGame);
        assertEquals(5, actionGenerator.getPlayerActionMaps().get(playerInGame.getName()).size());
    }

    @Test
    void testUpdateReturnTokenActions_extraToken_2() {
        int extraTokenCount = 2;
        EnumMap<Colour, Integer> tokensTaken = SplendorDevHelper.getInstance()
            .getRawTokenColoursMap();
        for (Colour c : tokensTaken.keySet()) {
            if (!c.equals(Colour.GOLD)) {
                tokensTaken.put(c, 2);
            }
        }
        // remove the tokens from the bank
        actionGenerator.getTableTop().getBank().takeToken(tokensTaken);

        // give valid amount of tokens to player
        playerInGame.getTokenHand().addToken(tokensTaken);

        // update the actions
        actionGenerator.updateReturnTokenActions(extraTokenCount, playerInGame);
        assertEquals(15, actionGenerator.getPlayerActionMaps().get(playerInGame.getName()).size());
    }

    @Test
    void testUpdateReturnTokenActions_extraToken_3() {
        int extraTokenCount = 3;
        EnumMap<Colour, Integer> tokensTaken = SplendorDevHelper.getInstance()
            .getRawTokenColoursMap();
        for (Colour c : tokensTaken.keySet()) {
            if (!c.equals(Colour.GOLD)) {
                tokensTaken.put(c, 3);
            }
        }
        // remove the tokens from the bank
        actionGenerator.getTableTop().getBank().takeToken(tokensTaken);

        // give valid amount of tokens to player
        playerInGame.getTokenHand().addToken(tokensTaken);

        // update the actions
        actionGenerator.updateReturnTokenActions(extraTokenCount, playerInGame);
        assertEquals(35, actionGenerator.getPlayerActionMaps().get(playerInGame.getName()).size());
    }


    @Test
    void testUpdateClaimCityActions() {
        List<Integer> cityCardIndices = Arrays.asList(1, 2);
        String name = playerInGame.getName();
        actionGenerator.updateClaimCityActions(cityCardIndices, playerInGame.getName());
        assertEquals(2, actionGenerator.getPlayerActionMaps().get(name).size());

    }



    @Test
    void testUpdateCascadeActions_BURN_NoPair() {
        EnumMap<Colour, Integer> price = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price.put(Colour.RED, 2);
        DevelopmentCard purchasedCard = CardFactory.getInstance()
            .getOneOrientCard(Colour.GREEN,3,List.of(CardEffect.BURN_CARD));

        for (int i = 0; i < 2; i++) {
            DevelopmentCard cardInHand = CardFactory.getInstance().getOneBaseCard(Colour.BLUE,2);
            playerInGame.getPurchasedHand().addDevelopmentCard(cardInHand);
        }
        actionGenerator.updateCascadeActions(playerInGame,purchasedCard, CardEffect.BURN_CARD);
        assertEquals(2, actionGenerator.getPlayerActionMaps().get(playerInGame.getName()).size());
    }


    @Test
    void testUpdateCascadeActions_BURN_WithPair() {
        EnumMap<Colour, Integer> price = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price.put(Colour.RED, 2);
        DevelopmentCard purchasedCard = CardFactory.getInstance()
            .getOneOrientCard(Colour.GREEN,3,List.of(CardEffect.BURN_CARD));

        for (int i = 0; i < 2; i++) {
            DevelopmentCard cardInHand = CardFactory.getInstance().getOneBaseCard(Colour.BLUE,2);
            playerInGame.getPurchasedHand().addDevelopmentCard(cardInHand);
        }

        DevelopmentCard pariCard = CardFactory.getInstance().getOneOrientCard(Colour.ORIENT, 1, List.of(CardEffect.SATCHEL));
        playerInGame.getPurchasedHand().getDevelopmentCards().get(0).pairCard(pariCard);

        actionGenerator.updateCascadeActions(playerInGame,purchasedCard, CardEffect.BURN_CARD);
        assertEquals(1, actionGenerator.getPlayerActionMaps().get(playerInGame.getName()).size());
    }

    @Test
    void testUpdateCascadeActions_SATCHEL() {

        DevelopmentCard purchasedCard = CardFactory.getInstance().getOneBaseCard(Colour.BLUE, 2);

        for (int i = 0; i < 2; i++) {
            DevelopmentCard cardInHand = CardFactory.getInstance().getOneBaseCard(Colour.BLUE,2);
            playerInGame.getPurchasedHand().addDevelopmentCard(cardInHand);
        }
        actionGenerator.updateCascadeActions(playerInGame,purchasedCard, CardEffect.SATCHEL);
        assertEquals(2, actionGenerator.getPlayerActionMaps().get(playerInGame.getName()).size());
    }

    @Test
    void testUpdateCascadeActions_RESERVE_NOBLE() {

        DevelopmentCard purchasedCard = CardFactory.getInstance().getOneBaseCard(Colour.BLUE, 2);

        actionGenerator.updateCascadeActions(playerInGame,purchasedCard, CardEffect.RESERVE_NOBLE);
        assertEquals(3, actionGenerator.getPlayerActionMaps().get(playerInGame.getName()).size());
    }


    @Test
    void testCardsToPurchaseActions_NoCardsReserved()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method cardsToPurchaseAction = ActionGenerator.class
            .getDeclaredMethod("cardsToPurchaseAction",
                BaseBoard.class, OrientBoard.class, PlayerInGame.class);

        EnumMap<Colour, Integer> tokensTaken = SplendorDevHelper.getInstance()
            .getRawTokenColoursMap();
        for (Colour c : tokensTaken.keySet()) {
            // give player a bunch of tokens to purchase everything
            if (!c.equals(Colour.GOLD)){
                tokensTaken.put(c, 10);
            }
        }

        // give valid amount of tokens to player
        playerInGame.getTokenHand().addToken(tokensTaken);
        for (Colour c : tokensTaken.keySet()) {
            if (!c.equals(Colour.GOLD)) {
                for (int i = 0; i < 3; i++) {
                    DevelopmentCard card = CardFactory.getInstance().getOneBaseCard(c, 1);
                    playerInGame.getPurchasedHand().addDevelopmentCard(card);
                }
            }
        }

        cardsToPurchaseAction.setAccessible(true);
        BaseBoard baseBoard = (BaseBoard) actionGenerator.getTableTop().getBoard(Extension.BASE);
        OrientBoard orientBoard = (OrientBoard) actionGenerator.getTableTop().getBoard(Extension.ORIENT);
        List<Action> results = (List<Action>) cardsToPurchaseAction.invoke(actionGenerator, baseBoard, orientBoard, playerInGame);

        // player now have 2 cards of any regular colour and 10 tokens of any colour
        // all cards should be purchasable
        assertEquals(18, results.size());
        for (Action action : results) {
            assertTrue(action instanceof PurchaseAction);
        }
    }


    @Test
    void testCardsToPurchaseActions_ReservedSomeCards()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method cardsToPurchaseAction = ActionGenerator.class
            .getDeclaredMethod("cardsToPurchaseAction",
                BaseBoard.class, OrientBoard.class, PlayerInGame.class);

        EnumMap<Colour, Integer> tokensTaken = SplendorDevHelper.getInstance()
            .getRawTokenColoursMap();
        for (Colour c : tokensTaken.keySet()) {
            // give player a bunch of tokens to purchase everything
            if (!c.equals(Colour.GOLD)){
                tokensTaken.put(c, 10);
            }
        }

        // give valid amount of tokens to player
        playerInGame.getTokenHand().addToken(tokensTaken);
        for (Colour c : tokensTaken.keySet()) {
            if (!c.equals(Colour.GOLD)) {
                for (int i = 0; i < 3; i++) {
                    DevelopmentCard card = CardFactory.getInstance().getOneBaseCard(c, 1);
                    playerInGame.getPurchasedHand().addDevelopmentCard(card);
                }
            }
        }
        // adding one to reserve hand
        playerInGame.getReservedHand().addDevelopmentCard(CardFactory.getInstance().getOneBaseCard(Colour.BLUE, 1));

        cardsToPurchaseAction.setAccessible(true);
        BaseBoard baseBoard = (BaseBoard) actionGenerator.getTableTop().getBoard(Extension.BASE);
        OrientBoard orientBoard = (OrientBoard) actionGenerator.getTableTop().getBoard(Extension.ORIENT);
        List<Action> results = (List<Action>) cardsToPurchaseAction.invoke(actionGenerator, baseBoard, orientBoard, playerInGame);

        // player now have 2 cards of any regular colour and 10 tokens of any colour
        // all cards should be purchasable
        // then we should be able to purchase 19 cards
        assertEquals(19, results.size());
        for (Action action : results) {
            assertTrue(action instanceof PurchaseAction);
        }
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

    @Test
    void testGenerateTakeTokenActions_35Actions_withPowerOn()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TableTop tableTop = actionGenerator.getTableTop();
        TraderBoard traderBoard = (TraderBoard)tableTop.getBoard(Extension.TRADING_POST);
        Power power = traderBoard.getPlayerOnePower("Bob", PowerEffect.TWO_PLUS_ONE);
        power.unlock();

        Method generateTakeTokenActions = ActionGenerator
            .class.getDeclaredMethod("generateTakeTokenActions", Bank.class, PlayerInGame.class);
        generateTakeTokenActions.setAccessible(true);
        List<Action> results = (List<Action>) generateTakeTokenActions.invoke(actionGenerator, bank, playerInGame);
        assertEquals(30, results.size());
        for (Action action : results) {
            assertTrue(action instanceof TakeTokenAction);
        }
    }
    @Test
    void testNewCanBeBought_NoGoldInvolved1() {
        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 0);
            put(Colour.RED, 3);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 0);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);
        EnumMap<Colour, Integer> price1 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price1.put(Colour.RED, 3);
        DevelopmentCard[] orientCardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneOrientCard(Colour.RED, 1,List.of(CardEffect.DOUBLE_GOLD), price1)
        };
        assertEquals(1, actionGenerator.listOfDevCardsToPurchaseAction(orientCardsToBuy, 1, playerInGame).size());
    }

    @Test
    void testNewCanBeBought_NoGoldInvolved2() {
        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 1);
            put(Colour.RED, 1);
            put(Colour.BLACK, 2);
            put(Colour.GREEN, 1);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 0);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);
        EnumMap<Colour, Integer> price1 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price1.put(Colour.RED, 1);
        price1.put(Colour.WHITE, 1);
        price1.put(Colour.BLUE, 1);
        price1.put(Colour.BLACK, 2);
        playerInGame.getPurchasedHand().addDevelopmentCard(CardFactory.getInstance().getOneBaseCard(Colour.WHITE, 1, price1));

        DevelopmentCard[] cardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneBaseCard(Colour.WHITE, 1, price1)
        };

        System.out.println(playerInGame.getWealth());
        System.out.println("Tokens in hand: " + playerInGame.getTokenHand().getAllTokens());
        System.out.println("Gems in hand: " + playerInGame.getTotalGems());
        PurchaseAction action = (PurchaseAction) actionGenerator.listOfDevCardsToPurchaseAction(cardsToBuy, 1, playerInGame).get(0);
        System.out.println("Tokens to be paid: " + action.getTokensToBePaid());
    }

    @Test
    void testNewCanBeBought_NoGoldInvolved3() {
        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 1);
            put(Colour.RED, 1);
            put(Colour.BLACK, 1);
            put(Colour.GREEN, 1);
            put(Colour.WHITE, 1);
            put(Colour.GOLD, 0);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);
        EnumMap<Colour, Integer> price1 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price1.put(Colour.RED, 1);
        price1.put(Colour.WHITE, 1);
        price1.put(Colour.BLUE, 1);
        price1.put(Colour.BLACK, 2);
        playerInGame.getPurchasedHand().addDevelopmentCard(CardFactory.getInstance().getOneBaseCard(Colour.BLACK, 1, price1));

        DevelopmentCard[] cardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneBaseCard(Colour.GREEN, 1, price1)
        };

        System.out.println(playerInGame.getWealth());
        System.out.println("Tokens in hand: " + playerInGame.getTokenHand().getAllTokens());
        System.out.println("Gems in hand: " + playerInGame.getTotalGems());
        PurchaseAction action = (PurchaseAction) actionGenerator.listOfDevCardsToPurchaseAction(cardsToBuy, 1, playerInGame).get(0);
        System.out.println("Tokens to be paid: " + action.getTokensToBePaid());
    }

    @Test
    void testNewCanBeBought_NoGoldInvolved4() {
        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 1);
            put(Colour.RED, 1);
            put(Colour.BLACK, 1);
            put(Colour.GREEN, 1);
            put(Colour.WHITE, 1);
            put(Colour.GOLD, 0);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);
        EnumMap<Colour, Integer> price1 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price1.put(Colour.RED, 1);
        price1.put(Colour.WHITE, 1);
        price1.put(Colour.BLUE, 1);
        price1.put(Colour.BLACK, 2);
        playerInGame.getPurchasedHand().addDevelopmentCard(CardFactory.getInstance().getOneBaseCard(Colour.BLACK, 1, price1));
        playerInGame.getPurchasedHand().addDevelopmentCard(CardFactory.getInstance().getOneBaseCard(Colour.BLACK, 1, price1));

        DevelopmentCard[] cardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneBaseCard(Colour.GREEN, 1, price1)
        };

        System.out.println(playerInGame.getWealth());
        System.out.println("Tokens in hand: " + playerInGame.getTokenHand().getAllTokens());
        System.out.println("Gems in hand: " + playerInGame.getTotalGems());
        PurchaseAction action = (PurchaseAction) actionGenerator.listOfDevCardsToPurchaseAction(cardsToBuy, 1, playerInGame).get(0);
        System.out.println("Tokens to be paid: " + action.getTokensToBePaid());
    }





    @Test
    void testNewCanBeBought_NoGoldCard() {
        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 0);
            put(Colour.RED, 2);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 1);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);
        EnumMap<Colour, Integer> price1 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        EnumMap<Colour, Integer> price2 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price1.put(Colour.RED, 3);
        price2.put(Colour.BLUE, 3);
        DevelopmentCard[] orientCardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneOrientCard(Colour.RED, 1,List.of(CardEffect.DOUBLE_GOLD), price1),
            CardFactory.getInstance().getOneOrientCard(Colour.BLUE, 1,List.of(CardEffect.DOUBLE_GOLD), price2)
        };
        assertEquals(1, actionGenerator.listOfDevCardsToPurchaseAction(orientCardsToBuy, 1, playerInGame).size());
    }

    @Test
    void testNewCanBeBought_HasGoldCard1_False() {

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        EnumMap<Colour, Integer> price3 = SplendorDevHelper.getInstance().getRawTokenColoursMap();

        price3.put(Colour.BLUE, 1);
        price3.put(Colour.WHITE, 3);
        price3.put(Colour.BLACK, 1);
        DevelopmentCard[] orientCardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneOrientCard(Colour.BLUE, 1,List.of(CardEffect.DOUBLE_GOLD), price3)
        };

        playerInGame.getPurchasedHand().addDevelopmentCard(
            CardFactory.getInstance().getOneOrientCard(Colour.GOLD, 1, List.of(CardEffect.DOUBLE_GOLD)));

        System.out.println(playerInGame.getWealth());
        List<Action> actions = actionGenerator.listOfDevCardsToPurchaseAction(orientCardsToBuy, 1, playerInGame);
        assertTrue(actions.isEmpty());
        //PurchaseAction purchaseAction = (PurchaseAction) actions.get(0);
        //System.out.println("Tokens needed to be paid: " + purchaseAction.getTokensToBePaid());
        //System.out.println("Num of Gold Token Card needed: " + purchaseAction.getGoldCardsRequired());
    }


    @Test
    void testNewCanBeBought_HasGoldCard2() {
        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 0);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);
        EnumMap<Colour, Integer> price3 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price3.put(Colour.BLUE, 4);

        DevelopmentCard[] orientCardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneOrientCard(Colour.BLUE, 1,List.of(CardEffect.DOUBLE_GOLD), price3)
        };

        playerInGame.getPurchasedHand().addDevelopmentCard(
            CardFactory.getInstance().getOneOrientCard(Colour.GOLD, 1, List.of(CardEffect.DOUBLE_GOLD)));

        System.out.println(playerInGame.getWealth());

        List<Action> actions = actionGenerator.listOfDevCardsToPurchaseAction(orientCardsToBuy, 1, playerInGame);
        PurchaseAction purchaseAction = (PurchaseAction) actions.get(0);
        System.out.println("Tokens needed to be paid: " + purchaseAction.getTokensToBePaid());
        System.out.println("Num of Gold Token Card needed: " + purchaseAction.getGoldCardsRequired());
    }

    @Test
    void testNewCanBeBought_HasGoldCard3() {
        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 1);
            put(Colour.GOLD, 0);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);

        EnumMap<Colour, Integer> price3 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price3.put(Colour.BLUE, 4);
        price3.put(Colour.WHITE, 2);

        DevelopmentCard[] orientCardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneOrientCard(Colour.BLUE, 1,List.of(CardEffect.DOUBLE_GOLD), price3)
        };

        playerInGame.getPurchasedHand().addDevelopmentCard(
            CardFactory.getInstance().getOneOrientCard(Colour.GOLD, 1, List.of(CardEffect.DOUBLE_GOLD)));

        System.out.println("Player wealth: " + playerInGame.getWealth());

        List<Action> actions = actionGenerator.listOfDevCardsToPurchaseAction(orientCardsToBuy, 1, playerInGame);
        PurchaseAction purchaseAction = (PurchaseAction) actions.get(0);
        System.out.println("Tokens needed to be paid: " + purchaseAction.getTokensToBePaid());
        System.out.println("Num of Gold Token Card needed: " + purchaseAction.getGoldCardsRequired());

    }

    @Test
    void testNewCanBeBought_HasGoldCard4() {
        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 2);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 2);
            put(Colour.GOLD, 0);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);

        EnumMap<Colour, Integer> price3 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price3.put(Colour.BLUE, 4);
        price3.put(Colour.WHITE, 2);

        DevelopmentCard[] orientCardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneOrientCard(Colour.BLUE, 1,List.of(CardEffect.DOUBLE_GOLD), price3)
        };

        playerInGame.getPurchasedHand().addDevelopmentCard(
            CardFactory.getInstance().getOneOrientCard(Colour.GOLD, 1, List.of(CardEffect.DOUBLE_GOLD)));

        System.out.println("Player wealth: " + playerInGame.getWealth());
        System.out.println("Player gold card: " + playerInGame
            .getPurchasedHand()
            .getDevelopmentCards()
            .stream().filter(c -> c.getGemColour() == Colour.GOLD).count());

        List<Action> actions = actionGenerator.listOfDevCardsToPurchaseAction(orientCardsToBuy, 1, playerInGame);
        PurchaseAction purchaseAction = (PurchaseAction) actions.get(0);
        System.out.println("Tokens needed to be paid: " + purchaseAction.getTokensToBePaid());
        System.out.println("Num of Gold Token Card needed: " + purchaseAction.getGoldCardsRequired());

    }


    @Test
    void testNewCanBeBought_HasGoldCard_PowerOn_True() {
        TableTop tableTop = actionGenerator.getTableTop();
        TraderBoard traderBoard = (TraderBoard)tableTop.getBoard(Extension.TRADING_POST);
        Power power = traderBoard.getPlayerOnePower("ruoyu", PowerEffect.DOUBLE_GOLD);
        power.unlock();

        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 2);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 2);
            put(Colour.GOLD, 0);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);

        EnumMap<Colour, Integer> price3 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price3.put(Colour.BLUE, 4);
        price3.put(Colour.WHITE, 4);

        DevelopmentCard[] orientCardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneOrientCard(Colour.BLUE, 1,List.of(CardEffect.DOUBLE_GOLD), price3)
        };

        playerInGame.getPurchasedHand().addDevelopmentCard(
            CardFactory.getInstance().getOneOrientCard(Colour.GOLD, 1, List.of(CardEffect.DOUBLE_GOLD)));

        System.out.println("Player wealth: " + playerInGame.getWealth());

        List<Action> actions = actionGenerator.listOfDevCardsToPurchaseAction(orientCardsToBuy, 1, playerInGame);
        PurchaseAction purchaseAction = (PurchaseAction) actions.get(0);
        System.out.println("Tokens needed to be paid: " + purchaseAction.getTokensToBePaid());
        System.out.println("Num of Gold Token Card needed: " + purchaseAction.getGoldCardsRequired());
    }

    @Test
    void testNewCanBeBought_HasGoldCard_PowerOn_False() {
        //TableTop tableTop = actionGenerator.getTableTop();
        //TraderBoard traderBoard = (TraderBoard)tableTop.getBoard(Extension.TRADING_POST);
        //Power power = traderBoard.getPlayerOnePower("ruoyu", PowerEffect.DOUBLE_GOLD);
        //power.unlock();

        EnumMap<Colour, Integer> playerWealth = new EnumMap<>(Colour.class){{
            put(Colour.BLUE, 2);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 2);
            put(Colour.GOLD, 0);
        }};

        PlayerInGame playerInGame = new PlayerInGame("ruoyu");
        playerInGame.getTokenHand().addToken(playerWealth);

        EnumMap<Colour, Integer> price3 = SplendorDevHelper.getInstance().getRawTokenColoursMap();
        price3.put(Colour.BLUE, 4);
        price3.put(Colour.WHITE, 4);

        DevelopmentCard[] orientCardsToBuy = new DevelopmentCard[] {
            CardFactory.getInstance().getOneOrientCard(Colour.BLUE, 1,List.of(CardEffect.DOUBLE_GOLD), price3)
        };

        playerInGame.getPurchasedHand().addDevelopmentCard(
            CardFactory.getInstance().getOneOrientCard(Colour.GOLD, 1, List.of(CardEffect.DOUBLE_GOLD)));

        System.out.println("Player wealth: " + playerInGame.getWealth());

        List<Action> actions = actionGenerator.listOfDevCardsToPurchaseAction(orientCardsToBuy, 1, playerInGame);
        System.out.println(actions.size());
        //PurchaseAction purchaseAction = (PurchaseAction) actions.get(0);
        //System.out.println("Tokens needed to be paid: " + purchaseAction.getTokensToBePaid());
        //System.out.println("Num of Gold Token Card needed: " + purchaseAction.getGoldCardsRequired());
    }



}
