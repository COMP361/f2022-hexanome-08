package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
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

  public void addDevelopmentCard(DevelopmentCard card) {
    developmentCards.add(card);
  }

  public void addNobleCard(NobleCard card) {
    nobleCards.add(card);
  }

  public void removeDevelopmentCard(DevelopmentCard card) {
    developmentCards.remove(card);
  }

  public int getSize() {
    return developmentCards.size();
  }

  public List<DevelopmentCard> getDevelopmentCards() {
    return developmentCards;
  }

  public List<NobleCard> getNobleCards() {
    return nobleCards;
  }


}
