package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class OrientCard extends DevelopmentCard{
    private OrientType type;

    public OrientCard(int paramPrestigePoints,
                    EnumMap<Colour, Integer> paramPrice, String cardName, int level,
                    Optional<Colour> gemColor, boolean isPaired, String pairedCardId, int gemNumber, OrientType paramType) {
        super(paramPrestigePoints, paramPrice, cardName, level,
                gemColor, isPaired, pairedCardId, gemNumber);
        type = paramType;
    }

    public OrientType getType() {
        return type;
    }
}
