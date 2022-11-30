package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.Launcher;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class TableTop implements BroadcastContent {

  private Map<Integer, Deck> decks;
  private BaseBoard baseBoard;
  private NobleBoard nobleBoard;
  private Bank bank;


  // TODO: implement orientBoard later WITHOUT Optional
  //private Optional<Board> orientBoard; //
  //private Optional<Map<Integer,Deck>> orientDeck;


  public TableTop(ArrayList<PlayerInGame> playerInGames) {
    this.decks = new HashMap<>();
    for (int i = 1; i<4; i++){
      decks.put(i,new Deck(i));
    }
    initialiseBaseDecks();
    //this.playerInGames = playerInGames;
    this.baseBoard = new BaseBoard(3,4);
    this.nobleBoard = new NobleBoard(playerInGames.size()+1, 1);
    initialiseNobleBoard(playerInGames);
    initialiseDevelopmentCardBoard();
    bank = new Bank(playerInGames.size());
  }

  @Override
  public boolean isEmpty() {

    return baseBoard.getCards().isEmpty()
        && decks.isEmpty()
        //&& playerInGames.isEmpty()
        && nobleBoard.getCards().isEmpty()
        && bank.getAllTokens().isEmpty();
  }

  private void initialiseDevelopmentCardBoard(){
    for(int i=0; i<3; i++){
      for(int j=0; j<4; j++){
        baseBoard.add(i,j,decks.get(3-i).pop());
      }
    }
  }


  private void initialiseNobleBoard(ArrayList<PlayerInGame> playerInGames) {
    List<NobleCard> nobles = generateNobleCards();
    for(int i = 0; i < playerInGames.size() +1; i++){
      nobleBoard.add(i,0, nobles.get(i));
    }
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
    return new BaseCard(prestigePoints, price, cardName, cardLevel, gemColour, isPaired, pairedCardId, gemNumber);
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


  private List<BaseCard> generateBaseCards(){
    JSONParser jsonParser = new JSONParser();
    List<BaseCard> resultCards = new ArrayList<>();
    try (FileReader reader = new FileReader(ResourceUtils.getFile("classpath:cardinfo_basecard.json"))){
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
      try (FileReader reader = new FileReader(ResourceUtils.getFile("classpath:cardinfo_noble.json"))){
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

  private void initialiseBaseDecks(){
    for(BaseCard card : generateBaseCards()){
      int level = card.getLevel();
      decks.get(level).add(card);
    }
    for(int i = 1; i<4; i++){
      decks.get(i).shuffle();
    }
  }



  public Map<Integer, Deck> getDecks() {
    return decks;
  }

  public BaseBoard getBaseBoard() {
    return baseBoard;
  }

  public NobleBoard getNobleBoard() {
    return nobleBoard;
  }

  public Bank getBank() {
    return bank;
  }

}
