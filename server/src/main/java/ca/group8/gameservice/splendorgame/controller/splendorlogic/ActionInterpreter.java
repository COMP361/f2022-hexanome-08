package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.PurchasedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.ArrayList;
import java.util.Arrays;
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
  private PlayerStates playerStates;
  private GameInfo gameInfo;
  private ActionGenerator actionGenerator;

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

  /**
   * After parsing the object from json, we need to relink the references.
   * between this action interpreter and the other game states information.
   *
   * @param gameInfo a game info instance
   * @param playerStates player states instance
   */
  public void relinkReferences(GameInfo gameInfo, PlayerStates playerStates) {
    this.playerStates = playerStates;
    this.gameInfo = gameInfo;
    Map<String, Map<String, Action>> playerActionMaps = gameInfo.getPlayerActionMaps();
    TableTop tableTop = gameInfo.getTableTop();
    this.actionGenerator = new ActionGenerator(playerActionMaps, tableTop);
  }

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

    // the action has been executed, and the player's action map is possibly empty now, check!
    if (actionMap.isEmpty()) {
      // if the current player's action map is empty, we do end turn check
      // and then set to next player's turn

      // nobles check (all nobles the player can unlock)
      BaseBoard baseBoard =  (BaseBoard) tableTop.getBoard(Extension.BASE);

      List<Integer> nobleIndices = new ArrayList<>();
      List<NobleCard> allNobles = baseBoard.getNobles();
      for (int i = 0; i < allNobles.size(); i++) {
        NobleCard nobleCard = allNobles.get(i);
        if(nobleCard.canVisit(playerInGame)) {
          nobleIndices.add(i);
        }
      }

      PurchasedHand purchasedHand = playerInGame.getPurchasedHand();
      if(!nobleVisited) {
        // if the player unlocked one noble, added it to player hand,
        // remove it from baseboard
        if (nobleIndices.size() == 1) {
          NobleCard nobleCard = allNobles.get(nobleIndices.get(0));
          baseBoard.removeNoble(nobleCard);
          purchasedHand.addNobleCard(nobleCard);
          nobleVisited = true;
        }

        if (nobleIndices.size() > 1) {
          actionGenerator.updateClaimNobleActions(nobleIndices, playerInGame);
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

      }

      // winners check (optionally with city extension)
      if (tableTop.getGameBoards().containsKey(Extension.CITY)) {
        // city winning check
      } else {
        // regular winning check
        int points = playerInGame.getPrestigePoints();
        if (points >= 15) {
          List<String> winners = gameInfo.getWinners();
          List<String> allPlayers = gameInfo.getPlayerNames();
          // if the current player is the last player, this game is over
          if (playerName.equals(allPlayers.get(allPlayers.size()-1))) {
            if (winners.isEmpty()) { // this is the first and last winner
              winners.add(playerName);
              // game is over!
              gameInfo.setFinished();
            } else {
              // we have several potential winners
              winners.add(playerName);
              decideWinner(winners);
              gameInfo.setFinished();
            }
          } else {
            // add the winner
            winners.add(playerName);
            // and can not say the game is finished
          }
        }

      }

      if (!gameInfo.isFinished()) {
        gameInfo.setNextPlayer();
      }

      // set next turn
      // TODO: before changing to next player, reset everything
      // the flags to default values
      //  private boolean nobleVisited = false;
      //  private int freeCardLevel = 0;
      //  private int burnCardCount = 0;
      //  private Colour burnCardColour = null;
      //  private DevelopmentCard stashedCard = null;

    }


    // TODO: is responsible to check whether we need to generate more cascading actions or not

    // extra need to check whether we need to update player's action map
    // according to their power or not


    // extra need to check the winning condition of the player


    // extra need to check the orient part of extra actions need to be generated



    //TODO: Set to the next players turn? check winner?
  }


  private void decideWinner(List<String> winnerNames) {
    // sorted the playerInGame descending
    List<PlayerInGame> playerInGames = winnerNames.stream()
        .map(playerStates::getOnePlayerInGame).sorted(Comparator.comparing(
            (PlayerInGame player) -> player.getPurchasedHand().getTotalCardCount()))
        .collect(Collectors.toList());
    gameInfo.setWinners(Arrays.asList(playerInGames.get(0).getName()));
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

  //todo
  public void setBurnCardInfo(EnumMap<Colour, Integer> cardPrice) {
    //set colour and cards to burn
  }

  public Colour getBurnCardColour() {
    return burnCardColour;
  }

  public void setStashedCard(DevelopmentCard stashedCard) {
    assert stashedCard != null;
    this.stashedCard = stashedCard;
  }

  public DevelopmentCard getStashedCard() {
    assert stashedCard != null;
    return stashedCard;
  }

  public void setFreeCardLevel(int newLevel) {
    assert newLevel < 3; //Cannot have a free card that is level 3 or above.
    freeCardLevel = newLevel;
  }

  public int getFreeCardLevel() {
    return freeCardLevel;
  }

  public int getBurnCardCount() {
    return burnCardCount;
  }

  public void setBurnCardCount(int number) {
    burnCardCount += number;
  }

  public void setBurnCardColour(
      Colour burnCardColour) {
    this.burnCardColour = burnCardColour;
  }

}
