<<<<<<< HEAD:server/src/main/java/ca/group8/gameservice/splendorgame/model/NobleCard.java
package ca.group8.gameservice.splendorgame.model;
=======
package ca.group8.gameservice.splendorgame.model.splendormodel;
>>>>>>> 4b57e25d4d17bf41302b9651de6963d019761607:server/src/main/java/ca/group8/gameservice/splendorgame/model/splendormodel/NobleCard.java

import java.util.EnumMap;

<<<<<<< HEAD:server/src/main/java/ca/group8/gameservice/splendorgame/model/NobleCard.java
public class NobleCard extends Card{
=======
/**
 * Nobles.
 */
public class NobleCard implements Card {
    private final int cardId;
    private final int prestigePoints;
    private final EnumMap<Colour, Integer> price;

    /**
     * Constructor.
     */
    public NobleCard(int parId, int parPrestige, EnumMap<Colour,Integer> parPrice){
        cardId = parId;
        prestigePoints = parPrestige;
        price = parPrice;
    }
>>>>>>> 4b57e25d4d17bf41302b9651de6963d019761607:server/src/main/java/ca/group8/gameservice/splendorgame/model/splendormodel/NobleCard.java

    public NobleCard(int parId, int parPrestige, EnumMap<Colour,Integer> parPrice){
        super( parId, parPrestige, parPrice);
    }

}
