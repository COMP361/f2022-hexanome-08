package project.view.splendor.communication;

import java.util.EnumMap;

public class CityCard extends Card {
    private final int anyColourCount;

    public CityCard(int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice,
                    String paramCardName, int anyColourCount) {
        super(paramPrestigePoints, paramPrice, paramCardName);
        super.type = this.getClass().getSimpleName();
        this.anyColourCount = anyColourCount;
    }

    public int getAnyColourCount() {
        return anyColourCount;
    }


}
