package ca.group8.gameservice.splendorgame.controller;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.Action;
import ca.group8.gameservice.splendorgame.model.splendormodel.Board;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.isharipov.gson.adapters.PolymorphDeserializer;
import java.util.EnumMap;

/**
 * A helper singleton class that helps to resolve all abstract.
 * serializing & deserializing json problems for splendor game.
 *
 * <p>Every abstract class was serialized/deserialized using the repository from:
 * https://medium.com/@iliamsharipov_56660/handling-polymorphism-with-gson-f4a702014ffe.
 * Thank him so much!!!!!!!!!!!!!!!
 */

public class SplendorDevHelper {
  private static SplendorDevHelper instance = null;

  private static Gson gson;

  private static EnumMap<Colour, Integer> rawTokenColoursMap;

  private SplendorDevHelper() {
    gson = new GsonBuilder()
        .registerTypeAdapter(Action.class, new PolymorphDeserializer<Action>())
        .registerTypeAdapter(Board.class, new PolymorphDeserializer<Board>())
        .registerTypeAdapter(Power.class, new PolymorphDeserializer<Power>())
        .registerTypeAdapter(Card.class, new PolymorphDeserializer<Card>())
        .create();
    rawTokenColoursMap = new EnumMap<>(Colour.class) {{
        put(Colour.BLUE, 0);
        put(Colour.RED, 0);
        put(Colour.BLACK, 0);
        put(Colour.GREEN, 0);
        put(Colour.WHITE, 0);
        put(Colour.GOLD, 0);
      }};
  }

  /**
   * getInstance.
   *
   * @return return a SplendorDevHelper
   */
  public static SplendorDevHelper getInstance() {
    if (instance == null) {
      instance = new SplendorDevHelper();
    }
    return instance;
  }

  /**
   * The Gson object that handles the complicated abstract json parse/un-parse problem.
   *
   * @return the amazing Gson object
   */
  public Gson getGson() {
    return gson;
  }


  /**
   * A raw map of token colours (red, blue, black, white, green, gold), value = 0.
   *
   * @return a list of token colours
   */
  public EnumMap<Colour, Integer> getRawTokenColoursMap() {
    return new EnumMap<>(rawTokenColoursMap);
  }

}
