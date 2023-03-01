package project.view.splendor.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for board for the three boards
 */
public class Board {
  String type;
  public Board(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
