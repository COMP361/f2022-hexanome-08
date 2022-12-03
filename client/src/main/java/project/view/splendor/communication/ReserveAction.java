package project.view.splendor.communication;


public class ReserveAction implements Action {

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
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

  public ReserveAction(Position position, Card card) {
    this.position = position;
    this.card = card;
  }

  @Override
  public void execute(GameInfo currentGameState, PlayerInGame playerState) {

  }

  @Override
  public boolean checkIsCardAction() {
    return true;
  }
}
