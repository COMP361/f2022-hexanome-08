package project.view.splendor.communication;


import java.util.EnumMap;


public class NobleCard extends Card {

  public NobleCard(int paramPrestigePoints,
                   EnumMap<Colour, Integer> paramPrice, String paramCardName) {
    super(paramPrestigePoints, paramPrice, paramCardName);
    super.type = this.getClass().getSimpleName();

  }
}

