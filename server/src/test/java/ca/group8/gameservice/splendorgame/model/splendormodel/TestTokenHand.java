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

    @BeforeEach
    void setup() {
        t1 = new TokenHand(3);
        price1 = new EnumMap<>(Colour.class);
        price2 = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            price2.put(colour, 2);
            price1.put(colour, 1);
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
}
