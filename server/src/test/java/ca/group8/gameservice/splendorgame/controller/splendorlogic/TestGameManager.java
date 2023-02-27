package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.PlayerInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
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
public class TestGameManager {
  @Autowired
  GameManager gameManager;

  String[] players = new String[]{"ruoyu", "penn"};
  String[] colours = new String[]{"red", "blue"};

  List<PlayerInfo> playerInfos = IntStream
      .range(0, players.length)
      .mapToObj(i -> new PlayerInfo(players[i], colours[i]))
      .collect(Collectors.toList());
  String gamename = "splendortrade";
  String[] savegameids = new String[]{"gameId1","gameId2","gameId3"};
  Savegame[] savegames = new Savegame[]{
      new Savegame(players,gamename,savegameids[0]),
      new Savegame(players,gamename,savegameids[1]),
      new Savegame(players,gamename,savegameids[2])};

  long[] gameIds = new long[] {5151551235L, 8723123151231L, 1231231512123L};

  private void launchAndSaveThreeGames() throws ModelAccessException {
    for (int i = 0; i < savegameids.length; i++) {
      LauncherInfo launcherInfo = new LauncherInfo(gamename,
          new LinkedList<>(playerInfos),
          players[0]);
      gameManager.launchGame(gameIds[i], launcherInfo);
      gameManager.saveGame(savegames[i],gameIds[i]);
    }
  }
  @Test
  public void testLaunchFromExistingAndNon_Existing() throws ModelAccessException {
    launchAndSaveThreeGames();


    LauncherInfo launcherInfo = new LauncherInfo(gamename,
        new LinkedList<>(playerInfos),
        players[0]);
    launcherInfo.setSavegame(savegameids[0]);
    gameManager.launchGame(12451517195L, launcherInfo);
  }

  @Test
  public void testGetAllGameIds() {
    List<String> testIds = Arrays.asList(savegameids);
    assertEquals(testIds,gameManager.getSavedGameIds());

  }

  @Test
  public void testDeleteSavedGames() {
    gameManager.deleteAllSavedGame();
  }



}
