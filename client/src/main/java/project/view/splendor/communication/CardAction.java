package project.view.splendor.communication;

public abstract class CardAction extends Action {
  public void setPosition(Position position) {
    this.position = position;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  private Position position;
  private Card card;

  public CardAction(boolean isCardAction, Position position, Card card) {
    super(isCardAction);
    this.position = position;
    this.card = card;
  }

  public Position getPosition() {
    return position;
  }

  public Card getCard() {
    return card;
  }

}
