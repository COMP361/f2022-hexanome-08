package project.view.splendor.communication;

public class PurchaseAction extends CardAction {
  private int goldTokenRequired;

  public PurchaseAction(boolean isCardAction,
                        Position position,
                        Card card, int goldTokenRequired) {
    super(isCardAction, position, card);
    this.goldTokenRequired = goldTokenRequired;
  }

  public int getGoldTokenRequired() {
    return goldTokenRequired;
  }

  public void setGoldTokenRequired(int goldTokenRequired) {
    this.goldTokenRequired = goldTokenRequired;
  }


}
