package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.EnumMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestTokenHand {
    TokenHand t1;
    EnumMap<Colour,Integer> price1;
    EnumMap<Colour,Integer> price2;
    EnumMap<Colour,Integer> price3;

    @BeforeEach
    void setup() {
        t1 = new TokenHand(3);
        price1 = new EnumMap<>(Colour.class);
        price2 = new EnumMap<>(Colour.class);
        price3 = new EnumMap<>(Colour.class);
        int counter = 1;
        for (Colour colour : Colour.values()) {
            price2.put(colour, 2);
            price1.put(colour, 1);
            price3.put(colour, counter);
            counter++;
        }
    }



    @Test
    void goldTokenAndAddRemoveTest() {
        assertEquals(t1.getGoldTokenNumber(),3);
        t1.removeToken(price2);
        assertEquals(t1.getGoldTokenNumber(),1);
        t1.addToken(price1);
        assertEquals(t1.getGoldTokenNumber(),2);
    }


    @Test
    public void AddRemoveTest() {
        t1.removeToken(price1);
        t1.removeToken(price2);
        t1.addToken(price3);
        EnumMap<Colour,Integer> hand = t1.getAllTokens();
        assertEquals(hand.get(Colour.RED),(Integer) 1);
        assertEquals(hand.get(Colour.BLUE),(Integer) 2);
        assertEquals(hand.get(Colour.GREEN),(Integer) 3);
        assertEquals(hand.get(Colour.WHITE),(Integer) 4);
        assertEquals(hand.get(Colour.BLACK),(Integer) 5);
        assertEquals(hand.get(Colour.GOLD),(Integer) 6);
    }
}
