package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameInfoTest {

    ArrayList<String> playerNames = new ArrayList<String>();
    ArrayList<PlayerInGame> activePlayerInGames = new ArrayList<PlayerInGame>();
    GameInfo g1;

    @BeforeEach
    void setup(){
        playerNames.add("P1");
        playerNames.add("p2");
        PlayerInGame p1 = new PlayerInGame("P1");
        PlayerInGame p2 = new PlayerInGame("P2");
        activePlayerInGames.add(p1);
        activePlayerInGames.add(p2);
        g1 = new GameInfo(playerNames);
    }

    @org.junit.jupiter.api.Test
    void setWinner() {
    }

    @org.junit.jupiter.api.Test
    void checkWinner() {
    }

    @org.junit.jupiter.api.Test
    void getNumOfPlayers() {
        assert (g1.getNumOfPlayers() == 2);
    }

    @org.junit.jupiter.api.Test
    void isFinished() {
    }

    @org.junit.jupiter.api.Test
    void getCurrentPlayer() {
    }

    @org.junit.jupiter.api.Test
    void setNextPlayer() {
    }

    @org.junit.jupiter.api.Test
    void getWinner() {
    }

    @org.junit.jupiter.api.Test
    void getFirstPlayer() {
    }

    @org.junit.jupiter.api.Test
    void getActivePlayers() {
        assert(activePlayerInGames.equals(g1.getActivePlayers()));

    }

    @org.junit.jupiter.api.Test
    void getPlayerNames() {
        assert (playerNames.equals(g1.getPlayerNames()));
    }

    @org.junit.jupiter.api.Test
    void getTableTop() {
    }
}