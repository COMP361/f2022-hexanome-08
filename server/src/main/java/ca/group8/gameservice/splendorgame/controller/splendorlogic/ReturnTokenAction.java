package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.SplendorGameException;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TokenHand;
import java.util.EnumMap;

/**
 * This class represents the action for returning tokens if the player exceeds the maximum
 * tokens in hand.
 */
public class ReturnTokenAction extends Action {

  private final EnumMap<Colour, Integer> tokensToReturn;
  private int extraTokenCount;

  public ReturnTokenAction(EnumMap<Colour, Integer> tokensToReturn, int extraTokens) {
    super.type = this.getClass().getSimpleName();
    this.tokensToReturn = tokensToReturn;
    extraTokenCount = extraTokens;
  }

  public EnumMap<Colour, Integer> getTokens() {
    return tokensToReturn;
  }


  public int getExtraTokenCount() {
    return extraTokenCount;
  }

  @Override
  void execute(TableTop curTableTop, PlayerInGame playerInGame,
               ActionGenerator actionListGenerator,
               ActionInterpreter actionInterpreter) {

    //remove tokens from Player's tokenHand & add those tokens to the bank
    playerInGame.getTokenHand().removeToken(tokensToReturn);
    curTableTop.getBank().returnToken(tokensToReturn);

  }

  @Override
  public Card getCurCard() throws NullPointerException {
    throw new NullPointerException("There is no card associated with this action.");
  }

  @Override
  public Position getCardPosition() throws NullPointerException {
    throw new NullPointerException("There is no card position associated with this action.");
  }
}
