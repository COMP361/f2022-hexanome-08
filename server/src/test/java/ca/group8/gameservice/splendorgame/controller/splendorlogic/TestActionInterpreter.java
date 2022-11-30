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
  EnumMap<Colour,Integer> tokens = new EnumMap<Colour, Integer>(Colour.class);
  PlayerInGame curPlayer;
  DevelopmentCard card;




  @BeforeEach
  void initialise() throws Exception{
    playerNames.add("Young");
    playerNames.add("Julia");
    game = new GameInfo(playerNames);
    curPlayer = game.getCurrentPlayer();
    Position position = new Position(2,2);
    card = (DevelopmentCard) game.getTableTop().getBaseBoard().getCard(2,2);
    purchaseAction = new PurchaseAction(true, position, card, 0);
    reserveAction = new ReserveAction(true, position,card);
    tokens.put(Colour.BLUE, 2);
    takeTokenAction = new TakeTokenAction(false, tokens);

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
    purchaseAction.execute(game, curPlayer);
    assert(curPlayer.getPurchasedHand().getSize()==1);
    assert(curPlayer.getPurchasedHand().getDevelopmentCards().get(0)==card);
    assert(game.getTableTop().getBaseBoard().getCard(2,2)!=card);
    assert(game.getTableTop().getDecks().get(1).size()==35);
  }


  void testReserve(){
    reserveAction.execute(game, curPlayer);

  }


  //TODO: test next player
  // test purchase, reserve take token
}
