package ca.group8.gameservice.splendorgame.controller.splendorlogic;
import ca.group8.gameservice.splendorgame.controller.GameRestController;
import ca.group8.gameservice.splendorgame.controller.SplendorRegistrator;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.SplendorGameManager;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import com.google.gson.Gson;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import java.util.Optional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class SplendorRestController implements GameRestController {

  private SplendorRegistrator splendorRegistrator;
  private SplendorGameManager splendorGameManager;

  private String gameServiceName;

  private final Map<Long, BroadcastContentManager<TableTop>> broadcastContentManagers;

  private final Logger logger;

  public SplendorRestController(
      @Autowired SplendorRegistrator splendorRegistrator, SplendorGameManager splendorGameManager,
                                @Value("${gameservice.name}") String gameServiceName) {
    this.splendorRegistrator = splendorRegistrator;
    this.splendorGameManager = splendorGameManager;
    this.gameServiceName = gameServiceName;
    this.broadcastContentManagers = new HashMap<>();
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

  //private BaseCard parseCardObject(JSONObject card) {
  //  String cardName = (String) card.get("cardName");
  //  logger.info("card level is: " + card.get("level"));
  //  int cardLevel = ((Long) card.get("level")).intValue();
  //  int prestigePoints = ((Long) card.get("prestigePoints")).intValue();
  //  Optional<Colour> gemColour = Optional.of(Colour.valueOf((String) card.get("gemColour")));
  //  int gemNumber = ((Long) card.get("gemNumber")).intValue();
  //  Boolean isPaired = (Boolean) card.get("isPaired");
  //  String pairedCardId = (String) card.get("pairedCardId");
  //  EnumMap<Colour, Integer> price = parsePriceObject((JSONObject) card.get("price"));
  //  return new BaseCard(prestigePoints, price, cardName, cardLevel, gemColour, isPaired, pairedCardId, gemNumber);
  //}
  //
  //
  //private EnumMap<Colour, Integer> parsePriceObject(JSONObject price) {
  //  EnumMap<Colour, Integer> result = new EnumMap<>(Colour.class);
  //  int bluePrice = ((Long) price.get("BLUE")).intValue();
  //  result.put(Colour.BLUE, bluePrice);
  //  int blackPrice = ((Long) price.get("BLACK")).intValue();
  //  result.put(Colour.BLACK, blackPrice);
  //  int redPrice = ((Long) price.get("RED")).intValue();
  //  result.put(Colour.RED, redPrice);
  //  int greenPrice = ((Long) price.get("GREEN")).intValue();
  //  result.put(Colour.GREEN, greenPrice);
  //  int whitePrice = ((Long) price.get("WHITE")).intValue();
  //  result.put(Colour.WHITE, whitePrice);
  //  return result;
  //}
  //
  //@GetMapping("/cards")
  //public void generateCards() {
  //  JSONParser jsonParser = new JSONParser();
  //  try (FileReader reader = new FileReader(ResourceUtils.getFile("classpath:cardinfo_basecard.json"))){
  //    Object obj = jsonParser.parse(reader);
  //    JSONArray cardList = (JSONArray) obj;
  //    for (Object o : cardList) {
  //      BaseCard c = parseCardObject((JSONObject) o);
  //      logger.info(c.getCardName());
  //    }
  //
  //  } catch (ParseException | IOException e) {
  //    throw new RuntimeException(e);
  //  }
  //}

  @Override
  @PutMapping(value = "/api/games/{gameId}", consumes = "application/json; charset=utf-8")
  public ResponseEntity<String> launchGame(@PathVariable long gameId,
                                           @RequestBody LauncherInfo launcherInfo)
      throws FileNotFoundException{
    // TODO: Handle the logic of how the game is managed in game manager
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
      broadcastContentManagers.put(gameId, new BroadcastContentManager<>(curTableTop));
      return ResponseEntity.status(HttpStatus.OK).build();

    } catch (ModelAccessException e) {
      // something did not go well, reply with a bad request message
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<String> deleteGame(long gameId) {
    return null;
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getBoard(long gameId, String hash) {
    return null;
  }

  @Override
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

  @Override
  public ResponseEntity<String> getActions(long gameId, String player, String accessToken) {
    return null;
  }

  @Override
  public ResponseEntity<String> selectAction(long gameId, String player, String actionMD5,
                                             String accessToken) {
    return null;
  }

  @Override
  public ResponseEntity<String> getRanking(long gameId) {
    return null;
  }
}
