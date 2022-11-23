package ca.group8.gameservice.splendorgame.model;

import java.util.ArrayList;
import java.util.List;

public class PurchasedHand {

    private List<DevelopmentCard> developmentCards = new ArrayList<>();
    private List<NobleCard> nobleCards = new ArrayList<>();

    public PurchasedHand(List<DevelopmentCard> developmentCards, List<NobleCard> nobleCards) {
        this.developmentCards = developmentCards;
        this.nobleCards = nobleCards;
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

    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public List<NobleCard> getNobleCards() {
        return nobleCards;
    }


}
