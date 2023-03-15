package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.PlayerInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.SavedGameState;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestGameManager{
  @Autowired
  GameManager gameManager;

  String[] players = new String[]{"ruoyu", "pengyu"};
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

  @Test
  @Order(1)
  public void testLaunchSave() throws ModelAccessException {
    gameManager.deleteAllSavedGame();
    LauncherInfo launcherInfo = new LauncherInfo(gamename,
        new LinkedList<>(playerInfos),
        players[0]);
    gameManager.launchGame(gameIds[0], launcherInfo);
    gameManager.saveGame(savegames[0],gameIds[0]);
    assertEquals(1,gameManager.getSavedGameIds().size());

  }

  @Test
  @Order(2)
  public void testLaunchWithSavedGameId() throws ModelAccessException {
    String[] newPlayers = new String[] {"julia", "young"};
    List<PlayerInfo> playerInfos = IntStream
        .range(0, newPlayers.length)
        .mapToObj(i -> new PlayerInfo(newPlayers[i], colours[i]))
        .collect(Collectors.toList());
    LauncherInfo launcherInfo2 = new LauncherInfo(gamename,
        new LinkedList<>(playerInfos),
        newPlayers[0]);
    launcherInfo2.setSavegame(savegameids[0]);
    long newGameId = 12451517195L;
    SavedGameState savedGameState = gameManager.launchGame(newGameId, launcherInfo2);
    // there should be two new players whose game was loaded based on one previously saved game
    assertEquals(new HashSet<>(Arrays.asList(newPlayers)),
        new HashSet<>(gameManager.getGameById(newGameId).getPlayerNames()));
    assertEquals(newPlayers[0], savedGameState.getGameInfo().getCreator());

    //gameManager.deleteAllSavedGame();
    //// after all saved game being deleted, all saved game ids remain empty
    //assertEquals(new ArrayList<>(), gameManager.getSavedGameIds());
  }


  @Test
  @Order(2)
  public void testLaunchAndKeepPlaying() throws ModelAccessException {
    String[] newPlayers = new String[] {"julia", "ruoyu"};
    List<PlayerInfo> playerInfos = IntStream
        .range(0, newPlayers.length)
        .mapToObj(i -> new PlayerInfo(newPlayers[i], colours[i]))
        .collect(Collectors.toList());
    LauncherInfo launcherInfo = new LauncherInfo(gamename,
        new LinkedList<>(playerInfos),
        players[0]);
    launcherInfo.setSavegame(savegameids[0]);
    long newGameId = 12451517195L;
    SavedGameState savedGameState = gameManager.launchGame(newGameId, launcherInfo);
    //// there should be two new players whose game was loaded based on one previously saved game
    //assertEquals(new HashSet<>(Arrays.asList(newPlayers)),
    //    new HashSet<>(gameManager.getGameById(newGameId).getPlayerNames()));

    // observe the player actions after loading the file
    GameInfo gameInfo = savedGameState.getGameInfo();
    PlayerStates playerStates = savedGameState.getPlayerStates();
    System.out.println();

  }

}
