package project.view.splendor.communication;

public class CardAction implements Action {
  public void setPosition(Position position) {
    this.position = position;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  private Position position;
  private Card card;

  public boolean isCardAction() {
    return isCardAction;
  }

  public void setCardAction(boolean cardAction) {
    isCardAction = cardAction;
  }

  private boolean isCardAction;

  public CardAction(boolean isCardAction, Position position, Card card) {
    this.position = position;
    this.card = card;
    this.isCardAction = isCardAction;
  }

  public Position getPosition() {
    return position;
  }

  public Card getCard() {
    return card;
  }

  @Override
  public boolean checkIsCardAction() {
    return false;
  }
}
