package project.view.splendor.communication;

import java.util.EnumMap;
import project.view.splendor.Colour;

public class BaseCard extends DevelopmentCard {


  public BaseCard(int paramPrestigePoints,
                  EnumMap<Colour, Integer> paramPrice, String cardName, int level,
                  Colour gemColour, boolean isPaired, String pairedCardId, int gemNumber) {
    super(paramPrestigePoints, paramPrice, cardName, level,
        gemColour, isPaired, pairedCardId, gemNumber);

  }

}
