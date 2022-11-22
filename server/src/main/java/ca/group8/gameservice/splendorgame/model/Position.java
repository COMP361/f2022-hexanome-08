package ca.group8.gameservice.splendorgame.model;

public class Position {

  // indexing starting from zero
  private int width;
  private int height;

  public Position(int paramWidth, int paramHeight) {
    width = paramWidth;
    height = paramHeight;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
}
