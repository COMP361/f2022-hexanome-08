package project.view.splendor.communication;

/**
 * Action that allows a player to reserve a card.
 */
public class ReserveAction extends Action {

  private DevelopmentCard curCard;
  private Position cardPosition;

  public ReserveAction(Position position, DevelopmentCard curCard) {
    super.type = this.getClass().getSimpleName();
    this.cardPosition = position;
    this.curCard = curCard;
  }

  public DevelopmentCard getCurCard() {
    return curCard;
  }

  public void setCurCard(DevelopmentCard curCard) {
    this.curCard = curCard;
  }

  public Position getCardPosition() {
    return cardPosition;
  }

  public void setCardPosition(Position cardPosition) {
    this.cardPosition = cardPosition;
  }
}
