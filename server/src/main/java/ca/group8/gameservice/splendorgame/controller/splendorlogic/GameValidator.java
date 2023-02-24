package ca.group8.gameservice.splendorgame.controller.splendorlogic;


import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GameValidator {

  // Game instance and game state change related fields
  private final GameManager gameManager;

  // Game registration related fields
  private final List<String> gameServiceNames;

  private final LobbyCommunicator lobbyCommunicator;

  // Debug logger
  private final Logger logger;


  public GameValidator(
      @Autowired GameManager gameManager,
      @Autowired LobbyCommunicator lobbyCommunicator,
      @Value("${gameservice.names}") String[] gameServiceNames) {
    this.gameManager = gameManager;
    this.lobbyCommunicator = lobbyCommunicator;
    this.gameServiceNames = Arrays.asList(gameServiceNames);
    this.logger = LoggerFactory.getLogger(SplendorRestController.class);
  }

  /**
   * Throws exception if we can not launch the game.
   *
   * @param gameId       game id
   * @param launcherInfo launcher info that contains info about the session to start
   */
  public void validLauncherInfo(long gameId, LauncherInfo launcherInfo)
      throws ModelAccessException {
    if (launcherInfo == null || launcherInfo.getGameServer() == null) {
      throw new ModelAccessException("Invalid launcher info provided");
    }

    // this one should avoid duplicates, thus it throws error when it is TRUE!!!
    if (gameManager.containsGameId(gameId)) {
      throw new ModelAccessException("Duplicate game instance, can not launch it!");
    }

    if (!gameServiceNames.contains(launcherInfo.getGameServer())) {
      throw new ModelAccessException("No such game registered in LS");
    }
  }


  /**
   * Checks 3 things, sequentially.
   * 1. Whether the token matches player's name.
   * 2. Whether the game with the gameid exists
   * 3. Whether the player exists in the game with the game id
   * @param accessToken access token of the player
   * @param playerName player's name
   * @param gameId game id of the player
   * @throws ModelAccessException anything goes wrong of above 3 conditions check, throw it.
   */
  public void gameIdPlayerNameValidCheck(String accessToken, String playerName, long gameId)
      throws ModelAccessException {
    // if the access token and player name does not match, throw an error
    if (!lobbyCommunicator.isValidToken(accessToken, playerName)) {
      throw new ModelAccessException("User token and user name does not match");
    }

    // if the game does not exist in the game manager, throw an exception
    if (!gameManager.containsGameId(gameId)) {
      throw new ModelAccessException("There is no game with game id: "
          + gameId + " launched, try again later");
    }

    // if the current player is not in the game, there is no need to update anything
    if (!gameManager.containsPlayer(gameId, playerName)) {
      throw new ModelAccessException("Player:" + playerName + " is not in game: " + gameId);
    }
  }


}
