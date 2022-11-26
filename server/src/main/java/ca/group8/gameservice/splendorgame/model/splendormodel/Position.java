package ca.group8.gameservice.splendorgame.model.splendormodel;

public class Position {

  // indexing starting from zero
  private int x;
  private int y;

  public Position(int paramWidth, int paramHeight) {
    x = paramWidth;
    y = paramHeight;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
