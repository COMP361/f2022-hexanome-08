package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.PlayerInfo;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
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

  // Game registration related fields
  private final List<String> gameServiceNames;
  private final String lobbyServiceAddress;
  private final SplendorRegistrator splendorRegistrator;

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
      @Autowired SplendorRegistrator splendorRegistrator,
      @Autowired GameManager gameManager,
      @Value("${gameservice.names}") String[] gameServiceNames,
      @Value("${lobbyservice.location}") String lobbyServiceAddress,
      @Value("${long.poll.timeout}") long longPollTimeOut) {
    this.splendorRegistrator = splendorRegistrator;
    this.gameManager = gameManager;
    this.lobbyServiceAddress = lobbyServiceAddress;
    this.gameServiceNames = Arrays.asList(gameServiceNames);
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

  //@GetMapping(value = "/splendorbase")
  //public String gameTest() {
  //  return "Hello, splendorbase!";
  //}
  //
  //@GetMapping(value = "/splendororient")
  //public String gameTest2() {
  //  return "Hello, splendororient!";
  //}

  @GetMapping(value = {"/splendororient", "/splendorbase"})
  public String gameTest3() {
    return "Hello, splendor games!";
  }


  //@GetMapping(value = "/test/{gameId}", produces = "application/json; charset=utf-8")
  //public DeferredResult<ResponseEntity<String>>
  // debugEndPoint(@RequestParam(required = false) String hash,
  //                                                            @PathVariable long gameId)
  //    throws ModelAccessException {
  //
  //  // No hash provided at all -> return a synced update. We achieve this by setting a hash that
  //  clearly differs from any valid hash.
  //  if (hash == null)
  //    hash = "-";
  //
  //  // Hash was provided, but is empty -> return an asynchronous update,
  //  as soon as something has changed
  //  if (hash.isEmpty())
  //    ResponseGenerator.getAsyncUpdate(longPollTimeOut, testManager.get(0));
  //
  //  GameInfo g = splendorGameManager.getGameById(gameId);
  //  ArrayList<PlayerInGame> names = g.getPlayersInGame();
  //  TableTopTest t = new TableTopTest(names);
  //  BroadcastContentManager<TableTopTest> test = new BroadcastContentManager<>(t);
  //
  //  testManager.put(0, test);
  //  // A hash was provided, or we want to provoke a hash mismatch because no hash
  //  (not even an empty hash) was provided
  //  return ResponseGenerator.getHashBasedUpdate(longPollTimeOut, testManager.get(0), hash);
  //}

  /**
   * handle the long polling requests for all public visible game details.
   *
   * @param gameId game id
   * @param hash   hashed previous response payload
   * @return a deferred return payload JSON string response
   */
  @GetMapping(value = {"/splendororient/api/games/{gameId}",
      "/splendorbase/api/games/{gameId}"}, produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getGameDetail(@PathVariable long gameId,
                                                              @RequestParam(required = false)
                                                              String hash) {
    try {
      // if the game does not exist in the game manager, throw an exception
      if (!gameManager.isExistentGameId(gameId)) {
        throw new ModelAccessException("There is no game with game id: "
            + gameId + " launched, try again later");
      }
      if (hash == null) {
        hash = "-";
      }

      // hash is either "-" or the hashed value from previous payload, use long polling
      //long longPollingTimeOut = Long.parseLong(longPollTimeOut);


      if (hash.isEmpty()) {
        ResponseGenerator.getAsyncUpdate(longPollTimeOut,
            gameInfoBroadcastContentManager.get(gameId));
      }
      //GameInfo curGameInfo
      //    = gameInfoBroadcastContentManager.get(gameId).getCurrentBroadcastContent();
      //String serverHash = DigestUtils.md5Hex(new Gson().toJson(curGameInfo));
      //logger.info("GameInfo hash from client: " + hash);
      //logger.info("GameInfo hash generated on server side: " + serverHash);

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
   * Throws exception if we can not launch the game.
   *
   * @param gameId       game id
   * @param launcherInfo launcher info that contains info about the session to start
   */
  private void validLauncherInfo(long gameId, LauncherInfo launcherInfo)
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

  /**
   * Launch game PUT request handling endpoint. It handles the PUT request of creating
   * game service sent from LS. LS sent this PUT request because client sends a launch request to
   * LS first
   *
   * @param gameId       game id
   * @param launcherInfo JSON request body that contains the info needed to PUT a game service
   * @return a response body of the reply of launch game request.
   */
  @PutMapping(value = {"/splendororient/api/games/{gameId}",
      "/splendorbase/api/games/{gameId}"}, consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchGame(@PathVariable long gameId,
                                           @RequestBody LauncherInfo launcherInfo) {
    try {
      // check validity of this launcherInfo
      validLauncherInfo(gameId, launcherInfo);

      String gameServerName = launcherInfo.getGameServer();
      List<Extension> gameExtensions = new ArrayList<>();
      gameExtensions.add(Extension.BASE);
      gameExtensions.add(Extension.ORIENT);
      if (gameServerName.equals("splendorCity")) {
        gameExtensions.add(Extension.CITY);
      }

      if (gameServerName.equals("splendorTrade")) {
        gameExtensions.add(Extension.TRADING_POST);
      }

      // get all player names
      List<String> playerNames = launcherInfo
          .getPlayers()
          .stream()
          .map(PlayerInfo::getName).toList();

      GameInfo newGameInfo = new GameInfo(gameExtensions, playerNames);
      PlayerStates newPlayerStates = new PlayerStates(playerNames);
      ActionInterpreter newActionInterpreter = new ActionInterpreter(newGameInfo, newPlayerStates);

      // added game info, player states and action interpreter for this specific gameId
      gameManager.addGame(gameId, newGameInfo);
      gameManager.addGamePlayerStates(gameId, newPlayerStates);
      gameManager.addGameActionInterpreter(gameId, newActionInterpreter);

      // add the game info broadcast content for long polling
      gameInfoBroadcastContentManager
          .put(gameId, new BroadcastContentManager<>(newGameInfo));

      // add the player states broadcast content for long polling
      allPlayerInfoBroadcastContentManager
          .put(gameId, new BroadcastContentManager<>(newPlayerStates));

      // telling a good news
      logger.info(String.format("Successfully added game: %s", gameId));

      return ResponseEntity.status(HttpStatus.OK).build();

    } catch (ModelAccessException e) {
      // something did not go well, reply with a bad request message
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }


  /**
   * TODO: Finish this later for M8.
   */
  @DeleteMapping(value = {"/splendororient/api/games/{gameId}",
      "/splendorbase/api/games/{gameId}"}, consumes = "application/json; charset=utf-8")
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
    GameInfo gameInfo = gameManager.getGameById(gameId);
    if (!saveGameId.equals("")) { // not empty, then we need to send a saveGameRequest to LS
      // prepare player names as array to construct Savegame object
      String[] playerNames = new String[gameInfo.getNumOfPlayers()];
      for (int i = 0; i < gameInfo.getNumOfPlayers(); i++) {
        playerNames[i] = gameInfo.getPlayerNames().get(i);
      }
      // no matter what, we have a non-empty savegameid that we can send to LS
      //Savegame saveGameInfo = new Savegame(playerNames, gameServiceName, saveGameId);
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
            .header("Authorization", "Bearer " + accessToken).asString();

    String responseUserName = nameResponse.getBody();
    logger.info("access token represents: " + responseUserName);
    logger.info("current player is: " + playerName);
    return responseUserName.equals(playerName);

  }

  private boolean isPlayerTurn(String playerNameInRequest, GameInfo gameInfo) {
    String curPlayerName = gameInfo.getCurrentPlayer();
    return curPlayerName.equals(playerNameInRequest);
  }

  /**
   * Long polling for the game board content, optional hash value.
   */
  @GetMapping(value = {"/splendororient/api/games/{gameId}/playerStates",
      "/splendorbase/api/games/{gameId}/playerStates"},
      produces = "application/json; charset=utf-8")
  public DeferredResult<ResponseEntity<String>> getPlayerStates(
      @PathVariable long gameId, @RequestParam(required = false) String hash) {
    try {

      // if the game does not exist in the game manager, throw an exception
      if (!gameManager.isExistentGameId(gameId)) {
        throw new ModelAccessException("There is no game with game id: "
            + gameId + " launched, try again later");
      }
      if (hash == null) {
        hash = "-";
      }

      // hash is either "-" or the hashed value from previous payload, use long polling
      if (hash.isEmpty()) {
        ResponseGenerator.getAsyncUpdate(longPollTimeOut,
            allPlayerInfoBroadcastContentManager.get(gameId));
      }

      //PlayerStates curPlayerStates
      //    = allPlayerInfoBroadcastContentManager.get(gameId).getCurrentBroadcastContent();
      //String serverHash = DigestUtils.md5Hex(new Gson().toJson(curPlayerStates));
      //logger.info("GameInfo hash from client: " + hash);
      //logger.info("GameInfo hash generated on server side: " + serverHash);
      return ResponseGenerator.getHashBasedUpdate(longPollTimeOut,
          allPlayerInfoBroadcastContentManager.get(gameId), hash);
    } catch (ModelAccessException e) {
      // Request does not go through, we need a deferred result
      DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
      deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
      return deferredResult;
    }
  }

  private void gameIdPlayerNameValidCheck(String accessToken, String playerName, long gameId)
      throws ModelAccessException, UnirestException {
    // if the access token and player name does not match, throw an error
    if (!isValidToken(accessToken, playerName)) {
      throw new ModelAccessException("User token and user name does not match");
    }

    // if the game does not exist in the game manager, throw an exception
    if (!gameManager.isExistentGameId(gameId)) {
      throw new ModelAccessException("There is no game with game id: "
          + gameId + " launched, try again later");
    }

    // if the current player is not in the game, there is no need to update anything
    if (!gameManager.isPlayerInGame(gameId, playerName)) {
      throw new ModelAccessException("Player:" + playerName + " is not in game: " + gameId);
    }

  }

  /**
   * Get players.
   */
  @GetMapping(value = {"/splendororient/api/games/{gameId}/players",
      "/splendorbase/api/games/{gameId}/players"}, produces = "application/json; charset=utf-8")
  public ResponseEntity<String> getPlayers(@PathVariable long gameId) {
    try {
      // check if our game manager contains this game id, if not, we did not PUT it correctly!
      logger.info("Current gameId:" + gameId);
      logger.info("Stored key set of game ids: " + gameManager.getActiveGames().keySet());
      logger.info("boolean result of splendorGameManager.isExistentGameId(gameId): "
          + gameManager.isExistentGameId(gameId));
      if (!gameManager.isExistentGameId(gameId)) {
        throw new ModelAccessException("Can not get players for game " + gameId
            + ". The game has not been launched or does not exist!");
      }
      // if we can find the game, print the list of player names
      String allPlayersInGame =
          new Gson().toJson(gameManager.getGameById(gameId).getPlayerNames());
      return ResponseEntity.status(HttpStatus.OK).body(allPlayersInGame);
    } catch (ModelAccessException e) {
      // something went wrong.
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  /**
   * TODO: send GET request to this location TWICE per turn, one at beginning, one at the end.
   */
  @GetMapping(value = {"/splendororient/api/games/{gameId}/players/{playerName}/actions",
      "/splendorbase/api/games/{gameId}/players/{playerName}/actions"},
      produces = "application/json; charset=utf-8")
  public ResponseEntity<String> getActions(@PathVariable long gameId,
                                           @PathVariable String playerName,
                                           @RequestParam(value = "access_token")
                                           String accessToken) {
    // TODO: Redo based on sequence diagram

    return null;


    //try {
    //  // Merged several logical checks before perform any actions to the server data
    //  // equivalent to the condition check of
    //  gameIdPlayerNameValidCheck(accessToken, playerName, gameId);
    //
    //  // looks good, we can generate the actions for this player now
    //  GameInfo gameInfo = splendorGameManager.getGameById(gameId);
    //  PlayerInGame playerInGame = splendorGameManager
    //      .getPlayerStatesById(gameId)
    //      .getPlayersInfo()
    //      .get(playerName);
    //  // failed to generate the map
    //  actionGenerator.generateActions(gameId, gameInfo, playerInGame);
    //
    //  // check if the generation went well and generate a mapping even it's empty it's fine
    //  Map<String, Action> actionsAvailableToPlayer =
    //      actionGenerator.lookUpActions(gameId, playerName);
    //  if (actionsAvailableToPlayer == null) {
    //    throw new ModelAccessException("Generation for actions failed for some reasons, debug!");
    //  }
    //
    //  logger.warn("action map generated for player: " + playerName + " are "
    //      + actionsAvailableToPlayer.keySet());
    //
    //  // actionsAvailableToPlayer is either empty hash map or have something, not important,
    //  // just give it to client
    //  Gson actionGson = SplendorRestController.getActionGson();
    //  // added this type conversion to serialization
    //  Type actionMapType = new TypeToken<Map<String, Action>>() {
    //  }.getType();
    //  String actionHashedMapInStr = actionGson.toJson(actionsAvailableToPlayer, actionMapType);
    //  return ResponseEntity.status(HttpStatus.OK).body(actionHashedMapInStr);
    //} catch (ModelAccessException | UnirestException e) {
    //  // something went wrong, reply with a bad request
    //  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    //}
  }


  private static Gson getActionGson() {
    RuntimeTypeAdapterFactory<Action> actionFactory =
        RuntimeTypeAdapterFactory
            .of(Action.class, "type")
            .registerSubtype(ReserveAction.class)
            .registerSubtype(PurchaseAction.class)
            .registerSubtype(TakeTokenAction.class);

    return new GsonBuilder()
        .registerTypeAdapterFactory(actionFactory).create();

  }


  /**
   * Select action.
   */
  @PostMapping(value = {
      "/splendororient/api/games/{gameId}/players/{playerName}/actions/{actionId}",
      "/splendorbase/api/games/{gameId}/players/{playerName}/actions/{actionId}"})
  public ResponseEntity<String> selectAction(@PathVariable long gameId,
                                             @PathVariable String playerName,
                                             @PathVariable String actionId,
                                             @RequestParam(value = "access_token")
                                             String accessToken) {

    // TODO: Redo based on sequence diagram

    return null;

    //try {
    //  gameIdPlayerNameValidCheck(accessToken, playerName, gameId);
    //
    //  // if the client accidentally sends a request to server to ask for updates on inventory,
    //  // it will be wrong because the player is not supposed to have updates on inventory
    //  // outside out their turn
    //  GameInfo gameInfo = splendorGameManager.getGameById(gameId);
    //  if (!isPlayerTurn(playerName, gameInfo)) {
    //    throw new ModelAccessException("It is not this player's turn, no POST request should be"
    //        + "sent to this resource location yet!");
    //  }
    //
    //  Action newAction;
    //  //if (userAction == null) {
    //  //  // the actions provided from GET is not changed, we just need the actionId to retrieve
    //  //  // the action from actionGenerator map
    //  //  newAction = splendorActionListGenerator.lookUpActions(gameId, playerName).get(actionId);
    //  //
    //  //} else {
    //  //  // otherwise, we interpret based on this newly generated action userAction
    //  //  newAction = userAction;
    //  //}
    //  //newAction = splendorActionListGenerator.lookUpActions(gameId, playerName).get(actionId);
    //
    //  PlayerInGame playerInGame = splendorGameManager
    //      .getPlayerStatesById(gameId)
    //      .getPlayersInfo()
    //      .get(playerName);
    //  // interpret this action regardless is modified by user or not
    //
    //  actionInterpreter.interpretAction(actionId, playerName);
    //
    //  // notify the async updates
    //  // TODO: When things that might affect the long polling content we want
    //  //  in this case, actions were executed, then we want to touch the managers so that
    //  //  we can have our result back
    //  allPlayerInfoBroadcastContentManager.get(gameId).touch();
    //  gameInfoBroadcastContentManager.get(gameId).touch();
    //  return ResponseEntity.status(HttpStatus.OK).body(null);
    //
    //} catch (ModelAccessException | UnirestException e) {
    //  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    //}
  }

}
