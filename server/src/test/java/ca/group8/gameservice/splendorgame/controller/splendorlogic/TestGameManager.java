package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.PlayerInfo;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.Savegame;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
public class TestGameManager {
  @Autowired
  GameManager gameManager;

  String[] players = new String[]{"ruoyu", "penn"};
  String[] colours = new String[]{"red", "blue"};

  List<PlayerInfo> playerInfos = IntStream
      .range(0, players.length)
      .mapToObj(i -> new PlayerInfo(players[i], colours[i]))
      .collect(Collectors.toList());
  String gamename = "splendorbase";
  String savegameid = "gameId1";
  Savegame savegame = new Savegame(players, gamename, savegameid);
  long gameId = 123123151235L;
  LauncherInfo launcherInfo = new LauncherInfo(gamename,
      new LinkedList<>(playerInfos),
      players[0]);
  @Test
  public void testLaunchGame() throws ModelAccessException {
    gameManager.launchGame(gameId, launcherInfo);
    assertEquals(0,0);
  }
  @Test
  public void testWriteSavedGameMetaDataToFile() {
    gameManager.saveGame(savegame,gameId);
  }


}
