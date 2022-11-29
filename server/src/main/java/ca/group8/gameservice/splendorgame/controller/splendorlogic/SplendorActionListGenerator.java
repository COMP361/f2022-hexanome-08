package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.Action;
import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import ca.group8.gameservice.splendorgame.model.splendormodel.Board;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;

public class SplendorActionListGenerator {

    private static boolean isParticipant(GameInfo gameInfo, PlayerInGame player) {
        return gameInfo.getActivePlayers().contains(player);
    }

    /**
     1. create a new map -- DONE
     - get player wealth
     - get flag
     2. Get list of cards from Base Board (baseBoardCards)
     3. Iterate through baseBoardCards -->
     3a. call getPosition() on base board object, and pass in the card
     3b. verify if you can purchase card --> If yes, create purchase card action [PARAM: WEALTH]
     3c. verify if you can reserve card
     4. return the map
     */
    private static List<Action> cardsToActions(GameInfo gameInfo, PlayerInGame player){
        ArrayList<Action> actionOptions = new ArrayList<>();
        EnumMap<Colour, Integer> wealth = player.getWealth();
        Boolean canReserve = !player.getReservedHand().isFull();
        Board baseBoard = gameInfo.getTableTop().getBaseBoard();
        List<Card> baseBoardCards = baseBoard.getCards();

        for (Card card : baseBoardCards) {
            Position position = baseBoard.getCardPosition(card);
            //start of purchase card verification

            //this creates a goldCounter, to see if gold tokens are needed
            int goldCounter = 0;
            EnumMap<Colour, Integer> cardPrice = card.getPrice();
            for(Colour col : Colour.values()){
                if(cardPrice.get(col) != 0){
                    if(cardPrice.get(col) > wealth.get(col)){
                        goldCounter += cardPrice.get(col) - wealth.get(col);
                    }
                }
            }

            //checks if you can purchase (with or without gold tokens)
            if (goldCounter<=player.getTokenHand().getGoldTokenNumber()) {
                //create new purchase action option & add it to the actionList.
                actionOptions.add(new SplendorPurchaseAction(player, gameInfo, position, card, goldCounter));

            }

            //verify if player can reserve card
            if (canReserve) {
                actionOptions.add(new SplendorReserveAction(player, gameInfo, position, card));
            }


        }



        return actionOptions;
    }

    public Map<String,Action> generateActions(GameInfo gameInfo, PlayerInGame player){
        if(isParticipant(gameInfo,player)){
            return new HashMap<>();
        }
        if(gameInfo.isFinished()){
            return new HashMap<>();
        }
        if(!(gameInfo.getCurrentPlayer().getName() == player.getName())){
            return new HashMap<>();
        }

        Map<String,Action> actions = new HashMap<>();
        for(Action action : cardsToActions(gameInfo,player)){
            String actionMd5 = DigestUtils.md5Hex(new Gson().toJson(action)).toUpperCase();
            actions.put(actionMd5, action);
        }
        SplendorTakeTokenAction takeTokenAction = new SplendorTakeTokenAction(player, gameInfo);
        String takeTokenActionMd5 = DigestUtils.md5Hex(new Gson().toJson(takeTokenAction)).toUpperCase();
        actions.put(takeTokenActionMd5,takeTokenAction);
        return actions;
    }



}