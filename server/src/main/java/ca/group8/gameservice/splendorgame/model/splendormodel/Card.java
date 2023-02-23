package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.controller.splendorlogic.CardExtraAction;
import ca.group8.gameservice.splendorgame.controller.splendorlogic.ClaimNobleAction;
import ca.group8.gameservice.splendorgame.controller.splendorlogic.PowerExtraAction;
import ca.group8.gameservice.splendorgame.controller.splendorlogic.PurchaseAction;
import ca.group8.gameservice.splendorgame.controller.splendorlogic.ReserveAction;
import ca.group8.gameservice.splendorgame.controller.splendorlogic.TakeTokenAction;
import ca.group8.gameservice.splendorgame.controller.splendorlogic.UpdateReturnTokenAction;
import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;
import java.util.EnumMap;
import java.util.Objects;

/**
 * This class represents the SuperClass of all Cards/nobles.
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

  String type;
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
