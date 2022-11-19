package project.connection;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import project.view.lobby.Session;

/**
 * class that is responsible to send different requests to the LS.
 */
public class LobbyServiceRequestSender {
  private String lobbyUrl;
  private String accessToken;

  private final Map<String, Session> sessionIdMap = new HashMap<>();

  public LobbyServiceRequestSender(String lobbyUrlInput) {
    lobbyUrl = lobbyUrlInput;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getLobbyUrl() {
    return lobbyUrl;
  }

  public Map<String, Session> getSessionIdMap() {
    return sessionIdMap;
  }

  /**
   * update the info about session deletion or creation.
   *
   * @throws UnirestException in case Unirest failed to make the request.
   */
  public void updateSessions() throws UnirestException {
    // TODO: figure out a way to add GUI update into this method after sessionIdMap is updated
    HttpResponse<JsonNode> lobbyMainPageResponse =
        Unirest.get(lobbyUrl + "/api/sessions").asJson();
    JSONObject sessionObject =
        lobbyMainPageResponse.getBody().getObject().getJSONObject("sessions");
    JSONArray sessionsArray = sessionObject.toJSONArray(sessionObject.names());
    JSONArray sessionIds = sessionObject.names();
    Gson gson = new Gson();
    // add new session into the mapping if there is one
    for (int i = 0; i < sessionsArray.length(); i++) {
      Session session = gson.fromJson(sessionsArray.getJSONObject(i).toString(), Session.class);
      String sessionId = sessionIds.getString(i);
      if (!sessionIdMap.containsKey(sessionId)) {
        sessionIdMap.put(sessionId, session);
      }
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
   * send request to check user authority.
   *
   * @param accessToken access token
   * @return a JsonObject with user authority
   * @throws UnirestException in case unirest failed to send a request
   */
  public JSONObject sendAuthorityRequest(String accessToken) throws UnirestException {
    // queryString: request param
    HttpResponse<JsonNode> authorityResponse = Unirest.get(lobbyUrl + "/oauth/role")
        .queryString("access_token", accessToken).asJson();
    JSONArray jsonarray = new JSONArray(authorityResponse.getBody().toString());
    return jsonarray.getJSONObject(0);
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
   * @param accessToken access token
   * @param gameName game name
   * @param saveGameName save game name
   * @return a JsonObject with user authority
   * @throws UnirestException in case unirest failed to send a request
   */
  public String sendCreateSessionRequest(String accessToken,
                                         String gameName,
                                         String saveGameName) throws UnirestException {
    String creatorName = sendUserNameRequest(accessToken);

    JSONObject requestBody = new JSONObject();
    requestBody.put("creator", creatorName);
    requestBody.put("game", gameName);
    requestBody.put("savegame", saveGameName);

    System.out.println(requestBody);

    HttpResponse<String> createSessionResponse = Unirest.post(lobbyUrl + "/api/sessions")
        .header("Content-Type", "application/json")
        .queryString("access_token", accessToken)
        .body(requestBody)
        .asString();

    return createSessionResponse.getBody();
  }
}
