package ca.group8.gameservice.splendorgame.testutils;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.CityCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class CardFactory {
  private static CardFactory cardFactory = null;
  private CardFactory() {}

  public static CardFactory getInstance() {
    if (cardFactory == null) {
      cardFactory = new CardFactory();
    }
    return cardFactory;
  }

  public DevelopmentCard getOneBaseCard(Colour colour, int level) {
    EnumMap<Colour, Integer> price = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    for (Colour c : price.keySet()) {
      if (!c.equals(Colour.GOLD)) {
        price.put(c, new Random().nextInt(6));
      }
    }
    return new DevelopmentCard(5,
        price, "c1", level, colour, 1,
        new ArrayList<>());
  }

  public DevelopmentCard getOneBaseCard(Colour colour, int level, EnumMap<Colour, Integer> price) {

    return new DevelopmentCard(5,
        price, "c1", level, colour, 1,
        new ArrayList<>());
  }

  public DevelopmentCard getOneOrientCard(Colour colour, int level, List<CardEffect> effects) {
    EnumMap<Colour, Integer> price = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    if (effects.contains(CardEffect.BURN_CARD)) {
      // always purchase by burning 2 blue gems
      price.put(Colour.BLUE, 2);
    } else if (effects.contains(CardEffect.DOUBLE_GOLD)) {
      return new DevelopmentCard(5,
          price, "c1", level, Colour.GOLD, 2,
          effects);
    } else {
      for (Colour c : price.keySet()) {
        if (!c.equals(Colour.GOLD)) {
          price.put(c, new Random().nextInt(6));
        }
      }
    }


    int gemNum = 1;
    if (effects.isEmpty()) {
      // if orient card has no effect, then return gem number = 2
      gemNum = 2;
    }
    return new DevelopmentCard(5,
        price, "c1", level, colour, gemNum,
        effects);
  }

  public DevelopmentCard getOneOrientCard(Colour colour, int level, List<CardEffect> effects,
                                          EnumMap<Colour, Integer> price) {
    int gemNum = 1;
    if (effects.isEmpty()) {
      // if orient card has no effect, then return gem number = 2
      gemNum = 2;
    }
    return new DevelopmentCard(5,
        price, "c1", level, colour, gemNum,
        effects);
  }

  public NobleCard getOneNobleCard() {
    EnumMap<Colour, Integer> price = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    for (Colour c : price.keySet()) {
      if (!c.equals(Colour.GOLD)) {
        price.put(c, new Random().nextInt(6));
      }
    }

    return new NobleCard(5,price, "n1");
  }


  public CityCard getOneCityCard(int anyCount, int points) {
    EnumMap<Colour, Integer> price = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    for (Colour c : price.keySet()) {
      if (!c.equals(Colour.GOLD)) {
        price.put(c, new Random().nextInt(6));
      }
    }

    return new CityCard(points, price, "city", anyCount);
  }

}
