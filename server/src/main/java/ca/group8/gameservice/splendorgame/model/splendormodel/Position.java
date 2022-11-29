package ca.group8.gameservice.splendorgame.model.splendormodel;

public class Position {

  // indexing starting from zero
  private int x;
  private int y;

  public Position(int paramX, int paramY) {
    x = paramX;
    y = paramY;
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

  @Override
  public boolean equals(Object o) {
    Position position = (Position) o;
    return (position.getX() == this.getX() && position.getY() == this.getY());
  }
}
