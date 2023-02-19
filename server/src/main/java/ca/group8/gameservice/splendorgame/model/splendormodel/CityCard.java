package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

public class CityCard extends Card {

  private final int anyColourCount;

  /**
   * * params include prestige points, price and name.
   *
   * @param paramPrestigePoints
   * @param paramPrice
   * @param paramCardName
   */
  public CityCard(int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice,
                  String paramCardName, int anyColourCount) {
    super(paramPrestigePoints, paramPrice, paramCardName);
    this.anyColourCount = anyColourCount;
  }

  /**
   *
   * @return get the number of any colour dev card price for the city card
   */
  public int getAnyColourCount() {
    return anyColourCount;
  }


}
