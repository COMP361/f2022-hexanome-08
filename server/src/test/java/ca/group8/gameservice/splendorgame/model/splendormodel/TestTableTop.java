package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableTopTest {

    TableTop t1;


    @BeforeEach
    void setup() {
        List<String> playerNames = new ArrayList<String>();
        playerNames.add("P1");
        playerNames.add("P2");
        playerNames.add("P3");
        playerNames.add("P4");
        List<Extension> DLC = new ArrayList<Extension>();
        DLC.add(Extension.CITY);
        DLC.add(Extension.BASE);
        DLC.add(Extension.ORIENT);
        DLC.add(Extension.TRADING_POST);
        t1 = new TableTop(playerNames,DLC);


    }
    @Test
    void getBoard() {
        Board b1 = t1.getBoard(Extension.CITY);
        assert (b1.getClass().toString().equals("class ca.group8.gameservice.splendorgame.model.splendormodel.CityBoard"));
        Board b2 = t1.getBoard(Extension.BASE);
        assert (b2.getClass().toString().equals("class ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard"));
        Board b3 = t1.getBoard(Extension.ORIENT);
        assert (b3.getClass().toString().equals("class ca.group8.gameservice.splendorgame.model.splendormodel.OrientBoard"));
        Board b4 = t1.getBoard(Extension.TRADING_POST);
        assert (b4.getClass().toString().equals("class ca.group8.gameservice.splendorgame.model.splendormodel.TraderBoard"));
    }

    @Test
    void isEmpty() {
    }

    @Test
    void getBank() {
    }
}