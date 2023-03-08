package ca.group8.gameservice.splendorgame.model.splendormodel;

import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;
import java.util.EnumMap;
import java.util.Objects;

/**
 * This class represents the SuperClass of all Cards/nobles.
 * <p>
 * Every abstract class was serialized/deserialized using the repository from:
 * * https://medium.com/@iliamsharipov_56660/handling-polymorphism-with-gson-f4a702014ffe.
 * * Thank him so much!!!!!!!!!!!!!!!
 */

@JsonType(
    property = "type",
    subtypes = {
        @JsonSubtype(clazz = CityCard.class, name = "CityCard"),
        @JsonSubtype(clazz = DevelopmentCard.class, name = "DevelopmentCard"),
        @JsonSubtype(clazz = NobleCard.class, name = "NobleCard")
    }
)
public abstract class Card {

  private final int prestigePoints;
  private final EnumMap<Colour, Integer> price;
  private final String cardName;
  String type;

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
