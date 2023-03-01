package project.view.splendor.communication;

public class PurchaseAction extends Action {
  private DevelopmentCard curCard;
  private int goldTokenRequired;
  private Position cardPosition;

  public PurchaseAction(String type, DevelopmentCard curCard, int goldTokenRequired, Position cardPosition) {
    this.curCard = curCard;
    this.goldTokenRequired = goldTokenRequired;
    this.cardPosition = cardPosition;
  }

  public DevelopmentCard getCurCard() {
    return curCard;
  }

  public void setCurCard(DevelopmentCard curCard) {
    this.curCard = curCard;
  }

  public int getGoldTokenRequired() {
    return goldTokenRequired;
  }

  public void setGoldTokenRequired(int goldTokenRequired) {
    this.goldTokenRequired = goldTokenRequired;
  }

  public Position getCardPosition() {
    return cardPosition;
  }

  public void setCardPosition(Position cardPosition) {
    this.cardPosition = cardPosition;
  }

}
