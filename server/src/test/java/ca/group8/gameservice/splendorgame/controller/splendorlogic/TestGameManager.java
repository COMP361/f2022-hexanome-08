package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.PlayerInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    List<String> testIds = Arrays.asList(savegameids);
    assertEquals(testIds,gameManager.getSavedGameIds());

    String[] newPlayers = new String[] {"penn", "muzhi"};
    List<PlayerInfo> playerInfos = IntStream
        .range(0, newPlayers.length)
        .mapToObj(i -> new PlayerInfo(newPlayers[i], colours[i]))
        .collect(Collectors.toList());
    LauncherInfo launcherInfo = new LauncherInfo(gamename,
        new LinkedList<>(playerInfos),
        players[0]);
    launcherInfo.setSavegame(savegameids[0]);
    long newGameId = 12451517195L;
    gameManager.launchGame(newGameId, launcherInfo);
    assertEquals(new HashSet<>(Arrays.asList(newPlayers)),
        new HashSet<>(gameManager.getGameById(newGameId).getPlayerNames()));


  }

  @Test
  public void testDeleteSavedGames() {
    gameManager.deleteAllSavedGame();
  }

}
