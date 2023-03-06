package project;

import ca.mcgill.comp361.splendormodel.model.GameInfo;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.connection.GameRequestSender;
import project.view.lobby.communication.Savegame;

public class SaveGamePopUpController implements Initializable {
  @FXML
  private TextField saveGameIdTextField;

  @FXML
  private Button saveButton;

  @FXML
  private Button cancelButton;

  private final GameInfo gameInfo;
  private final long gameId;
  public SaveGamePopUpController(GameInfo gameInfo, long gameId) {
    this.gameInfo = gameInfo;
    this.gameId = gameId;
  }

  private EventHandler<ActionEvent> createOnClickSaveButton(Savegame savegame, String accessToken) {
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      sender.sendSaveGameRequest(gameId,savegame,accessToken);
      Button button = (Button) event.getSource();
      Stage curWindow = (Stage) button.getScene().getWindow();
      curWindow.close();
    };
  }

  private EventHandler<ActionEvent> createOnClickCancelButton() {
    return event -> {
      Button button = (Button) event.getSource();
      Stage curWindow = (Stage) button.getScene().getWindow();
      curWindow.close();
    };
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    List<String> playerNamesList = gameInfo.getPlayerNames();
    String[] playerNames = playerNamesList.toArray(new String[playerNamesList.size()]);
    String gameName = App.getGameRequestSender().getGameServiceName();
    cancelButton.setOnAction(createOnClickCancelButton());
    Thread saveGameFiledThread = new Thread(()->{
      while (true) {
        // keep looping until user input something
        String saveGameId = saveGameIdTextField.getText();
        if (saveGameId == null || saveGameId.equals("")) {
          saveButton.setDisable(true);
        } else {
          saveButton.setDisable(false);
          Savegame savegame = new Savegame(playerNames, gameName, saveGameId);
          String accessToken = App.getUser().getAccessToken();
          saveButton.setOnAction(createOnClickSaveButton(savegame,accessToken));
        }
      }
    });
    saveGameFiledThread.start();
  }
}
