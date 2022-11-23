import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a deck.
 */
public class Deck {

  //TODO: Depends on Card interface
  private List<Card> cards = new ArrayList();

  //TODO Make a LEVEL enum to assign to Deck? Or do we want an int?
  private int level;

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
    if (!this.isEmpty()) {
      Card card = cards.remove(0);
    } else {
      throw new UnsupportedOperationException(); //TODO Throw a more relevant exception
    }

    return card;
  }

  /**
   * Check if Deck is empty.
   */
  public boolean isEmpty() {
    return aCards.isEmpty();
  }
}
