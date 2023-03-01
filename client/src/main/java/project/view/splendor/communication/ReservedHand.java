package project.view.splendor.communication;

import java.util.ArrayList;
import java.util.List;

public class ReservedHand {
  private final List<DevelopmentCard> developmentCards;
  private final List<NobleCard> nobleCards;
  public ReservedHand(List<DevelopmentCard> developmentCards, List<NobleCard> nobleCards) {
    this.developmentCards = developmentCards;
    this.nobleCards = nobleCards;
  }

  public List<DevelopmentCard> getDevelopmentCards() {
    return developmentCards;
  }

  public List<NobleCard> getNobleCards() {
    return nobleCards;
  }


}
