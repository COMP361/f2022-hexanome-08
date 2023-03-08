package ca.group8.gameservice.splendorgame.controller.splendorlogic;


import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.PlayerInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.SavedGameState;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * GameValidator.
 */
@Component
public class GameValidator {

  // Game instance and game state change related fields
  private final GameManager gameManager;

  // Game registration related fields
  private final List<String> gameServiceNames;

  private final LobbyCommunicator lobbyCommunicator;

  // Debug logger
  private final Logger logger;

  /**
   * GameValidator.
   *
   * @param gameManager gameManager
   * @param lobbyCommunicator lobbyCommunicator
   * @param gameServiceNames gameServiceNames
   */
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

    if (!gameServiceNames.contains(launcherInfo.getGameServer())) {
      throw new ModelAccessException("No such game registered in LS");
    }

    // this one should avoid duplicates, thus it throws error when it is TRUE!!!
    if (gameManager.getActiveGames().containsKey(gameId)) {
      throw new ModelAccessException("Duplicate game instance, can not launch it!");
    }

    // the sessions we are about to launch has a save game id, verify if it exits before
    // pass this condition check if savegame = ""
    if (!launcherInfo.getSavegame().isEmpty()) {
      String curSaveGameId = launcherInfo.getSavegame();
      if (!gameManager.getSavedGameIds().contains(curSaveGameId)) {
        throw new ModelAccessException("The game id requested is not previously saved!");
      }
      // In the case of we have more players play this loaded game than
      // the amount of players who saved this game, throw exception as well
      List<String> curPlayerNames = launcherInfo.getPlayers().stream()
          .map(PlayerInfo::getName)
          .collect(Collectors.toList());
      int curPlayersCount = curPlayerNames.size();
      try {
        String saveGameId = launcherInfo.getSavegame();
        Map<String, SavedGameState> savedGames = gameManager.readSavedGameDataFromFile();
        SavedGameState savedGame = savedGames.get(saveGameId);
        PlayerStates playerStates = savedGame.getPlayerStates();
        int prePlayersCount = playerStates.getPlayersInfo().size();
        if (curPlayersCount > prePlayersCount) {
          throw new ModelAccessException("Can not have more players than the saved game!");
        }
      } catch (IOException e) {
        throw new ModelAccessException(e.getMessage());
      }
    }

  }


  /**
   * Checks 3 things, sequentially.
   * 1. Whether the token matches player's name.
   * 2. Whether the game with the gameid exists
   * 3. Whether the player exists in the game with the game id
   *
   * @param accessToken access token of the player
   * @param playerName  player's name
   * @param gameId      game id of the player
   * @throws ModelAccessException anything goes wrong of above 3 conditions check, throw it.
   */
  public void gameIdPlayerNameValidCheck(String accessToken, String playerName, long gameId)
      throws ModelAccessException {
    // if the access token and player name does not match, throw an error
    if (!lobbyCommunicator.isValidToken(accessToken, playerName)) {
      throw new ModelAccessException("User token and user name does not match");
    }

    // if the game does not exist in the game manager, throw an exception
    if (!gameManager.getActiveGames().containsKey(gameId)) {
      throw new ModelAccessException("There is no game with game id: "
          + gameId + " launched, try again later");
    }

    // if the current player is not in the game, there is no need to update anything
    if (!gameManager.containsPlayer(gameId, playerName)) {
      throw new ModelAccessException("Player:" + playerName + " is not in game: " + gameId);
    }
  }


}
