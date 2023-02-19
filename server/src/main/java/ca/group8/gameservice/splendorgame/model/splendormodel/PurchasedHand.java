package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

  public List<NobleCard> getNobleCards() {
    return Collections.unmodifiableList(nobleCards);
  }


}
