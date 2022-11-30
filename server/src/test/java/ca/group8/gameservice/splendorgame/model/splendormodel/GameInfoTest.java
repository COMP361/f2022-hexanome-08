//package ca.group8.gameservice.splendorgame.model.splendormodel;
//
//import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
//import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GameInfoTest {
//
//    ArrayList<String> playerNames = new ArrayList<String>();
//    ArrayList<PlayerInGame> activePlayerInGames = new ArrayList<PlayerInGame>();
//    GameInfo g1;
//    TableTop tableTop;
//
//    @BeforeEach
//    void setup() throws FileNotFoundException {
//        playerNames.add("P1");
//        playerNames.add("P2");
//        PlayerInGame p1 = new PlayerInGame("P1");
//        PlayerInGame p2 = new PlayerInGame("P2");
//        activePlayerInGames.add(p1);
//        activePlayerInGames.add(p2);
//        tableTop = new TableTop(activePlayerInGames);
//        g1 = new GameInfo(playerNames);
//    }
//
//    @org.junit.jupiter.api.Test
//    void addWinner() {
//    }
//
//    @org.junit.jupiter.api.Test
//    void checkWinner() {
//    }
//
//    @org.junit.jupiter.api.Test
//    void getNumOfPlayers() {
//        assert (g1.getNumOfPlayers() == 2);
//    }
//
//    @org.junit.jupiter.api.Test
//    void isFinished() {
//        assert (!g1.isFinished());
//    }
//
//    @org.junit.jupiter.api.Test
//    void getCurrentPlayer() {
//        assert (activePlayerInGames.get(0).equals(g1.getCurrentPlayer()));
//    }
//
//    @org.junit.jupiter.api.Test
//    void setNextPlayer() {
//        g1.setNextPlayer();
//        assert (activePlayerInGames.get(1).equals(g1.getCurrentPlayer()));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getWinner() {
//        assert (g1.getWinners().size() == 0);
//    }
//
//    @org.junit.jupiter.api.Test
//    void getFirstPlayer() {
//        assert (g1.getFirstPlayer().equals("P1"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getActivePlayers() {
//        assert (g1.getActivePlayers().size() == activePlayerInGames.size());
//    }
//
//    @org.junit.jupiter.api.Test
//    void getPlayerNames() {
//        assert (playerNames.equals(g1.getPlayerNames()));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getTableTop() {
//        assert (g1.getTableTop().getPlayers().size() == activePlayerInGames.size());
//        assert (g1.getTableTop().getPlayers().get(0).equals(activePlayerInGames.get(0)));
//        assert (g1.getTableTop().getPlayers().get(1).equals(activePlayerInGames.get(1)));
//    }
//}