package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * X and Y coordinates on board.
 */
public class Position {

  // indexing starting from zero
  private int coordinateX;
  private int coordinateY;

  public Position(int paramX, int paramY) {
    coordinateX = paramX;
    coordinateY = paramY;
  }

  public int getX() {
    return coordinateX;
  }

  public void setX(int x) {
    this.coordinateX = x;
  }

  public int getY() {
    return coordinateY;
  }

  public void setY(int y) {
    this.coordinateY = y;
  }

  @Override
  public boolean equals(Object o) {
    Position position = (Position) o;
    return (position.getX() == this.getX() && position.getY() == this.getY());
  }
}
