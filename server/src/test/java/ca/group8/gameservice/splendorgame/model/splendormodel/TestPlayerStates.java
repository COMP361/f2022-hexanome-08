package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.SplendorLogicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerStates {

    PlayerStates p1;

    @BeforeEach
    void setUp() {
        List<String> playerNames = new ArrayList<String>();
        playerNames.add("P1");
        playerNames.add("P2");
        playerNames.add("P3");
        playerNames.add("P4");
        p1 = new PlayerStates(playerNames);
    }

    @Test
    void getPlayersInfo() {
        assert (p1.getPlayersInfo().size() == 4);
    }

    @Test
    void getOnePlayerInGame() throws SplendorLogicException {
        assert (p1.getOnePlayerInGame("P1").getName().equals("P1"));
        assert (p1.getOnePlayerInGame("P2").getName().equals("P2"));
        assert (p1.getOnePlayerInGame("P3").getName().equals("P3"));
        assert (p1.getOnePlayerInGame("P4").getName().equals("P4"));

    }

    @Test
    void isEmpty() {
        assert (!p1.isEmpty());
    }
}