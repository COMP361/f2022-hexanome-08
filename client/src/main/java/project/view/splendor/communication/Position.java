package project.view.splendor.communication;

public class Position {

  public int getCoordinateX() {
    return coordinateX;
  }

  public int getCoordinateY() {
    return coordinateY;
  }

  public void setCoordinateX(int coordinateX) {
    this.coordinateX = coordinateX;
  }

  public void setCoordinateY(int coordinateY) {
    this.coordinateY = coordinateY;
  }

  public Position(int coordinateX, int coordinateY) {
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
  }

  // indexing starting from zero
  private int coordinateX;
  private int coordinateY;

}
