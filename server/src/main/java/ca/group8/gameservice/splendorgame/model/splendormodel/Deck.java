package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a deck.
 */
public class Deck {


  //TODO: Depends on Card interface
  private List<Card> cards = new ArrayList();

  //TODO Make a LEVEL enum to assign to ca.group8.gameservice.splendorgame.model.splendormodel.Deck? Or do we want an int?
  private int level;

  public Deck(List<Card> cards, int level) {
    this.cards = cards;
    this.level = level;
  }

  /**
   * Get the size of the deck.
   */
  public int size() {
    return cards.size();
  }

  /**
   * Remove the top Card off of the deck.
   */
  public Card pop() {
    Card card;
    if (!this.isEmpty()) {
      card = cards.remove(0);
    } else {
      throw new UnsupportedOperationException(); //TODO Throw a more relevant exception
    }

    return card;
  }

  /**
   * Check if ca.group8.gameservice.splendorgame.model.splendormodel.Deck is empty.
   */
  public boolean isEmpty() {
    return cards.isEmpty();
  }
}
