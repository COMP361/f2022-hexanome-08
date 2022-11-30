package project.connection;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Arrays;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * TODO.
 */
public class SplendorServiceRequestSender {

  private final String gameUrl;

  public SplendorServiceRequestSender(String gameUrl) {
    this.gameUrl = gameUrl;
  }


  /**
   * Send the request and get back a list of strings encoded in one string
   *
   * @param gameId
   * @return
   *
   */
  public String[] sendGetAllPlayerNamesList(long gameId) {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    String body = "";
    String url = gameUrl + "/api/games/" + gameId + "/players";

    HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
    ResponseEntity<String> responseEntity =
        rest.exchange(url, HttpMethod.GET, requestEntity, String.class);
    // TODO: Parse this raw string using Gson to an Array
    return new Gson().fromJson(responseEntity.getBody(), String[].class);
  }

  /**
   * GET Request to resource from /api/games/{gameId}
   *
   * @param gameId
   * @param hashPreviousResponse put a empty string as "" to avoid stuck in the long polling loop
   * @return
   */
  public ResponseEntity<String> sendGetGameInfoRequest(long gameId, String hashPreviousResponse){
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    String body = "";
    String url = gameUrl + "/api/games/" + gameId;
    if (!hashPreviousResponse.equals("")) {
      // if we are sending something in as hash, then we need to add it to the end of url
      url += "?hash=" + hashPreviousResponse;
    }
    HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
    return rest.exchange(url, HttpMethod.GET, requestEntity, String.class);
  }

  public String getGameUrl(){
    return gameUrl;
  }
}
