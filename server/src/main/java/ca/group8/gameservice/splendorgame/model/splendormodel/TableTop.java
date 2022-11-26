package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;

public class TableTop {
  private ArrayList<Deck> decks;
  private ArrayList<Player> players;
  private BaseBoard baseBoard;
  private NobleBoard nobleBoard;
  private Bank bank;

  //assuming both board and deck will initialise in their constructors
  public TableTop(ArrayList<Player> players){
    for (int i = 1; i<4; i++){
      decks.add(new Deck(i));
    }
    this.players=players;
    this.baseBoard = new BaseBoard(4,3);
    this.nobleBoard = new NobleBoard(1, players.size()+1);
    bank = new Bank(players.size());
  }

  public ArrayList<Deck> getDecks() {
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
