package project.view.splendor.communication;

public class PurchaseAction extends Action {
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

  public PurchaseAction(boolean isCardAction,
                        Position position,
                        Card card, int goldTokenRequired) {
    super(isCardAction);
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


}
