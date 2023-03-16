package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.PlayerInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.SavedGameState;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TestGameValidator {
    @Autowired
    GameManager gameManager1;
    @Autowired
    GameManager gameManager2;
    @Autowired
    GameManager gameManager3;
    @Autowired
    LobbyCommunicator lobbyCommunicator;

    GameValidator gameValidator1;
    GameValidator gameValidator2;
    GameValidator gameValidator3;
    GameValidator gameValidator4;

    private String accessToken;
    private final String userNameStr = "ruoyu";
    private final String userPasswordStr = "abc123_ABC123";

    String[] gameServiceNames = {"splendorbase","splendorcity","splendortrade"};

    String[] players = new String[]{"ruoyu", "pengyu"};

    String[] players2 = new String[]{"ruoyu", "pengyu","muzhi"};

    String[] players3 = new String[]{"pengyu","muzhi"};
    String[] colours = new String[]{"red", "blue"};
    String[] colours2 = new String[]{"red", "blue","yellow"};

    List<PlayerInfo> playerInfos = IntStream
            .range(0, players.length)
            .mapToObj(i -> new PlayerInfo(players[i], colours[i]))
            .collect(Collectors.toList());

    List<PlayerInfo> playerInfos2 = IntStream
            .range(0, players2.length)
            .mapToObj(i -> new PlayerInfo(players2[i], colours2[i]))
            .collect(Collectors.toList());

    List<PlayerInfo> playerInfos3 = IntStream
            .range(0, players3.length)
            .mapToObj(i -> new PlayerInfo(players3[i], colours[i]))
            .collect(Collectors.toList());
    String gamename = "splendortrade";
    String gamename2 = "wrongName";

    String[] savegameids = new String[]{"gameId1","gameId2","gameId3"};
    Savegame[] savegames = new Savegame[]{
            new Savegame(players,gamename,savegameids[0]),
            new Savegame(players,gamename,savegameids[1]),
            new Savegame(players,gamename,savegameids[2])};

    long[] gameIds = new long[] {5151551235L, 8723123151231L, 1231231512123L};

    LauncherInfo launcherInfo = new LauncherInfo(gamename,
            new LinkedList<>(playerInfos),
            players[0],"gameId1");

    LauncherInfo launcherInfo2 = new LauncherInfo(gamename2,
            new LinkedList<>(playerInfos),
            players[0]);
    LauncherInfo launcherInfo3;


    LauncherInfo launcherInfo4 = new LauncherInfo(gamename,
            new LinkedList<>(playerInfos),
            players[0],"wrongGameId");

    LauncherInfo launcherInfo5 = new LauncherInfo(gamename,
            new LinkedList<>(playerInfos2),
            players[0],"gameId1");

    LauncherInfo launcherInfo6 = new LauncherInfo(gamename,
            new LinkedList<>(playerInfos3),
            players3[0],"gameId1");
    @Value("${lobbyservice.location}")
    private String lobbyUrl;
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

    @BeforeEach
    void setup() throws ModelAccessException, IOException, UnirestException, JSONException {

        gameManager1.launchGame(gameIds[0], launcherInfo);

        gameManager3.launchGame(gameIds[0],launcherInfo6);

        gameValidator1 = new GameValidator(gameManager1,lobbyCommunicator,gameServiceNames);

        gameValidator2 = new GameValidator(gameManager2,lobbyCommunicator,gameServiceNames);

        gameValidator3 = new GameValidator(gameManager2,lobbyCommunicator,gameServiceNames);

        gameValidator4 = new GameValidator(gameManager3,lobbyCommunicator,gameServiceNames);

    }

    @Test
    void validLauncherInfo() throws ModelAccessException {
        assertDoesNotThrow(() -> {gameValidator1.validLauncherInfo(8888888,launcherInfo);});

        Exception exception1 = assertThrows(ModelAccessException.class, () ->
        {gameValidator1.validLauncherInfo(5151551235L,launcherInfo);});

        String expectedMessage1 = "Duplicate game instance, can not launch it!";
        String actualMessage1 = exception1.getMessage();

        Exception exception2 = assertThrows(ModelAccessException.class, () ->
        {gameValidator2.validLauncherInfo(515112551235L,launcherInfo2);});

        String expectedMessage2 = "No such game registered in LS";
        String actualMessage2 = exception2.getMessage();

        Exception exception3 = assertThrows(ModelAccessException.class, () ->
        {gameValidator3.validLauncherInfo(5551235L,launcherInfo3);});

        String expectedMessage3 = "Invalid launcher info provided";
        String actualMessage3 = exception3.getMessage();

        Exception exception4 = assertThrows(ModelAccessException.class, () ->
        {gameValidator1.validLauncherInfo(5551235L,launcherInfo4);});

        String expectedMessage4 = "The game id requested is not previously saved!";
        String actualMessage4 = exception4.getMessage();

        Exception exception5 = assertThrows(ModelAccessException.class, () ->
        {gameValidator1.validLauncherInfo(515153251235L,launcherInfo5);});

        String expectedMessage5 = "Can not have more players than the saved game!";
        String actualMessage5 = exception5.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));
        assertTrue(actualMessage2.contains(expectedMessage2));
        assertTrue(actualMessage3.contains(expectedMessage3));
        assertTrue(actualMessage4.contains(expectedMessage4));
        assertTrue(actualMessage5.contains(expectedMessage5));
    }

    @Test
    void gameIdPlayerNameValidCheck() throws UnirestException, JSONException {
        accessToken = sendLogInRequest(userNameStr, userPasswordStr).getString("access_token");
        Exception exception1 = assertThrows(ModelAccessException.class, () ->
        {gameValidator1.gameIdPlayerNameValidCheck(accessToken,"pengyu",5151551235L);});

        String expectedMessage1 = "User token and user name does not match";
        String actualMessage1 = exception1.getMessage();

        Exception exception2 = assertThrows(ModelAccessException.class, () ->
        {gameValidator1.gameIdPlayerNameValidCheck(accessToken,"ruoyu",5151551235L);});

        //String expectedMessage2 = "There is no game with game id: "
        //        + 888888 + " launched, try again later";;
        //String actualMessage2 = exception2.getMessage();

        Exception exception3 = assertThrows(ModelAccessException.class, () ->
        {gameValidator4.gameIdPlayerNameValidCheck(accessToken,"ruoyu",5151551235L);});

        //String expectedMessage3 = "Player:" + "ruoyu" + " is not in game: " + 5151551235L;
        //String actualMessage3 = exception3.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));
        //assertTrue(actualMessage2.contains(expectedMessage2));
        //assertTrue(actualMessage3.contains(expectedMessage3));
    }
}