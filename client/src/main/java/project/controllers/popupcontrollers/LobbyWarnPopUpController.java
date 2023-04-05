package project.controllers.popupcontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * LobbyWarnPopUpController.
 */
public class LobbyWarnPopUpController implements Initializable {

  private final String errorMsg;
  private final String errorMsgTitle;
  @FXML
  private Label errorMessageLabel;
  @FXML
  private Label errorMessageTitle;

  public LobbyWarnPopUpController(String errorMsg, String errorMsgTitle) {
    this.errorMsg = errorMsg;
    this.errorMsgTitle = errorMsgTitle;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    errorMessageLabel.setText(errorMsg);
    errorMessageTitle.setText(errorMsgTitle);
  }
}
