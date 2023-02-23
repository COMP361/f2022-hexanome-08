package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.PlayerInfo;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.async.DeferredResult;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Seems to work fine
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestController {
  @Autowired
  private TestRestTemplate restTemplate;

  String gameServerName = "splendorbase";
  String[] playerNames = {"Bob", "Marry", "Tim"};
  long gameId = 1312312312312L;

  String basicUrl = String.format("/%s/api/games/%s", gameServerName, gameId);

  HttpHeaders header = new HttpHeaders();
  @BeforeEach
  public void headerSetup() {
    header.setContentType(MediaType.APPLICATION_JSON);
  }

  @Test
  public void testMyEndpoint() {
    String expectedResponse = "Hello, World!";

    ResponseEntity<String> response = restTemplate.getForEntity("/test", String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());
  }

  @Test
  public void testGameEndPoint() {
    String path1 = "/splendortrade";
    String path2 = "/splendorbase";

    String expectedResponse = "Hello, splendor games!";

    ResponseEntity<String> response = restTemplate.getForEntity(path1, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());

    response = restTemplate.getForEntity(path2, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());
  }

  @Test
  public void testLaunchGame() {
    LinkedList<PlayerInfo> playerInfos = new LinkedList<>();
    for (int i = 0; i < 3; i++) {
      playerInfos.add(new PlayerInfo(playerNames[i], "red"));
    }
    LauncherInfo launcherInfo = new LauncherInfo(gameServerName,playerInfos,playerNames[0]);
    HttpEntity<LauncherInfo> requestEntity = new HttpEntity<>(launcherInfo, header);

    ResponseEntity<String> response = restTemplate.exchange(basicUrl,
        HttpMethod.PUT, requestEntity, String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

  }

  @Test
  public void testGetGameDetail() {
    HttpEntity<String> requestEntity = new HttpEntity<>("",header);
    //ResponseEntity<String> response = restTemplate.getForEntity(basicUrl, String.class);
    ResponseEntity<String> response =
        restTemplate.exchange(basicUrl, HttpMethod.GET, requestEntity, String.class);
    assertEquals(HttpStatus.OK,response.getStatusCode());

  }
}
