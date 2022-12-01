<<<<<<< HEAD:server/src/test/java/ca/group8/gameservice/splendorgame/model/splendormodel/BankTest.java
//package ca.group8.gameservice.splendorgame.model.splendormodel;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.EnumMap;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class BankTest {
//    Bank b1 = new Bank(3);
//
//
//    @Test
//    void addToken() {
//        EnumMap<Colour,Integer> token1 = new EnumMap<>(Colour.class);
//        for (Colour colour : Colour.values()) {
//            if (colour == Colour.GREEN) {
//                token1.put(colour, 1);
//            } else {
//                token1.put(colour, 0);
//            }
//        }
//        b1.removeToken(token1);
//        b1.removeToken(token1);
//        b1.removeToken(token1);
//        b1.addToken(token1);
//        assert (b1.getAllTokens().get(Colour.GREEN) == 3);
//    }
//
//    @Test
//    void removeToken() {
//        EnumMap<Colour,Integer> token1 = new EnumMap<>(Colour.class);
//        for (Colour colour : Colour.values()) {
//            if (colour == Colour.GOLD) {
//                token1.put(colour, 1);
//            } else {
//                token1.put(colour, 0);
//            }
//        }
//        b1.removeToken(token1);
//        assert (b1.getAllTokens().get(Colour.GOLD) == 4);
//    }
//
//    @Test
//    void getAllTokens() {
//        for (Colour colour:Colour.values()) {
//            assert (b1.getAllTokens().get(colour) == 5);
//        }
//    }
//
//    @Test
//    void getInitialValue() {
//        assert (b1.getInitialValue() == 5);
//    }
//}
=======
package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;

class TestBank {
    Bank b1 = new Bank(3);


    @Test
    void addToken() {
        EnumMap<Colour,Integer> token1 = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            if (colour == Colour.GREEN) {
                token1.put(colour, 1);
            } else {
                token1.put(colour, 0);
            }
        }
        b1.removeToken(token1);
        b1.removeToken(token1);
        b1.removeToken(token1);
        b1.addToken(token1);
        assert (b1.getAllTokens().get(Colour.GREEN) == 3);
    }

    @Test
    void removeToken() {
        EnumMap<Colour,Integer> token1 = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            if (colour == Colour.GOLD) {
                token1.put(colour, 1);
            } else {
                token1.put(colour, 0);
            }
        }
        b1.removeToken(token1);
        assert (b1.getAllTokens().get(Colour.GOLD) == 4);
    }

    @Test
    void getAllTokens() {
        for (Colour colour:Colour.values()) {
            assert (b1.getAllTokens().get(colour) == 5);
        }
    }

    @Test
    void getInitialValue() {
        assert (b1.getInitialValue() == 5);
    }
}
>>>>>>> 16c951afa6e62f92b5103ef183ca7b0a204db2ff:server/src/test/java/ca/group8/gameservice/splendorgame/model/splendormodel/TestBank.java
