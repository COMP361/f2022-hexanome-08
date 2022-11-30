//package ca.group8.gameservice.splendorgame.model.splendormodel;
//
//import java.util.EnumMap;
//import java.util.Optional;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//
//public class DeckTest {
//
//  Deck deck = new Deck(1);
//
//  @Test
//  void testLevel(){
//    Assert.assertEquals(deck.getLevel(), 1);
//  }
//
//  @Test
//  void testPop(){
//    EnumMap<Colour,Integer> wealth = new EnumMap<Colour, Integer>(Colour.class);
//    DevelopmentCard card = new DevelopmentCard(3, wealth,"card", 2, Optional.of(Colour.BLUE),false, "32", 2);
//    deck.add(card);
//    Assert.assertEquals(deck.pop(),card);
//  }
//
//}
