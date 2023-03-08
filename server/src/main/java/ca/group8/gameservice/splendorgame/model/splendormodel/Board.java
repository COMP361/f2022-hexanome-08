package ca.group8.gameservice.splendorgame.model.splendormodel;

import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
   * Parse a json string to NobleCard.
   *
   * @param card noble card in json format
   * @return NobleCard instance decrypted from json
   */
  private NobleCard parseNobleCard(JSONObject card) {
    String cardName = (String) card.get("cardName");
    int prestigePoints = ((Long) card.get("prestigePoints")).intValue();
    EnumMap<Colour, Integer> price = parsePriceObject((JSONObject) card.get("price"));
    return new NobleCard(prestigePoints, price, cardName);
  }

  /**
   * Helper method for generate the NobleCards from cardinfo_noble.json file
   *
   * @return a list of all noble cards
   */
  protected List<NobleCard> generateNobleCards() {
    JSONParser jsonParser = new JSONParser();
    List<NobleCard> resultCards = new ArrayList<>();
    // write unit tests
    try (FileReader reader = new
        FileReader(ResourceUtils.getFile("classpath:cardinfo_noblecard.json"))) {
      Object obj = jsonParser.parse(reader);
      JSONArray cardList = (JSONArray) obj;
      for (Object o : cardList) {
        NobleCard nb = parseNobleCard((JSONObject) o);
        resultCards.add(nb);
      }
      return resultCards;
    } catch (ParseException | IOException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * Parse a json string to Base DevelopmentCard.
   *
   * @param card noble card in json format
   * @return NobleCard instance decrypted from json
   */
  private DevelopmentCard parseDevelopmentCard(JSONObject card) {
    String cardName = (String) card.get("cardName");
    int cardLevel = ((Long) card.get("level")).intValue();
    int prestigePoints = ((Long) card.get("prestigePoints")).intValue();
    Colour gemColour = Colour.valueOf((String) card.get("gemColour"));
    int gemNumber = ((Long) card.get("gemNumber")).intValue();
    EnumMap<Colour, Integer> price = parsePriceObject((JSONObject) card.get("price"));
    List<CardEffect> purchaseEffects = new ArrayList<>();
    JSONArray purchaseEffectsArray = (JSONArray) card.get("purchaseEffects");
    for (Object o : purchaseEffectsArray) {
      CardEffect effect = CardEffect.valueOf((String) o);
      purchaseEffects.add(effect);
    }
    return new DevelopmentCard(prestigePoints, price, cardName, cardLevel,
        gemColour, gemNumber, purchaseEffects);
  }


  /**
   * Helper for parse price that is part of Card.
   *
   * @param price price in json string format
   * @return price in EnumMap format
   */
  private EnumMap<Colour, Integer> parsePriceObject(JSONObject price) {
    EnumMap<Colour, Integer> result = new EnumMap<>(Colour.class);
    int bluePrice = ((Long) price.get("BLUE")).intValue();
    result.put(Colour.BLUE, bluePrice);
    int blackPrice = ((Long) price.get("BLACK")).intValue();
    result.put(Colour.BLACK, blackPrice);
    int redPrice = ((Long) price.get("RED")).intValue();
    result.put(Colour.RED, redPrice);
    int greenPrice = ((Long) price.get("GREEN")).intValue();
    result.put(Colour.GREEN, greenPrice);
    int whitePrice = ((Long) price.get("WHITE")).intValue();
    result.put(Colour.WHITE, whitePrice);
    return result;
  }

  /**
   * Generate dev cards based on file name that stores the info.
   *
   * @param fileName file name that we want to generate dev cards on (base or orient)
   * @return a list of dev cards (base or orient)
   */
  protected List<DevelopmentCard> generateDevelopmentCards(String fileName) {
    JSONParser jsonParser = new JSONParser();
    List<DevelopmentCard> resultCards = new ArrayList<>();
    String curFileName = String.format("classpath:%s.json", fileName);
    try (FileReader reader = new FileReader(ResourceUtils.getFile(curFileName))) {
      Object obj = jsonParser.parse(reader);
      JSONArray cardList = (JSONArray) obj;
      for (Object o : cardList) {
        DevelopmentCard c = parseDevelopmentCard((JSONObject) o);
        resultCards.add(c);
      }
      return resultCards;
    } catch (ParseException | IOException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * Parse a json string to CityCard.
   *
   * @param card city card in json format
   * @return NobleCard instance decrypted from json
   */

  private CityCard parseCityObject(JSONObject card) {
    String cardName = (String) card.get("cardName");
    int prestigePoints = ((Long) card.get("prestigePoints")).intValue();
    EnumMap<Colour, Integer> price = parsePriceObject((JSONObject) card.get("price"));
    int anyColourCount = ((Long) card.get("anyColourCount")).intValue();
    return new CityCard(prestigePoints, price, cardName, anyColourCount);
  }

  /**
   * Helper method for generate the CityCards from cardinfo_city.json file
   *
   * @return a list of all city cards
   */

  protected List<CityCard> generateCityCards() {
    JSONParser jsonParser = new JSONParser();
    List<CityCard> resultCards = new ArrayList<>();
    // write unit tests
    try (FileReader reader = new
        FileReader(ResourceUtils.getFile("classpath:cardinfo_citycard.json"))) {
      Object obj = jsonParser.parse(reader);
      JSONArray cardList = (JSONArray) obj;
      for (Object o : cardList) {
        CityCard cityCard = parseCityObject((JSONObject) o);
        resultCards.add(cityCard);
      }
      return resultCards;
    } catch (ParseException | IOException e) {
      throw new RuntimeException(e);
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
