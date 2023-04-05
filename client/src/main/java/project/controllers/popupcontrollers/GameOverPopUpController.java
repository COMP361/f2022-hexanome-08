package project.controllers.popupcontrollers;

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
import javafx.stage.Stage;
import project.App;
import project.connection.GameRequestSender;
import project.controllers.stagecontrollers.LobbyController;
import project.view.lobby.communication.Savegame;

/**
 * The buttons aside the player.
 */
public class GameOverPopUpController implements Initializable {

  private final Thread mainGameUpdateThread;
  private final Thread playerInfoThread;

  private final List<String> winnerNames;
  private final boolean optionToCancel;

  private final long gameId;

  @FXML
  private Label winnersLabel;
  @FXML
  private Button byeButton;

  /**
   * GameOverPopUpController.
   *
   * @param mainGameUpdateThread mainGameUpdateThread
   * @param playerInfoThread playerInfoThread
   * @param winnerNames winnerNames
   * @param gameId gameId
   * @param optionToCancel optionToCancel
   */
  public GameOverPopUpController(Thread mainGameUpdateThread, Thread playerInfoThread,
                                 List<String> winnerNames, long gameId,
                                 boolean optionToCancel) {
    this.mainGameUpdateThread = mainGameUpdateThread;
    this.playerInfoThread = playerInfoThread;
    this.winnerNames = winnerNames;
    this.gameId = gameId;
    this.optionToCancel = optionToCancel;
  }

  /**
   * It shows what happens after clicking the quit button.
   *
   * @param mainGameUpdateThread mainGameUpdateThread
   * @param playerInfoThread     playerInfoThread
   * @return return the eventHandler
   */
  public EventHandler<ActionEvent> clickOnQuitGameButton(Thread mainGameUpdateThread,
                                                         Thread playerInfoThread) {
    return event -> {
      App.loadNewSceneToPrimaryStage("lobby_page.fxml", new LobbyController());
      // once player clicks on quit button, stop the threads and load the lobby for them
      mainGameUpdateThread.interrupt();
      playerInfoThread.interrupt();

      // if we have the option to cancel, then this is a quit game popup
      // some  requests need to be sent to game server
      if (optionToCancel) {
        try {
          GameRequestSender sender = App.getGameRequestSender();
          // a dummy save game instance to the save game API
          // once received this, we know we should terminate the game
          Savegame savegame = new Savegame(new String[0], "", "");
          sender.sendSaveGameRequest(gameId, savegame);
        } catch (UnirestException e) {
          e.printStackTrace();
          throw new RuntimeException("could not quit the game!");
        }
      }
      // otherwise, this is a game over popup, no request sent

      // typical closing pop up logic
      Button button = (Button) event.getSource();
      Stage window = (Stage) button.getScene().getWindow();
      window.close();
    };
  }

  // indicate whether to have the close button in the pop up or not
  public boolean isOptionToCancel() {
    return optionToCancel;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    byeButton.setOnAction(clickOnQuitGameButton(mainGameUpdateThread, playerInfoThread));
    if (!optionToCancel) {
      if (!winnerNames.isEmpty()) {
        winnersLabel.setText("Winners: " + winnerNames);
      } else {
        winnersLabel.setText("No Winner Yet, Game Finish!");
      }
    }
  }

}
