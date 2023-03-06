package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.SplendorDevHelper;
import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.OrientBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TraderBoard;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * Makes list of all possible actions for current player.
 */
public class ActionGenerator {

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

  //previous name of this field/the field this is replacing: actionLookUpMap
  private final Map<String, Map<String, Action>> playerActionMaps;
  private final TableTop tableTop;

  public ActionGenerator(Map<String, Map<String, Action>> playerActionMaps, TableTop tableTop) {
    this.playerActionMaps = playerActionMaps;
    this.tableTop = tableTop;
  }


  // Translate all dev cards on base/orient board to reserve actions
  private List<Action> cardsToReserveAction(BaseBoard baseBoard,
                                            OrientBoard orientBoard,
                                            PlayerInGame curPlayerInfo) {
    List<Action> result = new ArrayList<>();
    boolean canReserve = !curPlayerInfo.getReservedHand().isFull();
    if (canReserve) {
      for (int level = 1; level <= 3; level++) {
        DevelopmentCard[] baseLevelCards = baseBoard.getLevelCardsOnBoard(level);
        DevelopmentCard[] orientLevelCards = orientBoard.getLevelCardsOnBoard(level);
        // generate reserve action for the first card of deck, not poped yet
        DevelopmentCard baseCardFromDeck = baseBoard.getDecks().get(level).get(0);
        DevelopmentCard orientCardFromDeck = orientBoard.getDecks().get(level).get(0);
        // the Y index as -1 to represent the deck position
        result.add(new ReserveAction(new Position(level, -1), baseCardFromDeck));
        result.add(new ReserveAction(new Position(level, -1), orientCardFromDeck));

        for (int cardIndex = 0; cardIndex < baseLevelCards.length; cardIndex++) {
          Position cardPosition = new Position(level, cardIndex);
          DevelopmentCard card = baseLevelCards[cardIndex];
          // always generate reserve actions for base cards for index 0,1,2,3
          result.add(new ReserveAction(cardPosition, card));
          if (cardIndex < 2) {
            // if index = 0 or 1, generate reserve action for orient cards
            DevelopmentCard orientCard = orientLevelCards[cardIndex];
            result.add(new ReserveAction(cardPosition, orientCard));
          }
        }
      }
    }
    return result;
  }

  // Translate all dev cards on base/orient board to purchase actions (including the cards
  // reserved in hand)
  private List<Action> cardsToPurchaseAction(BaseBoard baseBoard,
                                             OrientBoard orientBoard,
                                             PlayerInGame curPlayerInfo) {

    EnumMap<Colour, Integer> wealth = curPlayerInfo.getWealth();
    boolean hasDoubleGoldPower = false;
    String playerName = curPlayerInfo.getName();
    if (tableTop.getGameBoards().containsKey(Extension.TRADING_POST)) {
      TraderBoard traderBoard = (TraderBoard) tableTop.getBoard(Extension.TRADING_POST);
      hasDoubleGoldPower = traderBoard.
          getPlayerOnePower(playerName, PowerEffect.DOUBLE_GOLD).isUnlocked();
    }

    EnumMap<Colour, Integer> totalGems = curPlayerInfo.getTotalGems(); // discount (dev cards)
    EnumMap<Colour, Integer> totalTokens = curPlayerInfo.getTokenHand().getAllTokens(); // tokens

    // now when we decide whether a card is affordable or not, we need to consider the effect of
    // the double gold power is on
    List<Action> result = new ArrayList<>();
    int goldTokenNeeded;
    for (int level = 1; level <= 3; level++) {
      DevelopmentCard[] baseLevelCards = baseBoard.getLevelCardsOnBoard(level);
      DevelopmentCard[] orientLevelCards = orientBoard.getLevelCardsOnBoard(level);
      for (int cardIndex = 0; cardIndex < baseLevelCards.length; cardIndex++) {
        int goldCardsNeeded = 0;
        Position cardPosition = new Position(level, cardIndex);
        DevelopmentCard card = baseLevelCards[cardIndex];
        goldTokenNeeded = card.canBeBought(hasDoubleGoldPower, wealth);
        if (goldTokenNeeded == -1) {
          continue; // this card can not be bought
        }
        // always generate reserve actions for base cards for index 0,1,2,3
        EnumMap<Colour, Integer> tokensPaid = card.getPrice();
        for (Colour c : totalGems.keySet()) {
          if (c.equals(Colour.GOLD)) {
            continue;
          }
          //calculate price after discount
          int priceAfterDiscount = tokensPaid.get(c) - totalGems.get(c);
          //if price is negative, meaning you have more gems than required, just set tokens paid to 0.
          if (priceAfterDiscount <= 0) {
            tokensPaid.put(c, 0);
          }else if (priceAfterDiscount > totalTokens.get(c)){
            tokensPaid.put(c,totalTokens.get(c));
          } else {
            tokensPaid.put(c,priceAfterDiscount);
          }
        }

        if (goldTokenNeeded > 0) {
          int goldTokensInHand = totalTokens.get(Colour.GOLD);
          if (goldTokensInHand >= goldTokenNeeded) {
            tokensPaid.put(Colour.GOLD, goldTokenNeeded);
          } else {
            tokensPaid.put(Colour.GOLD, goldTokensInHand);
            goldCardsNeeded = (int) Math.round((double) (goldTokenNeeded - goldTokensInHand)/2);
          }
        }
        result.add(new PurchaseAction(cardPosition, card, goldCardsNeeded, tokensPaid));
      }

      for (int cardIndex = 0; cardIndex < orientLevelCards.length; cardIndex++) {
        // if index = 0 or 1, generate reserve action for orient cards
        int goldCardsNeeded = 0;
        Position cardPosition = new Position(level, cardIndex);
        DevelopmentCard card = orientLevelCards[cardIndex];
        goldTokenNeeded = card.canBeBought(hasDoubleGoldPower, wealth);
        if (goldTokenNeeded == -1) {
          continue; // this card can not be bought
        }
        // always generate reserve actions for base cards for index 0,1,2,3
        EnumMap<Colour, Integer> tokensPaid = card.getPrice();
        for (Colour c : totalGems.keySet()) {
          if (c.equals(Colour.GOLD)) {
            continue;
          }
          //calculate price after discount
          int priceAfterDiscount = tokensPaid.get(c) - totalGems.get(c);
          //if price is negative, meaning you have more gems than required, just set tokens paid to 0.
          if (priceAfterDiscount <= 0) {
            tokensPaid.put(c, 0);
          }else if (priceAfterDiscount > totalTokens.get(c)){
            tokensPaid.put(c,totalTokens.get(c));
          } else {
            tokensPaid.put(c,priceAfterDiscount);
          }
        }

        if (goldTokenNeeded > 0) {
          int goldTokensInHand = totalTokens.get(Colour.GOLD);
          if (goldTokensInHand >= goldTokenNeeded) {
            tokensPaid.put(Colour.GOLD, goldTokenNeeded);
          } else {
            tokensPaid.put(Colour.GOLD, goldTokensInHand);
            goldCardsNeeded = (int) Math.round((double) (goldTokenNeeded - goldTokensInHand)/2);
          }
        }
        result.add(new PurchaseAction(cardPosition, card, goldCardsNeeded, tokensPaid));
      }
    }

    return result;
  }


  // generate all possible combinations of tokens a player can take
  private List<Action> generateTakeTokenActions(Bank bank, PlayerInGame curPlayerInfo) {
    List<Action> result = new ArrayList<>();
    boolean hasTwoPlusOnePower = false;
    String playerName = curPlayerInfo.getName();
    if (tableTop.getGameBoards().containsKey(Extension.TRADING_POST)) {
      TraderBoard traderBoard = (TraderBoard) tableTop.getBoard(Extension.TRADING_POST);
      hasTwoPlusOnePower = traderBoard.
          getPlayerOnePower(playerName, PowerEffect.TWO_PLUS_ONE).isUnlocked();
    }
    // if bank has only 2 token or less left to be taken
    int regularTokenCount = bank.getRegularTokenCount();
    if (regularTokenCount <= 2 && regularTokenCount >= 0) {
      // if so, no take token action should be provided
      return result;
    }

    EnumMap<Colour, Integer> rawMap = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    EnumMap<Colour, Integer> tokenLeft = new EnumMap<>(bank.getAllTokens());
    tokenLeft.remove(Colour.GOLD); // exclude the gold token
    tokenLeft.remove(Colour.ORIENT); // exclude the orient token
    // generate actions for the colour with remaining >= 4
    for (Colour colour : tokenLeft.keySet()) {
      EnumMap<Colour, Integer> twoSameColourTokens = new EnumMap<>(rawMap);
      if (tokenLeft.get(colour) >= 4) {
        twoSameColourTokens.put(colour, 2);
        result.add(new TakeTokenAction(twoSameColourTokens));
      }

      // generate more cases if the player has the 2+1 power on
      if (hasTwoPlusOnePower) {
        List<Colour> otherColours = tokenLeft.keySet()
            .stream()
            .filter(c -> !c.equals(colour))
            .collect(Collectors.toList());
        for (Colour colour2 : otherColours) {
          EnumMap<Colour, Integer> twoPlusOneTokens = new EnumMap<>(twoSameColourTokens);
          if (tokenLeft.get(colour2) >= 1) {
            twoPlusOneTokens.put(colour2, 1);
            result.add(new TakeTokenAction(twoPlusOneTokens));
          }
        }
      }
    }

    // all possible combination of 5 choose 3 colours set
    Set<Set<Colour>> colours = Sets.combinations(tokenLeft.keySet(), 3);
    for (Set<Colour> colourSubset : colours) {
      EnumMap<Colour, Integer> threeDiffColourTokens = new EnumMap<>(rawMap);
      List<Colour> colourList = new ArrayList<>(colourSubset);
      if (colourList.stream().allMatch(c -> tokenLeft.get(c) >= 1)) {
        threeDiffColourTokens.put(colourList.get(0), 1);
        threeDiffColourTokens.put(colourList.get(1), 1);
        threeDiffColourTokens.put(colourList.get(2), 1);
      }
      result.add(new TakeTokenAction(threeDiffColourTokens));
    }

    return result;
  }

  /**
   * Set up the initial actions for curPlayerInfo.
   * PurchaseActions, ReserveAction and TakeTokenAction
   *
   * @param curPlayerInfo current player's associated player info
   */
  public void setInitialActions(PlayerInGame curPlayerInfo, String curTurnPlayerName) {
    if (!curTurnPlayerName.equals(curPlayerInfo.getName())) {
      String playerName = curPlayerInfo.getName();
      // clean up the action map if it's not your turn
      playerActionMaps.put(playerName, new HashMap<>());
      return; // skipped checking the rest
    }

    // we know by default, orient and base are always on the table
    BaseBoard baseBoard = (BaseBoard) tableTop.getBoard(Extension.BASE);
    OrientBoard orientBoard = (OrientBoard) tableTop.getBoard(Extension.ORIENT);
    Bank bank = tableTop.getBank();
    // generate the initial purchase, reserve and take token actions
    // with all combinations and considered power cases
    List<Action> allActions =
        new ArrayList<>(cardsToReserveAction(baseBoard, orientBoard, curPlayerInfo));
    allActions.addAll(cardsToPurchaseAction(baseBoard, orientBoard, curPlayerInfo));
    allActions.addAll(generateTakeTokenActions(bank, curPlayerInfo));
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    Map<String, Action> curActionMap = new HashMap<>();
    for (Action action : allActions) {
      String actionJson = gsonParser.toJson(action).toUpperCase();
      String actionId = DigestUtils.md5Hex(actionJson);
      curActionMap.put(actionId, action);
    }
    String playerName = curPlayerInfo.getName();
    playerActionMaps.put(playerName, curActionMap);

  }

  //TODO
  public void updateCascadeActions(PlayerInGame playerInGame, DevelopmentCard purchasedCard,
                                   CardEffect cardEffect) {
    List<Action> cascadeActions = new ArrayList<>();
    // update player's cascade actions according to the card effect of the card they purchased
    if (cardEffect.equals(CardEffect.RESERVE_NOBLE)) {
      List<NobleCard> nobleCards = ((BaseBoard) tableTop.getBoard(Extension.BASE)).getNobles();
      for (int i = 0; i < nobleCards.size(); i++) {
        Position position = new Position(0, i);
        NobleCard nobleCard = nobleCards.get(i);
        cascadeActions.add(new CardExtraAction(nobleCard, cardEffect, position));
      }
    }

    if (cardEffect.equals(CardEffect.FREE_CARD)) {
      int freeLevel = purchasedCard.getLevel() - 1;
      BaseBoard baseBoard = (BaseBoard) tableTop.getBoard(Extension.BASE);
      OrientBoard orientBoard = (OrientBoard) tableTop.getBoard(Extension.ORIENT);
      DevelopmentCard[] baseCardsToFree = baseBoard.getLevelCardsOnBoard(freeLevel);
      for (int i = 0; i < baseCardsToFree.length; i++) {
        Position position = new Position(freeLevel, i);
        DevelopmentCard curCard = baseCardsToFree[i];
        cascadeActions.add(new CardExtraAction(curCard, cardEffect, position));
      }

      DevelopmentCard[] orientCardsToFree = orientBoard.getLevelCardsOnBoard(freeLevel);
      for (int i = 0; i < orientCardsToFree.length; i++) {
        Position position = new Position(freeLevel, i);
        DevelopmentCard curCard = orientCardsToFree[i];
        cascadeActions.add(new CardExtraAction(curCard, cardEffect, position));
      }
    }

    if (cardEffect.equals(CardEffect.BURN_CARD)) {
      List<DevelopmentCard> cardsInHand = playerInGame.getPurchasedHand().getDevelopmentCards();
      Colour burnColourPrice = null;
      EnumMap<Colour, Integer> cardPrice = purchasedCard.getPrice();
      for (Colour colour : cardPrice.keySet()) {
        if (cardPrice.get(colour) > 0) {
          burnColourPrice = colour;
          break;
        }
      }
      // iterate to find the card in player's hand to find which card colour to burn
      Colour finalBurnColourPrice = burnColourPrice;
      List<Integer> pairedCardIndices = IntStream.range(0, cardsInHand.size())
          .filter(i -> cardsInHand.get(i).isPaired())
          .filter(i -> cardsInHand.get(i).getGemColour().equals(finalBurnColourPrice))
          .boxed()
          .collect(Collectors.toList());

      // if there is no paired card to use
      if (pairedCardIndices.size() == 0) {
        List<Integer> sameColourCardsIndices = IntStream.range(0, cardsInHand.size())
            .filter(i -> cardsInHand.get(i).getGemColour().equals(finalBurnColourPrice))
            .boxed()
            .collect(Collectors.toList());

        for (int i : sameColourCardsIndices) {
          Position position = new Position(0, i);
          DevelopmentCard card = cardsInHand.get(i);
          cascadeActions.add(new CardExtraAction(card, cardEffect, position));
        }
      }
      // if there is 1 or more than 1 paired card to use
      if (pairedCardIndices.size() >= 1) {
        for (int i : pairedCardIndices) {
          Position position = new Position(0, i);
          DevelopmentCard card = cardsInHand.get(i);
          cascadeActions.add(new CardExtraAction(card, cardEffect, position));
        }
      }
    }

    if (cardEffect.equals(CardEffect.SATCHEL)) {
      List<DevelopmentCard> cardsInHand = playerInGame.getPurchasedHand().getDevelopmentCards();
      List<Integer> unpairedCardsIndices = IntStream.range(0, cardsInHand.size())
          .filter(i -> !cardsInHand.get(i).isPaired())
          .boxed()
          .collect(Collectors.toList());
      for (int i : unpairedCardsIndices) {
        Position position = new Position(0, i);
        DevelopmentCard card = cardsInHand.get(i);
        cascadeActions.add(new CardExtraAction(card, cardEffect, position));
      }

    }

    Map<String, Action> actionMap = new HashMap<>();
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    for (Action action : cascadeActions) {
      String actionJson = gsonParser.toJson(action, CardExtraAction.class);
      String actionId = DigestUtils.md5Hex(actionJson).toUpperCase();
      actionMap.put(actionId, action);
    }

    String playerName = playerInGame.getName();
    playerActionMaps.put(playerName, actionMap);
  }


  public void updateBonusTokenPowerActions(PlayerInGame player) {
    List<Action> actions = new ArrayList<>();
    EnumMap<Colour, Integer> rawMap = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    EnumMap<Colour,Integer> bankTokens = tableTop.getBank().getAllTokens();
    for(Colour colour: rawMap.keySet()){
      if(bankTokens.get(colour)>0){
        actions.add(new BonusTokenPowerAction(player, colour));
      }
    }
    Map<String, Action> actionMap = new HashMap<>();
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    for (Action action : actions) {
      String actionJson = gsonParser.toJson(action, BonusTokenPowerAction.class);
      String actionId = DigestUtils.md5Hex(actionJson).toUpperCase();
      actionMap.put(actionId, action);
    }
    String playerName = player.getName();
    playerActionMaps.put(playerName, actionMap);
  }

  /**
   * Update the actions of which noble to claim.
   *
   * @param nobleIndices
   * @param playerInGame
   */
  public void updateClaimNobleActions(List<Integer> nobleIndices, PlayerInGame playerInGame) {
    BaseBoard baseBoard = (BaseBoard) tableTop.getBoard(Extension.BASE);
    List<Action> result = new ArrayList<>();
    for (int i : nobleIndices) {
      NobleCard nobleCard = baseBoard.getNobles().get(i);
      Position noblePosition = new Position(0, i);
      result.add(new ClaimNobleAction(nobleCard, noblePosition));
    }

    Map<String, Action> actionMap = new HashMap<>();
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    for (Action action : result) {
      String actionJson = gsonParser.toJson(action, ClaimNobleAction.class);
      String actionId = DigestUtils.md5Hex(actionJson).toUpperCase();
      actionMap.put(actionId, action);
    }
    String playerName = playerInGame.getName();
    playerActionMaps.put(playerName, actionMap);
  }

  /**
   * Update the player's action map to contain only ReturnTokenActions.
   *
   * @param extraTokenCount
   * @param playerInGame
   */
  public void updateReturnTokenActions(int extraTokenCount, PlayerInGame playerInGame) {
    List<EnumMap<Colour, Integer>> allCombos = new ArrayList<>();
    EnumMap<Colour, Integer> rawMap = new EnumMap<>(Colour.class) {{
      put(Colour.BLUE, 0);
      put(Colour.RED, 0);
      put(Colour.BLACK, 0);
      put(Colour.GREEN, 0);
      put(Colour.WHITE, 0);
    }};

    // generate all combinations based extra token count
    if (extraTokenCount == 1) {
      for (Colour c : rawMap.keySet()) {
        EnumMap<Colour, Integer> curMap = new EnumMap<>(rawMap);
        curMap.put(c, 1);
        allCombos.add(curMap);
      }
    }

    if (extraTokenCount == 2) {
      for (Colour c : rawMap.keySet()) {
        EnumMap<Colour, Integer> curMap = new EnumMap<>(rawMap);
        curMap.put(c, 2);
        allCombos.add(curMap);
      }

      Set<Set<Colour>> colours = Sets.combinations(rawMap.keySet(), 2);
      for (Set<Colour> otherColours : colours) {
        EnumMap<Colour, Integer> curMap = new EnumMap<>(rawMap);
        for (Colour c : otherColours) {
          curMap.put(c, 1);
        }
        allCombos.add(curMap);
      }
    }

    if (extraTokenCount == 3) {
      for (Colour c : rawMap.keySet()) {
        EnumMap<Colour, Integer> curMap = new EnumMap<>(rawMap);
        curMap.put(c, 3);
        allCombos.add(curMap);
      }

      Set<Set<Colour>> colours = Sets.combinations(rawMap.keySet(), 3);
      for (Set<Colour> otherColours : colours) {
        EnumMap<Colour, Integer> curMap = new EnumMap<>(rawMap);
        for (Colour c : otherColours) {
          curMap.put(c, 1);
        }
        allCombos.add(curMap);
      }

      for (Colour c : rawMap.keySet()) {
        EnumMap<Colour, Integer> curMap = new EnumMap<>(rawMap);
        curMap.put(c, 2);
        allCombos.add(curMap);
        List<Colour> otherColours = curMap.keySet().stream()
            .filter(c2 -> !c2.equals(c))
            .collect(Collectors.toList());
        for (Colour c2 : otherColours) {
          EnumMap<Colour, Integer> finalMap = new EnumMap<>(curMap);
          finalMap.put(c2, 1);
          allCombos.add(finalMap);
        }
      }
    }

    // after all combinations, we generate the actions
    EnumMap<Colour, Integer> playerTokens = playerInGame.getTokenHand().getAllTokens();
    List<Action> returnTokenActions = new ArrayList<>();
    for (EnumMap<Colour, Integer> combo : allCombos) {
      boolean isValid = true;
      for (Colour colour : combo.keySet()) {
        if (playerTokens.get(colour) < combo.get(colour)) {
          isValid = false;
        }
        // if the return went over the initial value, we do not allow it as well
        int upperBound = tableTop.getBank().getInitialValue();
        EnumMap<Colour,Integer> bankBalance = tableTop.getBank().getAllTokens();
        if(bankBalance.get(colour) + combo.get(colour) > upperBound) {
          isValid = false;
        }
      }
      if (isValid) {
        returnTokenActions.add(new ReturnTokenAction(combo, extraTokenCount));
      }
    }

    Map<String, Action> actionMap = new HashMap<>();
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    for (Action action : returnTokenActions) {
      String actionJson = gsonParser.toJson(action, ReturnTokenAction.class);
      String actionId = DigestUtils.md5Hex(actionJson).toUpperCase();
      actionMap.put(actionId, action);
    }
    String playerName = playerInGame.getName();
    playerActionMaps.put(playerName, actionMap);
  }


  public Map<String, Map<String, Action>> getPlayerActionMaps() {
    return playerActionMaps;
  }

  public TableTop getTableTop() {
    return tableTop;
  }

  //EVERYTHING AFTER THIS IS OLD CODE (not up to date based on M6 model)
  /*
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
  /*
  //TODO
  private static List<Action> cardsToActions(GameInfo gameInfo, PlayerInGame player) {
    List<Action> actionOptions = new ArrayList<>();
    EnumMap<Colour, Integer> wealth = player.getWealth();
    boolean canReserve = !player.getReservedHand().isFull();
   /*
    Map<Integer, List<BaseCard>> baseBoardCards =
        gameInfo.getTableTop().getBaseBoard().getBaseCardsOnBoard();

    for (int level : baseBoardCards.keySet()) {
      List<BaseCard> cardsPerLevel = baseBoardCards.get(level);
      for (int cardIndex = 0; cardIndex < cardsPerLevel.size(); cardIndex++) {
        Position cardPosition = new Position(level, cardIndex);
        BaseCard curBaseCard = cardsPerLevel.get(cardIndex);
        //start of purchase card verification
        //this creates a goldCounter, to see if gold tokens are needed
        int goldCounter = 0;
        EnumMap<Colour, Integer> cardPrice = curBaseCard.getPrice();
        for (Colour col : Colour.values()) {
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
          actionOptions.add(new PurchaseAction(cardPosition, curBaseCard, goldCounter));

        }
        //verify if player can reserve card
        if (canReserve) {
          actionOptions.add(new ReserveAction(cardPosition, curBaseCard));
        }
      }
    }



    return actionOptions;
  }
   */

  /*
   * Generate the hash -> Actions map provided: gameId, playerName (implicitly in PlayerInGame).
   * will be called everytime GET games/{gameId}/players/{playerName}/actions
   * will replace the previous Action Map every time
   */
  /*
  public void generateActions(long gameId, GameInfo gameInfo, PlayerInGame player) {

    // TODO: Player Identity will be verified before calling generateActions with access_token
    //  no need to check it here (we can safely assume player is valid before calling this)
    String curPlayerName = gameInfo.getCurrentPlayer();
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
      TakeTokenAction takeTokenAction = new TakeTokenAction(playerTokens);
      String takeTokenActionMd5 =
          DigestUtils.md5Hex(new Gson().toJson(takeTokenAction)).toUpperCase();

      // add the take token actions (card unrelated actions)
      hashActionMap.put(takeTokenActionMd5, takeTokenAction);
    }

    /*
    // once the hash -> Action map is ready, we add it for this specific player
    if (!actionLookUpMap.containsKey(gameId)) {
      // if the gameId is not recorded, means we have no players' actions, thus we are adding
      // the first player
      Map<String, Map<String, Action>> playerSpecificActionsMap = new HashMap<>();
      playerSpecificActionsMap.put(askedActionsPlayerName, hashActionMap);
      actionLookUpMap.put(gameId, playerSpecificActionsMap);
    } else {
      // otherwise, we must have at least one player name stored in the map, therefore we
      // can for sure get the Map<String, Map<String, Action>>
      // then either overwrites or adding new action map
      Map<String, Map<String, Action>> playerSpecificActionsMap = actionLookUpMap.get(gameId);
      playerSpecificActionsMap.put(askedActionsPlayerName, hashActionMap);
    }

  }

 */


  /*
   * Find the (potentially, might be empty map) previously generated hash -> Action map
   * when receive POST request on games/{gameId}/players/{playerName}/actions/{actionId}
   * first call this method to find the map, then with {actionId} provided, we can find the
   * right Action to execute.
   */
  /*
  public Map<String, Action> lookUpActions(long gameId, String playerName) {
    // whether player is in the game or not will be checked in RestController class
    // if this is an empty map, then there is no need to look up actionMd5, just reply
    // with a bad_request
    return this.actionLookUpMap.get(gameId).get(playerName);
  }

   */


}