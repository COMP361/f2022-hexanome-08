package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.SavedGameState;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Controls Rest actions.
 */
@RestController
public class SplendorRestController {

  // Game instance and game state change related fields
  private final GameManager gameManager;

  private final GameValidator gameValidator;

  // Long polling specific fields (Broadcast Content Managers and time out time)
  private final long longPollTimeOut;

  // managing game state differently from player states
  private final Map<Long, BroadcastContentManager<GameInfo>>
      gameInfoBroadcastContentManager;
  // managing player sates independently from game state
  private final Map<Long, BroadcastContentManager<PlayerStates>>
      allPlayerInfoBroadcastContentManager;

  // Debug fields
  private final Logger logger;

  /**
   * Constructor.
   */
  public SplendorRestController(
      @Autowired GameManager gameManager,
      @Autowired GameValidator gameValidator,
      @Value("${long.poll.timeout}") long longPollTimeOut) {
    this.gameManager = gameManager;
    this.gameValidator = gameValidator;
    this.longPollTimeOut = longPollTimeOut;
    this.allPlayerInfoBroadcastContentManager = new LinkedHashMap<>();
    this.gameInfoBroadcastContentManager = new LinkedHashMap<>();
    // for debug
    this.logger = LoggerFactory.getLogger(SplendorRestController.class);
  }

  @GetMapping(value = "/test")
  public String helloWorld() {
    return "Hello, World!";
  }

  @GetMapping(value = {"/splendortrade", "/splendorbase", "/splendorcity"})
  public String gameTest3() {
    return "Hello, splendor games!";
  }


  /**
   * handle the long polling requests for all public visible game details.
   *
   * @param gameId game id
   * @param hash   hashed previous response payload
   * @return a deferred return payload JSON string response
   */
  @GetMapping(value = {"/splendorbase/api/games/{gameId}",
      "/splendortrade/api/games/{gameId}",
      "/splendorcity/api/games/{gameId}"}, produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getGameDetail(@PathVariable long gameId,
                                                              @RequestParam(required = false)
                                                              String hash) {
    try {
      // if the game does not exist in the game manager, throw an exception
      gameManager.getGameById(gameId);
      if (hash == null) {
        hash = "-";
      }
      if (hash.isEmpty()) {
        ResponseGenerator.getAsyncUpdate(longPollTimeOut,
            gameInfoBroadcastContentManager.get(gameId));
      }

      return ResponseGenerator.getHashBasedUpdate(longPollTimeOut,
          gameInfoBroadcastContentManager.get(gameId), hash);
    } catch (ModelAccessException e) {
      // Request does not go through, we need a deferred result
      DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
      deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
      return deferredResult;
    }

  }

  /**
   * step to save the game.
   *
   * @param gameId gameId
   * @param saveGameInfo saveGameInfo
   * @param accessToken accessToken
   * @return ResponseEntity
   */
  @PutMapping(value = {
      "/splendorbase/api/games/{gameId}/savegame",
      "/splendortrade/api/games/{gameId}/savegame",
      "/splendorcity/api/games/{gameId}/savegame"}, consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> saveGame(@PathVariable long gameId,
                                         @RequestBody Savegame saveGameInfo,
                                         @RequestParam(value = "player_name") String playerName,
                                         @RequestParam(value = "access_token") String accessToken) {
    try {
      // ModelAccessException can happen if gameId is not found
      //String creatorName = gameManager.getGameById(gameId).getCreator();

      // check if the request is sent by the creator or not
      // if not, do not allow to save it

      // allow save game for any player in that game, not just creator!
      gameValidator.gameIdPlayerNameValidCheck(accessToken, playerName, gameId);
      logger.warn("Got save request successfully from: " +playerName);

      // save the game detailed info to our game server as json file
      // and the metadata Savegame to LS, might throw model access exception
      // if duplicate game is saved
      gameManager.saveGame(saveGameInfo, gameId);
      //// forcing this game to be finished
      //GameInfo curGame = gameManager.getGameById(gameId);
      //curGame.setFinished();
      //// immediately tell the client, game is over
      //gameInfoBroadcastContentManager.get(gameId).touch();
      //// remove anything related to this game from game manager
      //// implicitly tell LS to delete this session
      //gameManager.deleteGame(gameId);
      //
      //// remove the broadcast content manager which controls the
      //// long polling updates after letting clients know the game is finished
      //gameInfoBroadcastContentManager.remove(gameId);
      //allPlayerInfoBroadcastContentManager.remove(gameId);
      return ResponseEntity.status(HttpStatus.OK).body("");
    } catch (ModelAccessException e) {
      logger.warn(e.getMessage());
      // regardless any of the above exception happens, bad request
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }


  /**
   * Launch game PUT request handling endpoint. It handles the PUT request of creating
   * game service sent from LS. LS sent this PUT request because client sends a launch request to
   * LS
   *
   * @param gameId       game id
   * @param launcherInfo JSON request body that contains the info needed to PUT a game service
   * @return a response body of the reply of launch game request.
   */
  @PutMapping(value = {"/splendorbase/api/games/{gameId}",
      "/splendortrade/api/games/{gameId}",
      "/splendorcity/api/games/{gameId}"}, consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchGame(@PathVariable long gameId,
                                           @RequestBody LauncherInfo launcherInfo) {
    try {
      // check validity of this launcherInfo
      logger.info("Checking validity");
      gameValidator.validLauncherInfo(gameId, launcherInfo);

      // added the game related to this launcher info to manager
      // it's safe to just call launchGame in here because launcherInfo has been
      // verified by the validator
      SavedGameState savedGameState = gameManager.launchGame(gameId, launcherInfo);

      // add the game info broadcast content for long polling
      gameInfoBroadcastContentManager
          .put(gameId, new BroadcastContentManager<>(savedGameState.getGameInfo()));
      // add the player states broadcast content for long polling
      allPlayerInfoBroadcastContentManager
          .put(gameId, new BroadcastContentManager<>(savedGameState.getPlayerStates()));

      // telling a good news
      logger.info(String.format("Successfully added game: %s", gameId));

      return ResponseEntity.status(HttpStatus.OK).build();

    } catch (ModelAccessException e) {
      logger.warn(e.getMessage());
      // something did not go well, reply with a bad request message
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  /**
   * Long polling for the game board content, optional hash value.
   */
  @GetMapping(value = {
      "/splendortrade/api/games/{gameId}/playerStates",
      "/splendorbase/api/games/{gameId}/playerStates",
      "/splendorcity/api/games/{gameId}/playerStates"
  },
      produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getPlayerStates(
      @PathVariable long gameId, @RequestParam(required = false) String hash) {
    try {

      // if the game does not exist in the game manager, throw an exception
      gameManager.getGameById(gameId);
      if (hash == null) {
        hash = "-";
      }
      // hash is either "-" or the hashed value from previous payload, use long polling
      if (hash.isEmpty()) {
        ResponseGenerator.getAsyncUpdate(longPollTimeOut,
            allPlayerInfoBroadcastContentManager.get(gameId));
      }
      return ResponseGenerator.getHashBasedUpdate(longPollTimeOut,
          allPlayerInfoBroadcastContentManager.get(gameId), hash);
    } catch (ModelAccessException e) {
      // Request does not go through, we need a deferred result
      DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
      deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
      return deferredResult;
    }
  }

  /**
   * Get players.
   */
  @GetMapping(value = {"/splendortrade/api/games/{gameId}/players",
      "/splendorbase/api/games/{gameId}/players",
      "/splendorcity/api/games/{gameId}/players"}, produces = "application/json; charset=utf-8")
  public ResponseEntity<String> getPlayers(@PathVariable long gameId) {
    try {
      // check if our game manager contains this game id, if not, we did not PUT it correctly!
      //logger.info("Current gameId:" + gameId);
      //logger.info("Stored key set of game ids: " + gameManager.getActiveGames().keySet());
      //logger.info("boolean result of splendorGameManager.isExistentGameId(gameId): "
      //    + gameManager.containsGameId(gameId));
      //if (!gameManager.containsGameId(gameId)) {
      //  throw new ModelAccessException("Can not get players for game " + gameId
      //      + ". The game has not been launched or does not exist!");
      //}
      GameInfo game = gameManager.getGameById(gameId);
      // if we can find the game, print the list of player names
      Gson gsonParser = SplendorDevHelper.getInstance().getGson();
      Type listOfNames = new TypeToken<List<String>>() {
      }.getType();
      String allPlayersInGame = gsonParser.toJson(game.getPlayerNames(), listOfNames);
      return ResponseEntity.status(HttpStatus.OK).body(allPlayersInGame);
    } catch (ModelAccessException e) {
      // something went wrong.
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }



  /**
   * Select action.
   */
  @PostMapping(value = {
      "/splendortrade/api/games/{gameId}/players/{playerName}/actions/{actionId}",
      "/splendorbase/api/games/{gameId}/players/{playerName}/actions/{actionId}",
      "/splendorcity/api/games/{gameId}/players/{playerName}/actions/{actionId}"
  })
  public ResponseEntity<String> selectAction(@PathVariable long gameId,
                                             @PathVariable String playerName,
                                             @PathVariable String actionId,
                                             @RequestParam(value = "access_token")
                                             String accessToken) {

    // TODO: Redo based on sequence diagram

    try {
      gameValidator.gameIdPlayerNameValidCheck(accessToken, playerName, gameId);
      ActionInterpreter actionInterpreter = gameManager.getGameActionInterpreter(gameId);
      actionInterpreter.interpretAction(actionId, playerName);

      BroadcastContentManager<PlayerStates> playerStatesManager =
          allPlayerInfoBroadcastContentManager.get(gameId);

      BroadcastContentManager<GameInfo> gameInfoManger =
          gameInfoBroadcastContentManager.get(gameId);
      // if anything might have changed, let the client side know immediately
      //
      playerStatesManager.touch();
      gameInfoManger.touch();

      // end of turn check
      GameInfo curGame = gameManager.getGameById(gameId);
      if (curGame.isFinished()) {
        // remove anything related to this game from game manager
        // implicitly tells lobby to remove the session as well
        gameManager.deleteGame(gameId);
        // remove the broadcast content manager which controls the
        // long polling updates
        gameInfoBroadcastContentManager.remove(gameId);
        allPlayerInfoBroadcastContentManager.remove(gameId);
      }
      return ResponseEntity.status(HttpStatus.OK).body(null);
    } catch (ModelAccessException e) {
      logger.warn(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

  }

}
