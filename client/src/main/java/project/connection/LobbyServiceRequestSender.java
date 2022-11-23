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
import project.view.lobby.Session;
import project.view.lobby.SessionList;


/**
 * class that is responsible to send different requests to the LS.
 */
public class LobbyServiceRequestSender {
  private String lobbyUrl;
  private final Map<String, String> gameNameMapping;

  private final SessionList localSessionList;

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
   * Send a long poll get request to get detail on 1 session (whether ppl joined in the session or left).
   *
   * @param sessionId session id
   * @param hashPreviousResponse previous response payload body hashed with MD5
   * @returna response of the sessions details as String
   * @throws UnirestException in case unirest failed to send a request
   */
  public HttpResponse<String> sendGetOneSessionDetailRequest(
      Long sessionId,String hashPreviousResponse) throws UnirestException {
    if (hashPreviousResponse.equals("")) {
      return Unirest.get(lobbyUrl + "/api/sessions" + sessionId.toString()).asString();
    } else {
      return Unirest.get(lobbyUrl + "/api/sessions" + sessionId.toString())
          .queryString("hash", hashPreviousResponse).asString();
    }
  }


  ///**
  // * update the info about session deletion or creation locally.
  // * If remote has more sessions (result from REST) -> add session to sessionIdMap
  // * If local has more sessions (sessionIdMap > resultFromRest) -> delete it from sessionIdMap
  // *
  // * @throws UnirestException in case Unirest failed to make the request.
  // */
  //public Map<String, Session> getRemoteSessions() throws UnirestException {
  //
  //  HttpResponse<JsonNode> lobbyMainPageResponse =
  //      Unirest.get(lobbyUrl + "/api/sessions").asJson();
  //  JSONObject sessionObject = lobbyMainPageResponse
  //      .getBody()
  //      .getObject()
  //      .getJSONObject("sessions");
  //
  //  // JsonArray of session ids
  //  JSONArray remoteSessionIds = sessionObject.names();
  //  Gson gson = new Gson();
  //  Map<String, Session> returnMap = new HashMap<>();
  //
  //  // JsonArray of session details
  //  JSONArray remoteSessionsArray = sessionObject.toJSONArray(sessionObject.names());
  //  if (remoteSessionsArray == null) {
  //    return returnMap;
  //  }
  //  for (int i = 0; i < remoteSessionsArray.length(); i++) {
  //    String sessionId = remoteSessionIds.getString(i);
  //    Session session = gson
  //        .fromJson(remoteSessionsArray.getJSONObject(i).toString(), Session.class);
  //    returnMap.put(sessionId, session);
  //  }
  //  return returnMap;
  //}

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
   * request the username with access token.
   *
   * @param accessToken access token
   * @return username
   * @throws UnirestException in case unirest failed to send a request
   */
  public String sendUserNameRequest(String accessToken) throws UnirestException {
    HttpResponse<String> userNameResponse = Unirest.get(lobbyUrl + "/oauth/username")
        .queryString("access_token", accessToken).asString();
    return userNameResponse.getBody();
  }

  /**
   * UTF8-String, encoding the id of the newly created session (positive long).
   *
   * @param accessToken  access token
   * @param gameName     game name
   * @param saveGameName save game name
   * @return a JsonObject with user authority
   * @throws UnirestException in case unirest failed to send a request
   */
  public String sendCreateSessionRequest(String userName,
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

    return createSessionResponse.getBody();
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
   * send request to get detail session info.
   *
   * @param sessionId session id
   *                  //   * @return JsonObject with detailed session info
   * @throws UnirestException in case unirest failed to send a request
   */
  public JSONObject sendGetSessionDetailRequest(Long sessionId) throws UnirestException {
    // TODO: Later, pass optional "hash=???" request parameter
    return Unirest.get(String.format("%s/api/sessions/%s", lobbyUrl, sessionId.toString()))
        .asJson().getBody().getObject();
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
    Unirest.put(String.format("%s/api/sessions/%s/players/%s", lobbyUrl, sessionId.toString(), playerName))
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
    Unirest.delete(String.format("%s/api/sessions/%s/players/%s", lobbyUrl, sessionId.toString(), playerName))
        .queryString("access_token", accessToken).asString();

  }

}
