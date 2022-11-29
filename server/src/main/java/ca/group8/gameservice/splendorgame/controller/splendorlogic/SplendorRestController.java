package ca.group8.gameservice.splendorgame.controller.splendorlogic;
import ca.group8.gameservice.splendorgame.controller.GameRestController;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class SplendorRestController {

  // Game instance and game state change related fields
  private SplendorGameManager splendorGameManager;
  private SplendorActionInterpreter splendorActionInterpreter;
  private SplendorActionListGenerator splendorActionListGenerator;


  // Game registration related fields
  private String gameServiceName;
  private String lobbyServiceAddress;
  private SplendorRegistrator splendorRegistrator;

  // Long polling specific fields (Broadcast Content Managers and time out time)
  private final long longPollTimeOut;
  private final Map<Long, BroadcastContentManager<TableTop>> tableTopBroadcastContentManager;
  // Long: gameId, String: playerName
  private final Map<Long, Map<String, BroadcastContentManager<PlayerInGame>>> playerInfoBroadcastContentManager;


  // Debug fields
  private final Logger logger;

  public SplendorRestController(
      @Autowired SplendorRegistrator splendorRegistrator,
      @Autowired SplendorGameManager splendorGameManager,
      @Autowired SplendorActionListGenerator splendorActionListGenerator,
      @Autowired SplendorActionInterpreter splendorActionInterpreter,
      @Value("${gameservice.name}") String gameServiceName,
      @Value("${lobbyservice.location}") String lobbyServiceAddress,
      @Value("long.poll.timeout") long longPollTimeOut) {
    this.splendorRegistrator = splendorRegistrator;
    this.splendorGameManager = splendorGameManager;
    this.splendorActionListGenerator = splendorActionListGenerator;
    this.splendorActionInterpreter = splendorActionInterpreter;
    this.lobbyServiceAddress = lobbyServiceAddress;
    this.gameServiceName = gameServiceName;
    this.longPollTimeOut = longPollTimeOut;
    this.tableTopBroadcastContentManager = new HashMap<>();
    this.playerInfoBroadcastContentManager = new HashMap<>();
    // for debug
    this.logger = LoggerFactory.getLogger(SplendorRestController.class);
  }
  @GetMapping("/splendor/hello")
  public String hello() {
    return "Hello";
  }

  @GetMapping("/test")
  public String test() {
    return "This is working!";
  }

  @GetMapping("/splendor/api/test")
  public String test2() {
    return "This is working!";
  }

  @GetMapping("/api/games")
  public String getAllGames() {
    return splendorGameManager.getActiveGames().keySet().toString();
  }


  @PutMapping(value = "/api/games/{gameId}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchGame(@PathVariable long gameId,
                                           @RequestBody LauncherInfo launcherInfo)
      throws FileNotFoundException{
    // TODO: Handle the logic of how the game is managed in game manager

    //TODO: When the game is launched, there are several things to do
    // 1. construct the instance of GameInfo
    // 2. Add <gameId, tableTop> pair to tableTopBroadcastContentManager (gameId from pathVariable)
    // 3. Add <gameId, <playerName, PlayerInGame>> map to playerInfoBroadcastContentManager for each playerName
    //      - the playerName are from launcherInfo.getPlayerNames()
    try {

      if (launcherInfo == null || launcherInfo.getGameServer() == null) {
        throw new ModelAccessException("Invalid launcher info provided");
      }

      if(splendorGameManager.isExistentGameId(gameId)) {
        throw new ModelAccessException("Duplicate game instance, can not launch it!");
      }

      if(!launcherInfo.getGameServer().equals(gameServiceName)){
        throw new ModelAccessException("No such game registered in LS");
      }

      // seems like we can safely add the game to the game manager
      logger.info("before adding game info: " + splendorGameManager.getActiveGames());
      GameInfo gi = splendorGameManager.addGame(gameId, launcherInfo.getPlayerNames());
      logger.info("after adding game info: " + splendorGameManager.getActiveGames());

      logger.warn("Launcher player names:" + launcherInfo.getPlayerNames());
      TableTop curTableTop = splendorGameManager.getGameById(gameId).getTableTop();
      // store the gameId -> broadcast content map to the broadcast manager
      tableTopBroadcastContentManager.put(gameId, new BroadcastContentManager<>(curTableTop));
      return ResponseEntity.status(HttpStatus.OK).build();

    } catch (ModelAccessException e) {
      // something did not go well, reply with a bad request message
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }



  // TODO: Finish this later

  @DeleteMapping(value="/api/games/{gameId}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> deleteGame(@PathVariable long gameId,
                                           @RequestParam(value = "access_token") String accessToken,
                                           @RequestParam(value = "savegameid", required = false)
                                             String saveGameId)
      throws ModelAccessException {
    if (saveGameId == null) {
      saveGameId = "";
    }
    // if it's empty, then player does not want to save it (saveGameId) can be randomly generated
    // from client side
    GameInfo gameInfo = splendorGameManager.getGameById(gameId);


    if (!saveGameId.equals("")) { // not empty, then we need to send a saveGameRequest to LS
      // prepare player names as array to construct Savegame object
      String[] playerNames = new String[gameInfo.getNumOfPlayers()];
      for (int i = 0; i < gameInfo.getNumOfPlayers(); i++) {
        playerNames[i] = gameInfo.getPlayerNames().get(i);
      }
      // no matter what, we have a non-empty savegameid that we can send to LS
      Savegame saveGameInfo = new Savegame(playerNames, gameServiceName, saveGameId);
      // TODO: Send the PUT request to save game under
      //  /api/gameservices/{gameservice}/savegames/{savegame} to LS

      // Unirest.put().......

    }
    // no matter we sent the request to save it or not, we delete the running instance of
    // game in game service

    // TODO: Delete the game from gameManager and the 2 broadcast managers

    return null;
  }


  // TODO: Write a method, providing playerName and gameId, find the
  //  corresponding GameInfo and PlayerInGame


  // TODO: Write a general method to check if the access_token -> playerName refers to
  //  a valid request (Action request or Other request)
  //  Ex 1) Click on MyPurchase Cards do not need to be current player
  //  Ex 2) Purchase or Reserve or TakeToken can only happens when access_token_player == curPLayer


  // logic to heck whether if it's this player's turn
  // We need to check all these:
  // 1. isValidToken()
  // 2. gameExists() (running and managed by gameManager)
  //      -> MUST have a gameInfo
  //      -> Can have a String curPlayer
  // 3. playerInGame()
  // 4. curPlayer.equals(playerNameToCheck) or not....



  private boolean isValidToken(String accessToken, String playerName) throws UnirestException {
    HttpResponse<String> nameResponse =
        Unirest.get(lobbyServiceAddress + "/oauth/username")
            .queryString("access_token", accessToken).asString();
    String responseUserName = nameResponse.getBody();
    return responseUserName.equals(playerName);

  }

  // Long polling for the game board content, optional hash value
  @GetMapping(value="/api/games/{gameId}/tableTop", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getBoard(
      @PathVariable long gameId, @RequestParam(required = false) String hash) {
    try{
      if (hash == null || hash.equals("")) {
        hash = "-";
      }

      // if the game does not exist in the game manager, throw an exception
      if(!splendorGameManager.isExistentGameId(gameId)){
        throw new ModelAccessException("There is no game with game id: "
            + gameId + " launched, try again later");
      }

      // hash is either "-" or the hashed value from previous payload, use long polling
      return ResponseGenerator.getHashBasedUpdate(longPollTimeOut, tableTopBroadcastContentManager.get(gameId), hash);
    }catch (ModelAccessException e) {
      // Request does not go through, we need a deferred result
      DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
      deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
      return deferredResult;
    }
  }

  @GetMapping(value="/api/games/{gameId}/players/{playerName}/inventory", produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getPlayerInventory(
      @PathVariable long gameId, @PathVariable String playerName,
      @RequestParam(value = "access_token") String accessToken,
      @RequestParam(required = false) String hash
  ) {

    try{
      if (hash == null || hash.equals("")) {
        hash = "-";
      }
      // if the game does not exist in the game manager, throw an exception
      if(!splendorGameManager.isExistentGameId(gameId)){
        throw new ModelAccessException("There is no game with game id: "
            + gameId + " launched, try again later");
      }

      if(!isValidToken(accessToken, playerName)) {
        throw new ModelAccessException("User token and user name does not match");
      }


      // hash is either "-" or the hashed value from previous payload, use long polling
      return ResponseGenerator.getHashBasedUpdate(longPollTimeOut, tableTopBroadcastContentManager.get(gameId), hash);
    }catch (ModelAccessException | UnirestException e) {
      // Request does not go through, we need a deferred result
      DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
      deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
      return deferredResult;
    }

  }


  @GetMapping(value="/api/games/{gameId}/players", produces = "application/json; charset=utf-8")
  public ResponseEntity<String> getPlayers(@PathVariable long gameId) {
    try {
      // check if our game manager contains this game id, if not, we did not PUT it correctly!
      if (splendorGameManager.isExistentGameId(gameId)) {
        throw new ModelAccessException("Can not get players for game " + gameId +
            ". The game has not been launched or does not exist!");
      }
      // if we can find the game, print the players in the game
      String allPlayersInGame = new Gson().toJson(splendorGameManager.getGameById(gameId).getActivePlayers());
      return ResponseEntity.status(HttpStatus.OK).body(allPlayersInGame);
    } catch (ModelAccessException e) {
      // something went wrong.
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }


  public ResponseEntity<String> getActions(long gameId, String player, String accessToken) {
    return null;
  }

  public ResponseEntity<String> selectAction(long gameId, String player, String actionMD5,
                                             String accessToken) {
    return null;
  }

  public ResponseEntity<String> getRanking(long gameId) {
    return null;
  }

}
