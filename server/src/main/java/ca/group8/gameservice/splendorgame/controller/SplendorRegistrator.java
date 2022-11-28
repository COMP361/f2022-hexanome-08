package ca.group8.gameservice.splendorgame.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javax.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SplendorRegistrator {

  @Value("${lobbyService.location}")
  private String lobbyServiceAddress;
  @Value("${gameService.name}")
  private String gameServiceName;
  @Value("${gameService.displayName}")
  private String displayName;
  @Value("${gameService.location}")
  private String gameServiceLocation = "127.0.0.1";
  @Value("${server.port}")
  private String gameServicePort;
  @Value("${game.username}")
  private String gameServiceUsername;
  @Value("${game.password}")
  private String gameServicePassword;
  private int gameServerMaxPlayers = 4;
  private int gameServerMinPlayers = 2;


  @Autowired
  public SplendorRegistrator() {

  }
  @PostConstruct
  private void initialize() throws UnirestException {
    //TODO fill it out later
    System.out.println(getOauthToken());
  }

  /**
   * Get the OAuth token associated with the "Splendor_base" service user.
   * @return OAuth token
   */
  public String getOauthToken() throws UnirestException {

      JSONObject tokenResponse =  Unirest.post(lobbyServiceAddress + "/oauth/token")
          .basicAuth("bgp-client-name", "bgp-client-pw")
          .field("grant_type", "password")
          .field("username", gameServiceUsername)
          .field("password", gameServicePassword)
          .asJson()
          .getBody()
          .getObject();

      String oAuthToken = tokenResponse.getString("access_token").replaceAll("\"","");
      return oAuthToken;


  }



}


