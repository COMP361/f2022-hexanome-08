package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.Map;

/**
 * This class represents the Extra Actions associated with a specific PowerEffect.
 */
public class BonusTokenPowerAction extends Action {

  private Colour colour;
  private PlayerInGame player;

  public BonusTokenPowerAction(PlayerInGame player, Colour colour) {
    this.player = player;
    this.colour = colour;
  }

  @Override
  void execute(TableTop curTableTop, PlayerInGame playerInGame, ActionGenerator actionListGenerator,
      ActionInterpreter actionInterpreter) {
    Map<Colour,Integer> playerTokens = player.getTokenHand().getAllTokens();
    int oldTokenCount = playerTokens.get(colour);
    playerTokens.put(colour, oldTokenCount+1);
    Map<Colour,Integer> bankTokens = curTableTop.getBank().getAllTokens();
    int oldBankTokenCount = bankTokens.get(colour);
    bankTokens.put(colour,oldBankTokenCount-1);
  }

  public Card getCurCard() {
    return null;
  }

  public Position getCardPosition() throws NullPointerException {
    return null;
  }

  public Colour getColour() {
    return colour;
  }

  public PlayerInGame getPlayer() {
    return player;
  }

}
