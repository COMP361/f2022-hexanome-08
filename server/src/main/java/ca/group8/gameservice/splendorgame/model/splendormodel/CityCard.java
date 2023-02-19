package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;

/**
 * Class that holds info about CityBoard.
 */
public class CityCard extends Card {

  private final int anyColourCount;

  public CityCard(int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice,
                  String paramCardName, int anyColourCount) {
    super(paramPrestigePoints, paramPrice, paramCardName);
    this.anyColourCount = anyColourCount;
  }

  public int getAnyColourCount() {
    return anyColourCount;
  }


}
