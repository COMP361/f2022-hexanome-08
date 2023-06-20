package project.controllers.popupcontrollers.lobbypopup;

import ca.mcgill.comp361.splendormodel.model.GameInfo;
import ca.mcgill.comp361.splendormodel.model.SplendorDevHelper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.codec.digest.DigestUtils;
import project.App;
import project.connection.GameRequestSender;

public class GameConfigPopUpController implements Initializable {

  @FXML
  private Label instructionLabel;

  @FXML
  private TextField maxPointsTextField;

  @FXML
  private Button confirmMaxPointsButton;

  @FXML
  private Label warnLabel;

  private final long gameId;

  private final String viewerName;

  private final boolean inWatchMode;

  private final boolean isCreator;

  private final GameInfo firstGameInfo;

  public GameConfigPopUpController(long gameId, String viewerName) {
    this.gameId = gameId;
    this.viewerName = viewerName;
    this.firstGameInfo = getInstantGameInfo();
    // if viewer name is in the player name list, then it is NOT watch mode
    inWatchMode = !firstGameInfo.getPlayerNames().contains(viewerName);
    isCreator = !inWatchMode && firstGameInfo.getCreator().equals(viewerName);
  }


  private boolean validInput(String input) {
    String userInput = input.trim();
    if (userInput.matches("^-?\\d+$")) {
     // safely parse to integer
     int pointsInput = Integer.parseInt(userInput);
     return pointsInput >= 15 && pointsInput <= 100;
    } else {
      return false;
    }
  }

  private GameInfo getInstantGameInfo() {
    GameRequestSender gameRequestSender = App.getGameRequestSender();
    HttpResponse<String> firstGameInfoResponse =
        gameRequestSender.sendGetGameInfoRequest(gameId, "");
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    return gsonParser.fromJson(firstGameInfoResponse.getBody(), GameInfo.class);
  }

  // spawn a new thread that does the following:
  // - long pulling at the GameInfo, once the points in there is no longer negative
  //   then close current popup -> interrupt the current thread -> load game primary stage
  private Thread spawnUpdateGameInfoThread() {
    return new Thread(() -> {
      GameRequestSender gameRequestSender = App.getGameRequestSender();
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;

      while(!Thread.currentThread().isInterrupted()) {
        try {
          int responseCode = 408;
          while (responseCode == 408) {
            longPullResponse = gameRequestSender.sendGetGameInfoRequest(gameId, hashedResponse);
            responseCode = longPullResponse.getStatus();
            if (Thread.currentThread().isInterrupted()) {
              throw new InterruptedException("Game Config Thread: " +
                  Thread.currentThread().getName() + " terminated");
            }
          }
          if (responseCode == 200) {
            // update the MD5 hash of previous response
            hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
            // decode this response into GameInfo class with Gson
            String responseInJsonString = longPullResponse.getBody();
            Gson gsonParser = SplendorDevHelper.getInstance().getGson();
            GameInfo curGameInfo = gsonParser.fromJson(responseInJsonString, GameInfo.class);

            // if the max points in current game info becomes positive:
            // 1. interrupt the thread
            // 2. close the current pop up
            // 3. load the new game
            //if (true) {
            //  // 1. interrupt the thread
            //  Thread.currentThread().interrupt();
            //
            //  // 2. close the current pop up
            //  Platform.runLater(() -> {
            //    App.closePopupStage(App.getCurrentPopupStage());
            //  });
            //
            //  // 3. load the new game
            //}

          }




        } catch (InterruptedException e) {
          System.out.println(Thread.currentThread().getName() + " is dead!");
          break;
        }
      }
    });
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // bind action to confirm button
    // send a request to game server if input is valid, update the max points
    // otherwise do not approve this request
    confirmMaxPointsButton.setOnAction(event -> {
      if (validInput(maxPointsTextField.getText())) {
        // TODO: Send a request
      } else {
        warnLabel.setText("Please enter an integer (15 to 100)!");
        maxPointsTextField.clear();
      }
    });




    if (!isCreator) {
      instructionLabel.setText("Waiting for creator to determine winning points...");
      maxPointsTextField.setDisable(true);
      confirmMaxPointsButton.setDisable(true);
    }

    spawnUpdateGameInfoThread().start();

  }
}
