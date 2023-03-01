package project.view.splendor.communication;


import java.util.EnumMap;

public abstract class Card {

  String type;
  private final int prestigePoints;
  private final EnumMap<Colour, Integer> price;
  private final String cardName;
  public Card(int prestigePoints, EnumMap<Colour, Integer> price, String cardName) {
    this.prestigePoints = prestigePoints;
    this.price = price;
    this.cardName = cardName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

}
