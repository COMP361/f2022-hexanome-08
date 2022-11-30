//package ca.group8.gameservice.splendorgame.model.splendormodel;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.EnumMap;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class BoardTest {
//
//    Board b1;
//
//    @BeforeEach
//    void setUp() {
//        b1 = new Board(3,4);
//        for (int i = 0; i<b1.getRows(); i++) {
//            for (int j = 0; j < b1.getColumns(); j++) {
//                EnumMap<Colour, Integer> price = new EnumMap<>(Colour.class);
//                price.put(Colour.GOLD,1);
//                String name = "c"+i+j;
//                Card c1 = new Card(2, price,name);
//                b1.add(i,j,c1);
//            }
//        }
//    }
//
//    @Test
//    void add() {
//    }
//
//    @Test
//    void hasCard() {
//        EnumMap<Colour, Integer> price = new EnumMap<>(Colour.class);
//        price.put(Colour.GOLD,1);
//        Card c2 = new Card(2, price,"c11");
//        assert (b1.hasCard(c2));
//
//    }
//
//    @Test
//    void getCardPosition() {
//        EnumMap<Colour, Integer> price = new EnumMap<>(Colour.class);
//        price.put(Colour.GOLD,1);
//        Card c2 = new Card(2, price,"c11");
//        Position position = new Position(1,1);
//        assert (position.equals(b1.getCardPosition(c2)));
//
//
//    }
//
//    @Test
//    void takeAndReplaceCard() {
//        EnumMap<Colour, Integer> price = new EnumMap<>(Colour.class);
//        price.put(Colour.GOLD,1);
//        Card c2 = new Card(2, price,"c66");
//        Position position = new Position(1,1);
//        b1.takeAndReplaceCard(c2,position);
//        assert (b1.getCardPosition(c2).equals(position));
//    }
//
//    @Test
//    void getCards() {
//        assert (b1.getCards().size() == 12);
//    }
//}