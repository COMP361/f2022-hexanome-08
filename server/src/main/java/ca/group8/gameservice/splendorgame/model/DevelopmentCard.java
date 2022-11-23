
package ca.group8.gameservice.splendorgame.model;

import java.util.EnumMap;
import java.util.Optional;

public class DevelopmentCard extends Card{
    public int level;
    public Optional<Colour> GemColor;
    public boolean isPaired;
    public int pairedCardId;

    public DevelopmentCard(int paramCardId, int paramPrestigePoints,
        EnumMap<Colour, Integer> paramPrice) {
        super(paramCardId, paramPrestigePoints, paramPrice);
    }

}
