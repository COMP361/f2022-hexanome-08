package project.connection;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * TODO.
 */
public class SplendorServiceRequestSender {

  private final String gameUrl;
  private String gameServiceName;

  public SplendorServiceRequestSender(String gameUrl, String gameServiceName) {
    this.gameUrl = gameUrl;
    this.gameServiceName = gameServiceName;
  }

  public void setGameServiceName(String gameServiceName) {
    assert gameServiceName != null && !gameServiceName.equals("");
    this.gameServiceName = gameServiceName;
  }


  ///**
  // * Send the request and get back a list of strings encoded in one string.
  // *
  // * @param gameId game id
  // * @return a list of player names
  // */
  //public String[] sendGetAllPlayerNamesList(long gameId) {
  //  RestTemplate rest = new RestTemplate();
  //  HttpHeaders headers = new HttpHeaders();
  //  String body = "";
  //  String url = String.format("%s/api/games/%s/players", gameUrl, gameId);
  //
  //  HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
  //  ResponseEntity<String> responseEntity =
  //      rest.exchange(url, HttpMethod.GET, requestEntity, String.class);
  //  // TODO: Parse this raw string using Gson to an Array
  //  return new Gson().fromJson(responseEntity.getBody(), String[].class);
  //}


  /**
   * Send a long polling request for GameInfo updates.
   *
   * @param gameId game id
   * @param hashPreviousResponse hashed Previous Response
   * @return A http response with JSON string as the body
   * @throws UnirestException in case of a failed request
   */
  public HttpResponse<String> sendGetGameInfoRequest(long gameId, String hashPreviousResponse)
      throws UnirestException {
    if (hashPreviousResponse.equals("")) {
      return Unirest.get(gameUrl + gameServiceName + "/api/games/" + gameId).asString();
    } else {
      return Unirest.get(gameUrl + gameServiceName + "/api/games/" + gameId)
          .queryString("hash", hashPreviousResponse)
          .asString();
    }

  }

  /**
   * Send a long polling request for all player info updates.
   *
   * @param gameId game id
   * @param hashPreviousResponse hashed Previous Response
   * @return A http response with JSON string as the body
   * @throws UnirestException in case of a failed request
   */
  public HttpResponse<String> sendGetAllPlayerInfoRequest(long gameId,
                                                          String hashPreviousResponse)
      throws UnirestException {
    if (hashPreviousResponse.equals("")) {
      return Unirest.get(String.format("%s/api/games/%s/playerStates",
          gameUrl + gameServiceName, gameId)).asString();
    } else {
      return Unirest.get(String.format("%s/api/games/%s/playerStates",
              gameUrl + gameServiceName, gameId))
          .queryString("hash", hashPreviousResponse)
          .asString();

    }

  }

  /**
   * Send a instant response request to get all possible action map for current player.
   *
   * @param gameId game id
   * @param playerName current player name
   * @param accessToken current player's access token
   * @return A http response with JSON string as the body
   * @throws UnirestException in case of a failed request
   */
  public HttpResponse<String> sendGetPlayerActionsRequest(long gameId, String playerName,
                                                          String accessToken)
      throws UnirestException {

    return Unirest
        .get(String.format("%s/api/games/%s/players/%s/actions", gameUrl + gameServiceName,
            gameId, playerName))
        .queryString("access_token", accessToken).asString();
  }

  /**
   * Send a instant POST request to inform server which action did the user choose.
   *
   * @param gameId game id
   * @param playerName player name
   * @param accessToken access token
   * @param actionId the MD5 hashed id of the chosen action
   * @throws UnirestException in case of a failed request
   */
  public void sendPlayerActionChoiceRequest(long gameId, String playerName,
                                            String accessToken, String actionId)
      throws UnirestException {
    HttpResponse<String> response =
        Unirest.post(String.format("%s/api/games/%s/players/%s/actions/%s",
                gameUrl + gameServiceName, gameId, playerName, actionId))
            .queryString("access_token", accessToken).asString();
  }


  // TODO: Delete Request (later)


  public String getGameUrl() {
    return gameUrl;
  }
  public String getGameServiceName() {return gameServiceName;}

}
