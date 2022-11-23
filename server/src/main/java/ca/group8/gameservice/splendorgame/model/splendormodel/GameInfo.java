package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GameInfo {

  //TODO: Main issues: Dependency on TableTop class

  private String currentPlayer; //represents which player's turn it is currently
  private Optional<String> winner; //made optional for when Winner is not defined yet;
  private String firstPlayer; //should be Player Name of first player.
  private ArrayList<Player> activePlayers;
  private ArrayList<String> playerNames = new ArrayList<>();
  private TableTop tableTop;

  /**
   * @param players NOTE: In this implementation, activePlayers is an arrayList meaning you cannot get(Player)
   *               based on giving the player name that is in the list.(can only index list)
   *               <p>
   *               QUESTION: If we only store the PlayerNames (and not player objects), how do we ensure
   *               that the names we are given correspond to ACTUAL players?
   */
  public GameInfo(ArrayList<Player> players) {
    Random random = new Random(); //create a new random object
    activePlayers = players;
    for (Player player : activePlayers){
      playerNames.add(player.getName());
    }
    //generates a random number between 1 and size of activePlayers list
    firstPlayer = playerNames.get(random.nextInt(activePlayers.size()) + 1);
    currentPlayer = firstPlayer;
    tableTop = new TableTop(activePlayers);


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

}
