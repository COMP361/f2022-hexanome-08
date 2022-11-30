package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestPurchasedHand {

        PurchasedHand p1;
        Colour red = Colour.RED;
        EnumMap<Colour,Integer> price = new EnumMap<>(Colour.class);
        DevelopmentCard c1 = new DevelopmentCard(1,price,"card1",1,red,false,"-1",1);
        DevelopmentCard c2 = new DevelopmentCard(2,price,"card2",1,red,false,"-1",1);
        DevelopmentCard c3 = new DevelopmentCard(3,price,"card3",1,red,false,"-1",1);
        NobleCard n1 = new NobleCard(2, price,"Dave");

        @BeforeEach
        void setup() {
            p1 = new PurchasedHand();
        }


        @Test
        void addDevelopmentTest() {
                assertEquals(p1.getSize(),0);
                p1.addDevelopmentCard(c1);
                assertEquals(p1.getSize(),1);
                assertSame(p1.getDevelopmentCards().get(0),c1);
        }

        @Test
        void removeDevelopmentTest() {
                p1.addDevelopmentCard(c1);
                assertEquals(p1.getSize(),1);
                p1.removeDevelopmentCard(c1);
                assertEquals(p1.getSize(),0);
        }

        @Test
        void addNobleTest() {
                p1.addNobleCard(n1);
                assertEquals(p1.getNobleCards().size(),1);
                assertSame(p1.getNobleCards().get(0),n1);
        }

}
