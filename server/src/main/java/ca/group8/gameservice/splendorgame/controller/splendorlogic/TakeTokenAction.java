package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TokenHand;
import java.util.EnumMap;

/**
 * Action available every turn, taking tokens from bank.
 */
public class TakeTokenAction extends Action {

  private EnumMap<Colour, Integer> tokensTaken;

  /**
   * Construct a new Take Tokens Action.
   *
   * @param tokens an EnumMap of Colour to Integer representing the tokens selected by the user.
   */
  public TakeTokenAction(EnumMap<Colour, Integer> tokens) {
    super.type = this.getClass().getSimpleName();
    this.tokensTaken = tokens;
  }

  public EnumMap<Colour, Integer> getTokens() {
    return tokensTaken;
  }

  public void setTokens(EnumMap<Colour, Integer> tokens) {
    this.tokensTaken = tokens;
  }

  @Override
  void execute(TableTop curTableTop, PlayerInGame playerInGame,
               ActionGenerator actionGenerator,
               ActionInterpreter actionInterpreter) {
    Bank bank = curTableTop.getBank();
    TokenHand tokenHand = playerInGame.getTokenHand();
    // remove the tokens from bank
    bank.takeToken(tokensTaken);
    // add the tokens to the player
    tokenHand.addToken(tokensTaken);

    // if the number exceeds 10, we update the player's action to cantain only several
    // ReturnTokenAction
    int tokenLeft = tokenHand.getTokenTotalCount();
    if(tokenLeft > 10) {
      int tokensNeedToReturn = tokenLeft - 10;
      actionGenerator.updateReturnTokenActions(tokensNeedToReturn, playerInGame);
    }

    /* OLD TAKE TOKENS EXECUTE METHOD
    public void execute(GameInfo currentGameState, PlayerInGame playerState) {
    TokenHand tokenHand = playerState.getTokenHand();
    tokenHand.addToken(this.tokensTaken);

    for (Colour colour : Colour.values()) {
      int oldValue = currentGameState.getTableTop().getBank().getAllTokens().get(colour);
      currentGameState.getTableTop().getBank().getAllTokens().put(colour,
          oldValue - tokensTaken.get(colour));
    }
  }
     */

  }

  @Override
  Card getCurCard() {
    throw new NullPointerException("There is no card associated with this action.");
  }

  @Override
  Position getCardPosition() {
    throw new NullPointerException("There is no card position associated with this action.");
  }

}
