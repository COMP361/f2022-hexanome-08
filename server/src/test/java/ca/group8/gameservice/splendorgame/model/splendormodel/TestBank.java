
package ca.group8.gameservice.splendorgame.model.splendormodel;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;

class TestBank {
    //initial value of tokens should be 5
    //gold token is always initialized to 5
    Bank b1 = new Bank(3);


    @Test
    void TestInitialization_2Players() {
        Bank b2 = new Bank(2);
        EnumMap<Colour,Integer> tokensInBank = b2.getAllTokens();
        for (Colour colour : Colour.values()) {
             if (colour == Colour.ORIENT) {
                continue;
            }
            int tokenNum = tokensInBank.get(colour);
            if (colour == Colour.GOLD) {
                assertEquals(5, tokenNum);
            } else {
                assertEquals(4, tokenNum);
            }
        }
    }
    @Test
    void TestInitialization_3Players() {
        Bank b2 = new Bank(3);
        EnumMap<Colour,Integer> tokensInBank = b2.getAllTokens();
        for (Colour colour : Colour.values()) {
            if (colour == Colour.ORIENT) {
                continue;
            }
            int tokenNum = tokensInBank.get(colour);
            if (colour == Colour.GOLD) {
                assertEquals(5, tokenNum);
            } else {
                assertEquals(5, tokenNum);
            }
        }
    }


    //Removing tokens from bank
    @Test
    void TestTakeGoldToken() {
        EnumMap<Colour,Integer> token1 = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            if (colour == Colour.GOLD) {
                token1.put(colour, 1);
            } else {
                token1.put(colour, 0);
            }
        }
        b1.takeToken(token1);
        assert (b1.getAllTokens().get(Colour.GOLD) == 4);
    }

    @Test
    void TestTakeManyTokens() {
        EnumMap<Colour,Integer> token1 = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            if (colour == Colour.BLUE) {
                token1.put(colour, 1);
            } else if (colour == Colour.GREEN){
                token1.put(colour, 1);
            } else {
                token1.put(colour, 0);
            }
        }
        b1.takeToken(token1);
        assert (b1.getAllTokens().get(Colour.BLUE) == 4);
        assert (b1.getAllTokens().get(Colour.GREEN) == 4);
    }


    //Adding tokens to the bank
    @Test
    void TestReturnToken() {
        Bank b3 = new Bank(3);
        //initialize test by removing 2 green tokens from bank
        EnumMap<Colour,Integer> token2 = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            if (colour == Colour.GREEN) {
                token2.put(colour, 2);
            } else {
                token2.put(colour, 0);
            }
        }

        b3.takeToken(token2);

        //create map of 1 green token, which will be returned
        EnumMap<Colour,Integer> token1 = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            if (colour == Colour.GREEN) {
                token1.put(colour, 1);
            } else {
                token1.put(colour, 0);
            }
        }
        b3.returnToken(token1);
        int green_result = b3.getAllTokens().get(Colour.GREEN);
        assertEquals(4,green_result );
    }


    @Test
    void getAllTokens() {
        b1.getAllTokens();
        for (Colour colour:Colour.values()) {
            if (colour == Colour.ORIENT) {continue;}
            assert (b1.getAllTokens().get(colour) == 5);
        }
    }

    @Test
    void getInitialValue() {
        assert (b1.getInitialValue() == 5);
    }
}
