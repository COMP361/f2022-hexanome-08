package backend;

import java.util.ArrayList;
import java.util.List;

public class developmentCard implements Card{
    String cardID = "";
    Integer prestigePoint = 0;
    List<Integer>  price = new ArrayList<>();
    Integer level = 0;
    Integer gemNumber = 0;
    GemColour gemColour;
    Boolean linkable = true;

    @Override
    public void setCardID(String aID) {
        this.cardID = aID;
    }

    @Override
    public void setPrestigePoint(Integer aPrestigePoint) {
        this.prestigePoint = aPrestigePoint;
    }

/*    @Override
    public void setPrice(List<Integer> aPrice) {
        this.price = aPrice;
    }*/


    public void setLevel(int aLevel) {
        this.level = aLevel;
    }

    public void setGemNumber(int aGemNumber) {
        this.gemNumber = aGemNumber;
    }

    public void setGemColour(GemColour aGemColour) {
        gemColour = aGemColour;
    }

    public void setLinkable(Boolean linkable) {
        this.linkable = linkable;
    }

    @Override
    public String getCardID() {
        return this.cardID;
    }

    @Override
    public Integer getPrestigePoint() {
        return this.prestigePoint;
    }

/*    @Override
    public List<Integer> getPrice() {
        return this.price;
    }*/

    public Boolean getLinkable() {
        return linkable;
    }

    public GemColour getGemColour() {
        return gemColour;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getGemNumber() {
        return gemNumber;
    }



}
