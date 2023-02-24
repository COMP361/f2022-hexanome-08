package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.SavedGameState;
import ca.group8.gameservice.splendorgame.controller.SplendorJsonHelper;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.SplendorGameException;
import com.google.gson.reflect.TypeToken;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Manages instances of Splendor games.
 */
@Component
public class GameManager {

  private final String saveGameInfoFileName = "saved_games_data.json";
  private final Map<Long, PlayerStates> activePlayers;
  private final Map<Long, GameInfo> activeGames;
  private final Map<Long, ActionInterpreter> gameActionInterpreters;

  private final LobbyCommunicator lobbyCommunicator;
  private final Logger logger;

  /**
   * Construct a new GameManager instance (initialize all maps to empty HashMaps).
   */
  public GameManager(@Autowired LobbyCommunicator lobbyCommunicator) {
    this.lobbyCommunicator = lobbyCommunicator;
    this.activePlayers = new HashMap<>();
    this.activeGames = new HashMap<>();
    this.gameActionInterpreters = new HashMap<>();
    this.logger = LoggerFactory.getLogger(GameManager.class);
  }

  /**
   * get one instance of GameInfo object that contains the public game details on game board.
   *
   * @param gameId game id
   * @return one instance of GameInfo object
   */
  public GameInfo getGameById(long gameId) throws ModelAccessException {
    if (!containsGameId(gameId)) {
      throw new ModelAccessException("No registered game with that ID");
    }
    return activeGames.get(gameId);
  }

  public boolean containsGameId(long gameId) {
    return activeGames.containsKey(gameId);
  }

  /**
   * Add a new game instance to the list of games.
   *
   * @param gameId      ID of the new game instance.
   * @param newGameInfo Actual GameInfo instance.
   */
  public void addGame(long gameId, GameInfo newGameInfo) {
    assert newGameInfo != null;
    assert !activeGames.containsKey(gameId); //ensure gameId isn't already in list.

    activeGames.put(gameId, newGameInfo);
  }

  public PlayerStates getPlayerStatesById(long gameId) {
    assert activePlayers.containsKey(gameId);
    return activePlayers.get(gameId);
  }

  public void addGamePlayerStates(long gameId, PlayerStates newPlayerStates) {
    assert !activePlayers.containsKey(newPlayerStates);
    activePlayers.put(gameId, newPlayerStates);
  }

  public ActionInterpreter getGameActionInterpreter(long gameId) {
    assert gameActionInterpreters.containsKey(gameId);
    return gameActionInterpreters.get(gameId);
  }

  public void addGameActionInterpreter(long gameId, ActionInterpreter actionInterpreter) {
    assert activeGames.containsKey(gameId) && actionInterpreter != null;
    gameActionInterpreters.put(gameId, actionInterpreter);
  }

  /**
   * Remove all data related to a specific game.
   *
   * @param gameId game to be removed.
   */
  public void removeGameRelatedData(long gameId) {
    assert activeGames.containsKey(gameId);

    removePlayerStates(gameId);
    removeGame(gameId);
    removeActionInterpreter(gameId);

  }

  public void removePlayerStates(long gameId) {
    assert activePlayers.containsKey(gameId);
    activePlayers.remove(gameId);
  }

  public void removeGame(long gameId) {
    assert activeGames.containsKey(gameId);
    activeGames.remove(gameId);
  }

  public void removeActionInterpreter(long gameId) {
    assert gameActionInterpreters.containsKey(gameId);
    gameActionInterpreters.remove(gameId);
  }


  public Map<Long, GameInfo> getActiveGames() {
    return activeGames;
  }

  /**
   * Check whether current player is in game or not.
   */
  public boolean containsPlayer(long gameId, String playerName) {
    return activePlayers.get(gameId).getPlayersInfo().containsKey(playerName);
  }

  public Map<Long, PlayerStates> getActivePlayers() {
    return activePlayers;
  }

  /**
   * Internally, read all saved game instances & all needed metadata from json file.
   *
   * @param gameServiceName the game service that client wants info from.
   * @param accessToken the token of the client who requires displaying all saved games.
   * @param saveGameId the save game id (a string) which uniquely identify one game instance.
   * @param gameId the gameId/sessionId that we want to load the concrete data and store for.
   * @throws IOException in case the file is not there (accidentally deleted)
   */
  private void loadSavedGameToManager(String gameServiceName,
                                      String accessToken, String saveGameId, long gameId)
      throws IOException, SplendorGameException {
    try {
      // TODO: 1. send a quest to LS to get all available save game ids
      Savegame[] allSavedGames = lobbyCommunicator.getAllSavedGames(accessToken,gameServiceName);
      Map<String, SavedGameState> allSaveGames = readSaveGamesFromFile();
      if (!allSaveGames.containsKey(gameServiceName)) {
        // no such save game id existing in the file for some reasons
        String error = "SaveGameId: " + saveGameId + " not found in json file!";
        throw new SplendorGameException(error);
      }

      // TODO: 2. load the actual data into the file
      SavedGameState savedGame = allSaveGames.get(saveGameId);
      GameInfo gameInfo = savedGame.getGameInfo();
      PlayerStates playerStates = savedGame.getPlayerStates();
      ActionInterpreter actionInterpreter = savedGame.getActionInterpreter();
      activeGames.put(gameId, gameInfo);
      activePlayers.put(gameId, playerStates);
      gameActionInterpreters.put(gameId, actionInterpreter);

    } catch (IOException e) {
      throw new IOException("file: server/saved_games_data.json not found, please create one!");
    }
  }

  /**
   * Save gameInfo, playerStates and actionInterpreter of such gameId into the json file.
   * path: server/saved_games_data.json
   * NOTE: saveGameId and gameId are different, saveGameId is a Unique String name used to
   * identify the exact game instances data, gameId is a long which refers to session id
   * from LS.
   * @param savegame a class that stores metadata to save a game
   * @param gameId the game id used to retrieve info needed to store the game
   */
  public void saveGame(Savegame savegame, long gameId) {

    // TODO: 1. send a request to LS to save the Savegame
    try {
      lobbyCommunicator.putSaveGame(savegame);
    } catch (UnirestException e){

    }


    // TODO: 2. store the actual data into the file
    GameInfo gameInfo = activeGames.get(gameId);
    PlayerStates playerStates = activePlayers.get(gameId);
    ActionInterpreter actionInterpreter = gameActionInterpreters.get(gameId);
    SavedGameState newSaveGame = new SavedGameState(gameInfo, playerStates, actionInterpreter);

    try {
      Map<String,SavedGameState> allSaveGames = readSaveGamesFromFile();
      String saveGameId = savegame.getSavegameid();
      allSaveGames.put(saveGameId,newSaveGame);
      FileWriter fileWriter = new FileWriter(saveGameInfoFileName, StandardCharsets.UTF_8);
      Type mapOfSaveGameStates = new TypeToken<Map<String,SavedGameState>>() {}.getType();
      String newSaveGamesJson = SplendorJsonHelper.getInstance().getGson()
          .toJson(allSaveGames, mapOfSaveGameStates);
      fileWriter.write(newSaveGamesJson);
      fileWriter.close();
    } catch (IOException e) {
      logger.warn("Failed to read/write data to store the game detail information!");
    }
  }

  /**
   * Read a map from savegameId -> SavedGameState (which stores gameInfo, playerStates, interpreter)
   */
  private Map<String, SavedGameState> readSaveGamesFromFile() throws IOException {
    try {
      FileReader fileReader = new FileReader(saveGameInfoFileName, StandardCharsets.UTF_8);
      Type mapOfSaveGameStates = new TypeToken<Map<String,SavedGameState>>() {}.getType();
      return SplendorJsonHelper.getInstance().getGson().fromJson(fileReader, mapOfSaveGameStates);
    } catch (IOException e) {
      throw new IOException("file: server/saved_games_data.json not found, please create one!");
    }
  }
}
