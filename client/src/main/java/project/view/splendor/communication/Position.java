package project.view.splendor.communication;

public class Position {
  private int coordinateX;
  private int coordinateY;

  public Position(int coordinateX, int coordinateY) {
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
  }

  public int getCoordinateX() {
    return coordinateX;
  }

  public void setCoordinateX(int coordinateX) {
    this.coordinateX = coordinateX;
  }

  public int getCoordinateY() {
    return coordinateY;
  }

  public void setCoordinateY(int coordinateY) {
    this.coordinateY = coordinateY;
  }

  // indexing starting from zero

}
