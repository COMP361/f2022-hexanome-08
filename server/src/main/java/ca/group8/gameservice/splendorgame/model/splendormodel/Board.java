package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * The super abstract class that defines the behavior of all extension boards.
 *
 * <p>Every abstract class was serialized/deserialized using the repository from:
 * * https://medium.com/@iliamsharipov_56660/handling-polymorphism-with-gson-f4a702014ffe.
 * * Thank him so much!!!!!!!!!!!!!!!
 */

@JsonType(
    property = "type",
    subtypes = {
        @JsonSubtype(clazz = BaseBoard.class, name = "BaseBoard"),
        @JsonSubtype(clazz = CityBoard.class, name = "CityBoard"),
        @JsonSubtype(clazz = TraderBoard.class, name = "TraderBoard"),
        @JsonSubtype(clazz = OrientBoard.class, name = "OrientBoard")

    }
)
public abstract class Board {

  String type;

  // update the board
  public abstract void update();

  /**
   * Generate dev cards based on file name that stores the info.
   *
   * @param fileName file name that we want to generate dev cards on (base or orient)
   * @return a list of dev cards (base or orient)
   */
  protected List<DevelopmentCard> generateDevelopmentCards(String fileName) {
    String curFileName = String.format("%s.json", fileName);
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(curFileName)) {
      assert inputStream != null;
      try (InputStreamReader reader = new InputStreamReader(inputStream)) {
        DevelopmentCard[] cards =
            SplendorDevHelper.getInstance().getGson().fromJson(reader, DevelopmentCard[].class);
        return Arrays.asList(cards);
      }
    } catch (AssertionError | IOException e) {
      LoggerFactory.getLogger(Board.class).warn(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Helper method for generate the CityCards from cardinfo_city.json file
   *
   * @return a list of all city cards
   */
  protected List<CityCard> generateCityCards() {
    String curFileName = "cardinfo_citycard.json";
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(curFileName)) {
      assert inputStream != null;
      try (InputStreamReader reader = new InputStreamReader(inputStream)) {
        CityCard[] cards =
            SplendorDevHelper.getInstance().getGson().fromJson(reader, CityCard[].class);
        return Arrays.asList(cards);
      }
    } catch (AssertionError | IOException e) {
      LoggerFactory.getLogger(Board.class).warn(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Helper method for generate the NobleCards from cardinfo_noble.json file
   *
   * @return a list of all noble cards
   */
  protected List<NobleCard> generateNobleCards() {
    String curFileName = "cardinfo_noblecard.json";
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(curFileName)) {
      assert inputStream != null;
      try (InputStreamReader reader = new InputStreamReader(inputStream)) {
        NobleCard[] cards =
            SplendorDevHelper.getInstance().getGson().fromJson(reader, NobleCard[].class);
        return Arrays.asList(cards);
      }
    } catch (AssertionError | IOException e) {
      LoggerFactory.getLogger(Board.class).warn(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Call this method to rename the player names if the ones who want to play now does not.
   * match with the ones who saved this game before.
   *
   * @param playerNames the current player names who want to play this game
   */
  public void renamePlayers(List<String> playerNames) {
    // do nothing for base and orient board
  }
}
