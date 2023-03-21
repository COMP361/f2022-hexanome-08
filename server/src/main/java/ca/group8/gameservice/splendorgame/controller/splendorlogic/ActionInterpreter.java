package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.CityBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CityCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.PurchasedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TraderBoard;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interprets and executes actions.
 */
public class ActionInterpreter {

  private boolean nobleVisited = false;
  private int freeCardLevel = 0;
  private int burnCardCount = 0;
  private Colour burnCardColour = null;
  private DevelopmentCard stashedCard = null;
  private final PlayerStates playerStates;
  private final GameInfo gameInfo;
  private final ActionGenerator actionGenerator;
  private boolean bonusTokenStashed = false;

  /**
   * Constructor.
   *
   * @param playerStates the player states of players in the related game.
   * @param gameInfo     the relevant game info for this interpreter instance
   */
  public ActionInterpreter(GameInfo gameInfo, PlayerStates playerStates) {
    this.playerStates = playerStates;
    this.gameInfo = gameInfo;
    Map<String, Map<String, Action>> playerActionMaps = gameInfo.getPlayerActionMaps();
    TableTop tableTop = gameInfo.getTableTop();
    this.actionGenerator = new ActionGenerator(playerActionMaps, tableTop);
  }

  ///**
  // * After parsing the object from json, we need to relink the references.
  // * between this action interpreter and the other game states information.
  // *
  // * @param gameInfo     a game info instance
  // * @param playerStates player states instance
  // */
  //public void relinkReferences(GameInfo gameInfo, PlayerStates playerStates) {
  //  this.playerStates = playerStates;
  //  this.gameInfo = gameInfo;
  //  Map<String, Map<String, Action>> playerActionMaps = gameInfo.getPlayerActionMaps();
  //  TableTop tableTop = gameInfo.getTableTop();
  //  this.actionGenerator = new ActionGenerator(playerActionMaps, tableTop);
  //}

  /**
   * TODO: Fix this description at the end
   * This method will get called if POST on games/{gameId}/players/{playerName}/actions/{actionId}.
   * SplendorActionListGenerator (lookUpActions) ->
   * Map from hashed string to action - {actionId} -> Action playerChosenAction,
   * so we can know prior to this method called, we can find the Action the player wants to execute
   * TODO: Note: we only provide ValidActions to players, so execution can never failed
   *
   * @param actionId   the identifier of the action being interpreted
   * @param playerName the player associated with this action
   */
  public void interpretAction(String actionId, String playerName) {
    Logger logger = LoggerFactory.getLogger(ActionInterpreter.class);
    //logger.info("Before execute the action" + playerChosenAction.checkIsCardAction());
    Map<String, Action> actionMap = actionGenerator.getPlayerActionMaps().get(playerName);
    Action actionChosen = actionMap.get(actionId);
    // current tabletop and the player in game information
    TableTop tableTop = gameInfo.getTableTop();
    PlayerInGame playerInGame = playerStates.getOnePlayerInGame(playerName);
    actionChosen.execute(tableTop, playerInGame, actionGenerator, this);
    if (tableTop.getGameBoards().containsKey(Extension.TRADING_POST)) {
      TraderBoard board = (TraderBoard) tableTop.getBoard(Extension.TRADING_POST);
      Power power = board.getPlayerOnePower(playerName, PowerEffect.EXTRA_TOKEN);
      if (actionChosen instanceof PurchaseAction && power.isUnlocked()) {
        bonusTokenStashed = true;
      }
    }


    // the action has been executed, and the player's action map is possibly empty now, check!
    actionMap = actionGenerator.getPlayerActionMaps().get(playerName);
    if (actionMap.isEmpty()) {
      // if anything might have changed, let the client side know immediately
      //playerStatesManager.touch();
      //gameInfoManger.touch();
      // if the current player's action map is empty, we do end turn check
      // and then set to next player's turn

      // nobles check (all nobles the player can unlock)
      BaseBoard baseBoard = (BaseBoard) tableTop.getBoard(Extension.BASE);

      List<Integer> nobleIndices = new ArrayList<>();
      List<NobleCard> allNobles = baseBoard.getNobles();
      for (int i = 0; i < allNobles.size(); i++) {
        NobleCard nobleCard = allNobles.get(i);
        if (nobleCard.canVisit(playerInGame)) {
          nobleIndices.add(i);
        }
      }

      // TODO: Fix this later (unlock one in hand and one on board)
      PurchasedHand purchasedHand = playerInGame.getPurchasedHand();
      if (!nobleVisited) {
        // if the player unlocked one noble, added it to player hand,
        // remove it from baseboard
        if (nobleIndices.size() == 1) {
          NobleCard nobleCard = allNobles.get(nobleIndices.get(0));
          baseBoard.removeNoble(nobleCard);
          purchasedHand.addNobleCard(nobleCard);
          int noblePoints = nobleCard.getPrestigePoints();
          playerInGame.changePrestigePoints(noblePoints);
          nobleVisited = true;
        }

        if (nobleIndices.size() > 1) {
          actionGenerator.updateClaimNobleActions(nobleIndices, playerInGame);
          // we do not want to continue the other condition checks
          nobleVisited = true;
          return;
        }
      }


      // checking any noble unlocked from reserved hand
      List<Integer> noblesInReserveHandIndices = new ArrayList<>();
      List<NobleCard> nobleCardsInHand = playerInGame.getReservedHand().getNobleCards();
      for (int i = 0; i < nobleCardsInHand.size(); i++) {
        NobleCard nobleCard = nobleCardsInHand.get(i);
        if (nobleCard.canVisit(playerInGame)) {
          noblesInReserveHandIndices.add(i);
        }
      }

      if (!nobleVisited) {
        // if the player unlocked one noble, added it to player hand,
        // remove it from baseboard
        if (noblesInReserveHandIndices.size() == 1) {
          NobleCard nobleCard = nobleCardsInHand.get(noblesInReserveHandIndices.get(0));
          playerInGame.getReservedHand().removeNoble(nobleCard);
          purchasedHand.addNobleCard(nobleCard);
          int noblePoints = nobleCard.getPrestigePoints();
          playerInGame.changePrestigePoints(noblePoints);
          nobleVisited = true;
        }

        if (noblesInReserveHandIndices.size() > 1) {
          actionGenerator.updateClaimNobleActions(noblesInReserveHandIndices, playerInGame);
          // we do not want to continue the other condition checks
          nobleVisited = true;
          return;
        }
      }

      // (extensions) -> Orient is always checked

      // Orient Flags checking
      if (stashedCard != null) {
        if (burnCardCount > 0) {
          // generate burn card actions
          actionGenerator.updateCascadeActions(playerInGame, stashedCard, CardEffect.BURN_CARD);
          return;
        }

        if (freeCardLevel > 0) {
          // generate free card actions
          actionGenerator.updateCascadeActions(playerInGame, stashedCard, CardEffect.FREE_CARD);
          return;
        }
      }

      // extra end turn check for extensions
      // TODO: Later
      if (tableTop.getGameBoards().containsKey(Extension.TRADING_POST)) {
        // possible to generate power actions
        TraderBoard traderBoard = (TraderBoard) tableTop.getBoard(Extension.TRADING_POST);
        Map<PowerEffect, Power> playerPowers = traderBoard.getAllPlayerPowers().get(playerName);
        int powerCount = traderBoard.getUnlockedPowerCount(playerName);
        //boolean
        boolean firstTimeUnLockArmPower = false;
        for (PowerEffect powerEffect : playerPowers.keySet()) {
          // the player should have this power unlocked if
          // it's locked before, and it's now valid to unlock
          Power power = playerPowers.get(powerEffect);
          boolean validPowerCheck = power.validityCheck(playerInGame);
          boolean unlockPowerCheck = power.isUnlocked();
          if (validPowerCheck && !unlockPowerCheck) {
            power.unlock();
            if (powerEffect.equals(PowerEffect.FIVE_POINTS)) {
              playerInGame.changePrestigePoints(5);
            }
            if (powerEffect.equals(PowerEffect.ARM_POINTS)) {
              firstTimeUnLockArmPower = true;
            }
          }

          if (!validPowerCheck && unlockPowerCheck) {
            power.lock();
            if (powerEffect.equals(PowerEffect.FIVE_POINTS)) {
              playerInGame.changePrestigePoints(-5);
            }
            if (powerEffect.equals(PowerEffect.ARM_POINTS)) {
              playerInGame.changePrestigePoints(-powerCount);
            }
          }
        }
        Power armPointsPower = traderBoard.getPlayerOnePower(playerName, PowerEffect.ARM_POINTS);
        int curPowerCount = traderBoard.getUnlockedPowerCount(playerName);
        if (armPointsPower.isUnlocked()) {
          if (firstTimeUnLockArmPower) {
            playerInGame.changePrestigePoints(curPowerCount);
          } else {
            // the difference b/w beginning and updated one, can be negative
            int diffAmount = curPowerCount - powerCount;
            playerInGame.changePrestigePoints(diffAmount);
          }
        }
        //check if player still needs to take bonus token
        if (traderBoard.getPlayerOnePower(playerName, PowerEffect.EXTRA_TOKEN).isUnlocked()) {
          if (bonusTokenStashed) {
            bonusTokenStashed = false;
            actionGenerator.updateBonusTokenPowerActions(playerInGame);
          }
        }
        bonusTokenStashed = false;

      }


      // winners check (optionally with city extension)
      boolean playerWonTheGame;
      boolean hasCityExtension = tableTop.getGameBoards().containsKey(Extension.CITY);
      if (hasCityExtension) {
        // city winning check, can the player unlock a city or not.
        CityBoard cityBoard = (CityBoard) tableTop.getBoard(Extension.CITY);
        for (CityCard cityCard : cityBoard.getAllCityCards()) {
          // unlock the first city card that's available to unlock
          if (cityCard.canUnlock(playerInGame)) {
            cityBoard.assignCityCard(playerName, cityCard);
            break;
          }
        }
        // if the player has one city card, then one wins
        playerWonTheGame = cityBoard.getPlayerCities().get(playerName) != null;
      } else {
        // regular winning check
        int points = playerInGame.getPrestigePoints();
        playerWonTheGame = points >= 15;
      }

      // the boolean value will be decided differently based on whether we play
      // with city extension or not
      if (playerWonTheGame) {
        List<String> winners = gameInfo.getWinners();
        List<String> allPlayers = gameInfo.getPlayerNames();
        // if the current player is the last player, this game is over
        String lastPlayerName = allPlayers.get(allPlayers.size() - 1);

        if (playerName.equals(lastPlayerName)) {
          if (winners.isEmpty()) { // this is the first and last winner
            winners.add(playerName);
            // game is over!
            gameInfo.setFinished();
            logger.info(playerName + " won the game!!!");
          } else {
            // we have several potential winners
            winners.add(playerName);
            // choose and set all winners (might have tie)
            decideWinners(winners);
            // game is over!
            gameInfo.setFinished();
            logger.info(playerName + " won the game!!!");
          }
        } else {
          // add the winner
          winners.add(playerName);
          // and can not say the game is finished
        }
      }

      // set next turn
      // TODO: before changing to next player, reset everything
      // the flags to default values
      nobleVisited = false;

      // if the game is not finished, set next player
      if (!gameInfo.isFinished()) {
        gameInfo.setNextPlayer();
        // after you set next player, action map should be initialized again for all players
        String nextPlayerName = gameInfo.getCurrentPlayer();
        for (PlayerInGame player : playerStates.getPlayersInfo().values()) {
          actionGenerator.setInitialActions(player, nextPlayerName);
        }
      }
    }
  }


  // change the gameInfo winner list to only contain the winner
  private void decideWinners(List<String> winnerNames) {


    // first need to sort by points, see if there is a tie or not

    // descending order, sorted by points
    List<PlayerInGame> sortByPointsWinners = winnerNames.stream()
        .map(playerStates::getOnePlayerInGame)
        .sorted(Comparator.comparing(PlayerInGame::getPrestigePoints).reversed())
        .collect(Collectors.toList());

    // get all players with the highest points
    int highestPoints = sortByPointsWinners.get(0).getPrestigePoints();
    List<PlayerInGame> playersWithMaxPoints = sortByPointsWinners.stream()
        .filter(player -> player.getPrestigePoints() == highestPoints)
        .collect(Collectors.toList());

    // sort the playersWithMaxPoints now, ascending order by card counts
    List<PlayerInGame> sortByDevCardCountWinners = playersWithMaxPoints.stream()
        .sorted(Comparator.comparing(
            (PlayerInGame player) -> player.getPurchasedHand().getTotalCardCount()
        )).collect(Collectors.toList());

    int minDevCardCount = sortByDevCardCountWinners.get(0).getPurchasedHand().getTotalCardCount();
    // now decide the final winners with the highest points and min dev cards
    List<String> finalWinners = sortByDevCardCountWinners.stream()
        .filter(player -> player.getPurchasedHand().getTotalCardCount() == minDevCardCount)
        .map(PlayerInGame::getName)
        .collect(Collectors.toList());

    // set the winners names to gameInfo
    gameInfo.setWinners(finalWinners);
  }

  public ActionGenerator getActionGenerator() {
    assert actionGenerator != null;
    return actionGenerator;
  }

  public GameInfo getGameInfo() {
    assert gameInfo != null;
    return gameInfo;
  }

  public PlayerStates getPlayerStates() {
    assert playerStates != null;
    return playerStates;
  }

  /**
   * setBurnCardInfo.
   *
   * @param cardPrice cardPrice
   */
  public void setBurnCardInfo(EnumMap<Colour, Integer> cardPrice) {
    Logger logger = LoggerFactory.getLogger(ActionInterpreter.class);
    logger.info("AI: burn card price: "+cardPrice);
    //set colour and cards to burn
    burnCardCount = 2;
    for (Colour colour : cardPrice.keySet()) {
      if (cardPrice.get(colour) > 0) {
        burnCardColour = colour;
        logger.info("AI: Colour: "+colour);
        break;
      }
    }
  }

  public Colour getBurnCardColour() {
    return burnCardColour;
  }

  public void setBurnCardColour(
      Colour burnCardColour) {
    this.burnCardColour = burnCardColour;
  }

  public DevelopmentCard getStashedCard() {
    assert stashedCard != null;
    return stashedCard;
  }

  public void setStashedCard(DevelopmentCard stashedCard) {
    assert stashedCard != null;
    this.stashedCard = stashedCard;
  }

  public int getFreeCardLevel() {
    return freeCardLevel;
  }

  public void setFreeCardLevel(int newLevel) {
    assert newLevel < 3 && newLevel > 0; //Cannot have a free card that is level 3 or above.
    freeCardLevel = newLevel;
  }

  public int getBurnCardCount() {
    return burnCardCount;
  }

  public void addBurnCardCount(int number) {
    burnCardCount += number;
  }

  public void removeBurnCardCount(int number) {
    burnCardCount -= number;
  }

  public void setBonusToken(boolean bonusToken) {
    this.bonusTokenStashed = bonusToken;
  }
}
