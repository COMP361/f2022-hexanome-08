package ca.group8.gameservice.splendorgame.controller;


import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.splendorlogic.GameManager;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OauthValidator {

  // Game instance and game state change related fields
  private final GameManager gameManager;

  // Game registration related fields
  private final List<String> gameServiceNames;


  public OauthValidator(
      @Autowired GameManager gameManager,
      @Value("${gameservice.names}") String[] gameServiceNames) {
    this.gameManager = gameManager;
    this.gameServiceNames = Arrays.asList(gameServiceNames);
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
    if (gameManager.isExistentGameId(gameId)) {
      throw new ModelAccessException("Duplicate game instance, can not launch it!");
    }

    if (!gameServiceNames.contains(launcherInfo.getGameServer())) {
      throw new ModelAccessException("No such game registered in LS");
    }
  }

}
