package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class TestPosition {
    Position p1;

    @BeforeEach
    void setup() {
        p1 = new Position(3,5);
    }
    @Test
    void equalPositionTest() { assert (p1.equals(p1)); }

    @Test
    void notEqualPositionTest() {
        Position p2 = new Position(11,12);
        assertNotSame(p1,p2);
    }

    @Test
    void getYPositionTest() {
        assertEquals(p1.getY(),5);
    }

    @Test
    void getXPositionTest() {
        assertEquals(p1.getX(),3);
    }

    @Test
    void setYPositionTest() {
        p1.setY(2);
        assert (p1.getY() == 2);
    }

    @Test
    void setXPositionTest() {
        p1.setX(2);
        assert (p1.getX() == 2);
    }

}
