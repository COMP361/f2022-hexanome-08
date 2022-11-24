package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.Action;
import ca.group8.gameservice.splendorgame.model.PlayerReadOnly;
import ca.group8.gameservice.splendorgame.model.splendormodel.Board;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.Player;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplendorActionListGenerator {

  private static boolean isParticipant(GameInfo gameInfo, Player player) {
    return gameInfo.getActivePlayers().contains(player);
  }

//  private static Map<String, Action> emptyCellsToActions(XoxBoard board, PlayerReadOnly player) throws LogicException {
//    Map<String, Action> actionMap = new LinkedHashMap();
//
//    // Iterate over board
//    for (int yPos = 0; yPos < 3; yPos++) {
//      for (int xPos = 0; xPos < 3; xPos++) {
//        // Add an action if the position is free
//        if (board.isFree(xPos, yPos)) {
//          Action action = new XoxClaimFieldAction(xPos, yPos, player);
//          String actionMd5 = DigestUtils.md5Hex(new Gson().toJson(action)).toUpperCase();
//          actionMap.put(actionMd5, action);
//        }
//      }
//    }
//    return actionMap;
//  }

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
  private static List<Action> cardsToActions(GameInfo gameInfo, Player player){
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
        actionOptions.add(new SplendorPurchaseAction(player, gameInfo, position, card));

      }

      //verify if player can reserve card
      if (canReserve) {
        actionOptions.add(new SplendorReserveAction(player, gameInfo, position, card));
      }


    }



    return actionOptions;
  }



//  public Map<String, Action> generateActions(Game game, PlayerReadOnly player) throws LogicException {
//
//    // Verify and cast the game type
//    if (game.getClass() != XoxGame.class)
//      throw new LogicException("Xox Action Generator can only handle Xox games.");
//    XoxGame xoxGame = (XoxGame) game;
//
//    // Non participants (observers) always receive an empty action bundle.
//    if (player == null || !isParticipant(xoxGame, player))
//      return new LinkedHashMap<>();
//
//    // If the game is already over, return an empty set
//    if (xoxGame.isFinished())
//      return new LinkedHashMap<>();
//
//    // If not the player's turn, return an empty set. (Check is performed by comparing the name of the current player)
//    if (!player.getName().toLowerCase().equals(xoxGame.getCurrentPlayerName().toLowerCase()))
//      return new LinkedHashMap<>();
//
//    // Iterate over board and add an action for every unoccupied cell.
//    return emptyCellsToActions(xoxGame.getBoard(), player);
//  }

  public Map<String,Action> generateActions(GameInfo gameInfo, Player player){
    if(isParticipant(gameInfo,player)){
      return new HashMap<>();
    }
    if(gameInfo.isFinished()){
      return new HashMap<>();
    }
    if(!(gameInfo.getCurrentPlayer() == player.getName())){
      return new HashMap<>();
    }

    Map<String,Action> actions = new HashMap<>();
    for(Action action : cardsToActions(gameInfo,player)){
      actions.put(Integer.toString(action.hashCode()),action);
    }
    SplendorTakeTokenAction takeTokenAction = new SplendorTakeTokenAction(player, gameInfo);
    actions.put(Integer.toString(takeTokenAction.hashCode()),takeTokenAction);



    return actions;
  }



}