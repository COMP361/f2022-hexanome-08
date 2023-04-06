package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * This class represents the action for returning tokens if the player exceeds the maximum
 * tokens in hand.
 */
public class ReturnTokenAction extends Action {

  private final EnumMap<Colour, Integer> tokensToReturn;
  private final int extraTokenCount;

  /**
   * return token.
   *
   * @param tokensToReturn tokensToReturn
   * @param extraTokens extraTokens
   */
  public ReturnTokenAction(EnumMap<Colour, Integer> tokensToReturn, int extraTokens) {
    super.type = this.getClass().getSimpleName();
    this.tokensToReturn = tokensToReturn;
    extraTokenCount = extraTokens;
  }

  /**
   * Returns Map of tokens being returned.
   *
   * @return Map of tokens
   */
  public EnumMap<Colour, Integer> getTokens() {
    return tokensToReturn;
  }

  /**
   * Gets tokens above 10 in token hand.
   *
   * @return int of tokens above 10
   */
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

    actionListGenerator.getPlayerActionMaps().put(playerInGame.getName(), new HashMap<>());

  }

}
