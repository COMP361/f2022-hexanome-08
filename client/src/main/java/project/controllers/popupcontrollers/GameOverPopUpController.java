package project.controllers.popupcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import project.App;
import project.controllers.stagecontrollers.LobbyController;

/**
 * The buttons aside the player.
 */
public class GameOverPopUpController implements Initializable {

  @FXML
  private Button byeButton;

  private final Thread mainGameUpdateThread;
  private final Thread playerInfoThread;

  public GameOverPopUpController(Thread mainGameUpdateThread, Thread playerInfoThread) {
    this.mainGameUpdateThread = mainGameUpdateThread;
    this.playerInfoThread = playerInfoThread;
  }

  /**
   * It shows what happens after clicking the quit button.
   *
   * @param mainGameUpdateThread mainGameUpdateThread
   * @param playerInfoThread playerInfoThread
   * @return return the eventHandler
   */
  public EventHandler<ActionEvent> clickOnQuitGameButton(Thread mainGameUpdateThread,
                                                         Thread playerInfoThread) {
    return event -> {
      try {
        App.loadNewSceneToPrimaryStage("admin_lobby_page.fxml", new LobbyController());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      // once player clicks on quit button, stop the threads and load the lobby for them
      mainGameUpdateThread.interrupt();
      playerInfoThread.interrupt();
      // typical closing pop up logic
      Button button = (Button) event.getSource();
      Stage window = (Stage) button.getScene().getWindow();
      window.close();
    };
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    byeButton.setOnAction(clickOnQuitGameButton(mainGameUpdateThread, playerInfoThread));
  }

}
