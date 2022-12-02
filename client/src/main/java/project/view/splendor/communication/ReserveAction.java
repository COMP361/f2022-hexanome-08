package project.view.splendor.communication;

public class ReserveAction extends Action {

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

  public ReserveAction(boolean isCardAction, Position position, Card card) {
    super(isCardAction);
    this.position = position;
    this.card = card;
  }
}
