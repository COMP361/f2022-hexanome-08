package ca.group8.gameservice.splendorgame.controller.communicationbeans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.GameServerParameters;
import org.junit.jupiter.api.Test;

public class TestGameServerParamaters {

  GameServerParameters g = new GameServerParameters("Eden", "eden",
      "test", 4, 2,"no");

  @Test
  void testGetName() {
    assertEquals("Eden", g.getName());
  }

  @Test
  void testDisplayName() {
    assertEquals("eden", g.getDisplayName());
  }

  @Test
  void testGetLocations() {
    assertEquals("test", g.getLocation());
  }

  @Test
  void testGetMaxPlayers() {
    assertEquals(4, g.getMaxSessionPlayers());
  }

  @Test
  void testGetMinPlayers() {
    assertEquals(2, g.getMinSessionPlayers());
  }

  @Test
  void setName() {
    g.setName("Bob");
    assertEquals("Bob", g.getName());
  }
}
