package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Objects;

/**
 * This class represents the SuperClass of all Cards/nobles.
 */
public abstract class Card {

  private final int prestigePoints;
  private final EnumMap<Colour, Integer> price;
  private final String cardName;

  /**
   * * params include prestige points, price and name.
   */
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
    if (o == this) {
      return true;
    }

    if (!(o instanceof Card)) {
      return false;
    }

    Card other = (Card) o;

    return this.cardName.equals(other.cardName) &&
        this.prestigePoints == other.prestigePoints &&
        this.price.equals(other.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prestigePoints, cardName, price.hashCode());
  }

}
