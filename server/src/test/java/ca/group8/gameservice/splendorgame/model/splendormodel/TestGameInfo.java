//package ca.group8.gameservice.splendorgame.model.splendormodel;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import ca.group8.gameservice.splendorgame.controller.splendorlogic.Action;
//import org.junit.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class TestGameInfo {
//
//    private GameInfo gameInfo;
//    private List<String> playerNames;
//    private List<Extension> extensions;
//    private TableTop tableTop;
//    private String firstPlayer;
//
//    @BeforeEach
//    public void setUp() {
//
//        playerNames = new ArrayList<String>();
//        playerNames.add("Player1");
//        playerNames.add("Player2");
//
//        extensions = new ArrayList<Extension>();
//        extensions.add(Extension.BASE);
//        extensions.add(Extension.TRADING_POST);
//
//        gameInfo = new GameInfo(extensions, playerNames);
//        tableTop = new TableTop(playerNames, extensions);
//        firstPlayer = "Player1";
//
//        gameInfo = new GameInfo(extensions,playerNames);
//
//
//    }
//
//   /* @Test
//    void TestUpdatePlayerActionMap(){
//        Map<String, Action> actionMap = new HashMap<>();
//        actionMap.put("BuyCard", new Action("BuyCard", "Card1"));
//        gameInfo.updatePlayerActionMap("Player1", actionMap);
//        assertEquals(actionMap, gameInfo.getPlayerActionMaps().get("Player1"));
//
//    }*/
//
//    @Test
//    void testGetCurrentPlayer(){
//        assertEquals("Player1", gameInfo.getCurrentPlayer());
//    }
//
//    @Test
//    void testSetNextPlayer(){
//        gameInfo.setNextPlayer();
//        assertEquals("Player2", gameInfo.getCurrentPlayer());
//    }
//
//    @Test
//    void testAddWinner(){
//        gameInfo.addWinner("Player1");
//        assertTrue(gameInfo.getWinners().contains("Player1"));
//    }
//
//    @Test
//    void testIsFinished(){
//        assertFalse(gameInfo.isFinished());
//        gameInfo.addWinner("Player1");
//        assertTrue(gameInfo.isFinished());
//    }
//
//    @Test
//    void testGetExtensions(){
//        assertEquals(extensions, gameInfo.getExtensions());
//    }
//
//
//    @Test
//    void testIsEmpty(){
//        assertFalse(gameInfo.isEmpty());
//    }
//
//    @Test
//    void testGetFirstPlayerName(){
//        assertEquals(firstPlayer, gameInfo.getFirstPlayerName());
//    }
//
//    @Test
//    void testGetPlayerNames(){
//        assertEquals(playerNames, gameInfo.getPlayerNames());
//    }
//
//    @Test
//    void testGetNumOfPlayers(){
//        assertEquals(playerNames.size(), gameInfo.getNumOfPlayers());
//    }
//
//}
