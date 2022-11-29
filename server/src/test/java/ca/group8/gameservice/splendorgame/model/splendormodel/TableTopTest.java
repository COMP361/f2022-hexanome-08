package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TableTopTest {

    TableTop t1;
    ArrayList<PlayerInGame> activePlayerInGames = new ArrayList<PlayerInGame>();
    @BeforeEach
    void setup() {
        PlayerInGame p1 = new PlayerInGame("P1");
        PlayerInGame p2 = new PlayerInGame("P2");
        PlayerInGame p3 = new PlayerInGame("P3");
        activePlayerInGames.add(p1);
        activePlayerInGames.add(p2);
        activePlayerInGames.add(p3);
        t1 = new TableTop(activePlayerInGames);
    }

    @Test
    void isEmpty() {
        assert (t1.isEmpty());
    }

    @Test
    void getDecks() {
    }

    @Test
    void getPlayers() {
    }

    @Test
    void getBaseBoard() {
    }

    @Test
    void getNobleBoard() {
    }

    @Test
    void getBank() {
    }
}