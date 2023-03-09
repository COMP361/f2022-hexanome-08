package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Board;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.OrientBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCardExtraAction {

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
  DevelopmentCard d1 = new DevelopmentCard(1, price1,
      "card1", 1, Colour.GREEN, 2,cardEffects);
  DevelopmentCard d2 = new DevelopmentCard(1, price1,
      "card1", 1, Colour.GREEN, 2,cardEffects);
  DevelopmentCard d3 = new DevelopmentCard(1, price1,
      "card1", 1, Colour.GREEN, 2,cardEffects);
  DevelopmentCard d4 = new DevelopmentCard(1, price1,
      "card1", 1, Colour.GREEN, 2,cardEffects);
  DevelopmentCard d5 = new DevelopmentCard(1, price1,
      "card1", 1, Colour.GREEN, 2,cardEffects);
  Position position = new Position(2,1);
  EnumMap<Colour,Integer> price = new EnumMap<>(Colour.class);
  List<CardEffect> purchaseEffectsList = new ArrayList();
  DevelopmentCard d6 = new DevelopmentCard(1,price,
      "card1",1,Colour.BLUE,1,purchaseEffectsList);
  CardExtraAction freeCard = new CardExtraAction(d1, CardEffect.FREE_CARD,position);

  @BeforeEach
  void setup() {
    purchaseEffectsList.add(CardEffect.FREE_CARD);
    price.put(Colour.RED,1);
  }

  @Test
  void testFreeCardHelperBase1_ReplaceCardOnBoard() {
    BaseBoard b1 = (BaseBoard) gameInfo.getTableTop().getBoard(Extension.BASE);
    DevelopmentCard devCard = b1.getLevelCardsOnBoard(1)[0];
    CardExtraAction freeCard = new CardExtraAction(devCard,CardEffect.FREE_CARD,
        new Position(1,0));
    freeCard.freeCardActionHelper(gameInfo.getTableTop(),playerInGame,actionInterpreter);
    assertFalse(b1.getLevelCardsOnBoard(1)[0].equals(devCard));
  }

  //@Test
  //void testFreeCardHelperOrient1_ReplaceCardOnBoard() {
  //  OrientBoard o1 = (OrientBoard) gameInfo.getTableTop().getBoard(Extension.ORIENT);
  //  DevelopmentCard devCard = o1.getLevelCardsOnBoard(1)[0];
  //  CardExtraAction freeCard = new CardExtraAction(devCard,CardEffect.FREE_CARD,
  //      new Position(1,0));
  //  freeCard.freeCardActionHelper(gameInfo.getTableTop(),playerInGame,actionInterpreter);
  //  assertFalse(o1.getLevelCardsOnBoard(1)[0].equals(devCard));
  //}
  //
  //@Test
  //void testGetFreeSatchelCardOrDoubleGold() {
  //  OrientBoard o1 = (OrientBoard) gameInfo.getTableTop().getBoard(Extension.ORIENT);
  //  DevelopmentCard devCard = o1.getLevelCardsOnBoard(1)[1];
  //  System.out.println(devCard.getPurchaseEffects().get(0));
  //  CardExtraAction freeCard = new CardExtraAction(devCard,CardEffect.FREE_CARD,
  //      new Position(1,0));
  //  freeCard.freeCardActionHelper(gameInfo.getTableTop(),playerInGame,actionInterpreter);
  //  assertFalse(o1.getLevelCardsOnBoard(1)[0].equals(devCard));
  //
  //  if(devCard.getPurchaseEffects().get(0) == CardEffect.SATCHEL) {
  //    System.out.println("Stored Card is: " +
  //        actionInterpreter.getStashedCard().getPurchaseEffects());
  //    assertTrue(actionInterpreter.getStashedCard() == devCard);
  //  }
  //}

 // @Test
 // void testGetFreeandSatchel() {
 //   OrientBoard o1 = (OrientBoard) gameInfo.getTableTop().getBoard(Extension.ORIENT);
 //   DevelopmentCard[] devCards = o1.getLevelCardsOnBoard(2);
 //   DevelopmentCard developmentCard = devCards[1];
 //   System.out.println(developmentCard.getPurchaseEffects());
 //
 //   if (developmentCard.getPurchaseEffects().size()==1) {
 //     testCardHelperOrient1_ReplaceCardOnBoard2(developmentCard);
 //   }
 //   if (developmentCard.getPurchaseEffects().size()==2) {
 //     CardExtraAction freeCard = new CardExtraAction(developmentCard,CardEffect.FREE_CARD,
 //         new Position(2,1));
 //     freeCard.freeCardActionHelper(gameInfo.getTableTop(),playerInGame,actionInterpreter);
 //
 //     assertTrue(actionInterpreter.getStashedCard() == developmentCard);
 //     assertTrue(actionInterpreter.getFreeCardLevel() == developmentCard.getLevel()-1);
 //
 //
 //   }
 //
 //}

  //@Test
  //void testCardHelperOrient1_ReplaceCardOnBoard2(DevelopmentCard devCard) {
  //  OrientBoard o1 = (OrientBoard) gameInfo.getTableTop().getBoard(Extension.ORIENT);
  //  //DevelopmentCard devCard = o1.getLevelCardsOnBoard(1)[0];
  //  CardExtraAction freeCard = new CardExtraAction(devCard,devCard.getPurchaseEffects().get(0),
  //      new Position(2,1));
  //  freeCard.freeCardActionHelper(gameInfo.getTableTop(),playerInGame,actionInterpreter);
  //  assertFalse(o1.getLevelCardsOnBoard(1)[0].equals(devCard));
  //}

}
