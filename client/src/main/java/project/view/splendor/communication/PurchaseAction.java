package project.view.splendor.communication;

import java.util.EnumMap;

public class PurchaseAction implements Action {
  private int goldTokenRequired;

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
  public DevelopmentCard getCard() {
    return card;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public void setCard(DevelopmentCard card) {
    this.card = card;
  }

  private Position position;
  private DevelopmentCard card;

  public PurchaseAction(Position position, DevelopmentCard card, int goldTokenRequired) {
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