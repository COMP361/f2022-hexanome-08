package project.controllers.popupcontrollers;

import ca.mcgill.comp361.splendormodel.model.GameInfo;
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
import project.App;
import project.connection.GameRequestSender;
import project.view.lobby.communication.Savegame;

/**
 * Controller for save game pop up.
 */
public class SaveGamePopUpController implements Initializable {
  private final GameInfo gameInfo;
  private final long gameId;

  //@FXML
  //private Button cancelButton;
  @FXML
  private TextField saveGameIdTextField;
  @FXML
  private Button saveButton;
  private final Thread playerInfoThread;
  private final Thread mainGameUpdateThread;

  /**
   * Controller ofr save game pop up.
   *
   * @param gameInfo gameInfo
   * @param gameId gameId
   * @param playerInfoThread playerInfoThread
   * @param mainGameUpdateThread mainGameUpdateThread
   */
  public SaveGamePopUpController(GameInfo gameInfo, long gameId,
                                 Thread playerInfoThread, Thread mainGameUpdateThread) {
    this.gameInfo = gameInfo;
    this.gameId = gameId;
    this.playerInfoThread = playerInfoThread;
    this.mainGameUpdateThread = mainGameUpdateThread;

  }

  private EventHandler<ActionEvent> createOnClickSaveButton(Savegame savegame, String accessToken) {
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      // first save it, and then delete the current session to LS
      sender.sendSaveGameRequest(gameId, savegame, accessToken);
      // interrupt the threads when creator choose to save the game.
      //playerInfoThread.interrupt();
      //mainGameUpdateThread.interrupt();
      //try {
      //  App.loadNewSceneToPrimaryStage("lobby_page.fxml",
      //      App.getLobbyController());
      //} catch (IOException e) {
      //  throw new RuntimeException(e);
      //}
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
    //cancelButton.setOnAction(createOnClickCancelButton());
    Thread saveGameFiledThread = new Thread(() -> {
      while (true) {
        // keep looping until user input something
        String saveGameId = saveGameIdTextField.getText();
        if (saveGameId == null || saveGameId.equals("")) {
          saveButton.setDisable(true);
        } else {
          saveButton.setDisable(false);
          Savegame savegame = new Savegame(playerNames, gameName, saveGameId);
          String accessToken = App.getUser().getAccessToken();
          saveButton.setOnAction(createOnClickSaveButton(savegame, accessToken));
        }
      }
    });
    saveGameFiledThread.start();
  }
}
