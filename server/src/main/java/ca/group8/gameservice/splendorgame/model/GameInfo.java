package ca.group8.gameservice.splendorgame.model;

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
  private static List<String> activePlayers = new ArrayList<>();
  //private TableTop tableTop;

  /**
   * @param player NOTE: In this implementation, activePlayers is an arrayList meaning you cannot get(Player)
   *               based on giving the player name that is in the list.(can only index list)
   *               <p>
   *               QUESTION: If we only store the PlayerNames (and not player objects), how do we ensure
   *               that the names we are given correspond to ACTUAL players?
   */
  public GameInfo(String... player) {
    Random random = new Random(); //create a new random object
    activePlayers = Arrays.asList(player);
    //generates a random number between 1 and size of activePlayers list
    firstPlayer = activePlayers.get(random.nextInt(activePlayers.size()) + 1);
    currentPlayer = firstPlayer;
    //TODO Initialize TableTop based on its constructor


  }

  //TODO Figure out if this should be public/private/... based on what needs to call this method
  protected void setWinner(String player) {
    winner = Optional.of(player);
  }

  protected void checkWinner() {
    //TODO: Implement this operation (will be based on TableTop implementation)
  }

  protected static int getNumOfPlayers() {
    return activePlayers.size();
  }

}
