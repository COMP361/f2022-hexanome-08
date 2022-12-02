package project.view.splendor.communication;

import java.util.EnumMap;

public class PurchaseAction implements Action {
  private int goldTokenRequired;

  public Position getPosition() {
    return position;
  }

  public Card getCard() {
    return card;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  private Position position;
  private Card card;

  public PurchaseAction(Position position, Card card, int goldTokenRequired) {
    this.position = position;
    this.card = card;
    this.goldTokenRequired = goldTokenRequired;
  }

  public int getGoldTokenRequired() {
    return goldTokenRequired;
  }

  public void setGoldTokenRequired(int goldTokenRequired) {
    this.goldTokenRequired = goldTokenRequired;
  }


  @Override
  public void execute(GameInfo currentGameState, PlayerInGame playerState) {

  }

  @Override
  public boolean checkIsCardAction() {
    return true;
  }
}