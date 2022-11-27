package ca.group8.gameservice.splendorgame.model.splendormodel;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GameInfo { // TODO add gametype


  private String currentPlayer; //represents which player's turn it is currently
  private Optional<String> winner = Optional.empty(); //made optional for when Winner is not defined yet;
  private String firstPlayer; //should be Player Name of first player.
  private ArrayList<Player> activePlayers;
  private ArrayList<String> playerNames;
  private TableTop tableTop;


  /**
   * @param playerNames NOTE: In this implementation, activePlayers is an arrayList meaning you cannot get(Player)
   *               based on giving the player name that is in the list.(can only index list)
   */
  public GameInfo(ArrayList<String> playerNames) {
    Random random = new Random(); //create a new random object
    this.playerNames = playerNames;
    activePlayers = initializePlayers();
    //generates a random number between 1 and size of playerNames list
    firstPlayer = playerNames.get(random.nextInt(playerNames.size()) + 1);
    currentPlayer = firstPlayer;
    tableTop = new TableTop(activePlayers);


  }


  /**
   * This initializes Player objects.
   */
  private ArrayList<Player> initializePlayers() {
    ArrayList<Player> players = new ArrayList<>();
    for(String name:playerNames){
        players.add(new Player(name));
    }
    return players;
  }

  //TODO Figure out if this should be public/private/... based on what needs to call this method
  protected void setWinner(String player) {
    winner = Optional.of(player);
  }

  protected void checkWinner() {
    //TODO: Implement this operation (will be based on TableTop implementation)
  }

  public int getNumOfPlayers() {
    return activePlayers.size();
  }

  public boolean isFinished(){
    return winner.isPresent();
  }

  /**
   * @return Current player object (as a Player).
   */
  public Player getCurrentPlayer() {
    Player curPlayer=null;
    for (Player player : activePlayers) {
      if (player.getName()==currentPlayer) {
        curPlayer=player;
        break;
      }
    }
    if(curPlayer==null) {
      throw new IllegalStateException("Cannot find this current player in the active player list.");
    }
    return curPlayer;
  }

  public void setNextPlayer(){
    int index = playerNames.indexOf(currentPlayer);
    //if last player in list return to first player
    currentPlayer= playerNames.get((index+1)%4);
  }

  public Optional<String> getWinner() {
    return winner;
  }

  public String getFirstPlayer() {
    return firstPlayer;
  }

  public ArrayList<Player> getActivePlayers() {
    return activePlayers;
  }

  public ArrayList<String> getPlayerNames() {
    return playerNames;
  }

  public TableTop getTableTop() {
    return tableTop;
  }

}
