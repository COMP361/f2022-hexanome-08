package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.PlayerInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.SavedGameState;
import ca.group8.gameservice.splendorgame.controller.SplendorJsonHelper;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import com.google.gson.reflect.TypeToken;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
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
  private final String saveGameMetaFileName = "saved_games_meta.json";
  private List<String> savedGameIds;
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
   * Sync the game service saved game with LS.
   * It makes more sense to have LS to align with game service becasuse game
   * service contains the actual data (not just the metadata as LS does)
   */
  @PostConstruct
  public void syncSavedGames() throws ModelAccessException {

    try {
      // skip all steps if we do not have info in file
      if(readSavedGameDataFromFile() == null) {
        return;
      }
      List<String> gameIdsFromData = new ArrayList<>(readSavedGameDataFromFile().keySet());
      List<Savegame> savedMetaData = readSavedGameMetaDataFromFile();
      List<String> gameIdsFromMetaData = savedMetaData.stream().map(Savegame::getSavegameid)
          .collect(Collectors.toList());
      if (gameIdsFromData.size() != gameIdsFromMetaData.size()) {
        String error = "Inconsistency between meta/actual game data!";
        logger.warn(error);
        throw new ModelAccessException(error);
      }
      List<String> gameIdsFromLobby = lobbyCommunicator.getAllSaveGameIds();
      // first, delete the game ids in lobby that are not in server
      List<String> idsInLobbyNotInServer = gameIdsFromLobby.stream()
          .filter(gameIdsFromData::contains)
          .collect(Collectors.toList());
      // iterate through the ids that shouldn't be in lobby and delete them
      for (Savegame game : savedMetaData) {
        if (idsInLobbyNotInServer.contains(game.getSavegameid())) {
          lobbyCommunicator.deleteSavedGame(game.getGamename(), game.getSavegameid());
        }
      }
      // obtain an updated lobby ids (now server might have ids that lobby doesn't have)
      gameIdsFromLobby = lobbyCommunicator.getAllSaveGameIds();
      List<String> idsInServerNotInLobby = gameIdsFromData.stream()
          .filter(gameIdsFromLobby::contains)
          .collect(Collectors.toList());
      // iterate through again to put the ids into lobby (PUT request)
      for (Savegame game : savedMetaData) {
        if (idsInServerNotInLobby.contains(game.getSavegameid())) {
          lobbyCommunicator.putSaveGame(game);
        }
      }
      savedGameIds = new ArrayList<>(gameIdsFromData);
    } catch (IOException | UnirestException e) {
      logger.warn(e.getMessage());
    }
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
   * Launch a game based on game id and the launcher info given.
   * internally handle the fact that we might launch a game from saved file.
   *
   * @param gameId game id (long) of the game we want to launch
   * @param launcherInfo detailed info we need to launch the game
   * @return an object contains game info, player states and action interpreter
   * @throws ModelAccessException can not launch the game because an IO issue
   //*/
  public SavedGameState launchGame(long gameId, LauncherInfo launcherInfo)
      throws ModelAccessException{
    // if we have a non-empty savegame id, then we load
    // the game content rather than creating new objects.
    if (!launcherInfo.getSavegame().isEmpty()) {
      String saveGameId = launcherInfo.getSavegame();
      try {
        List<String> curPlayerNames = launcherInfo.getPlayers().stream()
            .map(PlayerInfo::getName)
            .collect(Collectors.toList());
        String creator = launcherInfo.getCreator();
        Map<String, SavedGameState> savedGames = readSavedGameDataFromFile();
        SavedGameState savedGame = savedGames.get(saveGameId);
        // rename the player names in this savedGameState
        savedGame.renamePlayers(curPlayerNames, creator);

        // put the renamed objects to manager
        activeGames.put(gameId, savedGame.getGameInfo());
        activePlayers.put(gameId, savedGame.getPlayerStates());
        gameActionInterpreters.put(gameId, savedGame.getActionInterpreter());
        return savedGame;
      } catch (IOException e) {
        logger.warn(e.getMessage());
        throw new ModelAccessException(e.getMessage());
      }
    } else { // the case where we are not launching a saved game, create gameinfo, etc from scratch
      String gameServerName = launcherInfo.getGameServer();
      List<Extension> gameExtensions = new ArrayList<>();
      gameExtensions.add(Extension.BASE);
      gameExtensions.add(Extension.ORIENT);
      if (gameServerName.equals("splendorcity")) {
        gameExtensions.add(Extension.CITY);
      }

      if (gameServerName.equals("splendortrade")) {
        gameExtensions.add(Extension.TRADING_POST);
      }
      // get all player names
      List<String> playerNames = launcherInfo
          .getPlayers()
          .stream()
          .map(PlayerInfo::getName)
          .collect(Collectors.toList());

      GameInfo newGameInfo = new GameInfo(gameExtensions, playerNames, launcherInfo.getCreator());
      PlayerStates newPlayerStates = new PlayerStates(playerNames);
      ActionInterpreter newActionInterpreter = new ActionInterpreter(newGameInfo, newPlayerStates);
      // added game info, player states and action interpreter for this specific gameId
      activeGames.put(gameId, newGameInfo);
      activePlayers.put(gameId, newPlayerStates);
      gameActionInterpreters.put(gameId, newActionInterpreter);
      return new SavedGameState(newGameInfo, newPlayerStates, newActionInterpreter);
    }
  }


  /**
   * Internally, verify the content of game ids to be synced and updated with LS.
   *
   * @return an updated game ids
   */
  public List<String> getSavedGameIds() {
    try {
      syncSavedGames();
    } catch (ModelAccessException e) {
      logger.warn(e.getMessage());
    }
    return savedGameIds;
  }


  public void deleteGame(long gameId) {

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
      logger.warn(e.getMessage());
    }

    // TODO: 2. store the actual data and metadata into the file
    SavedGameState newSaveGame =
        new SavedGameState(
            activeGames.get(gameId),
            activePlayers.get(gameId),
            gameActionInterpreters.get(gameId));
    writeSavedGameDataToFile(savegame.getSavegameid(), newSaveGame, true);
    writeSavedGameMetaDataToFile(savegame, true);
  }



  /**
   * Read a map of String -> SavedGameState (which stores gameInfo, playerStates, interpreter).
   * out of the json file.
   *
   * @return a map of String -> SavedGameState, if file is empty, return null
   * @throws IOException in case the json file is missing.
   */
  public Map<String, SavedGameState> readSavedGameDataFromFile() throws IOException {
    try {
      FileReader fileReader = new FileReader(saveGameInfoFileName, StandardCharsets.UTF_8);
      Type mapOfSaveGameStates = new TypeToken<Map<String,SavedGameState>>() {}.getType();
      return SplendorJsonHelper.getInstance().getGson().fromJson(fileReader, mapOfSaveGameStates);
    } catch (IOException e) {
      throw new IOException("file: server/saved_games_data.json not found, please create one!");
    }
  }

  /**
   * Write to actual data file.
   * Depending on addToFile being true/false, we decide whether to add/remove the content
   *
   * @param saveGameId the id of the content
   * @param savedGameState the content ready to be written
   * @param addToFile the flag indicating delete or not
   */
  private void writeSavedGameDataToFile(String saveGameId,
                                       SavedGameState savedGameState, boolean addToFile) {
    try {
      Map<String,SavedGameState> allSaveGames = readSavedGameDataFromFile();
      if (allSaveGames == null) {
        // in case the file is empty, just add the data
        allSaveGames = new HashMap<>();
        allSaveGames.put(saveGameId,savedGameState);
      } else { // if the file is not empty, check if we have duplicate id when putting
        if (addToFile) {
          if(allSaveGames.containsKey(saveGameId)) {
            return; // duplicate id, do not write anything
          }
          allSaveGames.put(saveGameId,savedGameState);
        } else {
          allSaveGames.remove(saveGameId);
        }
      }
      FileWriter dataWriter = new FileWriter(saveGameInfoFileName, StandardCharsets.UTF_8);
      Type mapOfSaveGameStates = new TypeToken<Map<String,SavedGameState>>() {}.getType();
      String newSaveGamesJson = SplendorJsonHelper.getInstance().getGson()
          .toJson(allSaveGames, mapOfSaveGameStates);
      dataWriter.write(newSaveGamesJson);
      dataWriter.close();

    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
  }



  /**
   *  Read a List of Savegame from file.
   *
   * @return a List of Savegame from file. if file is empty, return null
   * @throws IOException in case of a file missing
   */
  public List<Savegame> readSavedGameMetaDataFromFile() throws IOException {
    try {
      FileReader fileReader = new FileReader(saveGameMetaFileName, StandardCharsets.UTF_8);
      Type listOfSavegame = new TypeToken<List<Savegame>>() {}.getType();
      return SplendorJsonHelper.getInstance().getGson().fromJson(fileReader, listOfSavegame);
    } catch (IOException e) {
      throw new IOException("file: server/saved_games_meta.json not found, please create one!");
    }
  }



  /**
   * Write to metadata file.
   * Depending on addToFile being true/false, we decide whether to add/remove the content
   *
   * @param savegame the content ready to be written
   * @param addToFile the flag indicating delete or not
   */
  private void writeSavedGameMetaDataToFile(Savegame savegame, boolean addToFile) {
    try {
      List<Savegame> allSaveGamesMeta = readSavedGameMetaDataFromFile();
      if (allSaveGamesMeta == null) {
        allSaveGamesMeta = new ArrayList<>();
        allSaveGamesMeta.add(savegame);
      } else {
        if(addToFile) {
          boolean hasDuplicateId = allSaveGamesMeta.stream()
              .anyMatch(game -> game.getSavegameid().equals(savegame.getSavegameid()));
          if (hasDuplicateId) {
            return; // duplicated game id, do not add to json.
          }
          allSaveGamesMeta.add(savegame);
        } else {
          allSaveGamesMeta.remove(savegame);
        }
      }
      FileWriter metaDataWriter = new FileWriter(saveGameMetaFileName, StandardCharsets.UTF_8);
      Type listOfSavegame = new TypeToken<List<Savegame>>(){}.getType();
      String newSaveGamesMetaJson = SplendorJsonHelper
          .getInstance().getGson()
          .toJson(allSaveGamesMeta, listOfSavegame);

      metaDataWriter.write(newSaveGamesMetaJson);
      metaDataWriter.close();
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
  }

}
