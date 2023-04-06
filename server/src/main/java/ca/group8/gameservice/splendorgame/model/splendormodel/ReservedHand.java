package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds all reserved cards.
 */
public class ReservedHand {
  private final List<DevelopmentCard> developmentCards;
  private final List<NobleCard> nobleCards;

  /**
   * Default constructor.
   */
  public ReservedHand() {
    this.developmentCards = new ArrayList<>();
    this.nobleCards = new ArrayList<>();
  }

  //TODO: Add exception checks to methods (if card == null, throw an exception, etc...)

  /**
   * Adds card to reserve hand.
   *
   * @param card reserved card
   */
  public void addDevelopmentCard(DevelopmentCard card) {
    developmentCards.add(card);
  }

  /**
   * Adds noble card to reserve hand.
   *
   * @param card noble card
   */
  public void addNobleCard(NobleCard card) {
    nobleCards.add(card);
  }

  /**
   * Removes development card from reserve hand.
   *
   * @param card being removed
   */
  public void removeDevelopmentCard(DevelopmentCard card) {
    assert developmentCards.contains(card);
    developmentCards.remove(card);
  }

  /**
   * Removes noble card from reserve hand.
   *
   * @param card card being removed
   */
  public void removeNobleCard(NobleCard card) {
    assert nobleCards.contains(card);
    nobleCards.remove(card);
  }

  /**
   * Is the reserve hand full.
   *
   * @return boolean of if it is full
   */
  public boolean isFull() {
    return developmentCards.size() == 3;
  }

  /**
   * Gets amount of development cards reserved.
   *
   * @return number of development cards reserved
   */
  public int getSize() {
    return developmentCards.size();
  }

  /**
   * Gets List of reserved cards.
   *
   * @return unmodifiable list of reserved cards
   */
  public List<DevelopmentCard> getDevelopmentCards() {
    return Collections.unmodifiableList(developmentCards);
  }

  /**
   * Gets list of noble cards reserved.
   *
   * @return unmodifiable list of noble cards reserved
   */
  public List<NobleCard> getNobleCards() {
    return Collections.unmodifiableList(nobleCards);
  }

  /**
   * Removes noble from reserved hand.
   *
   * @param nobleCard being removed
   */
  public void removeNoble(NobleCard nobleCard) {
    nobleCards.remove(nobleCard);
  }
}
