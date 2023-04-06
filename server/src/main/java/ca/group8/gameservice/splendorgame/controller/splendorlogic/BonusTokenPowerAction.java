package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Extra Actions associated with a specific PowerEffect.
 */
public class BonusTokenPowerAction extends Action {

  private final Colour colour;
  private final PlayerInGame player;

  /**
   * Constructor for this power action.
   *
   * @param player The player who will be able to take this action.
   * @param colour The colour of the token the player can take.
   */
  public BonusTokenPowerAction(PlayerInGame player, Colour colour) {
    super.type = this.getClass().getSimpleName();
    this.player = player;
    this.colour = colour;
  }

  @Override
  void execute(TableTop curTableTop, PlayerInGame playerInGame, ActionGenerator actionListGenerator,
               ActionInterpreter actionInterpreter) {
    Map<Colour, Integer> playerTokens = player.getTokenHand().getAllTokens();
    int oldTokenCount = playerTokens.get(colour);
    playerTokens.put(colour, oldTokenCount + 1);
    Map<Colour, Integer> bankTokens = curTableTop.getBank().getAllTokens();
    int oldBankTokenCount = bankTokens.get(colour);
    bankTokens.put(colour, oldBankTokenCount - 1);
    // GO TO NEXT TURN
    actionListGenerator.getPlayerActionMaps().put(playerInGame.getName(), new HashMap<>());
  }

  /**
   * getColour.
   *
   * @return colour of bonus token power action
   */

  public Colour getColour() {
    return colour;
  }

  /**
   * getPlayer.
   *
   * @return current player
   */

  public PlayerInGame getPlayer() {
    return player;
  }

}
