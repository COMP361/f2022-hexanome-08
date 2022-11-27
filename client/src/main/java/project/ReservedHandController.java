package project;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class ReservedHandController {

  @FXML
  private HBox reservedDevCardsHbox;

  @FXML
  private HBox reservedNoblesHbox;


  public void initialize() {
    // initialize the reserved hands based on the long poll request sent to server
    // TODO: send the long poll request under player/inventory here, not with the button

  }
}
