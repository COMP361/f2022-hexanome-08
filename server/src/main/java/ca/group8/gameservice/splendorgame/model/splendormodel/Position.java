package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.Objects;

/**
 * X and Y coordinates on board.
 */
public class Position {

  // indexing starting from zero
  private int coordinateX;
  private int coordinateY;

  /**
   * Constructor.
   *
   * @param paramX card column
   * @param paramY card row
   */
  public Position(int paramX, int paramY) {
    coordinateX = paramX;
    coordinateY = paramY;
  }

  /**
   * Gets card's column.
   *
   * @return x coordinate
   */
  public int getX() {
    return coordinateX;
  }

  /**
   * Sets card's column.
   *
   * @param x coordinate
   */
  public void setX(int x) {
    this.coordinateX = x;
  }

  /**
   * Gets card's row.
   *
   * @return y coordinate
   */
  public int getY() {
    return coordinateY;
  }

  /**
   * Set's card's row.
   *
   * @param y coordinate
   */
  public void setY(int y) {
    this.coordinateY = y;
  }

  /**
   * Equals if they are the same, not equals if is not instance
   * of Position.
   *
   * @param obj the position
   * @return boolean of if they are equal
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Position)) {
      return false;
    }

    Position otherPosition = (Position) obj;

    return this.coordinateX == otherPosition.coordinateX
            && this.coordinateY == otherPosition.coordinateY;
  }

  /**
   * Hashcode method.
   *
   * @return new hashCode
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), coordinateX, coordinateY);
  }
}
