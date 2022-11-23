package ca.group8.gameservice.splendorgame.model;

public class BaseBoard extends Board {
  private int width;
  private int height;

  public BaseBoard(int paramWidth, int paramHeight) {
    super(paramWidth, paramHeight);
  }


  @Override
  public Position getCardPosition(int CardID) {
    return null;
  }

  @Override
  public Card takeCard(Position paramPosition) {
    return null;
  }

  @Override
  public Card takeAndReplaceCard(Card paramCard, Position paramPosition) {
    return null;
  }
}
