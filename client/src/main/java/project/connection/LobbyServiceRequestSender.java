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
import project.view.lobby.GameParameters;
import project.view.lobby.SessionList;


/**
 * class that is responsible to send different requests to the LS.
 */
public class LobbyServiceRequestSender {
  private final String lobbyUrl;
  private final Map<String, String> gameNameMapping;

  private final SessionList localSessionList;

  /**
   * lobby service request sender constructor.
   *
   * @param lobbyUrlInput lobby url
   */
  public LobbyServiceRequestSender(String lobbyUrlInput) {
    lobbyUrl = lobbyUrlInput;
    gameNameMapping = new HashMap<>();
    localSessionList = new SessionList();
  }

  public String getLobbyUrl() {
    return lobbyUrl;
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
    if (hashPreviousResponse.equals("")) {
      return Unirest.get(lobbyUrl + "/api/sessions").asString();
    } else {
      return Unirest.get(lobbyUrl + "/api/sessions")
          .queryString("hash", hashPreviousResponse).asString();
    }
  }

  /**
   * Send a long poll get request to get detail on
   * 1 session (whether ppl joined in the session or left).
   *
   * @param sessionId            session id
   * @param hashPreviousResponse previous response payload body hashed with MD5
   * @throws UnirestException in case unirest failed to send a request
   * @returna response of the sessions details as String
   */
  public HttpResponse<String> sendGetOneSessionDetailRequest(
      Long sessionId, String hashPreviousResponse) throws UnirestException {
    if (hashPreviousResponse.equals("")) {
      return Unirest.get(lobbyUrl + "/api/sessions/" + sessionId.toString()).asString();
    } else {
      return Unirest.get(lobbyUrl + "/api/sessions/" + sessionId.toString())
          .queryString("hash", hashPreviousResponse).asString();
    }
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
   * @throws UnirestException in case of a failed request
   */
  public String sendRefreshTokenRequest(String refreshToken) throws UnirestException {
    JSONObject refreshResponseJson = Unirest.post(lobbyUrl + "/oauth/token")
        .basicAuth("bgp-client-name", "bgp-client-pw")
        .field("grant_type", "refresh_token")
        .field("refresh_token", refreshToken)
        .asJson()
        .getBody()
        .getObject();
    return refreshResponseJson.getString("access_token");

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

    HttpResponse<String> createSessionResponse = Unirest.post(lobbyUrl + "/api/sessions")
        .header("Content-Type", "application/json")
        .queryString("access_token", accessToken)
        .body(requestBody)
        .asString();
  }

  /**
   * Used to get all available games in LS.
   *
   * @return A list of Game
   * @throws UnirestException in case unirest failed to send a request
   */
  public List<GameParameters> sendAllGamesRequest() throws UnirestException {
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
   * @throws UnirestException in case unirest failed to send a request
   */
  public void sendDeleteSessionRequest(String accessToken, Long sessionId)
      throws UnirestException {

    Unirest.delete(lobbyUrl + "/api/sessions/" + sessionId.toString())
        .queryString("access_token", accessToken).asString();
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
    Unirest.delete(
            String
                .format("%s/api/sessions/%s/players/%s",
                    lobbyUrl,
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
    Unirest.post(lobbyUrl + "/api/sessions/" + sessionId.toString())
        .queryString("access_token", accessToken).asString();
  }


}
