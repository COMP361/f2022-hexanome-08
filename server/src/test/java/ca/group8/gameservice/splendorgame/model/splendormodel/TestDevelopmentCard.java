package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.EnumMap;


public class TestDevelopmentCard {
    EnumMap<Colour, Integer> price = new EnumMap<>(Colour.class);
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

}
