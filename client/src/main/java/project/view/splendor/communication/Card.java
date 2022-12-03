package project.view.splendor.communication;

import java.util.EnumMap;
import project.view.splendor.Colour;

public class Card {

  private final int prestigePoints;
  private final EnumMap<Colour, Integer> price;
  private final String cardName;

  public Card(int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice, String paramCardName) {
    prestigePoints = paramPrestigePoints;
    price = paramPrice;
    cardName = paramCardName;
  }

  public int getPrestigePoints() {

    return prestigePoints;
  }

  public EnumMap<Colour, Integer> getPrice() {
    return price;
  }

  public String getCardName() {
    return cardName;
  }

  // Overriding card equals
  @Override
  public boolean equals(Object o) {
    boolean name = true;
    boolean preprestigePoint = true;
    boolean price = true;
    Card card = (Card) o;

    if (!this.getCardName().equals(card.getCardName())) {
      name = false;
    } else if (this.getPrestigePoints() != card.getPrestigePoints()) {
      preprestigePoint = false;
    }
    for (Colour i : Colour.values()) {
      if (this.getPrice().get(i) != card.getPrice().get(i)) {
        price = false;
      }
    }
    return (name && preprestigePoint && price);
  }

}