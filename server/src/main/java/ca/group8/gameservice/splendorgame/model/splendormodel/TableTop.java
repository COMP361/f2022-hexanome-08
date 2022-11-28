package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.Launcher;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class TableTop implements BroadcastContent {
  private Map<Integer, Deck> decks;
  private ArrayList<PlayerInGame> playerInGames;
  private BaseBoard baseBoard;
  private NobleBoard nobleBoard;
  private Bank bank;

  private Logger logger = LoggerFactory.getLogger(TableTop.class);
  //assuming both board and deck will initialise in their constructors
  public TableTop(ArrayList<PlayerInGame> playerInGames) throws FileNotFoundException {
    this.decks = new HashMap<>();
    for (int i = 1; i<4; i++){
      decks.put(i,new Deck(i));
    }
    initialiseBaseDecks();
    this.playerInGames = playerInGames;
    this.baseBoard = new BaseBoard(4,3);
    this.nobleBoard = new NobleBoard(1, playerInGames.size()+1);
    initialiseNobleBoard();
    initialiseDevelopmentCardBoard();
    bank = new Bank(playerInGames.size());
  }

  private void initialiseDevelopmentCardBoard(){
    for(int i=0; i<3; i++){
      for(int j=0; j<4; j++){
        baseBoard.add(i,j,decks.get(3-i).pop());
      }
    }
  }


  private void initialiseNobleBoard() throws FileNotFoundException {
    List<NobleCard> nobles = generateNobleCards();
    for(int i = 0; i<= playerInGames.size() +1; i++){
      nobleBoard.add(i,0, nobles.get(i));
    }
  }

  private List<NobleCard> generateNobleCards() {
    try {
      File file = ResourceUtils.getFile("classpath:/static/cardinfo_noble.json");
      // write unit tests
      Gson gson = new Gson();
      JsonReader reader =new JsonReader(new FileReader(file));
      NobleCard[] availableNobles = gson.fromJson(reader,NobleCard[].class);
      List<NobleCard> nobles = Arrays.asList(availableNobles);
      Collections.shuffle(nobles);
      logger.info("One noble: " + nobles.get(0).getCardName());
      return nobles;

    } catch (IOException e) {
      logger.error(e.getMessage());
      return new ArrayList<>();
    }
  }

  private List<DevelopmentCard> generateBaseCards(){
    try {
      File file = ResourceUtils.getFile("classpath:cardinfo_basecard.json");
      Gson gson = new Gson();
      JsonReader reader = new JsonReader(new FileReader(file));
      DevelopmentCard[] availableCards = gson.fromJson(reader,DevelopmentCard[].class);
      logger.info("One card: " + availableCards[0].getCardName());
      return Arrays.asList(availableCards);
    } catch (IOException e) {
      logger.error(e.getMessage());
      return new ArrayList<>();
    }
  }

  private void initialiseBaseDecks() throws FileNotFoundException {
    for(DevelopmentCard card :  generateBaseCards()){
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

  public ArrayList<PlayerInGame> getPlayers() {
    return playerInGames;
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

  @Override
  public boolean isEmpty() {
    return false;
  }
}
