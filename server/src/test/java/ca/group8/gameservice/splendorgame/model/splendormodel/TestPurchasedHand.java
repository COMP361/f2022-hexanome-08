package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.BeforeEach;

import java.util.EnumMap;

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


}
