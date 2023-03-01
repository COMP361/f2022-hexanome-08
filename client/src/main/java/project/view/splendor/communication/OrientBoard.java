package project.view.splendor.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrientBoard extends Board {
    private final Map<Integer, List<DevelopmentCard>> decks = new HashMap<>();

    private final Map<Integer, DevelopmentCard[]> cardsOnBoard = new HashMap<>();

    public OrientBoard(String type) {
        super(type);
    }

    public Map<Integer, List<DevelopmentCard>> getDecks() {
        return decks;
    }



    public Map<Integer, DevelopmentCard[]> getCardsOnBoard() {
        return cardsOnBoard;
    }


}
