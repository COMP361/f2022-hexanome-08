import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card c1;

    @BeforeEach
    void setup() {
        EnumMap<Colour, Integer> price = new EnumMap<>(Colour.class);
        price.put(Colour.GOLD,1);
        c1 = new Card(2, price,"c1");
    }
    @Test
    void getPrestigePoints() {
        assert (2 == c1.getPrestigePoints());
    }

    @Test
    void getPrice() {
        EnumMap<Colour, Integer> price1 = new EnumMap<>(Colour.class);
        price1.put(Colour.GOLD,1);
        assert (Objects.equals(price1.get(Colour.GOLD), c1.getPrice().get(Colour.GOLD)));
    }

    @Test
    void getCardName() {
        String name = "c1";
        assert (name.equals(c1.getCardName()));
    }
}