package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestDevelopmentCard {
    EnumMap<Colour, Integer> price = new EnumMap<>(Colour.class);
    DevelopmentCard dc1;

    /*
    @BeforeEach
    void setUp(){
        dc1 = new DevelopmentCard(2,price,"Steve",
                2,Colour.BLUE,false,"-1",1);
    }

    @Test
    void testGetters(){
        assertEquals(dc1.getPrestigePoints(),2);
        assertEquals(dc1.getPairedCardId(),"-1");
        assertEquals(dc1.getCardName(),"Steve");
        assertEquals(dc1.getLevel(),2);
        assertEquals(dc1.getGemColour(),Colour.BLUE);
        assertEquals(dc1.getGemNumber(),1);
        assertEquals(dc1.isPaired(),false);
    }

    @Test
    void testSetters(){
        dc1.setPaired(true);
        dc1.setPairedCardId("123");
        assertEquals(dc1.isPaired(),true);
        assertEquals(dc1.getPairedCardId(),"123");
    }

     */
}
