package ca.group8.gameservice.splendorgame.model.splendormodel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPowers {

  PlayerInGame p1 = new PlayerInGame("Eden");
  EnumMap<Colour,Integer> price_1 = new EnumMap<>(Colour.class);
  EnumMap<Colour,Integer> price_2 = new EnumMap<>(Colour.class);
  EnumMap<Colour,Integer> price_3 = new EnumMap<>(Colour.class);
  List<CardEffect> purchaseEffects = new ArrayList<>();
  DevelopmentCard c1 = new DevelopmentCard(1,price_1,"card1",1,Colour.RED,1,purchaseEffects);
  DevelopmentCard c2 = new DevelopmentCard(2,price_2,"card2",1,Colour.BLUE,2,purchaseEffects);
  DevelopmentCard c3 = new DevelopmentCard(3,price_3,"card3",1,Colour.BLACK,1,purchaseEffects);
  DevelopmentCard c4 = new DevelopmentCard(1,price_3,"card4",1,Colour.BLACK,1,purchaseEffects);
  DevelopmentCard c5 = new DevelopmentCard(1,price_3,"card5",1,Colour.BLACK,1,purchaseEffects);
  NobleCard n1 = new NobleCard(2, price_1,"Dave");

  @BeforeEach
  void setUp() {
    p1.getPurchasedHand().addDevelopmentCard(c1);
    p1.getPurchasedHand().addDevelopmentCard(c2);
    p1.getPurchasedHand().addDevelopmentCard(c3);
  }

  @Test
  void TestArmPointsPowerValidity() {
    ArmPointsPower armPointsPower = new ArmPointsPower();
    assertFalse(armPointsPower.validityCheck(p1));

    p1.getPurchasedHand().addDevelopmentCard(c4);
    p1.getPurchasedHand().addDevelopmentCard(c5);
    assertTrue(armPointsPower.validityCheck(p1));

  }

  @Test
  void TestDoubleGoldPowerValidity() {
    DoubleGoldPower power = new DoubleGoldPower();
    assertFalse(power.validityCheck(p1));

    DevelopmentCard card = new DevelopmentCard(1,price_1,"card",1,Colour.BLUE,1,purchaseEffects);
    DevelopmentCard another_card = new DevelopmentCard(1,price_1,"another_card",1,Colour.BLUE,2,purchaseEffects);
    p1.getPurchasedHand().addDevelopmentCard(card);
    p1.getPurchasedHand().addDevelopmentCard(another_card);
    assertTrue(power.validityCheck(p1));
  }

  @Test
  void TestExtraTokenPower() {
    ExtraTokenPower power = new ExtraTokenPower();
    assertFalse(power.validityCheck(p1));

    DevelopmentCard card = new DevelopmentCard(1,price_1,"card",1,Colour.WHITE,1,purchaseEffects);
    DevelopmentCard another_card = new DevelopmentCard(1,price_1,"another_card",1,Colour.RED,2,purchaseEffects);
    p1.getPurchasedHand().addDevelopmentCard(card);
    p1.getPurchasedHand().addDevelopmentCard(another_card);
    assertTrue(power.validityCheck(p1));
  }
  @Test
  void TestFivePointsPowerValidity() {
    FivePointsPower fivePointsPower = new FivePointsPower();
    assertFalse(fivePointsPower.validityCheck(p1));

    DevelopmentCard card = new DevelopmentCard(1,price_1,"card",1,Colour.GREEN,1,purchaseEffects);
    DevelopmentCard another_card = new DevelopmentCard(1,price_1,"another_card",1,Colour.GREEN,2,purchaseEffects);
    DevelopmentCard next_card = new DevelopmentCard(1,price_1,"next_card",1,Colour.GREEN,2,purchaseEffects);

    //Assert False Because we don't have noble yet (only have 5 green cards)
    p1.getPurchasedHand().addDevelopmentCard(card);
    p1.getPurchasedHand().addDevelopmentCard(another_card);
    p1.getPurchasedHand().addDevelopmentCard(next_card);
    assertFalse(fivePointsPower.validityCheck(p1));

    //add noble
    p1.getPurchasedHand().addNobleCard(n1);
    assertTrue(fivePointsPower.validityCheck(p1));

  }

  @Test
  void TestTwoPlusOnePower() {
    TwoPlusOnePower twoPlusOnePower = new TwoPlusOnePower();
    assertFalse(twoPlusOnePower.validityCheck(p1));

    DevelopmentCard card = new DevelopmentCard(1,price_1,"card",1,Colour.WHITE,1,purchaseEffects);
    DevelopmentCard another_card = new DevelopmentCard(1,price_1,"another_card",1,Colour.WHITE,2,purchaseEffects);
    p1.getPurchasedHand().addDevelopmentCard(card);
    p1.getPurchasedHand().addDevelopmentCard(another_card);
    assertTrue(twoPlusOnePower.validityCheck(p1));

  }

  @org.junit.Test
  public void test() {
    Map<String,Integer> playersInfo = new HashMap<>();
    playersInfo.put("ruoyu", 1);
    playersInfo.put("julia", 3);
    playersInfo.put("pengyu", 12);

    List<String> oldNames = new ArrayList<>(playersInfo.keySet());
    List<String> newNames = Arrays.asList("ruoyu", "muzhi", "eden");
    List<String> newNamesCopy = new ArrayList<>(newNames);
    if (!newNames.equals(oldNames)) {
      Map<String, Integer> newPlayerMap = new HashMap<>();
      for (String oldName : playersInfo.keySet()) {
        // if any oldName match a new name, remove this old name from newNameCopy list
        if (newNames.contains(oldName)) {
          newPlayerMap.put(oldName, playersInfo.get(oldName));
          newNamesCopy.remove(oldName);
        } else {
          int randomNewNameIndex = new Random().nextInt(newNamesCopy.size());
          String newName = newNamesCopy.get(randomNewNameIndex);
          Integer newPlayerInGame = playersInfo.get(oldName);
          newPlayerMap.put(newName, newPlayerInGame);
          newNamesCopy.remove(newName);
        }
      }
      System.out.println(newPlayerMap);
      // in the end, overwrite the previous map
      playersInfo = newPlayerMap;
    }
  }
  //@Test
  //void TestUnlock() {
  //  TwoPlusOnePower power = new TwoPlusOnePower();
  //  assertFalse(power.isUnlocked());
  //
  //  power.unlock();
  //  assertTrue(power.isUnlocked());
  //}
}

