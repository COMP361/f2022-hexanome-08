package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;

public class TableTop {
  private ArrayList<Deck> decks;
  private ArrayList<Player> players;
  private BaseBoard baseBoard;
  private Bank bank;

  public TableTop(ArrayList<Player> players){
    this.players=players;
  }



}
