package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLobbyCommunicator {
  @Autowired
  private LobbyCommunicator lobbyCommunicator;
  @Value("${lobbyservice.location}")
  private String lobbyUrl;

  private final String userNameStr = "ruoyu";
  private final String userPasswordStr = "abc123_ABC123";
  private String accessToken;
  private JSONObject sendLogInRequest(String userNameStr, String userPasswordStr)
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

  @Before
  public void setUp() throws UnirestException, JSONException {
    accessToken = sendLogInRequest(userNameStr, userPasswordStr).getString("access_token");
  }

  // Implicitly tested putSaveGame
  @Test
  public void testGetAllSavedGamesAndPutSaveGame() throws UnirestException {
    String serviceName = "splendorbase";
    String[] players = new String[] {"ruoyu", "julia"};
    String gameId = "ThisIsNotAFunGame";
    Savegame testSaveGame = new Savegame(players,serviceName,gameId);
    lobbyCommunicator.putSaveGame(testSaveGame);
    List<Savegame> result = Arrays.asList(lobbyCommunicator
        .getAllSavedGames(accessToken, "splendorbase"));
    // the game we just saved is indeed in the list of all saved game
    assertTrue(result.stream().anyMatch(game -> game.getSavegameid().equals(gameId)));
    int count = 0;
    for (Savegame game : result) {
      if (game.getSavegameid().equals(testSaveGame.getSavegameid())) {
        count += 1;
        assertEquals(testSaveGame.getSavegameid(), game.getSavegameid());
        assertEquals(testSaveGame.getGamename(), game.getGamename());
        assertArrayEquals(testSaveGame.getPlayers(), game.getPlayers());
      }
    }
    // only one such save game with game id can exist
    assertEquals(1, count);
    lobbyCommunicator.deleteSaveGame(testSaveGame);
  }

  @Test
  public void testIsValidToken() {
    assertTrue(lobbyCommunicator.isValidToken(accessToken, userNameStr));
  }

}
