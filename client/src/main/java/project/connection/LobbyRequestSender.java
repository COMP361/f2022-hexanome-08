package project.connection;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import project.App;
import project.view.lobby.communication.GameParameters;
import project.view.lobby.communication.Player;
import project.view.lobby.communication.Savegame;


/**
 * class that is responsible to send different requests to the LS.
 */
public class LobbyRequestSender {
  private final String lobbyUrl;
  private final Map<String, String> gameNameMapping;

  /**
   * lobby service request sender constructor.
   *
   * @param lobbyUrlInput lobby url
   */
  public LobbyRequestSender(String lobbyUrlInput) {
    lobbyUrl = lobbyUrlInput;
    gameNameMapping = new HashMap<>();
  }

  public Map<String, String> getGameNameMapping() {
    return new HashMap<>(gameNameMapping);
  }

  public void addGameNameMapping(String gameDisplayName, String gameName) {
    assert !gameNameMapping.containsKey(gameDisplayName);
    gameNameMapping.put(gameDisplayName, gameName);
  }

  /**
   * Send a long poll get request to get all sessions (whether 1 session is added or deleted).
   *
   * @param hashPreviousResponse previous response payload body hashed with MD5
   * @return a response of the sessions details as String
   * @throws UnirestException in case unirest failed to send a request
   */
  public HttpResponse<String> sendGetAllSessionDetailRequest(
      String hashPreviousResponse) throws UnirestException {
    HttpResponse<String> response;
    if (hashPreviousResponse.equals("")) {
      response = Unirest.get(lobbyUrl + "/api/sessions").asString();
    } else {
      response = Unirest.get(lobbyUrl + "/api/sessions")
          .queryString("hash", hashPreviousResponse)
          .asString();
    }
    if (response.getStatus() != 200 && response.getStatus() != 408) {
      System.out.println(response.getBody());
      throw new UnirestException(
          "Failed to get all sessions details, response status: " + response.getStatus());
    }
    return response;
  }


  /**
   * send a log in request to LS.
   *
   * @param userNameStr     username
   * @param userPasswordStr user password
   * @return a JsonObject with info needed
   * @throws UnirestException in case unirest failed to send a request
   */
  public JSONObject sendLogInRequest(String userNameStr, String userPasswordStr)
      throws UnirestException {
    return Unirest.post(lobbyUrl + "/oauth/token")
        .basicAuth("bgp-client-name", "bgp-client-pw")
        .field("grant_type", "password")
        .field("username", userNameStr)
        .field("password", userPasswordStr)
        .asJson()
        .getBody()
        .getObject();
  }


  /**
   * Send request to keep refreshing and get the refreshed new access_token of this user.
   *
   * @param refreshToken refresh access token
   * @return A string that is the new access token
   */
  public String sendRefreshTokenRequest(String refreshToken) {
    try {
      JSONObject refreshResponseJson = Unirest.post(lobbyUrl + "/oauth/token")
          .basicAuth("bgp-client-name", "bgp-client-pw")
          .field("grant_type", "refresh_token")
          .field("refresh_token", refreshToken)
          .asJson()
          .getBody()
          .getObject();
      return refreshResponseJson.getString("access_token");
    } catch (UnirestException e) {
      e.printStackTrace();
      return "";
    }
  }


  /**
   * send request to check user authority.
   *
   * @param accessToken access token
   * @return a user authority in string
   * @throws UnirestException in case unirest failed to send a request
   */
  public String sendAuthorityRequest(String accessToken) throws UnirestException {
    // queryString: request param
    HttpResponse<JsonNode> authorityResponse = Unirest.get(lobbyUrl + "/oauth/role")
        .queryString("access_token", accessToken).asJson();
    JSONArray jsonarray = new JSONArray(authorityResponse.getBody().toString());
    return jsonarray.getJSONObject(0).getString("authority");
  }

  /**
   * UTF8-String, encoding the id of the newly created session (positive long).
   *
   * @param accessToken  access token
   * @param gameName     game name
   * @param saveGameName save game name
   * @param userName user name
   * @throws UnirestException in case unirest failed to send a request
   */
  public void sendCreateSessionRequest(String userName,
                                       String accessToken,
                                       String gameName,
                                       String saveGameName) throws UnirestException {

    JSONObject requestBody = new JSONObject();
    requestBody.put("creator", userName);
    requestBody.put("game", gameName);
    requestBody.put("savegame", saveGameName);

    HttpResponse<String> response = null;
    try {
      response = Unirest.post(lobbyUrl + "/api/sessions")
          .header("Content-Type", "application/json")
          .queryString("access_token", accessToken)
          .body(requestBody)
          .asString();
    } catch (UnirestException e) {
      System.out.println(e.getMessage());
    }
    assert response != null;
    if (response.getStatus() != 200) {
      throw new UnirestException("Failed to send create session request");
    }

  }

  /**
   * Used to get all available games in LS.
   *
   * @return A list of Game
   */
  public List<GameParameters> sendAllGamesRequest() {
    try {
      HttpResponse<JsonNode> allGamesResponse =
          Unirest.get(lobbyUrl + "/api/gameservices").asJson();
      JSONArray allGamesJsonArray = allGamesResponse.getBody().getArray();
      Gson gson = new Gson();
      List<GameParameters> resultList = new ArrayList<>();
      for (int i = 0; i < allGamesJsonArray.length(); i++) {
        String jsonString = allGamesJsonArray.getJSONObject(i).toString();
        // this method will assign the attributes of Game that can be assigned at this time
        // name & displayName, the others will stay as null
        GameParameters curGameParameters = gson.fromJson(jsonString, GameParameters.class);
        resultList.add(curGameParameters);
      }

      return resultList;
    } catch (UnirestException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  /**
   * get the Json details given one game name.
   *
   * @param gameName game name
   * @return json detail info for the game
   * @throws UnirestException in case unirest failed to send a request
   */
  public JSONObject getGameDetailsRequest(String gameName) throws UnirestException {
    return Unirest.get(lobbyUrl + "/api/gameservices/" + gameName)
        .asJson()
        .getBody()
        .getObject();
  }

  /**
   * Send a request to LS to delete a session. Must throw the exception even returns void!
   *
   * @param accessToken access token
   * @param sessionId   session id
   */
  public void sendDeleteSessionRequest(String accessToken, Long sessionId) {
    try {
      Unirest.delete(lobbyUrl + "/api/sessions/" + sessionId.toString())
          .queryString("access_token", accessToken)
          .asString();
    } catch (UnirestException e) {
      e.printStackTrace();
    }
  }


  /**
   * send add player request to LS.
   *
   * @param accessToken access token
   * @param sessionId   session id
   * @param playerName  player name
   * @throws UnirestException in case unirest failed to send a request
   */
  public void sendAddPlayerRequest(String accessToken, Long sessionId, String playerName)
      throws UnirestException {
    Unirest.put(
            String.format("%s/api/sessions/%s/players/%s",
                lobbyUrl, sessionId.toString(), playerName))
        .queryString("access_token", accessToken).asString();

  }

  /**
   * send remove player request to LS.
   *
   * @param accessToken access token
   * @param sessionId   session id
   * @param playerName  player name
   * @throws UnirestException in case unirest failed to send a request
   */
  public void sendRemovePlayerRequest(String accessToken, Long sessionId, String playerName)
      throws UnirestException {
    Unirest.delete(String.format("%s/api/sessions/%s/players/%s", lobbyUrl,
            sessionId.toString(), playerName))
        .queryString("access_token", accessToken).asString();

  }

  /**
   * send launch session request to LS.
   *
   * @param sessionId   session id
   * @param accessToken access token
   * @throws UnirestException in case unirest failed to send a request
   */

  public void sendLaunchSessionRequest(Long sessionId, String accessToken) throws UnirestException {
    HttpResponse<String> response = Unirest
        .post(lobbyUrl + "/api/sessions/" + sessionId.toString())
        .queryString("access_token", accessToken)
        .asString();
    if (response.getStatus() != 200) {
      throw new UnirestException("Can not launch the session!, token = "
          + accessToken + " and id: " + sessionId + "Error code: " + response.getStatus());
    }
  }


  /**
   * Return an array of Savegame to one specific game service (base, city or trade).
   * to the player with accessToken.
   *
   * @param accessToken     access token of the player
   * @param gameServiceName splendorbase, splendorcity, ... (game service names)
   * @return an array of Savegame to one specific game service, can be empty
   */
  public Savegame[] getAllSavedGames(String accessToken, String gameServiceName) {
    String url = String.format("%s/api/gameservices/%s/savegames",
        lobbyUrl, gameServiceName);
    Savegame[] result = new Savegame[0];
    try {
      HttpResponse<String> response = Unirest.get(url)
          .queryString("access_token", accessToken)
          .asString();
      String jsonString = response.getBody();
      result = new Gson().fromJson(jsonString, Savegame[].class);

      if (response.getStatus() != 200) {
        String msg = "Unable to perform GET all sessions request to LS";
        throw new UnirestException(msg);
      }
    } catch (UnirestException e) {
      System.out.println(e.getMessage());
    }
    return result;
  }


  // /api/users : GET
  /**
   * getPlayers.
   *
   * @param accessToken accessToken
   * @return list
   */
  public Player[] getPlayers(String accessToken) {
    String url = String.format("%s/api/users", lobbyUrl);
    Player[] result = new Player[0];
    try {
      HttpResponse<String> response = Unirest.get(url)
          .queryString("access_token", accessToken)
          .asString();
      String responseJson = response.getBody();
      result = new Gson().fromJson(responseJson, Player[].class);
      if (response.getStatus() != 200) {
        String msg = "Unable to perform GET all players request to LS";
        throw new UnirestException(msg);
      }
    } catch (UnirestException e) {
      System.out.println(e.getMessage());
    }
    return result;
  }

  // /api/users/{users} : GET

  /**
   * getOnePlayer.
   *
   * @param accessToken accessToken
   * @param playerName playerName
   * @return player
   */
  public Player getOnePlayer(String accessToken, String playerName) {
    String url = String.format("%s/api/users/%s", lobbyUrl, playerName);
    Player result = null;
    try {
      HttpResponse<String> response = Unirest.get(url)
          .queryString("access_token", accessToken)
          .asString();
      String responseJson = response.getBody();
      result = new Gson().fromJson(responseJson, Player.class);
      if (response.getStatus() != 200) {
        String msg = "Unable to perform GET one player request to LS";
        msg += "\n Error from LS: " + response.getBody();
        msg += App.getUser().getAccessToken();
        throw new UnirestException(msg);
      }
    } catch (UnirestException e) {
      System.out.println(e.getMessage());
    }
    return result;
  }

  // /api/users/{users} : PUT
  //TODO: This Unirest Exception should be handled correctly to display something
  // that warns the admin about failure of creating a new user

  /**
   * putOneNewPlayer.
   *
   * @param accessToken accessToken
   * @param playerName playerName
   * @param player player
   * @throws UnirestException UnirestException
   */
  public void putOneNewPlayer(String accessToken, String playerName, Player player)
      throws UnirestException {
    String url = String.format("%s/api/users/%s", lobbyUrl, playerName);
    String requestBody = new Gson().toJson(player, Player.class);
    HttpResponse<String> response = Unirest.put(url)
        .queryString("access_token", accessToken)
        .header("Content-Type", "application/json")
        .body(requestBody)
        .asString();

    if (response.getStatus() != 200) {
      throw new UnirestException("Failed to add new player.");
    }

  }

  // /api/users/{users} : DELETE
  //TODO: This Unirest Exception should be handled correctly to display something
  // that warns the admin about failure of deleting an existing user

  /**
   * deleteOnePlayer.
   *
   * @param accessToken accessToken
   * @param playerName playerName
   * @throws UnirestException UnirestException
   */
  public void deleteOnePlayer(String accessToken, String playerName)
      throws UnirestException {
    String url = String.format("%s/api/users/%s", lobbyUrl, playerName);
    HttpResponse<String> response = Unirest.delete(url)
        .queryString("access_token", accessToken)
        .asString();

    if (response.getStatus() != 200) {
      throw new UnirestException("Delete player error :(");
    }
  }

  // /api/users/{users}/password : POST
  //TODO: This Unirest Exception should be handled correctly to display something
  // that warns the admin/user about failure of updating one's password (does not obey the rule)

  /**
   * updateOnePlayerPassword.
   *
   * @param accessToken accessToken
   * @param playerName playerName
   * @param oldPassword oldPassword
   * @param nextPassword nextPassword
   * @throws UnirestException UnirestException
   */
  public void updateOnePlayerPassword(String accessToken, String playerName,
                                      String oldPassword, String nextPassword)
      throws UnirestException {
    String url = String.format("%s/api/users/%s/password", lobbyUrl, playerName);
    JSONObject requestBody = new JSONObject();
    requestBody.put("nextPassword", nextPassword);
    requestBody.put("oldPassword", oldPassword);

    HttpResponse<String> response = Unirest.post(url)
        .queryString("access_token", accessToken)
        .header("Content-Type", "application/json")
        .body(requestBody)
        .asString();
    if (response.getStatus() != 200) {
      throw new UnirestException("");
    }
  }

  // /api/users/{users}/colour : POST
  //TODO: This Unirest Exception should be handled correctly to display something
  // that warns the admin/user about failure of updating one's colour

  /**
   * updateOnePlayerColour.
   *
   * @param accessToken accessToken
   * @param playerName playerName
   * @param colourCode colourCode
   * @throws UnirestException UnirestException
   */
  public void updateOnePlayerColour(String accessToken, String playerName, String colourCode)
      throws UnirestException {
    String url = String.format("%s/api/users/%s/colour", lobbyUrl, playerName);
    JSONObject requestBody = new JSONObject();
    requestBody.put("colour", colourCode);
    HttpResponse<String> response = Unirest.post(url)
        .queryString("access_token", accessToken)
        .header("Content-Type", "application/json")
        .body(requestBody)
        .asString();
    if (response.getStatus() != 200) {
      throw new UnirestException("Failed to update player's preferred colour!");
    }
  }

  /*
  /api/gameservices/{gameservice}: DELETE
  Sending delete request to LS to force unregister a game
   */

  /**
   * unregisterOneGameService.
   *
   * @param accessToken accessToken
   * @param gameServerName gameServerName
   * @throws UnirestException UnirestException
   */
  public void unregisterOneGameService(String accessToken, String gameServerName)
      throws UnirestException {
    String url = String.format("%s/api/gameservices/%s", lobbyUrl, gameServerName);
    HttpResponse<String> response = Unirest.delete(url)
        .queryString("access_token", accessToken)
        .asString();
    if (response.getStatus() != 200) {
      throw new UnirestException("Failed to force unregister a game!");
    }
  }

}
