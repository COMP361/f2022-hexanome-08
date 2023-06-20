package project.controllers.popupcontrollers.lobbypopup;

import ca.mcgill.comp361.splendormodel.model.GameInfo;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.App;
import project.connection.GameRequestSender;
import project.view.lobby.communication.Savegame;

/**
 * Controller for save game pop up.
 */
public class SaveGamePopUpController implements Initializable {
  private final GameInfo gameInfo;
  private final long gameId;
  @FXML
  private TextField saveGameIdTextField;
  @FXML
  private Button saveButton;

  @FXML
  private Label errorMsgLabel;

  /**
   * Controller ofr save game pop up.
   *
   * @param gameInfo             gameInfo
   * @param gameId               gameId
   */
  public SaveGamePopUpController(GameInfo gameInfo, long gameId) {
    this.gameInfo = gameInfo;
    this.gameId = gameId;

  }

  private EventHandler<ActionEvent> createOnClickSaveButton(Savegame savegame) {
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      String inputSaveGameId = savegame.getSavegameid();
      String pattern = "^[a-zA-Z0-9\\s]+$";
      if (inputSaveGameId.matches(pattern) && inputSaveGameId.length() <= 35) {
        // first save it, and then delete the current session to LS
        try {
          sender.sendSaveGameRequest(gameId, savegame);
          Button button = (Button) event.getSource();
          Stage curWindow = (Stage) button.getScene().getWindow();
          curWindow.close();
        } catch (UnirestException e) {
          errorMsgLabel.setText(e.getMessage());
          saveGameIdTextField.clear();
        }
      } else {
        errorMsgLabel.setText("Please respect save game id format!");
        saveGameIdTextField.clear();
      }
    };
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    List<String> playerNamesList = gameInfo.getPlayerNames();
    String[] playerNames = playerNamesList.toArray(new String[0]);
    String gameName = App.getGameRequestSender().getGameServiceName();
    saveButton.setDisable(true);
    saveGameIdTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.trim().isEmpty()) {
        saveButton.setDisable(true);
      } else {
        saveButton.setDisable(false);
        Savegame savegame = new Savegame(playerNames, gameName, newValue);
        saveButton.setOnAction(createOnClickSaveButton(savegame));
      }
    });
  }
}
