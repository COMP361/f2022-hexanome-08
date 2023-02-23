package ca.group8.gameservice.splendorgame.controller.communicationbeans;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.Action;
import ca.group8.gameservice.splendorgame.model.splendormodel.Board;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.isharipov.gson.adapters.PolymorphDeserializer;

/**
 * A helper singleton class that helps to resolve all abstract.
 * serializing & deserializing json problems for splendor game.
 *
 * Every abstract class was serialized/deserialized using the repository from:
 *  * https://medium.com/@iliamsharipov_56660/handling-polymorphism-with-gson-f4a702014ffe.
 *  * Thank him so much!!!!!!!!!!!!!!!
 */
public class SplendorJsonHelper {
  private static SplendorJsonHelper instance = null;

  private static Gson gson;

  private SplendorJsonHelper() {
    gson = new GsonBuilder()
        .registerTypeAdapter(Action.class, new PolymorphDeserializer<Action>())
        .registerTypeAdapter(Board.class, new PolymorphDeserializer<Board>())
        .registerTypeAdapter(Power.class, new PolymorphDeserializer<Power>())
        .registerTypeAdapter(Card.class, new PolymorphDeserializer<Card>())
        .create();
  }

  public static SplendorJsonHelper getInstance() {
    if (instance  == null) {
      instance = new SplendorJsonHelper();
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

}
