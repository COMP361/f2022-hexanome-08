package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestActionInterpreter {

  PurchaseAction purchaseAction;
  ReserveAction reserveAction;
  TakeTokenAction takeTokenAction;
  ArrayList<String> playerNames = new ArrayList<>();
  GameInfo game;
  EnumMap<Colour,Integer> tokens;
  PlayerInGame curPlayer;
  DevelopmentCard card;
  SplendorActionInterpreter interpreter = new SplendorActionInterpreter();


  @BeforeEach
  void initialise() throws Exception{
    tokens = new EnumMap<Colour, Integer>(Colour.class);
    playerNames.add("Young");
    playerNames.add("Julia");
    game = new GameInfo(playerNames);
    curPlayer = game.getCurrentPlayer();
    Position position = new Position(2,2);
    card = (DevelopmentCard) game.getTableTop().getBaseBoard().getCard(2,2);
    purchaseAction = new PurchaseAction(position, card, 0);
    reserveAction = new ReserveAction(position, card);
    for(Colour colour:Colour.values()){
      tokens.put(colour, 0);
    }
    tokens.put(Colour.BLUE, 2);
    takeTokenAction = new TakeTokenAction(tokens);

  }

  @Test
  void testNextPlayer(){
    PlayerInGame firstPlayer = curPlayer;
    game.setNextPlayer();
    assert(firstPlayer!= game.getCurrentPlayer());
    game.setNextPlayer();
    assert(firstPlayer== game.getCurrentPlayer());
  }

  @Test
  void testPurchase(){
    interpreter.interpretAction(purchaseAction, game, curPlayer);
    assert(curPlayer.getPurchasedHand().getSize()==1);
    assert(curPlayer.getPurchasedHand().getDevelopmentCards().get(0)==card);
    assert(game.getTableTop().getBaseBoard().getCard(2,2)!=card);
    assert(game.getTableTop().getDecks().get(1).size()==35);
  }

  @Test
  void testReserve(){
    interpreter.interpretAction(reserveAction, game, curPlayer);
    assert(curPlayer.getReservedHand().getSize()==1);
    assert(curPlayer.getReservedHand().getDevelopmentCards().get(0)==card);
    assert(game.getTableTop().getBaseBoard().getCard(2,2)!=card);
    assert(game.getTableTop().getDecks().get(1).size()==35);
  }

  @Test
  void testTakeTokens(){
    int initialBlueValue = game.getTableTop().getBank().getAllTokens().get(Colour.BLUE);


    interpreter.interpretAction(takeTokenAction, game, curPlayer);

    for(Colour colour: Colour.values()){
      if(colour == Colour.BLUE){
        assert(curPlayer.getTokenHand().getAllTokens().get(colour)==5);
        assert(initialBlueValue-2==game.getTableTop().getBank().getAllTokens().get(colour));
      }else{
        assert(curPlayer.getTokenHand().getAllTokens().get(colour)==3);
      }
    }
  }
}
