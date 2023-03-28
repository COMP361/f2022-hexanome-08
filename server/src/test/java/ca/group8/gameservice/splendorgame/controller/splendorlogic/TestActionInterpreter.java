package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.CityBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.ReservedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TokenHand;
import ca.group8.gameservice.splendorgame.testutils.CardFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestActionInterpreter {

  List<String> playerNames = Arrays.asList("julia", "ruoyu");
  List<Extension> extensions = Arrays.asList(Extension.BASE, Extension.ORIENT, Extension.CITY, Extension.TRADING_POST);
  GameInfo gameInfo = new GameInfo(extensions, playerNames, playerNames.get(0));
  PlayerStates playerStates = new PlayerStates(playerNames);
  ActionInterpreter actionInterpreter = new ActionInterpreter(gameInfo, playerStates);
  ActionGenerator actionGenerator = actionInterpreter.getActionGenerator();
  String curPlayerName = gameInfo.getCurrentPlayer();
  PlayerInGame playerInGame = playerStates.getOnePlayerInGame(curPlayerName);

  PlayerInGame lastPlayer = playerStates
      .getOnePlayerInGame(gameInfo.getPlayerNames().get(playerNames.size()-1));

  @Test
  public void testReserveAction_BaseCard() {
    actionGenerator.setInitialActions(playerInGame, curPlayerName);
    Map<String, Action> playerActionMap = gameInfo.getPlayerActionMaps().get(curPlayerName);
    DevelopmentCard reservedCard = null;
    for (String actionId : playerActionMap.keySet()) {
      Action curAction = playerActionMap.get(actionId);
      if (curAction instanceof ReserveAction) {
        ReserveAction reserveAction = (ReserveAction) curAction;
        Position position = reserveAction.getCardPosition();
        reservedCard = reserveAction.getCurCard();
        if (reservedCard.isBaseCard()){
          actionInterpreter.interpretAction(actionId, curPlayerName);
          break;
        }
      }
    }
    DevelopmentCard reservedCardInHand = playerInGame.getReservedHand().getDevelopmentCards().get(0);
    // there should be no more action map
    assertEquals(new HashMap<>(), actionGenerator.getPlayerActionMaps().get(curPlayerName));
    assertEquals(reservedCard,reservedCardInHand);
  }


  @Test
  public void testReserveAction_OrientCard() {
    actionGenerator.setInitialActions(playerInGame, curPlayerName);
    Map<String, Action> playerActionMap = gameInfo.getPlayerActionMaps().get(curPlayerName);
    DevelopmentCard reservedCard = null;
    for (String actionId : playerActionMap.keySet()) {
      Action curAction = playerActionMap.get(actionId);
      if (curAction instanceof ReserveAction) {
        ReserveAction reserveAction = (ReserveAction) curAction;
        Position position = reserveAction.getCardPosition();
        reservedCard = reserveAction.getCurCard();
        if (!reservedCard.isBaseCard()){
          actionInterpreter.interpretAction(actionId, curPlayerName);
          break;
        }
      }
    }
    DevelopmentCard reservedCardInHand = playerInGame.getReservedHand().getDevelopmentCards().get(0);
    // there should be no more action map
    assertEquals(new HashMap<>(), actionGenerator.getPlayerActionMaps().get(curPlayerName));
    assertEquals(reservedCard,reservedCardInHand);
  }


  @Test
  public void testTakeTokenAction_NoReturnToken() {
    actionGenerator.setInitialActions(playerInGame, curPlayerName);
    Map<String, Action> playerActionMap = gameInfo.getPlayerActionMaps().get(curPlayerName);
    EnumMap<Colour, Integer> tokensTakenFromBank = new EnumMap<>(Colour.class);
    for (String actionId : playerActionMap.keySet()) {
      Action curAction = playerActionMap.get(actionId);
      if (curAction instanceof TakeTokenAction) {
        TakeTokenAction takeTokenAction = (TakeTokenAction) curAction;
        tokensTakenFromBank = takeTokenAction.getTokens();
        actionInterpreter.interpretAction(actionId, curPlayerName);
        break;
      }
    }
    // note the all tokens in hand include the gold token, thus we want to exclude that before
    // comparison
    EnumMap<Colour, Integer> regularTokensInHand =
        new EnumMap<>(playerInGame.getTokenHand().getAllTokens());
    // there should be no more action map
    assertEquals(regularTokensInHand, tokensTakenFromBank);
  }


  @Test
  public void testTakeTokenAction_ReturnOneToken()
      throws NoSuchFieldException, IllegalAccessException {
    // pre-set the tokens for the player
    EnumMap<Colour, Integer> presetTokens = new EnumMap<>(Colour.class) {{
      put(Colour.BLUE, 0);
      put(Colour.RED, 0);
      put(Colour.BLACK, 0);
      put(Colour.GREEN, 3);
      put(Colour.WHITE, 4);
      put(Colour.GOLD, 2);
    }};



    Field tokenHand = PlayerInGame.class.getDeclaredField("tokenHand");
    tokenHand.setAccessible(true);

    Field allTokens = TokenHand.class.getDeclaredField("allTokens");
    allTokens.setAccessible(true);

    TokenHand presetTokenHand = new TokenHand(0);
    allTokens.set(presetTokenHand, presetTokens);
    tokenHand.set(playerInGame, presetTokenHand);
    //System.out.println(playerInGame.getTokenHand().getAllTokens());


    actionGenerator.setInitialActions(playerInGame, gameInfo.getCurrentPlayer());
    Map<String, Action> playerActionMap = gameInfo.getPlayerActionMaps().get(curPlayerName);
    EnumMap<Colour, Integer> tokensTakenFromBank = new EnumMap<>(Colour.class);
    for (String actionId : playerActionMap.keySet()) {
      Action curAction = playerActionMap.get(actionId);
      if (curAction instanceof TakeTokenAction) {
        TakeTokenAction takeTokenAction = (TakeTokenAction) curAction;
        tokensTakenFromBank = takeTokenAction.getTokens();
        actionInterpreter.interpretAction(actionId, curPlayerName);
        break;
      }
    }
    System.out.println(tokensTakenFromBank);
    System.out.println(playerInGame.getTokenHand().getAllTokens());
    System.out.println(actionGenerator.getPlayerActionMaps().get(curPlayerName));
    Map<String, Action> returnTokenActionMap =
        actionGenerator.getPlayerActionMaps().get(curPlayerName);
    assertEquals(1, returnTokenActionMap.size());
  }


  @Test
  public void testUnlockNobleAfterTakeToken() {

    for (Colour c : SplendorDevHelper.getInstance().getRawTokenColoursMap().keySet()) {
      if (!c.equals(Colour.GOLD)) {
        for (int i = 0; i < 6; i++) {
          DevelopmentCard card = CardFactory.getInstance().getOneBaseCard(c, 1);
          playerInGame.getPurchasedHand().addDevelopmentCard(card);
        }
      }
    }

    // remove 2 nobles from the board
    for (int i = 0; i<2; i++) {
      BaseBoard baseBoard = (BaseBoard) gameInfo.getTableTop().getBoard(Extension.BASE);
      NobleCard nobleCard = baseBoard.getNobles().get(i);
      baseBoard.removeNoble(nobleCard);
    }

    // should have enough cards to unlock all nobles on board, which are 3
    actionGenerator.setInitialActions(playerInGame, gameInfo.getCurrentPlayer());
    Map<String, Action> playerActionMap = gameInfo.getPlayerActionMaps().get(curPlayerName);
    for (String actionId : playerActionMap.keySet()) {
      Action curAction = playerActionMap.get(actionId);
      if (curAction instanceof TakeTokenAction) {
        actionInterpreter.interpretAction(actionId, curPlayerName);
        break;
      }
    }

  }


  @Test
  public void testWinnerAfterOneAction() {
    // since we are playing city, winning condition is checked if a player has a city card or not
    CityBoard cityBoard = (CityBoard) gameInfo.getTableTop().getBoard(Extension.CITY);
    cityBoard.assignCityCard("julia", CardFactory.getInstance().getOneCityCard(2,15));
    cityBoard.assignCityCard("ruoyu", CardFactory.getInstance().getOneCityCard(2,16));



    // should have enough cards to unlock all nobles on board, which are 3

    List<PlayerInGame> oneRoundOfPlayers = List.of(playerInGame, lastPlayer);
    for (PlayerInGame player : oneRoundOfPlayers) {
      actionGenerator.setInitialActions(player, gameInfo.getCurrentPlayer());
      Map<String, Action> playerActionMap = gameInfo.getPlayerActionMaps().get(player.getName());
      for (String actionId : playerActionMap.keySet()) {
        Action curAction = playerActionMap.get(actionId);
        if (curAction instanceof TakeTokenAction) {
          actionInterpreter.interpretAction(actionId, player.getName());
          break;
        }
      }
    }
  }


  @Test
  public void testSetBurnCardInfo() {
    EnumMap<Colour, Integer> burnPrice = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    burnPrice.put(Colour.BLUE, 2);
    actionInterpreter.setBurnCardInfo(burnPrice);
    assertEquals(Colour.BLUE, actionInterpreter.getBurnCardColour());
    assertEquals(2, actionInterpreter.getBurnCardCount());
    actionInterpreter.removeBurnCardCount(1);
    assertEquals(1, actionInterpreter.getBurnCardCount());
  }

  @Test
  public void testStashedCard() {
    DevelopmentCard stashedCard = CardFactory.getInstance().getOneOrientCard(Colour.BLUE, 3, List.of(
        CardEffect.BURN_CARD));
    actionInterpreter.setStashedCard(stashedCard);
    assertEquals(stashedCard, actionInterpreter.getStashedCard());
  }

}
