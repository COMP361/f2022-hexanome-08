package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.EnumMap;

public class UpdateReturnTokenAction extends Action {

  private final EnumMap<Colour, Integer> tokensInHand;
  private int extraTokenCount;

  public UpdateReturnTokenAction(EnumMap<Colour, Integer> tokensInHand, int extraTokens) {
    this.tokensInHand = tokensInHand;
    extraTokenCount = extraTokens;
  }

  /**
   * This method should set the private EnumMap tokensInHand equal to the parameter Map
   *
   * @param tokens is an EnumMap of tokens
   */
  public void setTokens(EnumMap<Colour, Integer> tokens) {
    for (Colour colour : Colour.values()) {
      tokensInHand.put(colour, tokens.get(colour));
    }
  }

  public EnumMap<Colour, Integer> getTokens() {
    return tokensInHand;
  }

  public void setExtraTokenCount(int number) {
    //TODO: Check whether number must be GREATER than 0, or >=0
    assert number>=0;
    extraTokenCount=number;
  }

  public int getExtraTokenCount() {
    return extraTokenCount;
  }

  @Override
  //TODO
  void execute(TableTop curTableTop, PlayerInGame playerInGame,
               SplendorActionListGenerator actionListGenerator,
               SplendorActionInterpreter actionInterpreter) {

  }

  @Override
  Card getCurCard() throws NullPointerException {
    throw new NullPointerException("There is no card associated with this action.");
  }

  @Override
  Position getCardPosition() throws NullPointerException {
    throw new NullPointerException("There is no card position associated with this action.");
  }
}
