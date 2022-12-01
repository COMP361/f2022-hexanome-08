package project.view.splendor.communication;

import java.util.ArrayList;
import java.util.List;

public class PurchasedHand {

    private List<DevelopmentCard> developmentCards;
    private List<NobleCard> nobleCards;


    public PurchasedHand() {
        this.developmentCards = new ArrayList<>();
        this.nobleCards = new ArrayList<>();
    }
    public void addDevelopmentCard(DevelopmentCard card){
        developmentCards.add(card);
    }

    public void addNobleCard(NobleCard card){
        nobleCards.add(card);
    }

    public void removeDevelopmentCard(DevelopmentCard card){
        developmentCards.remove(card);
    }

    public int getSize(){
        return developmentCards.size();
    }

    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public List<NobleCard> getNobleCards() {
        return nobleCards;
    }


}
