package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import ca.group8.gameservice.splendorgame.testutils.CardFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.EnumMap;


public class TestDevelopmentCard {
    EnumMap<Colour, Integer> price = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    DevelopmentCard baseCard = new DevelopmentCard(3, price, "c1",
        1, Colour.BLUE, 1, new ArrayList<>());
    List<CardEffect> orientEffects = new ArrayList<>();
    DevelopmentCard orientCard;
    @BeforeEach
    void setUp() {
        orientEffects.add(CardEffect.FREE_CARD);
        orientEffects.add(CardEffect.SATCHEL);
        orientCard = new DevelopmentCard(3, price, "c1",
            1, Colour.ORIENT, 1, orientEffects);
    }

    @Test
    void testGetLevel() {
        assertEquals(1, baseCard.getLevel());
    }

    @Test
    void testGetGemColour() {
        assertEquals(Colour.BLUE, baseCard.getGemColour());
    }

    @Test
    void testGetPairedCard_FailCase() {
        try {
            baseCard.getPairedCard();
        } catch (SplendorGameException e) {
            assertEquals("Card is not paired yet", e.getMessage());
        }
    }

    /**
     * Implicitly tested the override equals method of DevelopmentCard
     */
    @Test
    void testPairCardAndGetPairedCard_SuccessCase() {
        setUp();
        assertFalse(baseCard.isPaired());
        assertEquals(1, baseCard.getGemNumber());
        baseCard.pairCard(orientCard);
        DevelopmentCard pairedCard = null;
        try {
            pairedCard = baseCard.getPairedCard();
        } catch (SplendorGameException e) {
            System.out.println("Card not paired for some reasons");
        }

        assertEquals(orientCard, pairedCard);
        assertTrue(baseCard.isPaired());
        assertEquals(2, baseCard.getGemNumber());

    }


    @Test
    void testGetPurchaseEffects() {
        setUp();
        assertEquals(orientEffects, orientCard.getPurchaseEffects());
        assertEquals(new ArrayList<>(), baseCard.getPurchaseEffects());
    }


    @Test
    void testIsBaseCard() {
        setUp();
        assertFalse(orientCard.isBaseCard());
        assertTrue(baseCard.isBaseCard());
    }


    @Test
    void testHasRegularGemColour() {
        setUp();
        assertFalse(orientCard.hasRegularGemColour());
        assertTrue(baseCard.hasRegularGemColour());
    }

    @Test
    void testEquals() {
        DevelopmentCard c1 = new DevelopmentCard(5, new EnumMap<>(Colour.class),
            "name", 1, Colour.BLACK, 1, new ArrayList<>());
        DevelopmentCard c2 = new DevelopmentCard(5, new EnumMap<>(Colour.class),
            "name", 1, Colour.BLACK, 1, new ArrayList<>());

        DevelopmentCard c3 = new DevelopmentCard(5, new EnumMap<>(Colour.class),
            "name", 2, Colour.BLACK, 1, new ArrayList<>());

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
    }

    @Test
    void testCanBeBought_True1() {
        EnumMap<Colour, Integer> price = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 2);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
        }};

        EnumMap<Colour, Integer> playerWealth = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 1);
        }};

        DevelopmentCard baseCard = new DevelopmentCard(3, price, "c1",
            1, Colour.BLUE, 1, new ArrayList<>());

        assertEquals(1, baseCard.canBeBought(true, playerWealth, 0));
    }


    @Test
    void testCanBeBought_True2() {
        EnumMap<Colour, Integer> price = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 4);
            put(Colour.RED, 0);
            put(Colour.BLACK, 1);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
        }};

        EnumMap<Colour, Integer> playerWealth = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 2);
        }};

        DevelopmentCard baseCard = new DevelopmentCard(3, price, "c1",
            1, Colour.BLUE, 1, new ArrayList<>());

        assertEquals(2, baseCard.canBeBought(false, playerWealth, 0));
    }

    @Test
    void testCanBeBought_True3() {
        EnumMap<Colour, Integer> price = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 4);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
        }};

        EnumMap<Colour, Integer> playerWealth = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 0);
        }};

        DevelopmentCard baseCard = new DevelopmentCard(3, price, "c1",
            1, Colour.BLUE, 1, new ArrayList<>());

        assertEquals(2, baseCard.canBeBought(true, playerWealth, 1));
    }

    @Test
    void testCanBeBought_True4() {
        EnumMap<Colour, Integer> price = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 4);
            put(Colour.RED, 0);
            put(Colour.BLACK, 1);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
        }};

        EnumMap<Colour, Integer> playerWealth = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 0);
        }};

        DevelopmentCard baseCard = new DevelopmentCard(3, price, "c1",
            1, Colour.BLUE, 1, new ArrayList<>());

        assertEquals(2, baseCard.canBeBought(true, playerWealth, 1));
    }

    @Test
    void testCanBeBought_False1() {
        EnumMap<Colour, Integer> price = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 2);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 1);
        }};

        EnumMap<Colour, Integer> playerWealth = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 1);
        }};

        DevelopmentCard baseCard = new DevelopmentCard(3, price, "c1",
            1, Colour.BLUE, 1, new ArrayList<>());

        assertEquals(-1, baseCard.canBeBought(true, playerWealth, 0));
    }

    @Test
    void testCanBeBought_False2() {
        EnumMap<Colour, Integer> price = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 1);
            put(Colour.BLACK, 1);
            put(Colour.GREEN, 1);
            put(Colour.WHITE, 1);
        }};

        EnumMap<Colour, Integer> playerWealth = new EnumMap<Colour,Integer>(Colour.class){{
            put(Colour.BLUE, 3);
            put(Colour.RED, 0);
            put(Colour.BLACK, 0);
            put(Colour.GREEN, 0);
            put(Colour.WHITE, 0);
            put(Colour.GOLD, 2);
        }};

        DevelopmentCard baseCard = new DevelopmentCard(3, price, "c1",
            1, Colour.BLUE, 1, new ArrayList<>());

        assertEquals(-1, baseCard.canBeBought(true, playerWealth, 0));
    }

}
