package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestPurchasedHand {

        PurchasedHand p1;
        Colour red = Colour.RED;
        EnumMap<Colour,Integer> price = new EnumMap<>(Colour.class);
        EnumMap<Colour,Integer> price_1 = new EnumMap<>(Colour.class);
        EnumMap<Colour,Integer> price_2 = new EnumMap<>(Colour.class);
        EnumMap<Colour,Integer> price_3 = new EnumMap<>(Colour.class);
        List<CardEffect> purchaseEffects = new ArrayList<>();
        DevelopmentCard c1 = new DevelopmentCard(1,price_1,"card1",1,Colour.RED,1,purchaseEffects);
        DevelopmentCard c2 = new DevelopmentCard(2,price_2,"card2",1,Colour.BLUE,2,purchaseEffects);
        DevelopmentCard c3 = new DevelopmentCard(3,price_3,"card3",1,Colour.BLACK,1,purchaseEffects);
        DevelopmentCard c4 = new DevelopmentCard(3,price_3,"card4",1,Colour.BLACK,1,purchaseEffects);

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

        @Test
        void TestGetDevelopmentCards_Colour() {
                p1.addDevelopmentCard(c1);
                p1.addDevelopmentCard(c2);
                p1.addDevelopmentCard(c3);

                List<DevelopmentCard> result = p1.getOneColourDevelopmentCards(Colour.RED);

                for (DevelopmentCard card : result) {
                        assertEquals(Colour.RED, card.getGemColour());
                }
        }

        @Test
        void TestGetGemCountOfColour() {
                p1.addDevelopmentCard(c1);
                p1.addDevelopmentCard(c2);
                p1.addDevelopmentCard(c3);
                p1.addDevelopmentCard(c4);


                assertEquals(1,p1.getGemCountOfColour(Colour.RED));
                assertEquals(2,p1.getGemCountOfColour(Colour.BLUE));
                assertEquals(2,p1.getGemCountOfColour(Colour.BLACK));
        }

}
