
package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Optional;

public class Card {

    private int prestigePoints;
    private EnumMap<Colour,Integer> price;
    private String cardName;

    public Card(int paramPrestigePoints, EnumMap<Colour,Integer> paramPrice, String paramCardName){
        prestigePoints = paramPrestigePoints;
        price= paramPrice;
        cardName=paramCardName;
    }

    public int getPrestigePoints() {

        return prestigePoints;
    }

    public EnumMap<Colour, Integer> getPrice() {
        return price;
    }

    public String getCardName() {
        return cardName;
    }

    // Overriding card equals
    @Override
    public boolean equals(Object o) {
        boolean name = true;
        boolean preprestigePoint = true;
        boolean price = true;
        Card card = (Card) o;

        if (!this.getCardName().equals(card.getCardName())) {
            name = false;
        } else if (this.getPrestigePoints() != card.getPrestigePoints()) {
            preprestigePoint = false;
        }
        for (Colour i : Colour.values()) {
            if (this.getPrice().get(i) != card.getPrice().get(i)) {
                price = false;
            }
        }
        return (name && preprestigePoint && price);
    }

}
