package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import java.util.ArrayList;
import java.util.EnumMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class TestPurchaseAction {
  EnumMap<Colour, Integer> price = new EnumMap<Colour,Integer>(Colour.class){{
    put(Colour.BLUE, 3);
    put(Colour.RED, 0);
    put(Colour.BLACK, 2);
    put(Colour.GREEN, 0);
    put(Colour.WHITE, 0);
  }};
  DevelopmentCard card = new DevelopmentCard(3, price, "c1",
      1, Colour.BLUE, 1, new ArrayList<>());
  PurchaseAction action = new PurchaseAction(new Position(1, 1),
      card, 0, price);
}
