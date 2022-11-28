package ca.group8.gameservice.splendorgame.model.splendormodel;


import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class GameInfo { // TODO add gametype


  private String currentPlayer; //represents which player's turn it is currently
  private Optional<String> winner = Optional.empty(); //made optional for when Winner is not defined yet;
  private String firstPlayer; //should be Player Name of first player.
  private ArrayList<PlayerInGame> activePlayerInGames;
  private ArrayList<String> playerNames;
  private TableTop tableTop;


  /**
   * @param playerNames NOTE: In this implementation, activePlayers is an arrayList meaning you cannot get(Player)
   *               based on giving the player name that is in the list.(can only index list)
   */
  public GameInfo(ArrayList<String> playerNames) {
    Random random = new Random(); //create a new random object
    this.playerNames = playerNames;
    activePlayerInGames = initializePlayers();
    //generates a random number between 1 and size of playerNames list
    firstPlayer = playerNames.get(random.nextInt(playerNames.size()) + 1);
    currentPlayer = firstPlayer;
    tableTop = new TableTop(activePlayerInGames);


  }


  /**
   * This initializes Player objects.
   */
  private ArrayList<PlayerInGame> initializePlayers() {
    ArrayList<PlayerInGame> playerInGames = new ArrayList<>();
    for(String name:playerNames){
        playerInGames.add(new PlayerInGame(name));
    }
    return playerInGames;
  }

  //TODO Figure out if this should be public/private/... based on what needs to call this method
  protected void setWinner(String player) {
    winner = Optional.of(player);
  }

  protected void checkWinner() {
    //TODO: Implement this operation (will be based on TableTop implementation)
  }

  public int getNumOfPlayers() {
    return activePlayerInGames.size();
  }

  public boolean isFinished(){
    return winner.isPresent();
  }

  /**
   * @return Current player object (as a Player).
   */
  public PlayerInGame getCurrentPlayer() {
    PlayerInGame curPlayerInGame =null;
    for (PlayerInGame playerInGame : activePlayerInGames) {
      if (playerInGame.getName()==currentPlayer) {
        curPlayerInGame = playerInGame;
        break;
      }
    }
    if(curPlayerInGame ==null) {
      throw new IllegalStateException("Cannot find this current player in the active player list.");
    }
    return curPlayerInGame;
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

  public ArrayList<PlayerInGame> getActivePlayers() {
    return activePlayerInGames;
  }

  public ArrayList<String> getPlayerNames() {
    return playerNames;
  }

  public TableTop getTableTop() {
    return tableTop;
  }

}
