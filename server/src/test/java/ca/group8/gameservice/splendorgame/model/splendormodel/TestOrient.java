package ca.group8.gameservice.splendorgame.model.splendormodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestOrient {
    EnumMap<Colour, Integer> price = new EnumMap<>(Colour.class);
    OrientCard oc1 = new OrientCard(2,price,"Steve",2,Colour.BLUE,false,"-1",1, ca.group8.gameservice.splendorgame.model.splendormodel.OrientType.BURN);

    @Test
    void getType(){
        assertEquals(oc1.getType(),OrientType.BURN);
        assertNotEquals(oc1.getType(),OrientType.PAIR);
    }

}
