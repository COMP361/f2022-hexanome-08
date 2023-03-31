package project.connection;

import ca.mcgill.comp361.splendormodel.model.SplendorDevHelper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import project.App;
import project.view.lobby.communication.Savegame;

/**
 * TODO.
 */
public class GameRequestSender {

  private final String gameUrl;
  private String gameServiceName;

  public GameRequestSender(String gameUrl, String gameServiceName) {
    this.gameUrl = gameUrl;
    this.gameServiceName = gameServiceName;
  }

  /**
   * Send a long polling request for GameInfo updates.
   *
   * @param gameId               game id
   * @param hashPreviousResponse hashed Previous Response
   * @return A http response with JSON string as the body
   * @throws UnirestException in case of a failed request
   */
  public HttpResponse<String> sendGetGameInfoRequest(long gameId, String hashPreviousResponse) {
    HttpResponse<String> response = null;
    try {
      if (hashPreviousResponse.equals("")) {
        // instant request
        response = Unirest.get(gameUrl + gameServiceName + "/api/games/" + gameId).asString();
      } else {
        // long polling request
        response = Unirest.get(gameUrl + gameServiceName + "/api/games/" + gameId)
            .queryString("hash", hashPreviousResponse)
            .asString();
      }

    } catch (UnirestException e) {
      e.printStackTrace();
    }
    return response;
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
   * Send a save game request to our game service backend.
   *
   * @param gameId      gameId
   * @param savegame    savegame
   * @throws UnirestException when the game id is duplicated saved in game server side.
   */
  public void sendSaveGameRequest(long gameId, Savegame savegame) throws UnirestException{
    String url = String.format("%s/api/games/%s/savegame", gameUrl + gameServiceName, gameId);
    String body = SplendorDevHelper.getInstance().getGson().toJson(savegame, Savegame.class);
    // we can safely trust the username will be
    HttpResponse<String> response = Unirest.put(url)
        .header("Content-Type", "application/json")
        .queryString("player_name", App.getUser().getUsername())
        .queryString("access_token", App.getUser().getAccessToken())
        .body(body)
        .asString();
    if (response.getStatus() != 200) {
      throw new UnirestException("Duplicate Save Game Id! Re-enter another one!");
    }

  }

  /**
   * Send a long polling request for all player info updates.
   *
   * @param gameId               game id
   * @param hashPreviousResponse hashed Previous Response
   * @return A http response with JSON string as the body
   */
  public HttpResponse<String> sendGetAllPlayerInfoRequest(long gameId,
                                                          String hashPreviousResponse) {

    HttpResponse<String> response = null;
    try {
      if (hashPreviousResponse.equals("")) {
        response = Unirest.get(
            String.format("%s/api/games/%s/playerStates",
                gameUrl + gameServiceName, gameId)).asString();
      } else {
        response = Unirest.get(String.format("%s/api/games/%s/playerStates",
                gameUrl + gameServiceName, gameId))
            .queryString("hash", hashPreviousResponse)
            .asString();

      }
    } catch (UnirestException e) {
      e.printStackTrace();
    }

    return response;
  }

  /**
   * Send an instant response request to get all.
   * initial actions map for current player. (reserve, purchase, take token actions OR empty).
   *
   * @param gameId      game id
   * @param playerName  current player name
   * @param accessToken current player's access token
   * @return A http response with JSON string as the body
   */
  public HttpResponse<String> sendGetInitialPlayerActions(long gameId, String playerName,
                                                          String accessToken) {
    String url = String.format("%s/api/games/%s/players/%s/actions",
        gameUrl + gameServiceName,
        gameId, playerName);

    HttpResponse<String> response = null;
    try {
      response = Unirest.get(url)
          .queryString("access_token", accessToken)
          .asString();
    } catch (UnirestException e) {
      e.printStackTrace();
    }

    return response;
  }

  /**
   * Send a instant POST request to inform server which action did the user choose.
   *
   * @param gameId      game id
   * @param playerName  player name
   * @param accessToken access token
   * @param actionId    the MD5 hashed id of the chosen action
   */
  public void sendPlayerActionChoiceRequest(long gameId, String playerName,
                                            String accessToken, String actionId) {
    try {
      HttpResponse<String> response =
          Unirest.post(String.format("%s/api/games/%s/players/%s/actions/%s",
                  gameUrl + gameServiceName,
                  gameId,
                  playerName,
                  actionId))
              .queryString("access_token", accessToken)
              .asString();
    } catch (UnirestException e) {
      e.printStackTrace();
    }

  }

  public String getGameUrl() {
    return gameUrl;
  }


  // TODO: Delete Request (later)

  public String getGameServiceName() {
    return gameServiceName;
  }

  public void setGameServiceName(String gameServiceName) {
    assert gameServiceName != null && !gameServiceName.equals("");
    this.gameServiceName = gameServiceName;
  }

}
