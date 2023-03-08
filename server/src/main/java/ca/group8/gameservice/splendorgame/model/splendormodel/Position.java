package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.Objects;

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
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Position)) {
      return false;
    }

    Position otherPosition = (Position) obj;

    return this.coordinateX == otherPosition.coordinateX &&
        this.coordinateY == otherPosition.coordinateY;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), coordinateX, coordinateY);
  }
}
