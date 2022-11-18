package backend;

import java.util.ArrayList;
import java.util.List;

public interface Card {
    String cardID = "";
    Integer prestigePoint = 0;
    //List<Integer> price = new ArrayList<>();

    void setCardID(String aID);
    void setPrestigePoint(Integer aPrestigePoint);
    //void setPrice(List<Integer> aPrice);
    String getCardID();
    Integer getPrestigePoint();
    //List<Integer> getPrice();
}
