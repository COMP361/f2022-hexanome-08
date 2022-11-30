package project.view.splendor.communication;
import java.util.ArrayList;
import java.util.Map;


public class TableTop {

  private Map<Integer, Deck> decks;
  private ArrayList<PlayerInGame> playerInGames;
  private BaseBoard baseBoard;
  private NobleBoard nobleBoard;
  private Bank bank;

  public TableTop(Map<Integer, Deck> decks, ArrayList<PlayerInGame> playerInGames,
                  BaseBoard baseBoard, NobleBoard nobleBoard, Bank bank) {
    this.decks = decks;
    this.playerInGames = playerInGames;
    this.baseBoard = baseBoard;
    this.nobleBoard = nobleBoard;
    this.bank = bank;
  }

  public Map<Integer, Deck> getDecks() {
    return decks;
  }

  public ArrayList<PlayerInGame> getPlayerInGames() {
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

  public void setDecks(Map<Integer, Deck> decks) {
    this.decks = decks;
  }

  public void setPlayerInGames(
      ArrayList<PlayerInGame> playerInGames) {
    this.playerInGames = playerInGames;
  }

  public void setBaseBoard(BaseBoard baseBoard) {
    this.baseBoard = baseBoard;
  }

  public void setNobleBoard(NobleBoard nobleBoard) {
    this.nobleBoard = nobleBoard;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }



}