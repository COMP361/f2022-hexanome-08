package project.view.splendor.communication;

import java.util.EnumMap;

public class ReturnTokenAction extends Action{

    private final EnumMap<Colour, Integer> tokensToReturn;
    private int extraTokenCount;

    public ReturnTokenAction(String type, EnumMap<Colour, Integer> tokensToReturn, int extraTokenCount) {
        this.tokensToReturn = tokensToReturn;
        this.extraTokenCount = extraTokenCount;
    }

    public EnumMap<Colour, Integer> getTokensToReturn() {
        return tokensToReturn;
    }

    public int getExtraTokenCount() {
        return extraTokenCount;
    }

    public void setExtraTokenCount(int extraTokenCount) {
        this.extraTokenCount = extraTokenCount;
    }
}
