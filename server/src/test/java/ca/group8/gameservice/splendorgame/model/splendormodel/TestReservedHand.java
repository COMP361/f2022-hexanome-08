package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.Assert.*;

public class TestReservedHand {
    ReservedHand r1;
    Colour red = Colour.RED;
    EnumMap<Colour,Integer> price = new EnumMap<>(Colour.class);
    List<CardEffect> purchaseEffects = new ArrayList<>();

    DevelopmentCard c1 = new DevelopmentCard(1,price,"card1",1,red,1,purchaseEffects);
    DevelopmentCard c2 = new DevelopmentCard(2,price,"card1",1,red,1,purchaseEffects);
    DevelopmentCard c3 = new DevelopmentCard(3,price,"card1",1,red,1,purchaseEffects);
    NobleCard n1 = new NobleCard(2, price,"Dave");



    @BeforeEach
    void setup() {
        r1 = new ReservedHand();
    }

    @Test
    void isNotFullTest() {
        assertFalse (r1.isFull());
    }

    @Test
    void isFullTest() {
        r1.addDevelopmentCard(c1);
        r1.addDevelopmentCard(c2);
        r1.addDevelopmentCard(c3);
        assertTrue (r1.isFull());
    }

    @Test
    void addDevelopmentTest() {
        assertEquals(r1.getSize(),0);
        r1.addDevelopmentCard(c1);
        assertEquals(r1.getSize(),1);
        assertSame(r1.getDevelopmentCards().get(0),c1);
    }

    @Test
    void removeDevelopmentTest() {
        r1.addDevelopmentCard(c1);
        assertEquals(r1.getSize(),1);
        r1.removeDevelopmentCard(c1);
        assertEquals(r1.getSize(),0);
    }

    @Test
    void addNobleTest() {
        r1.addNobleCard(n1);
        assertSame(r1.getNobleCards().get(0),n1);
    }

    @Test
    void removeNobleTest() {
        r1.addNobleCard(n1);
        r1.removeNobleCard(n1);
        assertEquals(r1.getNobleCards().size(),0);
    }

}

