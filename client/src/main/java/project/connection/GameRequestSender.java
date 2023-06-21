package project.connection;

import ca.mcgill.comp361.splendormodel.model.GameInfo;
import ca.mcgill.comp361.splendormodel.model.SplendorDevHelper;
import com.google.gson.Gson;
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

  /**
   * GameRequestSender.
   *
   * @param gameUrl gameUrl
   * @param gameServiceName gameServiceName
   */
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
   */
  public HttpResponse<String> sendGetGameInfoRequest(long gameId, String hashPreviousResponse) {
    HttpResponse<String> response = null;
    String url = gameUrl + gameServiceName + "/api/games/" + gameId;
    try {
      if (hashPreviousResponse.equals("")) {
        // instant request
        response = Unirest.get(url).asString();
      } else {
        // long polling request
        response = Unirest.get(url)
            .queryString("hash", hashPreviousResponse)
            .asString();
      }

    } catch (UnirestException e) {
      e.printStackTrace();
    }
    return response;
  }

  /**
   * Send a save game request to our game service backend.
   *
   * @param gameId   gameId
   * @param savegame savegame
   * @throws UnirestException when the game id is duplicated saved in game server side.
   */
  public void sendSaveGameRequest(long gameId, Savegame savegame) throws UnirestException {
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
      String url = String.format("%s/api/games/%s/players/%s/actions/%s",
          gameUrl + gameServiceName,
          gameId,
          playerName,
          actionId);
      HttpResponse<String> response = Unirest.post(url)
              .queryString("access_token", accessToken)
              .asString();
    } catch (UnirestException e) {
      e.printStackTrace();
    }

  }

  /**
   * getGameUrl.
   *
   * @return url
   */
  public String getGameUrl() {
    return gameUrl;
  }


  /**
   * getGameServiceName.
   *
   * @return name
   */
  public String getGameServiceName() {
    return gameServiceName;
  }

  /**
   * setGameServiceName.
   *
   * @param gameServiceName gameServiceName
   */
  public void setGameServiceName(String gameServiceName) {
    assert gameServiceName != null && !gameServiceName.equals("");
    this.gameServiceName = gameServiceName;
  }


  /**
   * Get an instant response of game info from game server.
   *
   * @param gameId game id
   * @return the game info
   */
  public GameInfo getInstantGameInfo(long gameId) {
    HttpResponse<String> firstGameInfoResponse = sendGetGameInfoRequest(gameId, "");
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    return gsonParser.fromJson(firstGameInfoResponse.getBody(), GameInfo.class);
  }

  /**
   * Send POST request to update winning points.
   *
   * @param gameId
   * @param accessToken
   * @param creatorName
   * @param newPoints
   */
  public void updateWinningPoints(long gameId,
                                  String accessToken,
                                  String creatorName,
                                  int newPoints) {
    String url = String.format("%s/api/games/%s", gameUrl + gameServiceName, gameId);
    try {
      HttpResponse<String> response = Unirest.post(url)
          .queryString("access_token",accessToken)
          .queryString("creator_name", creatorName)
          .queryString("new_points", newPoints)
          .asString();
      if (response.getStatus() != 200) {
        throw new UnirestException("Failed to change new points!");
      }
    } catch (UnirestException e) {
      e.printStackTrace();
    }

  }
}
