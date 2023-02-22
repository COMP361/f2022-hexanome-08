package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.EnumMap;
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
  private final PlayerStates playerStates;
  private final GameInfo gameInfo;
  private final ActionGenerator actionGenerator;

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

    //TODO: Fix the execute method below based on new Action execute() paramaters
    //playerChosenAction.execute(currentGameState, playerState);

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
