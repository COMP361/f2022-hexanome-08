package project.view.splendor.communication;

import java.util.List;

/**
 * This class represents a deck.
 */
public class Deck {


  public List<Card> getCards() {
    return cards;
  }

  public int getLevel() {
    return level;
  }

  public void setCards(List<Card> cards) {
    this.cards = cards;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  private List<Card> cards;
  private int level;

  public Deck(List<Card> cards, int level) {
    this.cards = cards;
    this.level = level;
  }
}
