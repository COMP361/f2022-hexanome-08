package backend;

import java.util.ArrayList;

public class DevelopmentCardInitialize {
    ArrayList<DevelopmentCard> developmentCardlevel1 = new ArrayList<>(40);
    ArrayList<DevelopmentCard> developmentCardlevel2 = new ArrayList<>(30);
    ArrayList<DevelopmentCard> developmentCardlevel3 = new ArrayList<>(20);



    public void developmentCardInitializelevel1() {
        DevelopmentCard b1 = new DevelopmentCard();
        b1.setCardID("b1_3");
        b1.setGemColour(GemColour.BLACK);
        b1.setGemNumber(1);
        b1.setLevel(3);
        b1.setLinkable(true);
        b1.setPrestigePoint(3);
        ArrayList<Integer> price = new ArrayList<>(5);

    }
}
