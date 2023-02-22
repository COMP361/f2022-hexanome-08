package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import static org.junit.Assert.assertEquals;

import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestGameManager {

  List<String> names = new ArrayList<>();
  List<Extension> extensions = new ArrayList<>();
  PlayerStates playerStates = new PlayerStates(names);


  @BeforeEach
  void setup() {
    names.add("Bob");
    names.add("Mary");
    extensions.add(Extension.BASE);
  }


  //@Test
  //void TestGetActionInterpreter() {
  //  GameInfo game = new GameInfo(extensions,names);
  //  ActionInterpreter a1 = new ActionInterpreter(game, playerStates);
  //  GameManager gameManager1 = new GameManager();
  //  gameManager1.addGame(1234,game);
  //  gameManager1.addGameActionInterpreter(1234, a1);
  //  ActionInterpreter result = gameManager1.getGameActionInterpreter(1234);
  //  assertEquals(a1, result);
  //}

}
