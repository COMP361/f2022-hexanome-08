package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestReturnTokenAction {
  List<String> playerNames = Arrays.asList("julia", "ruoyu");
  List<Extension> extensions = Arrays.asList(Extension.BASE, Extension.ORIENT,
      Extension.TRADING_POST);
  GameInfo gameInfo = new GameInfo(extensions, playerNames, playerNames.get(0));
  PlayerStates playerStates = new PlayerStates(playerNames);
  ActionInterpreter actionInterpreter = new ActionInterpreter(gameInfo, playerStates);
  ActionGenerator actionGenerator = actionInterpreter.getActionGenerator();
  String curPlayerName = gameInfo.getCurrentPlayer();
  PlayerInGame playerInGame = playerStates.getOnePlayerInGame(curPlayerName);
  EnumMap<Colour, Integer> returnValue =
      new EnumMap<>(SplendorDevHelper.getInstance().getRawTokenColoursMap());
  Position position = new Position(2,1);
  ReturnTokenAction returnTokenAction = new ReturnTokenAction(returnValue,2);
  EnumMap<Colour, Integer> playerTokens =
      new EnumMap<>(SplendorDevHelper.getInstance().getRawTokenColoursMap());
  EnumMap<Colour, Integer> tokensToTake =
      new EnumMap<>(SplendorDevHelper.getInstance().getRawTokenColoursMap());

  @BeforeEach
  void setUp() {

    Map<Colour,Integer> colours = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    for (Colour colour : colours.keySet()) {
      if (colour==Colour.RED) {
        returnValue.put(colour.RED,2);
        playerTokens.put(colour,3);
        tokensToTake.put(colour,3);
      } else if (colour==Colour.GREEN) {
        returnValue.put(colour,1);
        playerTokens.put(Colour.GREEN,3);
        tokensToTake.put(colour,3);
      } else {
        returnValue.put(colour,0);
        playerTokens.put(colour,0);
        tokensToTake.put(colour,0);
      }
    }

    playerInGame.getTokenHand().addToken(playerTokens);
    gameInfo.getTableTop().getBank().takeToken(tokensToTake);

  }

  @Test
  void testReturnToken() {
    returnTokenAction.execute(
        gameInfo.getTableTop(),playerInGame,actionGenerator,actionInterpreter);
    assertEquals(1, playerInGame.getTokenHand().getAllTokens().get(Colour.RED));
    assertEquals(2, playerInGame.getTokenHand().getAllTokens().get(Colour.GREEN));
    assertEquals(3, gameInfo.getTableTop().getBank().getAllTokens().get(Colour.RED));
    assertEquals(2, gameInfo.getTableTop().getBank().getAllTokens().get(Colour.GREEN));


  }


}
