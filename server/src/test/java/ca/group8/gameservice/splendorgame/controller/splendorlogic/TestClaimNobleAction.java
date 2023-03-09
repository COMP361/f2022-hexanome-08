package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.TraderBoard;
import com.fasterxml.jackson.databind.ser.Serializers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestClaimNobleAction {

  List<String> playerNames = Arrays.asList("julia", "ruoyu");
  List<Extension> extensions = Arrays.asList(Extension.BASE, Extension.ORIENT,
      Extension.TRADING_POST);
  GameInfo gameInfo = new GameInfo(extensions, playerNames, playerNames.get(0));
  PlayerStates playerStates = new PlayerStates(playerNames);
  ActionInterpreter actionInterpreter = new ActionInterpreter(gameInfo, playerStates);
  ActionGenerator actionGenerator = actionInterpreter.getActionGenerator();
  String curPlayerName = gameInfo.getCurrentPlayer();
  PlayerInGame playerInGame = playerStates.getOnePlayerInGame(curPlayerName);
  EnumMap<Colour, Integer> price1 = new EnumMap<>(Colour.class);
  List<CardEffect> cardEffects = new ArrayList<>();
  DevelopmentCard d1 = new DevelopmentCard(0, price1,
      "card1", 3, Colour.GREEN, 2,cardEffects);
  DevelopmentCard d2 = new DevelopmentCard(0, price1,
      "card1", 2, Colour.GREEN, 2,cardEffects);
  DevelopmentCard d3 = new DevelopmentCard(0, price1,
      "card1", 1, Colour.GREEN, 2,cardEffects);
  DevelopmentCard d4 = new DevelopmentCard(0, price1,
      "card1", 2, Colour.GREEN, 1,cardEffects);
  DevelopmentCard d5 = new DevelopmentCard(0, price1,
      "card1", 1, Colour.GREEN, 2,cardEffects);
  NobleCard n1 = new NobleCard(1,price1, "noble1");
  Position position = new Position(2,1);
  ClaimNobleAction claimNobleAction = new ClaimNobleAction(n1, position);

  @BeforeEach
  void setUp() {

    price1.put(Colour.RED,2);
    playerInGame.getPurchasedHand().addDevelopmentCard(d1);
    playerInGame.getPurchasedHand().addDevelopmentCard(d2);
    playerInGame.getPurchasedHand().addDevelopmentCard(d3);
    playerInGame.getPurchasedHand().addDevelopmentCard(d4);
    playerInGame.getPurchasedHand().addDevelopmentCard(d5);
  }


  @Test
  void testClaimNobleActionExecution() {
    claimNobleAction.execute(gameInfo.getTableTop(),playerInGame,actionGenerator,actionInterpreter);
    assertEquals(n1, playerInGame.getPurchasedHand().getNobleCards().get(0));
    // the power is unlocked immediately, thus the points should be updated
    assertEquals(6, playerInGame.getPrestigePoints());

    BaseBoard b1 = (BaseBoard) gameInfo.getTableTop().getBoard(Extension.BASE);
    List<NobleCard> nobles = b1.getNobles();
    assertFalse(nobles.contains(n1));

    TraderBoard t = (TraderBoard) gameInfo.getTableTop().getBoard(Extension.TRADING_POST);
    Power p = t.getPlayerOnePower(playerInGame.getName(), PowerEffect.FIVE_POINTS);
    assertTrue(p.isUnlocked());
    assertEquals(6,playerInGame.getPrestigePoints());
  }

}
