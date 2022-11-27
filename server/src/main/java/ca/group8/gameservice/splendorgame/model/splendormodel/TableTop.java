package ca.group8.gameservice.splendorgame.model.splendormodel;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TableTop {
  private Map<Integer, Deck> decks= new HashMap<>();
  private ArrayList<Player> players;
  private BaseBoard baseBoard;
  private NobleBoard nobleBoard;
  private Bank bank;

  //assuming both board and deck will initialise in their constructors
  public TableTop(ArrayList<Player> players){
    for (int i = 1; i<4; i++){
      decks.put(i,new Deck(i));
    }
    initialiseBaseDecks();
    this.players=players;
    this.baseBoard = new BaseBoard(4,3);
    this.nobleBoard = new NobleBoard(1, players.size()+1);
    initialiseNobleBoard();
    initialiseDevelopmentCardBoard();
    bank = new Bank(players.size());
  }

  private void initialiseDevelopmentCardBoard(){
    for(int i=0; i<3; i++){
      for(int j=0; j<4; j++){
        baseBoard.add(i,j,decks.get(3-i).pop());
      }
    }
  }


  private void initialiseNobleBoard(){
    List<NobleCard> nobles = generateNobleCards();
    for(int i=0; i<= players.size() +1; i++){
      nobleBoard.add(i,0, nobles.get(i));
    }
  }

  private List<NobleCard> generateNobleCards(){
    File file1 = new File(
        Objects.requireNonNull(TableTop.class.getClassLoader().getResource("cardinfo_noble.json")).getFile()
    );
    // write unit tests
    Gson gson = new Gson();
    JsonReader reader = null;
    try {
      reader = new JsonReader(new FileReader(file1));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    NobleCard[] availableNobles = gson.fromJson(reader,NobleCard[].class);
    List<NobleCard> nobles = Arrays.asList(availableNobles);
    Collections.shuffle(nobles);

    return nobles;
  }

  private List<DevelopmentCard> generateBaseCards(){

    File file = new File(
        Objects.requireNonNull(TableTop.class.getClassLoader().getResource("cardinfo_basecard.json")).getFile()
    );
    // write unit tests
    Gson gson = new Gson();
    JsonReader reader = null;
    try {
      reader = new JsonReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    DevelopmentCard[] availableCards = gson.fromJson(reader,DevelopmentCard[].class);

    return Arrays.asList(availableCards);
  }

  private void initialiseBaseDecks(){
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

  public ArrayList<Player> getPlayers() {
    return players;
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
