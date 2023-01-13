package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.GameServerParameters;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class SplendorRegistrator {

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
  private final String gameServicePort;
  private final String gameServiceLocation;
  private final String lobbyServiceAddress;


  @Autowired
  SplendorRegistrator(@Value("${gameservice.names}") String[] gameServiceNames,
                      @Value("${gameservice.displayNames}") String[] gameServiceDisplayNames,
                      @Value("${game.usernames}") String[] gameServiceUsernames,
                      @Value("${game.passwords}") String[] gameServicePasswords,
                      @Value("${server.port}") String gameServicePort,
                      @Value("${gameservice.location}") String gameServiceLocation,
                      @Value("${lobbyservice.location}") String lobbyServiceAddress) {

    // first assigning all fields first, then construct the GameServerParameter class
    this.gameServiceNames = Arrays.asList(gameServiceNames);
    this.gameServiceDisplayNames = Arrays.asList(gameServiceDisplayNames);
    this.gameServiceUsernames = Arrays.asList(gameServiceUsernames);
    this.gameServicePasswords = Arrays.asList(gameServicePasswords);
    this.gameServicePort = gameServicePort;
    this.lobbyServiceAddress = lobbyServiceAddress;
    this.gameServiceLocation = gameServiceLocation;
    // for error messages
    this.logger = LoggerFactory.getLogger(SplendorRegistrator.class);
  }

  @PostConstruct
  private void init() throws UnirestException {
    // TODO: 1. Send a POST request under lobbyUrl + /oauth/token -> access_token
    //  then 2. With the token, send a GET to lobbyUrl + /oauth/role -> get the role
    //  (must be "authority": "ROLE_SERVICE") then 3. We can now send PUT request
    //  to lobbyUrl + /api/gameservices/{gameservicename} to register the game

    String accessToken;
    for (int i = 0; i < gameServiceUsernames.size(); i++) {
      try {
        // POST Request, log "the game service" user in
        accessToken = getOauthToken(gameServiceUsernames.get(i), gameServicePasswords.get(i));
      } catch (Exception e) {
        logger.warn("Failed to log the game service in with the game username and game password");
        return;
      }
      // GET Request, check if role is "ROLE_SERVICE"
      String roleOfGame = sendAuthorityRequest(accessToken);
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
        logger.warn("Wrong role of the provided username and password!");
      }
    }

  }

  /**
   * Adds game to lobby.
   */
  public void registerGameAtLobby(String accessToken,
                                  String gameServiceName,
                                  GameServerParameters gameServerParameters)
      throws UnirestException {
    String requestJsonString = new Gson().toJson(gameServerParameters);
    String url = lobbyServiceAddress + "/api/gameservices/" + gameServiceName;
    HttpResponse<String> response = Unirest.put(url)
        .header("Authorization", "Bearer " + accessToken)
        .header("Content-Type", "application/json")
        .body(requestJsonString).asString();
    logger.warn("register status: " + response.getStatus());
  }

  /**
   * Sends request to check authority of the access token.
   */
  private String sendAuthorityRequest(String accessToken) throws UnirestException {
    // queryString: request param
    HttpResponse<JsonNode> authorityResponse = Unirest.get(lobbyServiceAddress + "/oauth/role")
        .queryString("access_token", accessToken).asJson();
    JSONArray jsonarray = new JSONArray(authorityResponse.getBody().toString());
    return jsonarray.getJSONObject(0).getString("authority");
  }


  /**
   * Get the OAuth token associated with the "Splendor_base" service user.
   *
   * @return OAuth token
   */
  private String getOauthToken(String gameServiceUsername,
                               String gameServicePassword) throws UnirestException {
    JSONObject tokenResponse = Unirest.post(lobbyServiceAddress + "/oauth/token")
        .basicAuth("bgp-client-name", "bgp-client-pw")
        .field("grant_type", "password")
        .field("username", gameServiceUsername)
        .field("password", gameServicePassword)
        .asJson()
        .getBody()
        .getObject();

    String accessToken;
    try {
      accessToken = tokenResponse.getString("access_token");

    } catch (Exception e) {
      accessToken = "";
    }
    return accessToken;
  }

}


