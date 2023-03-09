package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.ReservedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TokenHand;
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
  List<Extension> extensions = Arrays.asList(Extension.BASE, Extension.ORIENT);
  GameInfo gameInfo = new GameInfo(extensions, playerNames, playerNames.get(0));
  PlayerStates playerStates = new PlayerStates(playerNames);
  ActionInterpreter actionInterpreter = new ActionInterpreter(gameInfo, playerStates);
  ActionGenerator actionGenerator = actionInterpreter.getActionGenerator();
  String curPlayerName = gameInfo.getCurrentPlayer();
  PlayerInGame playerInGame = playerStates.getOnePlayerInGame(curPlayerName);

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


    //actionGenerator.setInitialActions(playerInGame);
    Map<String, Action> playerActionMap = gameInfo.getPlayerActionMaps().get(curPlayerName);
    EnumMap<Colour, Integer> tokensTakenFromBank = new EnumMap<>(Colour.class);
    for (String actionId : playerActionMap.keySet()) {
      Action curAction = playerActionMap.get(actionId);
      if (curAction instanceof TakeTokenAction) {
        TakeTokenAction takeTokenAction = (TakeTokenAction) curAction;
        tokensTakenFromBank = takeTokenAction.getTokens();
        //actionInterpreter.interpretAction(actionId, curPlayerName);
        break;
      }
    }
    System.out.println(tokensTakenFromBank);
    System.out.println(playerInGame.getTokenHand().getAllTokens());
    System.out.println(actionGenerator.getPlayerActionMaps().get(curPlayerName));
    Map<String, Action> returnTokenActionMap =
        actionGenerator.getPlayerActionMaps().get(curPlayerName);
    for (String actionId : returnTokenActionMap.keySet()) {
      ReturnTokenAction action = (ReturnTokenAction) returnTokenActionMap.get(actionId);
      EnumMap<Colour, Integer> returnTokens = action.getTokens();
    }

  }



  //@BeforeEach
  //void initialise() throws Exception{
  //  tokens = new EnumMap<Colour, Integer>(Colour.class);
  //  playerNames.add("Young");
  //  playerNames.add("Julia");
  //  game = new GameInfo(playerNames);
  //  curPlayer = game.getCurrentPlayer();
  //  Position position = new Position(2,2);
  //  card = (DevelopmentCard) game.getTableTop().getBaseBoard().getCard(2,2);
  //  purchaseAction = new PurchaseAction(position, card, 0);
  //  reserveAction = new ReserveAction(position, card);
  //  for(Colour colour:Colour.values()){
  //    tokens.put(colour, 0);
  //  }
  //  tokens.put(Colour.BLUE, 2);
  //  takeTokenAction = new TakeTokenAction(tokens);
  //
  //}
  //
  //@Test
  //void testNextPlayer(){
  //  PlayerInGame firstPlayer = curPlayer;
  //  game.setNextPlayer();
  //  assert(firstPlayer!= game.getCurrentPlayer());
  //  game.setNextPlayer();
  //  assert(firstPlayer== game.getCurrentPlayer());
  //}
  //
  //@Test
  //void testPurchase(){
  //  interpreter.interpretAction(purchaseAction, game, curPlayer);
  //  assert(curPlayer.getPurchasedHand().getSize()==1);
  //  assert(curPlayer.getPurchasedHand().getDevelopmentCards().get(0)==card);
  //  assert(game.getTableTop().getBaseBoard().getCard(2,2)!=card);
  //  assert(game.getTableTop().getDecks().get(1).size()==35);
  //}
  //
  //@Test
  //void testReserve(){
  //  interpreter.interpretAction(reserveAction, game, curPlayer);
  //  assert(curPlayer.getReservedHand().getSize()==1);
  //  assert(curPlayer.getReservedHand().getDevelopmentCards().get(0)==card);
  //  assert(game.getTableTop().getBaseBoard().getCard(2,2)!=card);
  //  assert(game.getTableTop().getDecks().get(1).size()==35);
  //}
  //
  //@Test
  //void testTakeTokens(){
  //  int initialBlueValue = game.getTableTop().getBank().getAllTokens().get(Colour.BLUE);
  //
  //
  //  interpreter.interpretAction(takeTokenAction, game, curPlayer);
  //
  //  for(Colour colour: Colour.values()){
  //    if(colour == Colour.BLUE){
  //      assert(curPlayer.getTokenHand().getAllTokens().get(colour)==5);
  //      assert(initialBlueValue-2==game.getTableTop().getBank().getAllTokens().get(colour));
  //    }else{
  //      assert(curPlayer.getTokenHand().getAllTokens().get(colour)==3);
  //    }
  //  }
  //}
}
