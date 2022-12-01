package ca.group8.gameservice.splendorgame.controller.splendorlogic;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Makes list of all possible actions for current player.
 */
@Component
public class SplendorActionListGenerator {

  // This is a Singleton Component Class that's used to control all action map for all players
  // for all games. Once provided a gameId and a playerName, we can identify a unique
  // Map<String (MD5 hashed Action objs), Action> to such player in that specific gam


  // 1. Long: gameId
  // 2. String: playerName
  // 3. String: MD5 hashed Action object

  // In GET request, 1 and 2 will be provided from the request path variable
  // but 3 will be generated by interpreting the access_token sent from client
  // so that we know what's the KEY to put in the Map<playerName, Map<String,Action>>


  // In any POST request, 1,2 and 3 will be provided, we can just use this
  // nested 3 level map to find the corresponding ONE specific Action and call execute() on it.

  private final Map<Long, Map<String, Map<String, Action>>> actionLookUpMap;

  public SplendorActionListGenerator() {
    this.actionLookUpMap = new HashMap<>();
  }

  /**
   * 1. create a new map -- DONE
   * - get player wealth
   * - get flag
   * 2. Get list of cards from Base Board (baseBoardCards)
   * 3. Iterate through baseBoardCards -->
   * 3a. call getPosition() on base board object, and pass in the card
   * 3b. verify if you can purchase card --> If yes, create purchase card action [PARAM: WEALTH]
   * 3c. verify if you can reserve card
   * 4. return the map
   */
  private static List<Action> cardsToActions(GameInfo gameInfo, PlayerInGame player) {
    ArrayList<Action> actionOptions = new ArrayList<>();
    EnumMap<Colour, Integer> wealth = player.getWealth();
    boolean canReserve = !player.getReservedHand().isFull();
    Board baseBoard = gameInfo.getTableTop().getBaseBoard();
    List<Card> baseBoardCards = baseBoard.getCards();

    Logger logger = LoggerFactory.getLogger(SplendorActionListGenerator.class);
    logger.info("Baseboad card 1: " + baseBoardCards.get(0));
    logger.info("Size of the board cards: " + baseBoardCards.size());

    for (Card card : baseBoardCards) {
      Position position = baseBoard.getCardPosition(card);
      //start of purchase card verification
      //this creates a goldCounter, to see if gold tokens are needed
      int goldCounter = 0;

      logger.info("Card prices: " + card.getPrice());
      EnumMap<Colour, Integer> cardPrice = card.getPrice();
      for (Colour col : Colour.values()) {
        // TODO: Gold is not part of the card price!!!!!!!!!!!!!!!!!!
        //  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (col.equals(Colour.GOLD)) {
          continue;
        }
        if (cardPrice.get(col) != 0) {
          if (cardPrice.get(col) > wealth.get(col)) {
            goldCounter += cardPrice.get(col) - wealth.get(col);
          }
        }
      }

      //checks if you can purchase (with or without gold tokens)
      if (goldCounter <= player.getTokenHand().getGoldTokenNumber()) {
        //create new purchase action option & add it to the actionList.
        actionOptions.add(new PurchaseAction(true, position, card, goldCounter));

      }

      //verify if player can reserve card
      if (canReserve) {
        actionOptions.add(new ReserveAction(true, position, card));
      }
    }

    return actionOptions;
  }

  /**
   * Generate the hash -> Actions map provided: gameId, playerName (implicitly in PlayerInGame).
   * will be called everytime GET games/{gameId}/players/{playerName}/actions
   * will replace the previous Action Map every time
   */
  public void generateActions(long gameId, GameInfo gameInfo, PlayerInGame player) {

    // TODO: Player Identity will be verified before calling generateActions with access_token
    //  no need to check it here (we can safely assume player is valid before calling this)

    //if(!(gameInfo.getCurrentPlayer().getName().equals(player.getName()))){
    //    return new HashMap<>();
    //}

    //if(!isParticipant(gameInfo,player)){
    //    return new HashMap<>();
    //}

    String curPlayerName = gameInfo.getCurrentPlayer().getName();
    String askedActionsPlayerName = player.getName();
    Map<String, Action> hashActionMap = new HashMap<>();
    if ((!gameInfo.isFinished()) && (curPlayerName.equals(askedActionsPlayerName))) {
      // only generate the actions if the game is NOT finished and
      // the player asked for actions IS the current turn player

      // adding cardActions
      for (Action action : cardsToActions(gameInfo, player)) {
        String actionMd5 = DigestUtils.md5Hex(new Gson().toJson(action)).toUpperCase();
        hashActionMap.put(actionMd5, action);
      }
      EnumMap<Colour, Integer> playerTokens = player.getTokenHand().getAllTokens();
      TakeTokenAction takeTokenAction = new TakeTokenAction(false, playerTokens);
      String takeTokenActionMd5 =
          DigestUtils.md5Hex(new Gson().toJson(takeTokenAction)).toUpperCase();

      // add the take token actions (card unrelated actions)
      hashActionMap.put(takeTokenActionMd5, takeTokenAction);
    }

    // once the hash -> Action map is ready, we add it for this specific player
    Map<String, Map<String, Action>> playerSpecificActionsMap = new HashMap<>();
    playerSpecificActionsMap.put(askedActionsPlayerName, hashActionMap);
    this.actionLookUpMap.put(gameId, playerSpecificActionsMap);
  }

  /**
   * Find the (potentially, might be empty map) previously generated hash -> Action map
   * when receive POST request on games/{gameId}/players/{playerName}/actions/{actionId}
   * first call this method to find the map, then with {actionId} provided, we can find the
   * right Action to execute.
   */
  public Map<String, Action> lookUpActions(long gameId, String playerName) {
    // whether player is in the game or not will be checked in RestController class
    // if this is an empty map, then there is no need to look up actionMd5, just reply
    // with a bad_request
    return this.actionLookUpMap.get(gameId).get(playerName);
  }

}