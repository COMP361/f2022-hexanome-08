package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.SplendorJsonHelper;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.GameServerParameters;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Registrator class.
 */
@Component
public class LobbyCommunicator {

  private final Logger logger;
  // Since we first need the value to construct this singleton class
  // prior to assign value to this filed, we use @Value annotation
  // in the constructor and then assign it to the field,
  // same thing apply to all fields that are needed to construct
  // the GameServerParameter communication bean object.
  private final List<String> gameServiceNames;
  private final List<String> gameServiceDisplayNames;
  private final List<String> gameServiceUsernames;
  private final List<String> gameServicePasswords;
  private final String gameServiceLocation;
  private final String lobbyServiceAddress;


  @Autowired
  LobbyCommunicator(@Value("${gameservice.names}") String[] gameServiceNames,
                    @Value("${gameservice.displayNames}") String[] gameServiceDisplayNames,
                    @Value("${game.usernames}") String[] gameServiceUsernames,
                    @Value("${game.passwords}") String[] gameServicePasswords,
                    @Value("${gameservice.location}") String gameServiceLocation,
                    @Value("${lobbyservice.location}") String lobbyServiceAddress) {

    // first assigning all fields first, then construct the GameServerParameter class
    this.gameServiceNames = Arrays.asList(gameServiceNames);
    this.gameServiceDisplayNames = Arrays.asList(gameServiceDisplayNames);
    this.gameServiceUsernames = Arrays.asList(gameServiceUsernames);
    this.gameServicePasswords = Arrays.asList(gameServicePasswords);
    this.lobbyServiceAddress = lobbyServiceAddress;
    this.gameServiceLocation = gameServiceLocation;
    // for error messages
    this.logger = LoggerFactory.getLogger(LobbyCommunicator.class);
  }

  @PostConstruct
  private void registerAllGameServices() {
    // TODO: 1. Send a POST request under lobbyUrl + /oauth/token -> access_token
    //  then 2. With the token, send a GET to lobbyUrl + /oauth/role -> get the role
    //  (must be "authority": "ROLE_SERVICE") then 3. We can now send PUT request
    //  to lobbyUrl + /api/gameservices/{gameservicename} to register the game

    String accessToken;
    String roleOfGame;
    for (int i = 0; i < gameServiceUsernames.size(); i++) {
      String userName = gameServiceUsernames.get(i);
      String passWord = gameServicePasswords.get(i);

      try {
        accessToken = getGameOauthToken(userName, passWord);
        // GET Request, check if role is "ROLE_SERVICE"
        roleOfGame = getAuthorityRequest(accessToken);
        if (roleOfGame.equals("ROLE_SERVICE")) {
          // Send the PUT request if it is "ROLE_SERVICE"
          String curGameName = gameServiceNames.get(i);
          String curDisplayName = gameServiceDisplayNames.get(i);
          String curGameLocation = gameServiceLocation + curGameName;
          GameServerParameters curGameServerParams
              = new GameServerParameters(curGameName, curDisplayName,
              curGameLocation, 4, 2, "false");
          registerGameAtLobby(accessToken, curGameName, curGameServerParams);
        } else {
          String msg = String.format("Wrong role of user with username: %s and password: %s",
                  userName, passWord);
          logger.warn(msg);
        }
      } catch (UnirestException e) {
        logger.warn(e.getMessage());
        break;
      }
    }

  }

  /**
   * Send a delete request to LS to indicate that we want to delete.
   * the game: gameName, with saveGameId.
   *
   * @param gameName name of the game service. (fixed, such splendorbase)
   * @param saveGameId id of the saved game. (a saved customized name)
   */
  public void deleteSavedGame(String gameName, String saveGameId) throws UnirestException {
    String url = String.format("%s/api/gameservices/%s/savegames/%s", lobbyServiceAddress,
        gameName, saveGameId);
    try{
      String accessToken = getGameOauthToken(gameName, gameServicePasswords.get(0));
      HttpResponse<String> response = Unirest.delete(url)
          .queryString("access_token",accessToken).asString();
      if(response.getStatus() != 200) {
        throw new UnirestException("Failed to delete the game with id: " + saveGameId);
      }
    } catch (UnirestException e) {
      logger.warn(e.getMessage());
    }
  }
  /**
   * Get all saved game ids of all game servers registered at LS.
   *
   * @return a list of string of such saved game ids
   */
  public List<String> getAllSaveGameIds() {
    List<String> allIds = new ArrayList<>();
    for (String serviceName : gameServiceNames) {
      String accessToken;
      try {
        accessToken = getGameOauthToken(serviceName, gameServicePasswords.get(0));
        Savegame[] allSaveGames = getAllSavedGames(accessToken, serviceName);
        // add all saved game ids of this game server
        allIds.addAll(Arrays.stream(allSaveGames)
          .map(Savegame::getSavegameid)
          .collect(Collectors.toList()));
      } catch (UnirestException e) {
        logger.warn(e.getMessage());
        break;
      }
    }
    return allIds;
  }

  /**
   * Return an array of Savegame to one specific game service (base, city or trade).
   * to the player with accessToken.
   *
   * @param accessToken access token of the player
   * @param gameServiceName splendorbase, splendorcity, ... (game service names)
   * @return an array of Savegame to one specific game service, can be empty
   */
  public Savegame[] getAllSavedGames(String accessToken, String gameServiceName)
  throws UnirestException{
    String url = String.format("%s/api/gameservices/%s/savegames",
        lobbyServiceAddress, gameServiceName);
    Savegame[] result = new Savegame[0];
    try {
      HttpResponse<String> response = Unirest.get(url)
          .queryString("access_token", accessToken)
          .asString();
      String jsonString = response.getBody();
      result = new Gson().fromJson(jsonString, Savegame[].class);
      logger.info(Arrays.toString(result));
      if (response.getStatus() != 200) {
        String msg = "Unable to perform GET all sessions request to LS";
        logger.warn(msg);
        throw new UnirestException(msg);
      }
    } catch (UnirestException e) {
      logger.warn(e.getMessage());
    }
    return result;
  }

  /**
   * send a request to LS to save the Savegame.
   *
   * @param savegame the Savegame instance
   */
  public void putSaveGame(Savegame savegame) throws UnirestException {
    String serviceName = savegame.getGamename();
    String saveGameId = savegame.getSavegameid();
    String body = SplendorJsonHelper.getInstance().getGson().toJson(savegame, Savegame.class);
    String url = String.format("%s/api/gameservices/%s/savegames/%s", lobbyServiceAddress,
        serviceName, saveGameId);
    String accessToken;
    try {
      accessToken = getGameOauthToken(serviceName,gameServicePasswords.get(0));
      logger.info("Token:" + accessToken);
      HttpResponse<String> response = Unirest.put(url)
          .header("Content-Type", "application/json")
          .queryString("access_token", accessToken)
          .body(body)
          .asString();
      if (response.getStatus() != 200) {
        throw new UnirestException(response.getBody());
      }

    } catch (UnirestException e) {
      logger.warn(e.getMessage());
    }

  }

  /**
   * Send a DELETE request to lobby service and delete the save game.
   *
   * @param savegame save game instance
   */
  public void deleteSaveGame(Savegame savegame) {
    String serviceName = savegame.getGamename();
    String saveGameId = savegame.getSavegameid();
    String url = String.format("%s/api/gameservices/%s/savegames/%s",
        lobbyServiceAddress, serviceName, saveGameId);
    String accessToken;
    try {
      accessToken = getGameOauthToken(serviceName,gameServicePasswords.get(0));
      logger.info("Token:" + accessToken);
      HttpResponse<String> response = Unirest.delete(url)
          .queryString("access_token", accessToken).asString();

      if (response.getStatus() != 200) {
        throw new UnirestException(response.getBody());
      }

    } catch (UnirestException e) {
      logger.warn(e.getMessage());
    }
  }


  /**
   * register a game to lobby.
   */
  private void registerGameAtLobby(String accessToken,
                                  String gameServiceName,
                                  GameServerParameters gameServerParameters)
      throws UnirestException {
    String requestJsonString = new Gson().toJson(gameServerParameters);
    String url = lobbyServiceAddress + "/api/gameservices/" + gameServiceName;
    HttpResponse<String> response = Unirest.put(url)
        .header("Authorization", "Bearer " + accessToken)
        .header("Content-Type", "application/json")
        .body(requestJsonString).asString();
    if (response.getStatus() == 200) {
      logger.info(String.format("Game: %s successfully registered as LS!",
          gameServerParameters.getDisplayName()));
    } else {
      String msg = String.format("Failed to register game (they might exist): %s, status: %s",
          gameServerParameters.getDisplayName(),response.getStatus());
      logger.warn(msg);
    }
  }

  /**
   * Check whether the token provided matches the player name in LS.
   *
   * @param accessToken access token of log-in user
   * @param playerName player name in record in LS
   * @return whether the names match or not
   */
  public boolean isValidToken (String accessToken, String playerName) {
    try {
      HttpResponse<String> nameResponse = Unirest.get(lobbyServiceAddress + "/oauth/username")
          .header("Authorization", "Bearer " + accessToken)
          .asString();
      String responseUserName = nameResponse.getBody();
      logger.info("access token represents: " + responseUserName);
      logger.info("current player is: " + playerName);
      return responseUserName.equals(playerName);
    } catch (UnirestException e) {
      logger.warn("Can not get access token for: " + playerName);
      return false;
    }
  }

  /**
   * Sends request to check authority of the access token.
   */
  private String getAuthorityRequest(String accessToken) throws UnirestException {
    // queryString: request param
    HttpResponse<JsonNode> authorityResponse = Unirest.get(lobbyServiceAddress + "/oauth/role")
        .queryString("access_token", accessToken).asJson();
    JSONArray jsonarray = new JSONArray(authorityResponse.getBody().toString());
    if(authorityResponse.getStatus() != 200) {
      throw new UnirestException("Failed to get the role of game");
    }
    return jsonarray.getJSONObject(0).getString("authority");
  }


  /**
   * Get the OAuth token associated with the "Splendor_base" service user.
   *
   * @return OAuth token
   */
  private String getGameOauthToken(String gameServiceUsername,
                                   String gameServicePassword) throws UnirestException {
    JSONObject tokenResponse;
    String accessToken = "";
    try {
      HttpResponse<JsonNode> response = Unirest.post(lobbyServiceAddress + "/oauth/token")
          .basicAuth("bgp-client-name", "bgp-client-pw")
          .field("grant_type", "password")
          .field("username", gameServiceUsername)
          .field("password", gameServicePassword)
          .asJson();
      if (response.getStatus() != 200) {
        throw new UnirestException("Wrong Token received: " +response.getBody());
      }
      tokenResponse = response.getBody().getObject();
      accessToken = tokenResponse.getString("access_token");
    } catch (Exception e) {
      accessToken = "";
    }
    return accessToken;

  }

}


