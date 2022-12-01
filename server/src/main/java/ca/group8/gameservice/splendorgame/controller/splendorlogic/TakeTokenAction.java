package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.TokenHand;
import java.util.EnumMap;

/**
 * Action available every turn, taking tokens from bank.
 */
public class TakeTokenAction implements Action {
  private EnumMap<Colour, Integer> tokens;

  public TakeTokenAction(EnumMap<Colour, Integer> tokens) {
    this.tokens = tokens;
  }

  public EnumMap<Colour, Integer> getTokens() {
    return tokens;
  }

  @Override
  public void execute(GameInfo currentGameState, PlayerInGame playerState) {
    TokenHand tokenHand = playerState.getTokenHand();
    tokenHand.addToken(this.tokens);

    for (Colour colour : Colour.values()) {
      int oldValue = currentGameState.getTableTop().getBank().getAllTokens().get(colour);
      currentGameState.getTableTop().getBank().getAllTokens().put(colour,
          oldValue - tokens.get(colour));
    }
  }

  @Override
  public boolean checkIsCardAction() {
    return false;
  }
}
