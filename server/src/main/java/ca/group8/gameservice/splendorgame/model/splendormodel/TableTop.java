package ca.group8.gameservice.splendorgame.model.splendormodel;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.ResourceUtils;

/**
 * Everything visible on the table when playing a board game.
 */
public class TableTop implements BroadcastContent {

  private final List<NobleCard> nobles;
  private final BaseBoard baseBoard;
  private final Bank bank;


  // TODO: implement orientBoard later WITHOUT Optional

  /**
   * Constructor.
   */
  public TableTop(int numOfPlayers) {
    this.nobles = initializeNobles(numOfPlayers);
    this.baseBoard = new BaseBoard(generateBaseCards());
    bank = new Bank(numOfPlayers);
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  private List<NobleCard> initializeNobles(int numOfPlayers) {
    List<NobleCard> allNobles = generateNobleCards();
    Collections.shuffle(allNobles);
    List<NobleCard> requiredNobles = new ArrayList<>();
    for (int i = 0; i < numOfPlayers + 1; i++) {
      requiredNobles.add(allNobles.get(i));
    }
    return requiredNobles;
  }


  private BaseCard parseCardObject(JSONObject card) {
    String cardName = (String) card.get("cardName");
    int cardLevel = ((Long) card.get("level")).intValue();
    int prestigePoints = ((Long) card.get("prestigePoints")).intValue();
    Colour gemColour = Colour.valueOf((String) card.get("gemColour"));
    int gemNumber = ((Long) card.get("gemNumber")).intValue();
    Boolean isPaired = (Boolean) card.get("isPaired");
    String pairedCardId = (String) card.get("pairedCardId");
    EnumMap<Colour, Integer> price = parsePriceObject((JSONObject) card.get("price"));
    return new BaseCard(prestigePoints, price, cardName,
        cardLevel, gemColour, isPaired, pairedCardId, gemNumber);
  }

  private NobleCard parseNobleObject(JSONObject card) {
    String cardName = (String) card.get("cardName");
    int prestigePoints = ((Long) card.get("prestigePoints")).intValue();
    EnumMap<Colour, Integer> price = parsePriceObject((JSONObject) card.get("price"));
    return new NobleCard(prestigePoints, price, cardName);
  }


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


  private List<BaseCard> generateBaseCards() {
    JSONParser jsonParser = new JSONParser();
    List<BaseCard> resultCards = new ArrayList<>();
    try (FileReader reader = new
        FileReader(ResourceUtils.getFile("classpath:cardinfo_basecard.json"))) {
      Object obj = jsonParser.parse(reader);
      JSONArray cardList = (JSONArray) obj;
      for (Object o : cardList) {
        BaseCard c = parseCardObject((JSONObject) o);
        resultCards.add(c);
      }
      return resultCards;
    } catch (ParseException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<NobleCard> generateNobleCards() {
    JSONParser jsonParser = new JSONParser();
    List<NobleCard> resultCards = new ArrayList<>();
    // write unit tests
    try (FileReader reader = new
        FileReader(ResourceUtils.getFile("classpath:cardinfo_noble.json"))) {
      Object obj = jsonParser.parse(reader);
      JSONArray cardList = (JSONArray) obj;
      for (Object o : cardList) {
        NobleCard nb = parseNobleObject((JSONObject) o);
        resultCards.add(nb);
      }
      return resultCards;
    } catch (ParseException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public BaseBoard getBaseBoard() {
    return baseBoard;
  }

  public List<NobleCard> getNobles() {
    return nobles;
  }

  public Bank getBank() {
    return bank;
  }


}
