package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Where all purchased cards go.
 */
public class PurchasedHand {

  private final List<DevelopmentCard> developmentCards;
  private final List<NobleCard> nobleCards;


  public PurchasedHand() {
    this.developmentCards = new ArrayList<>();
    this.nobleCards = new ArrayList<>();
  }

  //TODO: Add exception checks (if card==null, throw an exception, etc...)
  public void addDevelopmentCard(DevelopmentCard card) {
    developmentCards.add(card);
  }

  public void addNobleCard(NobleCard card) {
    nobleCards.add(card);
  }

  public void removeDevelopmentCard(DevelopmentCard card) {
    assert developmentCards.contains(card);
    developmentCards.remove(card);
  }

  public void removeNobleCard(NobleCard card) {
    assert nobleCards.contains(card);
    nobleCards.remove(card);
  }

  public int getSize() {
    return developmentCards.size();
  }

  public List<DevelopmentCard> getDevelopmentCards() {
    return Collections.unmodifiableList(developmentCards);
  }

  /**
   * Get certain development cards of a specific colour.
   *
   * @param colour of development cards you want to retrieve.
   * @return List of development cards of the colour.
   */
  public List<DevelopmentCard> getOneColourDevelopmentCards(Colour colour) {
    List<DevelopmentCard> result = new ArrayList<>();
    for (DevelopmentCard card : developmentCards) {
      if (card.getGemColour() == colour) {
        result.add(card);
      }
    }
    return result;
  }


  /**
   * Get the total number of dev cards in hand (paired cards count as 2).
   *
   * @return int
   */
  public int getTotalCardCount() {
    List<DevelopmentCard> pairedCards = developmentCards.stream()
        .filter(DevelopmentCard::isPaired)
        .collect(Collectors.toList());

    return pairedCards.size() + developmentCards.size();
  }

  /**
   * Count the number of Gems of a certain colour present in the PurchasedHand.
   *
   * @param colour The colour of gems you want to count.
   * @return int of number of gems found of this colour
   */
  public int getGemCountOfColour(Colour colour) {
    List<DevelopmentCard> cards =
        this.getOneColourDevelopmentCards(colour);

    int gemCount = 0;
    for (DevelopmentCard card : cards) {
      gemCount += card.getGemNumber();
    }

    return gemCount;
  }

  public List<NobleCard> getNobleCards() {
    return Collections.unmodifiableList(nobleCards);
  }


}
