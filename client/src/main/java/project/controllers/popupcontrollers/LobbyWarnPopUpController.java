package project.controllers.popupcontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class LobbyWarnPopUpController implements Initializable {

  private final String errorMsg;
  @FXML
  private Label errorMessageLabel;

  public LobbyWarnPopUpController(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    errorMessageLabel.setText(errorMsg);
  }
}