package backend;

import java.util.ArrayList;

public class developmentCardInitialize {
    ArrayList<developmentCard> developmentCardlevel1 = new ArrayList<>(40);
    ArrayList<developmentCard> developmentCardlevel2 = new ArrayList<>(30);
    ArrayList<developmentCard> developmentCardlevel3 = new ArrayList<>(20);



    public void developmentCardInitializelevel1() {
        developmentCard b1 = new developmentCard();
        b1.setCardID("b1_3");
        b1.setGemColour(GemColour.BLACK);
        b1.setGemNumber(1);
        b1.setLevel(3);
        b1.setLinkable(true);
        b1.setPrestigePoint(3);
        ArrayList<Integer> price = new ArrayList<>(5);

    }
}
