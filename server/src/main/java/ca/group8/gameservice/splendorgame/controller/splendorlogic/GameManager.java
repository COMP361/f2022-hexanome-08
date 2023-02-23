package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.SavedGameState;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.SplendorJsonHelper;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;


/**
 * Manages instances of Splendor games.
 */
@Component
public class GameManager {

  private final Map<Long, PlayerStates> activePlayers;
  private final Map<Long, GameInfo> activeGames;
  private final Map<Long, ActionInterpreter> gameActionInterpreters;

  /**
   * Construct a new GameManager instance (initialize all maps to empty HashMaps).
   */
  public GameManager() {
    this.activePlayers = new HashMap<>();
    this.activeGames = new HashMap<>();
    this.gameActionInterpreters = new HashMap<>();
  }

  /**
   * get one instance of GameInfo object that contains the public game details on game board.
   *
   * @param gameId game id
   * @return one instance of GameInfo object
   * @throws ModelAccessException if model access went wrong
   */
  public GameInfo getGameById(long gameId) throws ModelAccessException {
    if (!isExistentGameId(gameId)) {
      throw new ModelAccessException("No registered game with that ID");
    }
    return activeGames.get(gameId);
  }

  public boolean isExistentGameId(long gameId) {
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
  public boolean isPlayerInGame(long gameId, String playerName) {
    return activePlayers.get(gameId).getPlayersInfo().containsKey(playerName);
  }

  public Map<Long, PlayerStates> getActivePlayers() {
    return activePlayers;
  }



  public void readFileToGame() throws IOException {
    Type listOfSaveGamesType = new TypeToken<List<SavedGameState>>() {}.getType();
    ClassPathResource resource = new ClassPathResource(
        "ca/group8/gameservice/splendorgame/controller/splendorlogic/saved_games_data.json");
    try {
      InputStreamReader reader =
          new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
      String savedStatesJson = FileCopyUtils.copyToString(reader);
      List<SavedGameState> savedGameStates
          = new Gson().fromJson(savedStatesJson, listOfSaveGamesType);
      for (SavedGameState savedGame : savedGameStates) {
        long gameId = savedGame.getGameId();
        GameInfo gameInfo = savedGame.getGameInfo();
        PlayerStates playerStates = savedGame.getPlayerStates();
        ActionInterpreter actionInterpreter = savedGame.getActionInterpreter();
        activeGames.put(gameId, gameInfo);
        activePlayers.put(gameId, playerStates);
        gameActionInterpreters.put(gameId, actionInterpreter);
      }
    } catch (IOException e) {
      //List<SavedGameState> emptyList = new ArrayList<>();
      //ResourceLoader resourceLoader = new DefaultResourceLoader();
      //String saveGameJson = new Gson().toJson(emptyList, listOfSaveGamesType);
      //Resource resource1 = resourceLoader.getResource("classpath:saved_games_data.json");
      //File tempFile = File.createTempFile("saved_games_data", ".json");
      //
      //Files.copy(resource1.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      //
      //
      //OutputStreamWriter writer = new OutputStreamWriter
      //    (resource1.getOutputStream(), StandardCharsets.UTF_8);
      //writer.write(saveGameJson);
      //writer.close();

    }
  }

    public void writeToFile() throws IOException {
      Type listOfSaveGamesType = new TypeToken<List<SavedGameState>>() {}.getType();
      List<SavedGameState> emptyList = new ArrayList<>();
      for (long gameId:activeGames.keySet()) {
        GameInfo game = activeGames.get(gameId);
        PlayerStates players = activePlayers.get(gameId);
        ActionInterpreter actionInterpreter = gameActionInterpreters.get(gameId);
        SavedGameState ss = new SavedGameState(gameId, game, players, actionInterpreter);
        emptyList.add(ss);
      }
      String saveGameJson = SplendorJsonHelper.getInstance().getGson()
          .toJson(emptyList, listOfSaveGamesType);
      String fileName = "saved_games_data.json";
      FileWriter fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8);
      fileWriter.write(saveGameJson);
      fileWriter.close();
    }

    public void readFromFile() throws IOException {
      List<SavedGameState> holder = new ArrayList<>();
      String fileName = "saved_games_data.json";
      FileReader fr = new FileReader(fileName, StandardCharsets.UTF_8);
      Type listOfSaveGamesType = new TypeToken<List<SavedGameState>>() {}.getType();
      holder = SplendorJsonHelper.getInstance().getGson().fromJson(fr, listOfSaveGamesType);
      System.out.println(holder.get(0).getGameId());

    }

}
