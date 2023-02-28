package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interprets and executes actions.
 */
public class ActionInterpreter {

  private int freeCardLevel;
  private int burnCardCount;
  private Colour burnCardColour;
  private DevelopmentCard stashedCard;
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
    // the action executed can depend on the type of it

    // TODO: is responsible to check whether we need to generate more cascading actions or not

    // extra need to check whether we need to update player's action map
    // according to their power or not
    if(tableTop.getGameBoards().containsKey(Extension.TRADING_POST)) {

    }

    // extra need to check the winning condition of the player
    if(tableTop.getGameBoards().containsKey(Extension.CITY)) {

    }

    // extra need to check the orient part of extra actions need to be generated


    // TODO: Cascade action check
    if (burnCardCount != 0) {

    }

    //TODO: Set to the next players turn? check winner?
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
