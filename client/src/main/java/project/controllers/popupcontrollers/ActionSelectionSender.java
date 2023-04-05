package project.controllers.popupcontrollers;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.App;
import project.connection.GameRequestSender;

/**
 * ActionSelectionSender.
 */
public class ActionSelectionSender {

  private final long gameId;

  public ActionSelectionSender(long gameId) {
    this.gameId = gameId;
  }

  protected EventHandler<MouseEvent> createOnActionSelectionClick(String actionId) {
    return event -> {
      GameRequestSender gameRequestSender = App.getGameRequestSender();
      String playerName = App.getUser().getUsername();
      String accessToken = App.getUser().getAccessToken();
      // sends a POST request that tells the server which action we chose
      gameRequestSender.sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionId);

      ImageView imageView = (ImageView) event.getSource();
      Stage curWindow = (Stage) imageView.getScene().getWindow();
      curWindow.close();
    };
  }
}
