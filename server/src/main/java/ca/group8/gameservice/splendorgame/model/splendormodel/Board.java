package ca.group8.gameservice.splendorgame.model.splendormodel;

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
 */
public abstract class Board {

  // update the board
  public abstract void update(Card card, int index);

  /**
   * Parse a json string to NobleCard.
   *
   * @param card noble card in json format
   * @return NobleCard instance decrypted from json
   */
  private NobleCard parseNobleObject(JSONObject card) {
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
        NobleCard nb = parseNobleObject((JSONObject) o);
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
  private DevelopmentCard parseCardObject(JSONObject card) {
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
        DevelopmentCard c = parseCardObject((JSONObject) o);
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




}


//import java.util.ArrayList;
//import java.util.Arrays;
//
///**
// * Interface for board for the three boards.
// */
//public class Board {
//
//  private final Card[][] cardBoard;
//  private final int columns;
//  private final int rows;
//
//  /**
//   * TODO: initialize method.
//   */
//  public Board(int paramHeight, int paramWidth) {
//    columns = paramWidth;
//    rows = paramHeight;
//    cardBoard = new Card[rows][columns];
//    //initialise method needed
//
//  }
//
//  public int getColumns() {
//    return columns;
//  }
//
//  public int getRows() {
//    return rows;
//  }
//
//  public void add(int row, int column, Card card) {
//    cardBoard[row][column] = card;
//  }
//
//  public Card getCard(int row, int column) {
//    return cardBoard[row][column];
//  }
//
//  boolean hasCard(Card paramCard) {
//    for (Card[] array : cardBoard) {
//      for (Card card : array) {
//        if (card.equals(paramCard)) {
//          return true;
//        }
//      }
//    }
//    return false;
//  }
//
//  /**
//   * Get x,y position of a card on the board.
//   */
//  public Position getCardPosition(Card paramCard) {
//    if (!hasCard(paramCard)) {
//      throw new IllegalArgumentException("aaaahhhh");
//    }
//
//    for (int i = 0; i < rows; i++) {
//      for (int j = 0; j < columns; j++) {
//        if (cardBoard[i][j].equals(paramCard)) {
//          return new Position(j, i);
//        }
//      }
//    }
//    return null;
//  }
//
//  /**
//   * Remove card and replace with new card from deck.
//   */
//  public Card takeAndReplaceCard(Card paramCard, Position paramPosition) {
//    Card takenCard = cardBoard[paramPosition.getY()][paramPosition.getX()];
//    cardBoard[paramPosition.getY()][paramPosition.getX()] = paramCard;
//    return takenCard;
//  }
//
//  public void remove(Position paramPosition) {
//    cardBoard[paramPosition.getY()][paramPosition.getX()] = null;
//  }
//
//  /**
//   * return a list of all cards in the board.
//   */
//  public ArrayList<Card> getCards() {
//    ArrayList<Card> allCards = new ArrayList<>();
//    for (int i = 0; i < rows; i++) {
//      allCards.addAll(Arrays.asList(cardBoard[i]).subList(0, columns));
//    }
//    return allCards;
//  }
//}
